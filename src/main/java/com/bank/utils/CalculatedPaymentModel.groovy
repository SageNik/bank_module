package com.bank.utils

import groovy.transform.ToString

/**
 * Created by Ник on 14.07.2017.
 */
@ToString
class CalculatedPaymentModel {

    /**
     * Номер платежа
     */
     Integer number

    /**
     * Остаток кредита
     */
     BigDecimal residualCredit

    /**
     * Сумма погашения долга
     */
     BigDecimal amortization

    /**
     * Проценты по кредиту
     */
     BigDecimal percents

    /**
     * Месячный платёж
     */
     BigDecimal monthPayment

}
