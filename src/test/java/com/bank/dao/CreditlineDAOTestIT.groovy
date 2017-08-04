package com.bank.dao

import com.bank.BankModuleApplicationTests
import com.bank.entity.Client
import com.bank.entity.Creditline
import com.bank.enumeration.CreditlineState
import com.bank.enumeration.Currency
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Stepwise

import java.sql.Date

/**
 * Created by Ник on 01.08.2017.
 */
@Stepwise
class CreditlineDAOTestIT extends BankModuleApplicationTests {

    @Autowired
    CreditlineDAO creditlineDAO
    @Autowired
    ClientDAO clientDAO
    static private firstCount
    static private Client savedClient
    static private Long creditlineId
    static private Long creditlineId2
    def setup() {

        firstCount = creditlineDAO.findCreditlineCount()
    }

    def "Save creditline"() {

        given:
        Client client = new Client( name: "TestName", surname: "TestSurname", itn: "121212", phoneNumber: "123123")
         savedClient = clientDAO.save(client)

        Creditline creditline = new Creditline(openDate: new Date(2017,07,26), amount: 20000, percent: 10,
                monthDuration: 10, state: CreditlineState.OPENED,type: "annuit",
                currency: Currency.USD, client: savedClient)
        Creditline creditline2 = new Creditline(openDate: new Date(2017,07,26), amount: 20000, percent: 10,
                monthDuration: 10, state: CreditlineState.CLOSED,type: "annuit",closeDate: new Date(2017,07,27),
                currency: Currency.UAH, client: savedClient)
        when:
        Creditline savedCreditline = creditlineDAO.save(creditline)
        creditlineId = savedCreditline.id
        Creditline savedCreditline2 = creditlineDAO.save(creditline2)
        creditlineId2 = savedCreditline2.id
        then:
        firstCount +2 == creditlineDAO.findCreditlineCount()
        savedCreditline.currency == Currency.USD.toString()
        savedCreditline.state == CreditlineState.OPENED
        savedCreditline.client.itn == "121212"
        savedCreditline.client.name == "TestName"
        savedCreditline.client.surname == "TestSurname"
        savedCreditline.client.phoneNumber == "123123"
        !savedCreditline.payments
        savedCreditline2.currency == Currency.UAH.toString()
        savedCreditline2.state == CreditlineState.CLOSED
        savedCreditline2.closeDate == new Date(2017,07,27)
        savedCreditline2.client.itn == "121212"
    }

    def "Get All Client Creditlines "() {

        when:
        List<Creditline> creditlines = creditlineDAO.getAllClientCreditline(savedClient)
        then:
        creditlines.size() == 2
        creditlines[0].currency == Currency.USD.toString()
        creditlines[0].state == CreditlineState.OPENED
        creditlines[0].client.itn == "121212"
        creditlines[0].client.name == "TestName"
        creditlines[0].client.surname == "TestSurname"
        creditlines[0].client.phoneNumber == "123123"
        creditlines[1].currency == Currency.UAH.toString()
        creditlines[1].state == CreditlineState.CLOSED
        creditlines[1].closeDate == new Date(2017,07,27)
        creditlines[1].client.itn == "121212"
    }

    def "Get All Client Current Creditlines  "() {

        when:
        List<Creditline> creditlines = creditlineDAO.getAllClientCurrentCreditline(savedClient)
        then:
        creditlines.size() == 1
        creditlines[0].client.itn == "121212"
        creditlines[0].client.name == "TestName"
        creditlines[0].client.surname == "TestSurname"
        creditlines[0].client.phoneNumber == "123123"
        creditlines[0].currency == Currency.USD.toString()
        creditlines[0].state == CreditlineState.OPENED
    }

    def "Get creditline by Id"() {

        when:
        Creditline foundCreditline = creditlineDAO.getById(creditlineId)
        then:
        foundCreditline.currency == Currency.USD.toString()
        foundCreditline.state == CreditlineState.OPENED
        foundCreditline.client.itn == "121212"
        foundCreditline.client.name == "TestName"
        foundCreditline.client.surname == "TestSurname"
        foundCreditline.client.phoneNumber == "123123"
        !foundCreditline.payments
    }

    def "Get creditlines by Open Date"() {

        when:
        Date openDate = new Date(2017,07,26)
        List<Creditline> creditlines = creditlineDAO.getByOpenDate(openDate)
        then:
        creditlines.size() == 2
        creditlines[0].currency == Currency.USD.toString()
        creditlines[0].state == CreditlineState.OPENED
        creditlines[0].client.itn == "121212"
        creditlines[0].client.name == "TestName"
        creditlines[0].client.surname == "TestSurname"
        creditlines[0].client.phoneNumber == "123123"
        creditlines[0].openDate == new Date(2017,07,26)
        creditlines[1].currency == Currency.UAH.toString()
        creditlines[1].state == CreditlineState.CLOSED
        creditlines[1].openDate == new Date(2017,07,26)
        creditlines[1].client.itn == "121212"
    }

    def "Get creditlines by Open Date if not found any creditline"() {

        when:
        Date openDate = new Date(2017,06,26)
        List<Creditline> creditlines = creditlineDAO.getByOpenDate(openDate)
        then:
        creditlines.size() == 0
    }

    def "Get creditlines by Close Date"() {

        when:
        Date closeDate = new Date(2017,07,27)
        List<Creditline> creditlines = creditlineDAO.getByCloseDate(closeDate)
        then:
        creditlines.size() == 1
        creditlines[0].currency == Currency.UAH.toString()
        creditlines[0].state == CreditlineState.CLOSED
        creditlines[0].client.itn == "121212"
        creditlines[0].client.name == "TestName"
        creditlines[0].client.surname == "TestSurname"
        creditlines[0].client.phoneNumber == "123123"
        creditlines[0].closeDate == new Date(2017,07,27)
    }

    def "Get creditlines by Close Date if not found any creditline"() {

        when:
        Date closeDate = new Date(2017,06,25)
        List<Creditline> creditlines = creditlineDAO.getByCloseDate(closeDate)
        then:
        creditlines.size() == 0
    }

    def "Delete creditline"() {

        when:
        Boolean isDeleted = creditlineDAO.delete(creditlineId)
        Boolean isDeleted2 = creditlineDAO.delete(creditlineId2)
        clientDAO.deleteClient(savedClient)
        then:
        !isDeleted
        !isDeleted2
        !creditlineDAO.getById(creditlineId)
        !creditlineDAO.getById(creditlineId2)
    }
}
