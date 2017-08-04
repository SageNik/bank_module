package com.bank.dao.mySql.repository

import com.bank.entity.Creditline
import com.bank.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Ник on 21.07.2017.
 */
interface PaymentRepository extends JpaRepository<Payment, Long>{

    /**
     * Метод для получения всех платежей по кредитной линии клиента
     * @param Creditline creditline - кредитная линия клиента
     * @return List<Payment> - список найденных платежей
     */
    List<Payment> findAllByCreditline(Creditline creditline)

    /**
     * Метод для получения всех платежей по идентификатору кредитной линии клиента
     * @param Long creditlineId - кредитная линия клиента
     * @return List<Payment> - список найденных платежей
     */
    List<Payment> getAllByCreditlineId(Long creditlineId)

}