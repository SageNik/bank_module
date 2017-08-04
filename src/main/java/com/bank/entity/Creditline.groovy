package com.bank.entity

import com.bank.enumeration.CreditlineState
import com.bank.model.dto.CreditlineDTO

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import java.sql.Date

/**
 * Created by Ник on 17.07.2017.
 */
@Entity
@Table(name = "creditline")
class Creditline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id

    @Column(nullable = false)
    private String type

    @Column(nullable = false)
    private Date openDate

    @Column
    private Date closeDate

    @Column(nullable = false)
    private int monthDuration

    @Column(nullable = false)
    private Long amount

    @Column(nullable = false)
    private int percent

    @Column (nullable = false)
    private String currency

    @Column(nullable = false)
    private CreditlineState state

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "creditline")
    private Set<Payment> payments

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client

    Creditline(){}

    Creditline(CreditlineDTO creditlineDTO, Client currentClient){
        this.type = creditlineDTO.type
        this.openDate = new Date(new java.util.Date().getTime())
        this.monthDuration = creditlineDTO.duration
        this.amount = creditlineDTO.amount
        this.percent = creditlineDTO.percent
        this.currency = creditlineDTO.currency
        this.client = currentClient
        this.state = CreditlineState.OPENED
    }
}
