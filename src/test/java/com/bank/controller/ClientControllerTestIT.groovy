package com.bank.controller

import com.bank.BankModuleApplicationTests
import com.bank.service.interfaces.ClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.Rollback
import spock.lang.Stepwise
import spock.lang.Unroll


/**
 * Created by Ник on 02.08.2017.
 */
@Stepwise
class ClientControllerTestIT extends BankModuleApplicationTests {

    private TestRestTemplate restTemplate = new TestRestTemplate()

    def "Add New Client GET"() {

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/addNewClient"),String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    @Rollback
    def "Add Client POST"() {

        when:
        def itn = "1111"
        def name = "TestClient"
        def surname = "Testovich"
        def phoneNumber = "123123"
        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("itn", itn)
        urlParams.put("name", name)
        urlParams.put("surname", surname)
        urlParams.put("phoneNumber", phoneNumber)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/addClient")+"?itn="+itn+
                "&name="+name+"&surname="+surname+"&phoneNumber="+phoneNumber,urlParams,String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "All Clients GET"() {

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/clients"),String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    @Unroll("search by #searchCriteria with #inputData should return status #status")
    def "Search Client POST"() {

        when:
        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("searchCriteria", searchCriteria)
        urlParams.put("inputData", inputData)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/searchClient") + "?searchCriteria="+
                searchCriteria + "&inputData=" + inputData,urlParams,String.class)
        then:
        response.statusCode == status
        where:
        searchCriteria    | inputData     || status
        "name"            | "TestClient"  || HttpStatus.OK
        "surname"         | "Testovich"   || HttpStatus.OK
        "itn"             | "1111"        || HttpStatus.OK
        "phoneNumber"     | "123123"      || HttpStatus.OK
    }

    def "Client GET"() {

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/client?itnClient=1111"),String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Change Client GET"() {

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/changeClient?itnClient=1111"),String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Update Client POST"() {

        when:
        def itn = "1111"
        def name = "TestClient"
        def surname = "Updated"
        def phoneNumber = "123123"
        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("itn", itn)
        urlParams.put("name", name)
        urlParams.put("surname", surname)
        urlParams.put("phoneNumber", phoneNumber)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/updateClient")+"?itn="+itn+
                "&name="+name+"&surname="+surname+"&phoneNumber="+phoneNumber,urlParams,String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Delete Client POST"() {

        when:
        def itnClient = "1111"
        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("itnClient", itnClient)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/deleteClient") + "?itnClient="+
                itnClient ,urlParams,String.class)
        then:
        response.statusCode == HttpStatus.OK
    }
}
