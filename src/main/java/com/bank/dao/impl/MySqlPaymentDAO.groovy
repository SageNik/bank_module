package com.bank.dao.impl

import com.bank.dao.PaymentDAO
import com.bank.dao.mySql.repository.PaymentRepository
import com.bank.entity.Creditline
import com.bank.entity.Payment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by Ник on 21.07.2017.
 */
@Repository
class MySqlPaymentDAO implements PaymentDAO {

    @Autowired
    private PaymentRepository paymentRepository

    @Override
    List<Payment> getAllByCreditline(Creditline creditline) {
        return paymentRepository.findAllByCreditline(creditline)
    }

    @Override
    List<Payment> getAllByCreditlineId(Long creditlineId) {
        return paymentRepository.getAllByCreditlineId(creditlineId)
    }

    @Override
    Payment addNewPayment(Payment payment) {
        return paymentRepository.save(payment)
    }

    @Override
    boolean deletePayment(Long paymentId) {
          return paymentRepository.delete(paymentId)
    }

    @Override
    Payment getById(Long paymentId) {
        return paymentRepository.findOne(paymentId)
    }
}