package com.bank.controller

import com.bank.BankModuleApplicationTests
import com.bank.dao.mySql.repository.ClientRepository
import com.bank.dao.mySql.repository.CreditlineRepository
import com.bank.dao.mySql.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.Rollback
import spock.lang.Stepwise

/**
 * Created by Ник on 03.08.2017.
 */
@Stepwise
class PaymentControllerTestIT extends BankModuleApplicationTests {

    @Autowired
    private ClientRepository clientRepository
    @Autowired
    private CreditlineRepository creditlineRepository
    @Autowired
    private PaymentRepository paymentRepository

    private TestRestTemplate restTemplate = new TestRestTemplate()
    private static Long creditlineId
    private  static Long clientId
    private  static Long paymentId
    @Rollback
    def "Add Payment POST"() {

        setup:
        def itnClient = "1111"
        def name = "TestClient"
        def surname = "Testovich"
        def phoneNumber = "123123"
        Map<String, String> urlParamsClient = new HashMap<String, String>()
        urlParamsClient.put("itn", itnClient)
        urlParamsClient.put("name", name)
        urlParamsClient.put("surname", surname)
        urlParamsClient.put("phoneNumber", phoneNumber)
        ResponseEntity<String> responseClient = restTemplate.postForEntity(service("/addClient")+"?itn="+itnClient+
                "&name="+name+"&surname="+surname+"&phoneNumber="+phoneNumber,urlParamsClient,String.class)
        clientId = clientRepository.findAll().last().id

        def type = "annuit"
        def duration = 12
        def amount = 15000
        def percent = 25
        def currency = "UAH"
        Map<String, String> urlParamsCreditline = new HashMap<String, String>()
        urlParamsCreditline.put("itnClient", itnClient)
        urlParamsCreditline.put("duration", duration.toString())
        urlParamsCreditline.put("amount", amount.toString())
        urlParamsCreditline.put("percent", percent.toString())
        urlParamsCreditline.put("currency", currency)
        urlParamsCreditline.put("type", type)
        ResponseEntity<String> responseCreditline = restTemplate.postForEntity(service("/addCreditline")+"?itnClient="+itnClient+
                "&duration="+duration+"&amount="+amount+"&percent="+percent+"&currency="+currency+"&type="+type,urlParamsCreditline,String.class)
        creditlineId = creditlineRepository.findAll().last().id

        when:
        Map<String, Object> urlParams = new HashMap<String, Object>()
        urlParams.put("itnClient", itnClient)
        urlParams.put("creditlineId", creditlineId)
        urlParams.put("amount", amount)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/addPayment")+"?itnClient="+itnClient+
                "&amount="+amount+"&creditlineId="+creditlineId,urlParams,String.class)
        paymentId = paymentRepository.findAll().last().id

        then:
        response.statusCode == HttpStatus.OK
        responseClient.statusCode == HttpStatus.OK
        responseCreditline.statusCode == HttpStatus.OK
        clientId
        creditlineId
        paymentId

    }

    def "Ge tAll Client Payments GET"() {

            when:
            ResponseEntity<String> response = restTemplate.getForEntity(serviceURI("/allPayments?itnClient=1111"),String.class)
            then:
            response.statusCode == HttpStatus.OK
    }

    def "Delete Payment POST"() {

        setup:
        def itnClient = "1111"
        when:
        Map<String, Object> urlParams = new HashMap<String, Object>()
        urlParams.put("itnClient", itnClient)
        urlParams.put("creditlineId", creditlineId)
        ResponseEntity<String> response = restTemplate.postForEntity(service("/deletePayment")+"?itnClient="+itnClient+
                "&creditlineId="+creditlineId+"&paymentId="+paymentId,urlParams,String.class)
        then:
        response.statusCode == HttpStatus.OK

        cleanup:
        creditlineRepository.delete(creditlineId)
        clientRepository.delete(clientId)
    }
}
