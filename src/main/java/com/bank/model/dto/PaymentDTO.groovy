package com.bank.model.dto

import com.bank.entity.Payment

import java.sql.Date

/**
 * Created by Ник on 21.07.2017.
 */
class PaymentDTO implements Comparable<PaymentDTO>{

     Long id

     Date date

     Long value

    Long creditlineId

    final static PaymentDTO buildFromPayment(Payment payment){

        return new PaymentDTO(
                id: payment.id,
                date: payment.date,
                value: payment.value,
                creditlineId: payment.creditline.id
        )
    }

    @Override
    int compareTo(PaymentDTO o) {
        return (this.date - o.date)
    }
}
