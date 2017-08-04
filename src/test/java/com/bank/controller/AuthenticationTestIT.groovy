package com.bank.controller

import com.bank.BankModuleApplicationTests
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import static org.springframework.http.HttpStatus.OK

/**
 * Created by Ник on 02.08.2017.
 */
@Stepwise
class AuthenticationTestIT extends BankModuleApplicationTests{

    @Shared
    def ROLE_TO_USER = [
            OPERATOR:    [username: 'operator', passwordHash: 'oper123'],
            ADMIN:   [username: 'admin', passwordHash: 'admin123']]

    @Unroll("calling #endpoint with user #user should return status #status")
    def "test authentication of #endpoint"() {
        given:
        TestRestTemplate restTemplate = new TestRestTemplate()
        when:
        ResponseEntity<String> response = restTemplate.withBasicAuth(user.username, user.passwordHash).getForEntity(new URI("http://localhost:8080/home-operator/${endpoint}"),String.class)
        then:
        response.statusCode == status
        where:
        endpoint                  | user                 || status
        ""                        | ROLE_TO_USER.OPERATOR|| OK
        ""                        | ROLE_TO_USER.ADMIN   || OK
 }
}
