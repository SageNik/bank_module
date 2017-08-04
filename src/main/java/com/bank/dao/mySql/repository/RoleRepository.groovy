package com.bank.dao.mySql.repository

import com.bank.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Ник on 06.07.2017.
 */
interface RoleRepository extends JpaRepository<Role, Long>{

}