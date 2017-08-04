package com.bank.service.interfaces

import com.bank.model.view.CreditlineViewModel
import com.bank.model.view.PaymentViewModel

/**
 * Created by Ник on 21.07.2017.
 */
interface PaymentService {

    /**
     * Метод для получения всех платежей клиента по его инн
     * @param String itnClient - инн клиента
     * @return PaymentViewModel Возвращает модель для отображения
     */
    PaymentViewModel getAllClientPayments(String itnClient)

    /**
     * Метод для добавления платежа клиента
     * @param Long creditlineId - идентификатор кредитной линии
     * @param BigDecimal amount - сумма платежа
     * @return CreditlineViewModel - модель для отображения
     */
    CreditlineViewModel addPayment(Long creditlineId, BigDecimal amount)

    /**
     * Метод для удаления платежа клиента
     * @param Long paymentId - идентификатор платежа
     * @return CreditlineViewModel - модель для отображения
     */
    CreditlineViewModel deletePayment(Long paymentId, Long creditlineId)
}