package com.bank.entity

import com.bank.model.dto.ClientDTO

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * Created by Ник on 17.07.2017.
 */
@Entity
@Table(name = "client")
class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id

    @Column( nullable = false)
    private String name

    @Column(nullable = false)
    private String surname

    @Column(nullable = false, unique = true)
    private String itn

    @Column(unique = true)
    private String phoneNumber

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "client")
    private Set<Creditline> creditlines

    Client(){}
    Client(ClientDTO clientDTO){
        this.name = clientDTO.name
        this.surname = clientDTO.surname
        this.phoneNumber = clientDTO.phoneNumber
        this.itn = clientDTO.itn
    }
}
