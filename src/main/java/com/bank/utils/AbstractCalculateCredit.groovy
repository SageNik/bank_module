package com.bank.utils

import groovy.util.logging.Slf4j

/**
 * Created by Ник on 14.07.2017.
 */
@Slf4j
abstract class AbstractCalculateCredit implements CalculateCredit {
    @Override
    List<CalculatedPaymentModel> calcPayments(BigDecimal amount, int monthDuration, int percent) {

        log.debug("Calculate credit payments")
        List<CalculatedPaymentModel> calcPayList = new ArrayList<CalculatedPaymentModel>()
        BigDecimal currentAmount = amount
        currentAmount = currentAmount.setScale(2, BigDecimal.ROUND_HALF_UP)
        for (int i = 0; i < monthDuration; i++) {
            CalculatedPaymentModel calcPaymentModel = new CalculatedPaymentModel()

            calcPaymentModel.number = (1 + i)as Integer
            calcPaymentModel = prepareCalcPayment(amount, currentAmount, monthDuration, percent, calcPaymentModel)
            calcPaymentModel = correctResidualCredit(calcPaymentModel, currentAmount)

            calcPayList.add(calcPaymentModel)
            currentAmount = currentAmount.subtract(calcPaymentModel.amortization)
        }
        log.debug("The credit payments was successful calculated. The payments list size: [${calcPayList.size()}] ")
        return calcPayList
    }

    private CalculatedPaymentModel prepareCalcPayment(BigDecimal amount, BigDecimal currentAmount, int monthDuration, int percent, CalculatedPaymentModel calcPaymentModel) {

        log.debug("Prepare calculated payment to add to list of payments")
        calcPaymentModel.amortization = calcAmortization(amount, currentAmount, monthDuration, percent)
        calcPaymentModel.monthPayment = calcMonthPayment(amount, currentAmount, monthDuration, percent)
        calcPaymentModel.percents = calcPercents(currentAmount, percent)
        calcPaymentModel.residualCredit = currentAmount
        log.debug("The payment was prepared : [${calcPaymentModel}]")
        return calcPaymentModel
    }

    abstract BigDecimal calcPercents(BigDecimal currentAmount, int percent)

    abstract BigDecimal calcMonthPayment(BigDecimal amount, BigDecimal currentAmount, int monthDuration, int percent)

    abstract BigDecimal calcAmortization(BigDecimal amount, BigDecimal currentAmount, int monthDuration, int percent)

    @SuppressWarnings("GrMethodMayBeStatic")
    private CalculatedPaymentModel correctResidualCredit(CalculatedPaymentModel calcPaymentModel, BigDecimal currentAmount) {

        // для того, чтобы уравнять разницу между последним платёжом и остатком по кредиту, которая возникает
        // при округлении суммы погашения долга (amortization)до двух знаков после запятой.
        log.debug("Correct residual credit")
        if (calcPaymentModel.monthPayment.compareTo(currentAmount) == 1) {
            calcPaymentModel.residualCredit = calcPaymentModel.amortization
        } else {
            calcPaymentModel.residualCredit = currentAmount
        }
        return calcPaymentModel
    }

    @Override
    BigDecimal calcFullCost(List<CalculatedPaymentModel> calcPaymentList) {

        log.debug("Calculate full cost of the credit")
        BigDecimal result = BigDecimal.valueOf(0.0)
        for(CalculatedPaymentModel payment : calcPaymentList){
            result=result.add(payment.monthPayment)
        }
        return result
    }

    @Override
    BigDecimal calcOverpayment(List<CalculatedPaymentModel> calcPaymentList, BigDecimal amount) {
        log.debug("Calculate overpayment of the credit")
        return calcFullCost(calcPaymentList).subtract(amount)
    }
}
