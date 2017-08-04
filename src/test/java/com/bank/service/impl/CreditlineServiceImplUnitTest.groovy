package com.bank.service.impl

import com.bank.constant.ViewConstant
import com.bank.dao.ClientDAO
import com.bank.dao.CreditlineDAO
import com.bank.dao.PaymentDAO
import com.bank.entity.Client
import com.bank.entity.Creditline
import com.bank.entity.Payment
import com.bank.enumeration.CreditlineState
import com.bank.enumeration.Currency
import com.bank.model.dto.CreditlineDTO
import com.bank.model.view.CalculateViewModel
import com.bank.model.view.ClientViewModel
import com.bank.model.view.CreditlineViewModel
import com.bank.service.interfaces.CalculateService
import com.bank.utils.CalculatedPaymentModel
import org.hibernate.HibernateException
import spock.lang.Specification

import javax.persistence.EntityNotFoundException
import java.sql.Date
import java.sql.Timestamp

/**
 * Created by Ник on 26.07.2017.
 */
class CreditlineServiceImplUnitTest extends Specification {

    final private static openDate = new Date(Timestamp.valueOf("2017-07-26 0:0:0.00").getTime())
    final private static closeDate = new Date(Timestamp.valueOf("2017-07-27 0:0:0.00").getTime())
    private CreditlineDAO creditlineDAO
    private CreditlineServiceImpl creditlineService
    private ClientDAO clientDAO
    private PaymentDAO paymentDAO
    private CalculateService calculateService

    def setup(){
        creditlineService = new CreditlineServiceImpl()
        creditlineDAO = Stub(CreditlineDAO)
        creditlineService.setCreditlineDAO(creditlineDAO)
        clientDAO = Stub(ClientDAO)
        creditlineService.setClientDAO(clientDAO)
        paymentDAO = Stub(PaymentDAO)
        creditlineService.setPaymentDAO(paymentDAO)
        calculateService = Stub(CalculateService)
        creditlineService.setCalculateService(calculateService)

        clientDAO.findByItn(_)>> {String itnClient ->
            if(itnClient=="121212"){
                return new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")
            }else{
                throw new EntityNotFoundException("The client with itn not found")
            }
        }

        calculateService.calculatePayments(_)>> new CalculateViewModel(fullCost: 100, overpayment: 10,
                calculatedPayments: [new CalculatedPaymentModel(number: 1,residualCredit: 100,amortization: 50,percents: 10, monthPayment: 60),
                                     new CalculatedPaymentModel(number: 2,residualCredit: 50,amortization: 50,percents: 10, monthPayment: 60)])

        creditlineDAO.getById(_)>>{Long creditlineId ->
            if(creditlineId == 1){
                return new Creditline(openDate: new Date(2017,07,26), amount: 20000, percent: 10, monthDuration: 10, state: CreditlineState.OPENED,
                        currency: Currency.USD, id: 2, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123"))
            }else{
                throw new EntityNotFoundException("Entity not found")
            }
        }

        creditlineDAO.getByOpenDate(_)>>[(new Creditline(openDate: openDate, amount: 10000, percent: 12, monthDuration: 12,state: CreditlineState.OPENED,
                currency: Currency.UAH, id: 1, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123"))),
                                         (new Creditline(openDate: openDate, amount: 20000, percent: 10, monthDuration: 10, state: CreditlineState.OPENED,
                                                 currency: Currency.USD, id: 2, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")))]

        creditlineDAO.getByCloseDate(_)>>[(new Creditline(openDate: openDate,closeDate:closeDate,  amount: 30000, percent: 12, monthDuration: 12,state: CreditlineState.OPENED,
                currency: Currency.UAH, id: 1, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")))]
    }

    def "GetCreditlineCount"() {
        given:
        creditlineDAO.findCreditlineCount()>> 3
        when:
        CreditlineViewModel viewModel = creditlineService.creditlineCount
        then:
        viewModel.creditlineCount == 3
    }

    def "Add New Creditline with correct input data"() {
        given:
            creditlineDAO.save(_)>> new Creditline(openDate: openDate, amount: 10000,percent: 12, monthDuration: 12,
                currency: Currency.UAH, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123") )
        when:
            CreditlineDTO creditlineDTO = new CreditlineDTO(id: 1, itnClient:"121212", openDate: openDate, type: "annuit",
                amount: 10000, duration: 12, percent: 12, currency: Currency.UAH)
        ClientViewModel viewModel = creditlineService.addNewCreditline(creditlineDTO)
        then:
            viewModel.clients.size() == 1
            viewModel.clients[0].itn == "121212"
            viewModel.clients[0].name == "TestClient"
            viewModel.clients[0].surname == "Testovich"
            viewModel.currentMessage == "successMesAddCredlin"
            viewModel.messageType == ViewConstant.MESS_SUCCESS
            viewModel.message
    }

    def "Add New Creditline with correct input data and fail save new creditline"() {
        given:
        creditlineDAO.save(_)>>  new HibernateException("Save failed")
        when:
        CreditlineDTO creditlineDTO = new CreditlineDTO(id: 1, itnClient: "121212", openDate: openDate, type: "annuit",
                amount: 10000, duration: 12, percent: 12, currency: Currency.UAH)
        ClientViewModel viewModel = creditlineService.addNewCreditline(creditlineDTO)
        then:
        viewModel.clients.size() == 1
        viewModel.clients[0].itn == "121212"
        viewModel.clients[0].name == "TestClient"
        viewModel.clients[0].surname == "Testovich"
        viewModel.currentMessage == "failAddCredlin"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Add New Creditline with uncorrect input data "() {

        when:
        CreditlineDTO creditlineDTO = new CreditlineDTO()
        ClientViewModel viewModel = creditlineService.addNewCreditline(creditlineDTO)
        then:
        viewModel.errorDataMessage == "errorAdminMesAdd"
        !viewModel.message
    }

    def "Add New Creditline if client not found by itn "() {
        when:
        CreditlineDTO creditlineDTO = new CreditlineDTO(id: 1, itnClient: "1111", openDate: openDate, type: "annuit",
                amount: 10000, duration: 12, percent: 12, currency: Currency.UAH)
        ClientViewModel viewModel = creditlineService.addNewCreditline(creditlineDTO)
        then:
        viewModel.clients == null
        viewModel.currentMessage == "failAddCredlin"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Get All Client Creditline if client with itn exist "() {

        given:
        creditlineDAO.getAllClientCreditline(_)>>
                  [(new Creditline(openDate:openDate, amount: 10000, percent: 12, monthDuration: 12,state: CreditlineState.OPENED,
                        currency: Currency.UAH, id: 1, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123"))),
                 (new Creditline(openDate: openDate, amount: 20000, percent: 10, monthDuration: 10, state: CreditlineState.CLOSED,
                         currency: Currency.USD, id: 2, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")))]

        when:
        String itnClient = "121212"
        CreditlineViewModel viewModel = creditlineService.getAllClientCreditline(itnClient)
        then:
        viewModel.creditlines.size() == 2
        viewModel.creditlines[0].itnClient == "121212"
        viewModel.creditlines[0].id == 1
        viewModel.creditlines[0].currency == "UAH"
        viewModel.creditlines[0].openDate == openDate
        viewModel.creditlines[0].amount == 10000
        viewModel.creditlines[0].percent == 12
        viewModel.creditlines[0].duration == 12
        viewModel.creditlines[1].id == 2
        viewModel.creditlines[1].itnClient == "121212"
        viewModel.creditlines[1].currency == "USD"
        viewModel.creditlines[1].amount == 20000
        viewModel.creditlines[1].percent == 10
        viewModel.creditlines[1].duration == 10
    }

    def "Get All Client Creditline if client with itn not exist "() {

        when:
        String itnClient = "1111"
        CreditlineViewModel viewModel = creditlineService.getAllClientCreditline(itnClient)
        then:
        viewModel.creditlines.size() == 0
        viewModel.currentMessage == "failGetClientCredlins"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Get All Client Creditline if client itn not correct "() {

        when:
        String itnClient = null
        CreditlineViewModel viewModel = creditlineService.getAllClientCreditline(itnClient)
        then:
        !viewModel.creditlines
        viewModel.currentMessage == "failGetClientCredlins"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Ge tAll Client Current Creditlines if client with itn exist"() {
        given:
        creditlineDAO.getAllClientCurrentCreditline(_)>>
                [(new Creditline(openDate: openDate, amount: 10000, percent: 12, monthDuration: 12,state: CreditlineState.OPENED,
                        currency: Currency.UAH, id: 1, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123"))),
                 (new Creditline(openDate: openDate, amount: 20000, percent: 10, monthDuration: 10, state: CreditlineState.OPENED,
                         currency: Currency.USD, id: 2, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")))]

        when:
        String itnClient = "121212"
        CreditlineViewModel viewModel = creditlineService.getAllClientCurrentCreditline(itnClient)
        then:
        viewModel.creditlines.size() == 2
        viewModel.creditlines[0].itnClient == "121212"
        viewModel.creditlines[0].id == 1
        viewModel.creditlines[0].currency == "UAH"
        viewModel.creditlines[0].openDate == openDate
        viewModel.creditlines[0].amount == 10000
        viewModel.creditlines[0].percent == 12
        viewModel.creditlines[0].duration == 12
        viewModel.creditlines[1].id == 2
        viewModel.creditlines[1].itnClient == "121212"
        viewModel.creditlines[1].currency == "USD"
        viewModel.creditlines[1].amount == 20000
        viewModel.creditlines[1].percent == 10
        viewModel.creditlines[1].duration == 10
    }

    def "Get All Client Current Creditlines if client with itn not exist "() {

        when:
        String itnClient = "1111"
        CreditlineViewModel viewModel = creditlineService.getAllClientCurrentCreditline(itnClient)
        then:
        viewModel.creditlines.size() == 0
        viewModel.currentMessage == "failGetClientCurrentCreditlines"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Get All ClientCurrent Creditlines if client itn not correct "() {

        when:
        String itnClient = null
        CreditlineViewModel viewModel = creditlineService.getAllClientCurrentCreditline(itnClient)
        then:
        !viewModel.creditlines
        viewModel.currentMessage == "failGetClientCurrentCreditlines"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Prepare To Show Creditline if creditline by id exist"() {

        given:
        paymentDAO.getAllByCreditlineId(_)>>[new Payment(id: 1,date: openDate,value: 60,
                creditline: new Creditline(id: 1,client: new Client(id: 1,itn: "121212"))),new Payment(id: 2,date: closeDate,value: 50,
                creditline: new Creditline(id: 1,client: new Client(id: 1,itn: "121212")))]
        when:
        CreditlineViewModel viewModel = creditlineService.prepareToShowCreditline(1,new CreditlineViewModel(
                creditlines: [new CreditlineDTO(amount: 20000,percent: 10,duration: 12,type: "anniut",id: 1)]))
        then:
        viewModel.overpayment == 10
        viewModel.fullCost == 100
        viewModel.fullPayments.size() == 2
        viewModel.fullPayments.payment[0].value == 60
        viewModel.fullPayments.payment[1].value == 50
        viewModel.fullPayments.payment[0].date == openDate
        viewModel.fullPayments.payment[1].date == closeDate
        viewModel.fullPayments.calculatedPayment[0].monthPayment == 60
        viewModel.fullPayments.calculatedPayment[1].monthPayment == 60
        viewModel.fullPayments.calculatedPayment[0].residualCredit == 100
        viewModel.fullPayments.calculatedPayment[1].residualCredit == 50
        viewModel.fullPayments.calculatedPayment[0].amortization == 50
        viewModel.fullPayments.calculatedPayment[1].amortization == 50
        viewModel.fullPayments.calculatedPayment[0].percents == 10
        viewModel.fullPayments.calculatedPayment[1].percents == 10

    }

    def "Prepare To Show Creditline if creditline by id not exist"() {

        when:
        CreditlineViewModel viewModel = creditlineService.prepareToShowCreditline(2,new CreditlineViewModel(
                creditlines: [new CreditlineDTO(amount: 20000,percent: 10,duration: 12,type: "anniut",id: 1)]))
        then:
        viewModel.currentMessage == "errorMesShowCreditline"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Prepare To Show Creditline if creditline id is null"() {

        when:
        CreditlineViewModel viewModel = creditlineService.prepareToShowCreditline(null,new CreditlineViewModel())
        then:
        viewModel.errorDataMessage == "errorAdminMesShow"
        !viewModel.message
    }

    def "Delete Creditline with correct input data"() {

        given:
        creditlineDAO.delete(_)>> true
        when:
        Long creditlineId = 1
        String itnClient = "121212"
        ClientViewModel viewModel = creditlineService.deleteCreditline(creditlineId,itnClient)
        then:
        viewModel.clients.size() == 1
        viewModel.clients[0].itn == "121212"
        viewModel.clients[0].name == "TestClient"
        viewModel.clients[0].surname == "Testovich"
        viewModel.currentMessage == "successMesDelCredlin"
        viewModel.messageType == ViewConstant.MESS_SUCCESS
        viewModel.message
    }

    def "Delete Creditline with correct input data and fail delete credit line"() {

        given:
        creditlineDAO.delete(_)>>{throw new HibernateException("Fail delete creditline")}
        when:
        Long creditlineId = 1
        String itnClient = "121212"
        ClientViewModel viewModel = creditlineService.deleteCreditline(creditlineId,itnClient)
        then:
        viewModel.clients.size() == 1
        viewModel.clients[0].itn == "121212"
        viewModel.clients[0].name == "TestClient"
        viewModel.clients[0].surname == "Testovich"
        viewModel.currentMessage == "errorMesDelCredlin"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Delete Creditline with uncorrect input data "() {

        when:
        Long creditlineId = null
        String itnClient = 111
        ClientViewModel viewModel = creditlineService.deleteCreditline(creditlineId,itnClient)
        then:
        viewModel.errorDataMessage == "errorAdminMesDel"
        !viewModel.message
    }

    def "Delete Creditline if client not found by itn "() {
        when:
        Long creditlineId = 1
        String itnClient = "111"
        ClientViewModel viewModel = creditlineService.deleteCreditline(creditlineId,itnClient)
        then:
        viewModel.clients == null
        viewModel.currentMessage == "errorMesDelCredlin"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Close Creditline with correct input data"() {
        given:
        creditlineDAO.save(_)>> new Creditline(openDate: openDate,closeDate: closeDate,state: CreditlineState.CLOSED, amount: 10000,percent: 12, monthDuration: 12,
                currency: Currency.UAH, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123") )
        when:
        Long creditlineId = 1
        String itnClient = "121212"
        ClientViewModel viewModel = creditlineService.closeCreditline(creditlineId,itnClient)
        then:
        viewModel.clients.size() == 1
        viewModel.clients[0].itn == "121212"
        viewModel.clients[0].name == "TestClient"
        viewModel.clients[0].surname == "Testovich"
        viewModel.currentMessage == "successMesClosCredlin"
        viewModel.messageType == ViewConstant.MESS_SUCCESS
        viewModel.message
    }

    def "Close Creditline with correct input data and fail delete credit line"() {

        given:
        creditlineDAO.save(_)>> {throw new HibernateException("Credit line not closed")}
        when:
        Long creditlineId = 1
        String itnClient = "121212"
        ClientViewModel viewModel = creditlineService.closeCreditline(creditlineId,itnClient)
        then:
        viewModel.clients.size() == 1
        viewModel.clients[0].itn == "121212"
        viewModel.clients[0].name == "TestClient"
        viewModel.clients[0].surname == "Testovich"
        viewModel.currentMessage == "errorMesClosCredlin"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Close Creditline with uncorrect input data "() {

        when:
        Long creditlineId = null
        String itnClient = 111
        ClientViewModel viewModel = creditlineService.closeCreditline(creditlineId,itnClient)
        then:
        viewModel.errorDataMessage == "errorAdminMesClos"
        !viewModel.message
    }

    def "Close Creditline if client not found by itn "() {
        when:
        Long creditlineId = 1
        String itnClient = "111"
        ClientViewModel viewModel = creditlineService.closeCreditline(creditlineId,itnClient)
        then:
        viewModel.clients == null
        viewModel.currentMessage == "errorMesClosCredlin"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Search Creditline with correct input data and search criteria: 'openDate'"() {

        given:
        creditlineDAO.getByOpenDate(_)>>[(new Creditline(openDate: openDate, amount: 10000, percent: 12, monthDuration: 12,state: CreditlineState.OPENED,
                currency: Currency.UAH, id: 1, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123"))),
                 (new Creditline(openDate: openDate, amount: 20000, percent: 10, monthDuration: 10, state: CreditlineState.OPENED,
                 currency: Currency.USD, id: 2, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")))]

        creditlineDAO.getByCloseDate(_)>>[(new Creditline(openDate: openDate,closeDate:closeDate,  amount: 30000, percent: 12, monthDuration: 12,state: CreditlineState.OPENED,
           currency: Currency.UAH, id: 1, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")))]

        when:
        Date inputDate = openDate
        String searchCriteria = "openDate"
        CreditlineViewModel viewModel = creditlineService.searchCreditline(searchCriteria,inputDate)
        then:
        viewModel.creditlines.size() == 2
        viewModel.creditlines[0].openDate == openDate
        viewModel.creditlines[0].amount == 10000
        viewModel.creditlines[0].closeDate == null
        viewModel.creditlines[1].openDate == openDate
        viewModel.creditlines[1].amount == 20000
        viewModel.creditlines[1].closeDate == null
        viewModel.answer
    }

    def "Search Creditline with correct input data and search criteria: 'closeDate'"() {

        when:
        Date inputDate = openDate
        String searchCriteria = "closeDate"
        CreditlineViewModel viewModel = creditlineService.searchCreditline(searchCriteria,inputDate)
        then:
        viewModel.creditlines.size() == 1
        viewModel.creditlines[0].openDate == openDate
        viewModel.creditlines[0].amount == 30000
        viewModel.creditlines[0].closeDate == closeDate
        viewModel.answer
    }

    def "Search Creditline with correct input data and search criteria: 'notExist'"() {

        when:
        Date inputDate = openDate
        String searchCriteria = "notExist"
        CreditlineViewModel viewModel = creditlineService.searchCreditline(searchCriteria,inputDate)
        then:
        viewModel.creditlines.size() == 0
        viewModel.answer
    }

    def "Search Creditline with uncorrect input data and search criteria" () {

        when:
        Date inputDate = null
        String searchCriteria = 111
        CreditlineViewModel viewModel = creditlineService.searchCreditline(searchCriteria,inputDate)
        then:
        !viewModel.creditlines
        viewModel.currentMessage == "errorMesSearchCreditline"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
        !viewModel.answer
    }
}
