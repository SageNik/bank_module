package com.bank.dao

import com.bank.BankModuleApplicationTests
import com.bank.entity.Client
import com.bank.entity.Creditline
import com.bank.entity.Payment
import com.bank.enumeration.CreditlineState
import com.bank.enumeration.Currency
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Stepwise

import java.sql.Date

/**
 * Created by Ник on 01.08.2017.
 */
@Stepwise
class PaymentDAOTestIT extends BankModuleApplicationTests {

    @Autowired
    private PaymentDAO paymentDAO
    @Autowired
    private ClientDAO clientDAO
    @Autowired
    private CreditlineDAO creditlineDAO

    static String itnClient = "121212"
    static Long creditlineId
    static Long paymentId


    def "Add New Payment"() {

        given:
        Client client = new Client( name: "TestName", surname: "TestSurname", itn: itnClient, phoneNumber: "123123")
        Client savedClient = clientDAO.save(client)

        Creditline creditline = new Creditline(openDate: new Date(2017,07,26), amount: 20000, percent: 10,
                monthDuration: 10, state: CreditlineState.OPENED,type: "annuit",
                currency: Currency.USD, client: savedClient)
        Creditline savedCreditline = creditlineDAO.save(creditline)
        creditlineId = savedCreditline.id

        Payment payment = new Payment(date: new Date(2017,07,27), value: 50, creditline: savedCreditline)
        when:
        Payment addedPayment = paymentDAO.addNewPayment(payment)
        paymentId = addedPayment.id
        then:
        addedPayment.date == new Date(2017,07,27)
        addedPayment.value == 50
        addedPayment.creditline.amount == 20000
        addedPayment.creditline.percent == 10
        addedPayment.creditline.monthDuration == 10
        addedPayment.creditline.state == CreditlineState.OPENED
        addedPayment.creditline.currency == Currency.USD.toString()
        paymentId == addedPayment.id
        creditlineId == savedCreditline.id
    }

    def "Get All payments by Creditline"() {

        when:
        Creditline currentCreditline = creditlineDAO.getById(creditlineId)
        List<Payment> payments = paymentDAO.getAllByCreditline(currentCreditline)
        then:
        payments.size() == 1
        payments[0].value == 50
        payments[0].creditline.amount == 20000
        payments[0].creditline.percent == 10
        payments[0].creditline.monthDuration == 10
        payments[0].creditline.state == CreditlineState.OPENED
        payments[0].creditline.currency == Currency.USD.toString()
        payments[0].date == new Date(2017,07,27)
    }

    def "Get All payments by Creditline if creditline not found"() {

        when:
        Creditline currentCreditline = creditlineDAO.getById(000)
        List<Payment> payments = paymentDAO.getAllByCreditline(currentCreditline)
        then:
        payments.size() == 0
    }


    def "Get All payments by Creditline Id"() {

        when:
        List<Payment> payments = paymentDAO.getAllByCreditlineId(creditlineId)
        then:
        payments.size() == 1
        payments[0].value == 50
        payments[0].creditline.amount == 20000
        payments[0].creditline.percent == 10
        payments[0].creditline.monthDuration == 10
        payments[0].creditline.state == CreditlineState.OPENED
        payments[0].creditline.currency == Currency.USD.toString()
        payments[0].date == new Date(2017,07,27)
    }

    def "Get All payments by Creditline id if creditline not found"() {

        when:
        List<Payment> payments = paymentDAO.getAllByCreditlineId(000)
        then:
        payments.size() == 0
    }

    def "Get payment by Id"() {

        when:
        Payment foundPayment = paymentDAO.getById(paymentId)
        then:
        foundPayment.value == 50
        foundPayment.creditline.amount == 20000
        foundPayment.creditline.percent == 10
        foundPayment.creditline.monthDuration == 10
        foundPayment.creditline.state == CreditlineState.OPENED
        foundPayment.creditline.currency == Currency.USD.toString()
        foundPayment.date == new Date(2017,07,27)
    }

    def "Delete Payment"() {

        when:
        Boolean isDeleted = paymentDAO.deletePayment(paymentId)
        String itnClient = "121212"
        Client deleteClient = clientDAO.findByItn(itnClient)
        clientDAO.deleteClient(deleteClient)
        then:
        !isDeleted
        !paymentDAO.getById(paymentId)
    }

}
