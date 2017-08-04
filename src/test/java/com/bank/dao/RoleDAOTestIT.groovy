package com.bank.dao

import com.bank.BankModuleApplicationTests
import com.bank.entity.Role
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Ник on 31.07.2017.
 */
class RoleDAOTestIT extends BankModuleApplicationTests {

    @Autowired
    private RoleDAO roleDAO

    def "Find All roles"() {

        when:
        Set<Role> roles = roleDAO.findAll()
        then:
        roles.size() == 2
        roles[0].roleName == "ROLE_ADMIN"
        roles[0].id == 1
        roles[1].roleName == "ROLE_OPERATOR"
        roles[1].id == 2
    }
}
