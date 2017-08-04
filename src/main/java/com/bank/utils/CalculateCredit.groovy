package com.bank.utils
/**
 * Created by Ник on 14.07.2017.
 */
interface CalculateCredit {

    /**
     * Метод для расчёта платежей по кредиту
     * @param BigDecimal amount сумма на которую выдан кредит
     * @param int monthDuration срок на который выдан кредит в месяцах
     * @param int percent процентная ставка кредита
     * @return List<CalculatedPaymentModel> Возвращает список платежей по кредиту
     */
    List<CalculatedPaymentModel> calcPayments (BigDecimal amount, int monthDuration, int percent)

    /**
     * Метод для расчёта полной стоимости кредита
     * @param List<CalculatedPaymentModel> calcPaymentList список рассчитанных платежей по кредиту
     * @return BigDecimal Возвращает полную стоимость кредита
     */
    BigDecimal calcFullCost(List<CalculatedPaymentModel> calcPaymentList)

    /**
     * Метод для расчёта переплаты по кредиту
     * @param List<CalculatedPaymentModel> calcPaymentList список рассчитанных платежей по кредиту
     * @param BigDecimal amount сумма на которую выдан кредит
     * @return BigDecimal Возвращает стоимость переплаты по кредиту
     */
    BigDecimal calcOverpayment (List<CalculatedPaymentModel> calcPaymentList, BigDecimal amount)

}