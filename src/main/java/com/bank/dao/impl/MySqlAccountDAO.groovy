package com.bank.dao.impl

import com.bank.dao.AccountDAO
import com.bank.dao.mySql.repository.AccountRepository
import com.bank.entity.Account
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by Ник on 06.07.2017.
 */
@Repository
class MySqlAccountDAO implements AccountDAO{

    @Autowired
    private AccountRepository accountRepository

    @Override
    Account findByUsername(String username) {
      return accountRepository.findByUsername(username)
    }
}
