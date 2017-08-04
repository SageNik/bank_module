package com.bank.entity

import com.bank.model.dto.PaymentDTO

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import java.sql.Date

/**
 * Created by Ник on 17.07.2017.
 */
@Entity
@Table(name = "payment")
class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id

    @Column(nullable = false)
    private Date date

    @Column(nullable = false)
    private Long value

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creditline_id", nullable = false)
    private Creditline creditline

    Payment(){}
    Payment(PaymentDTO paymentDTO, Creditline creditline1){
        this.id = paymentDTO.id
        this.date = paymentDTO.date
        this.value = paymentDTO.value
        this.creditline = creditline1
    }
}
