package com.bank.utils

import groovy.util.logging.Slf4j

/**
 * Created by Ник on 14.07.2017.
 */
@Slf4j
class AnnuityCredit extends AbstractCalculateCredit{
    @Override
    BigDecimal calcPercents(BigDecimal currentAmount, int percent) {

        log.debug("Calculate annuity percents")
        BigDecimal result = currentAmount.multiply(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100), 3, BigDecimal.ROUND_DOWN))
        result = result.divide(BigDecimal.valueOf(12), 3, BigDecimal.ROUND_DOWN)
        result = result.setScale(2,BigDecimal.ROUND_HALF_UP)
        return result
    }

    @Override
    BigDecimal calcMonthPayment(BigDecimal amount, BigDecimal currentAmount, int monthDuration, int percent) {

        log.debug("Calculate month annuity paymant")
        double prePow = Math.pow((1+(double)percent/100/12), monthDuration)
        double prePercent = (double)percent/100/12 * prePow
        double preResult = prePercent /(prePow-1)
        BigDecimal result = amount.multiply(BigDecimal.valueOf(preResult))
        result = result.setScale(2,BigDecimal.ROUND_HALF_UP)
        return result
    }

    @Override
    BigDecimal calcAmortization(BigDecimal amount, BigDecimal currentAmount, int monthDuration, int percent) {

        log.debug("Calculate annuity amortization")
        return  calcMonthPayment(amount, currentAmount,monthDuration,percent).subtract(calcPercents(currentAmount, percent))
    }
}
