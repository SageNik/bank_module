package com.bank.model.view

import com.bank.model.FullPaymentModel
import com.bank.model.dto.CreditlineDTO

/**
 * Created by Ник on 19.07.2017.
 */
class CreditlineViewModel extends BaseViewModel{

    /**
     * Количество кредитных линий банка
     */
    Integer creditlineCount

    /**
     * Инн текущего клиента
     */
    String itnClient

    /**
     * Список кредитных линий
     */
    List<CreditlineDTO> creditlines

    /**
     * Список платежей (рассчетных и совершенных)
     */
    List<FullPaymentModel> fullPayments

    /**
     * Флаг наличия сообщения
     */
    boolean messageAddPayment

    /**
     * Флаг наличия сообщения
     */
    boolean messageDelPayment

    /**
     * Рекомендуемый платеж
     */
    BigDecimal recommendPay

    /**
     * Полная стоимость кредита
     */
    BigDecimal fullCost

    /**
     * Переплата
     */
    BigDecimal overpayment
}
