package com.bank.service.impl

import com.bank.dao.AccountDAO
import com.bank.entity.Account
import com.bank.entity.Role
import com.bank.model.view.OperatorViewModel
import com.bank.service.interfaces.AccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import javax.transaction.Transactional

/**
 * Created by Ник on 07.07.2017.
 */
@Slf4j
@Service
class AccountServiceImpl implements AccountService{

    @Autowired
    AccountDAO accountDAO

    @Override
    @Transactional
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Try to get user details by username: [${username}]")
       Account account = accountDAO.findByUsername(username)

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>()
        for(Role role :account.roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.roleName))
        }

        log.debug("User details found by username: [${username}] with set of roles size: [${grantedAuthorities.size()}] ")
        return new User(account.username,account.passwordHash,grantedAuthorities)
    }

    @Override
    OperatorViewModel getAuthorizedUser() {

        log.debug("Try to get current authorized user")
        String username = SecurityContextHolder.getContext().getAuthentication().getName()
        Account account = accountDAO.findByUsername(username)
        log.debug("Current authorized user success found")
        return OperatorViewModel.buildFromAccount(account)
    }
}
