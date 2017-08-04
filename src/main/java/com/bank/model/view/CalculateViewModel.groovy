package com.bank.model.view

import com.bank.utils.CalculatedPaymentModel
import groovy.transform.ToString

/**
 * Created by Ник on 14.07.2017.
 */
@ToString
class CalculateViewModel {

    /**
     * Флаг отображения таблицы с расчётами по кредиту
     */
    boolean calcTable

    /**
     * Суммы кредита
     */
    BigDecimal creditAmount

    /**
     * Процент по кредиту
     */
    Integer creditPercent

    /**
     * Время кредитования
     */
    Integer creditTime

    /**
     * Метод расчёта кредита
     */
    String calcMethod

    /**
     * Полная стоимость кредита
     */
    BigDecimal fullCost

    /**
     * Переплата
     */
    BigDecimal overpayment

    /**
     * Список рассчётных платежей по кредиту
     */
    List<CalculatedPaymentModel> calculatedPayments

    final static CalculateViewModel buildFromCreditlineViewModel(CreditlineViewModel viewModel){

        return new CalculateViewModel(
                creditAmount: viewModel.creditlines[0].amount,
                creditPercent: viewModel.creditlines[0].percent,
                creditTime: viewModel.creditlines[0].duration,
                calcMethod: viewModel.creditlines[0].type
        )
    }

}
