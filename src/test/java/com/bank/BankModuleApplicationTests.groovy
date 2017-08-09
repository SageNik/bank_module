package com.bank

/**
 * Created by Ник on 31.07.2017.
 */

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BankModuleApplicationTests extends Specification{

    @Autowired
    WebApplicationContext context

    def "should boot up without errors"() {
        expect: "web application context exists"
        context != null
    }

        URI serviceURI(String path = "") {
            new URI("http://localhost:8080/home-operator${path}")
    }

    String service(String path = "") {
        new String("http://localhost:8080/home-operator${path}")
    }
}
