package com.bank.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

/**
 * Created by Ник on 06.07.2017.
 */
@Entity
@Table(name = "role")
class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id

    @Column(name = "role", nullable = false)
    private String roleName

    @ManyToMany(mappedBy = "roles")
    Set<Account> accounts
}
