package com.bank.config

import com.bank.dao.mySql.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Created by Ник on 07.07.2017.
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = AccountRepository.class)
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder()
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                http.authorizeRequests()
                    .antMatchers("/home-operator/").hasAnyAuthority("ROLE_ADMIN","ROLE_OPERATOR")
                    .and().formLogin().loginPage("/home").permitAll()
                    .defaultSuccessUrl("/home-operator")
                        .failureUrl("/home?error")
                        .usernameParameter("username")
                        .and().logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder())
    }
}
