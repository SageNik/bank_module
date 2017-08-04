package com.bank.dao.impl

import com.bank.dao.RoleDAO
import com.bank.dao.mySql.repository.RoleRepository
import com.bank.entity.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by Ник on 06.07.2017.
 */
@Repository
class MySqlRoleDAO implements RoleDAO{

    @Autowired
    private RoleRepository roleRepository

    @Override
    Set<Role> findAll() {
        return roleRepository.findAll()
    }
}
