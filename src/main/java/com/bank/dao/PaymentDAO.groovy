package com.bank.dao

import com.bank.entity.Creditline
import com.bank.entity.Payment


/**
 * Created by Ник on 21.07.2017.
 */
interface PaymentDAO {

    /**
     * Метод для получения всех платежей по кредитной линии клиента
     * @param Creditline creditline - кредитная линия клиента
     * @return List<Payment> - список найденных платежей
     */
    List<Payment> getAllByCreditline(Creditline creditline)

    /**
     * Метод для получения всех платежей по идентификатору кредитной линии клиента
     * @param Long creditlineId - кредитная линия клиента
     * @return List<Payment> - список найденных платежей
     */
    List<Payment> getAllByCreditlineId(Long creditlineId)

    /**
     * Метод для добавления платежа по кредитной линии клиента
     * @param Payment payment - платеж для сохранения
     * @return Payment - сохраненный платеж
     */
    Payment addNewPayment(Payment payment)

    /**
     * Метод для удаления платежа по идентификатору
     * @param Long paymentId - идентификатор платежа
     * @return true - если платеж удален, false - если нет
     */
    boolean deletePayment(Long paymentId)

    /**
     * Метод для получения платежа по идентификатору
     * @param Long paymentId - идентификатор платежа
     * @return Payment - найденный платеж
     */
    Payment getById(Long paymentId)

}