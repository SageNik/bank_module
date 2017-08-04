package com.bank.controller

import com.bank.BankModuleApplicationTests
import com.bank.dao.mySql.repository.CreditlineRepository
import com.bank.enumeration.CreditlineState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.Rollback
import spock.lang.Ignore
import spock.lang.Stepwise
import spock.lang.Unroll

import java.sql.Date
import java.sql.Timestamp

/**
 * Created by Ник on 03.08.2017.
 */
@Stepwise
class CreditlineControllerTestIT extends BankModuleApplicationTests {

    @Autowired
    private CreditlineRepository creditlineRepository

    private TestRestTemplate restTemplate = new TestRestTemplate()
    static private def creditlineId

    @Rollback
    def "Add New Creditline GET"() {

        setup:
        def itn = "1111"
        def name = "TestClient"
        def surname = "Testovich"
        def phoneNumber = "123123"
        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("itn", itn)
        urlParams.put("name", name)
        urlParams.put("surname", surname)
        urlParams.put("phoneNumber", phoneNumber)
        ResponseEntity<String> responseClient = restTemplate.postForEntity(service("/addClient")+"?itn="+itn+
                "&name="+name+"&surname="+surname+"&phoneNumber="+phoneNumber,urlParams,String.class)
        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/creditlines?itnClient=1111"),String.class)
        then:
        response.statusCode == HttpStatus.OK
        responseClient.statusCode == HttpStatus.OK
    }

    @Rollback
    def "Add Creditline POST"() {

        when:
        def itnClient = "1111"
        def type = "annuit"
        def duration = 12
        def amount = 15000
        def percent = 25
        def currency = "UAH"
        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("itnClient", itnClient)
        urlParams.put("duration", duration.toString())
        urlParams.put("amount", amount.toString())
        urlParams.put("percent", percent.toString())
        urlParams.put("currency", currency)
        urlParams.put("type", type)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/addCreditline")+"?itnClient="+itnClient+
                "&duration="+duration+"&amount="+amount+"&percent="+percent+"&currency="+currency+"&type="+type,urlParams,String.class)
      creditlineId = creditlineRepository.findAll().last().id
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Creditlines GET"() {

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/creditlines"),String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Calculate Payments POST"() {

        when:
        def type = "annuit"
        def duration = 12
        def amount = 15000
        def percent = 25
        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("duration", duration.toString())
        urlParams.put("amount", amount.toString())
        urlParams.put("percent", percent.toString())
        urlParams.put("type", type)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/addCreditline")+"?duration="
                +duration+"&amount="+amount+"&percent="+percent+"&type="+type,urlParams,String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Get All Client Credit lines GET"() {

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/allClientCreditlines?itnClient=1111"),String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Get All Client Current Credit lines GET"() {

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/allCurCreditlines?itnClient=1111"),String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Show Credit line GET"() {

        when:
        ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/showCreditline?idCredline=$creditlineId&itnClient=1111"),String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    def "Close Creditline POST"() {

        when:
        def itnClient = "1111"

        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("itnClient", itnClient)
        urlParams.put("creditlineId", creditlineId.toString())
        ResponseEntity<String> response = restTemplate.postForEntity(service("/closeCreditline")+"?itnClient="+itnClient+
                "&creditlineId="+creditlineId,urlParams,String.class)
        then:
        response.statusCode == HttpStatus.OK
    }

    @Unroll("search by #searchCriteria with #inputDate should return status #status")
    def "Search Creditline POST"() {

        when:
        Map<String, Object> urlParams = new HashMap<String, Object>()
        urlParams.put("searchCriteria", searchCriteria)
        urlParams.put("inputData", inputDate)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/searchCreditline") + "?searchCriteria="+
                searchCriteria + "&inputDate=" + inputDate,urlParams,String.class)
        then:
        response.statusCode == status
        where:        searchCriteria    | inputDate                                         || status
        "openDate"        | new Date(Timestamp.valueOf("2017-07-26 0:0:0.00").getTime()) || HttpStatus.OK
        "closeDate"       | new Date(new java.util.Date().getTime())                        || HttpStatus.OK
    }

    def "Delete Credit line POST"() {

        setup:
        def itnClient = "1111"
        when:
        Map<String, String> urlParams = new HashMap<String, String>()
        urlParams.put("itnClient", itnClient)
        urlParams.put("creditlineId", creditlineId.toString())
        ResponseEntity<String> response = restTemplate.postForEntity(service("/deleteCreditline")+"?itnClient="+itnClient+
                "&creditlineId="+creditlineId,urlParams,String.class)
        then:
        response.statusCode == HttpStatus.OK
        cleanup:
        Map<String, String> urlParamsCl = new HashMap<String, String>()
        urlParamsCl.put("itnClient", itnClient)
        ResponseEntity<String> responseClient = restTemplate.postForEntity(service("/deleteClient") + "?itnClient="+
                itnClient ,urlParamsCl,String.class)
    }

}
