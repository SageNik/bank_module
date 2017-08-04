package com.bank.dao.mySql.repository

import com.bank.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Ник on 06.07.2017.
 */
interface AccountRepository extends JpaRepository<Account, Long>{

    Account findByUsername(String username)
}