package com.bank.dao

import com.bank.BankModuleApplicationTests
import com.bank.entity.Account
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Ник on 31.07.2017.
 */
class AccountDAOTestIT extends BankModuleApplicationTests {

    @Autowired
    private AccountDAO accountDAO

    def "Find By Username"() {

        when:
       Account account = accountDAO.findByUsername("admin")
        then:
        account.username == "admin"
        account.fullName == "Генеральный Админ Админович"
        account.id == 1

        and:
        when:
        account = accountDAO.findByUsername("operator")
        then:
        account.username == "operator"
        account.fullName == "Виртуоз Моисей Кредитович"
        account.id == 2

        and:
        when:
        account = accountDAO.findByUsername("client")
        then:
        !account
    }
}
