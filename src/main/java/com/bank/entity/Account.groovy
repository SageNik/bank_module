package com.bank.entity

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * Created by Ник on 06.07.2017.
 */
@Entity
@Table(name = "account")
class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id

    @Column(name = "username", nullable = false, unique = true)
    private String username

    @Column(name = "full_name", nullable = false)
    private String fullName

    @Column(name = "password_hash", nullable = false)
    private String passwordHash

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
//    private Set<Creditline> creditlines
}

