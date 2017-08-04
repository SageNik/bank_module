package com.bank.utils

import groovy.util.logging.Slf4j

/**
 * Created by Ник on 14.07.2017.
 */
@Slf4j
class DifferentialCredit extends AbstractCalculateCredit{
    @Override
    BigDecimal calcPercents(BigDecimal currentAmount, int percent) {

        log.debug("Calculate differential percents")
        BigDecimal result = (currentAmount.multiply(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100),3,BigDecimal.ROUND_DOWN)))
        result = result.divide(BigDecimal.valueOf(12),3,BigDecimal.ROUND_DOWN)
        result = result.setScale(2,BigDecimal.ROUND_HALF_UP)
        return result
    }

    @Override
    BigDecimal calcMonthPayment(BigDecimal amount, BigDecimal currentAmount, int monthDuration, int percent) {

        log.debug("Calculate differential month payment")
        BigDecimal result = calcAmortization(amount, currentAmount, monthDuration,percent).add(calcPercents(currentAmount, percent))
        result = result.setScale(2,BigDecimal.ROUND_HALF_UP)
        return result
    }

    @Override
    BigDecimal calcAmortization(BigDecimal amount, BigDecimal currentAmount, int monthDuration, int percent) {

        log.debug("Calculate differential amortization")
        BigDecimal result = amount.divide(BigDecimal.valueOf(monthDuration),3,BigDecimal.ROUND_HALF_DOWN)
        result = result.setScale(2,BigDecimal.ROUND_HALF_UP)
        return result
    }
}
