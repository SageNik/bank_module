package com.bank.service.impl

import com.bank.dao.AccountDAO
import com.bank.entity.Account
import com.bank.entity.Role
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

/**
 * Created by Ник on 07.07.2017.
 */
class AccountServiceImplUnitTest extends Specification {

    AccountDAO accountDAO
    AccountServiceImpl service

  def setup(){
        service = new AccountServiceImpl()
        accountDAO = Stub(AccountDAO)
        service.setAccountDAO(accountDAO)

      accountDAO.findByUsername(_) >> { String username ->
          if (username == "Ivan") {

              return  new Account(id: 1, username: "Ivan", fullName: "Test User", passwordHash: "123User",
                      roles: new HashSet<Role>([new Role(id: 1, roleName: "ROLE_ADMIN"), new Role(id: 2, roleName: "ROLE_OPERATOR")]))

          }else{ throw new UsernameNotFoundException ("Account with name ${username} not found") }
      }
    }

    def "LoadUserByUsername if user exist "() {

        expect:
        UserDetails userDetails = service.loadUserByUsername(username)
        userDetails.getAuthorities().size() == 2
        userDetails.getPassword() == "123User"
        userDetails.getUsername() == "Ivan"

        where:
        username = "Ivan"
    }

        def "LoadUserByUsername and throw exception if user not exist"() {

            when:
            UserDetails userDetails = service.loadUserByUsername("Ivanna")

            then:
            thrown(UsernameNotFoundException)
    }

}
