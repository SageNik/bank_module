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
import com.bank.model.view.CreditlineViewModel
import com.bank.model.view.PaymentViewModel
import org.hibernate.HibernateException
import spock.lang.Specification

import javax.persistence.EntityNotFoundException
import java.sql.Date
import java.sql.Timestamp

/**
 * Created by Ник on 28.07.2017.
 */
class PaymentServiceImplUnitTest extends Specification {

   final private static openDate = new Date(Timestamp.valueOf("2017-07-26 0:0:0.00").getTime())
   final private static closeDate = new Date(Timestamp.valueOf("2017-07-27 0:0:0.00").getTime())
    private PaymentDAO paymentDAO
    private ClientDAO clientDAO
    private CreditlineDAO creditlineDAO
    private PaymentServiceImpl paymentService

    def setup(){

        paymentService = new PaymentServiceImpl()
        paymentDAO = Stub(PaymentDAO)
        paymentService.setPaymentDAO(paymentDAO)
        clientDAO = Stub(ClientDAO)
        paymentService.setClientDAO(clientDAO)
        creditlineDAO = Stub(CreditlineDAO)
        paymentService.setCreditlineDAO(creditlineDAO)

        clientDAO.findByItn(_)>> { String itnClient ->
            if (itnClient == "121212") {
                return new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")
            } else {
                null
            }
        }

        creditlineDAO.getAllClientCreditline(_)>> [(new Creditline(openDate: openDate, amount: 10000, percent: 12, monthDuration: 12,state: CreditlineState.OPENED,
                currency: Currency.UAH, id: 1, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123"))),
                                                   (new Creditline(openDate: openDate, amount: 20000, percent: 10, monthDuration: 10, state: CreditlineState.CLOSED,
                                                           currency: Currency.USD, id: 2, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")))]

        paymentDAO.getAllByCreditline(_)>>[new Payment(id: 1,date: openDate,value: 60,
                creditline: new Creditline(id: 1,client: new Client(id: 1,itn: "121212"))), new Payment(id: 2,date: closeDate,value: 50,
                creditline: new Creditline(id: 2,client: new Client(id: 1,itn: "121212")))]

        creditlineDAO.getById(_)>>{Long creditlineId ->
            if(creditlineId == 1){
                return new Creditline(openDate: openDate, amount: 20000, percent: 10, monthDuration: 10, state: CreditlineState.OPENED,
                        currency: Currency.USD, id: 2, client: new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123"))
            }else{
                throw new EntityNotFoundException("Entity not found")
            }
        }
    }
    def "Get All Client Payments with correct input data"() {

         when:
         String itnClient = "121212"
         PaymentViewModel viewModel = paymentService.getAllClientPayments(itnClient)
         then:
         viewModel.itnClient == "121212"
         viewModel.payments.size() == 4
         viewModel.payments[0].id == 1
         viewModel.payments[0].value == 60
         viewModel.payments[0].date == openDate
         viewModel.payments[2].id == 2
         viewModel.payments[2].value == 50
         viewModel.payments[2].date == closeDate
    }

    def "Get All Client Payments with correct input data if client with itn not exist"() {

        when:
        String itnClient = "111"
        PaymentViewModel viewModel = paymentService.getAllClientPayments(itnClient)
        then:
        !viewModel.payments
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.currentMessage == "failGetPaymentsByClient"
        viewModel.message
    }

    def "Get All Client Payments with correct input data if client  itn not correct"() {

        when:
        String itnClient = null
        PaymentViewModel viewModel = paymentService.getAllClientPayments(itnClient)
        then:
        !viewModel.payments
        viewModel.errorDataMessage == "errorAdminMesGetPaym"
        !viewModel.message
    }

    def "Add Payment with correct input data"() {

        given:
        paymentDAO.addNewPayment(_)>>new Payment(id: 1,date: openDate,value: 60,
                creditline: new Creditline(id: 1,client: new Client(id: 1,itn: "121212")))
        when:
        Long creditlineId = 1
        BigDecimal amount = 60
        CreditlineViewModel viewModel = paymentService.addPayment(creditlineId, amount)
        then:
        viewModel.currentMessage == "successMesAddPayment"
        viewModel.messageType == ViewConstant.MESS_SUCCESS
        !viewModel.message
        viewModel.messageAddPayment
    }

    def "Add Payment with correct input data and fail add new payment"() {

        given:
        paymentDAO.addNewPayment(_)>>{throw new HibernateException("Fail add payment")}
        when:
        Long creditlineId = 1
        BigDecimal amount = 60
        CreditlineViewModel viewModel = paymentService.addPayment(creditlineId, amount)
        then:
        viewModel.currentMessage == "errorMesAddPayment"
        viewModel.messageType == ViewConstant.MESS_ERROR
        !viewModel.message
        viewModel.messageAddPayment
    }

    def "Add Payment with correct input data if creditline with id not exist"() {

        when:
        Long creditlineId = 5
        BigDecimal amount = 60
        CreditlineViewModel viewModel = paymentService.addPayment(creditlineId, amount)
        then:
        viewModel.currentMessage == "errorMesAddPayment"
        viewModel.messageType == ViewConstant.MESS_ERROR
        !viewModel.message
        viewModel.messageAddPayment
    }

    def "Add Payment with uncorrect input data "() {

        when:
        Long creditlineId = null
        BigDecimal amount = null
        CreditlineViewModel viewModel = paymentService.addPayment(creditlineId, amount)
        then:
        viewModel.errorDataMessage == "errorAdminMesAddPaym"
        !viewModel.message
        !viewModel.messageAddPayment
    }

    def "Delete Payment with correct input data"() {

        given:
        paymentDAO.deletePayment(_)>> true
        when:
        Long creditlineId = 1
        Long paymentId = 1
        CreditlineViewModel viewModel = paymentService.deletePayment(paymentId,creditlineId)
        then:
        viewModel.currentMessage == "successMesDelPayment"
        viewModel.messageType == ViewConstant.MESS_SUCCESS
        viewModel.messageDelPayment
        !viewModel.messageAddPayment
        !viewModel.message
    }

    def "Delete Payment with correct input data and fail delete payment"() {

        given:
        paymentDAO.deletePayment(_)>> {throw new HibernateException("Fail delete payment")}
        when:
        Long creditlineId = 1
        Long paymentId = 1
        CreditlineViewModel viewModel = paymentService.deletePayment(paymentId,creditlineId)
        then:
        viewModel.currentMessage == "errorMesDelPayment"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.messageDelPayment
        !viewModel.messageAddPayment
        !viewModel.message
    }

    def "Delete Payment with correct input data if creditline by id not exist"() {

        given:
        paymentDAO.deletePayment(_)>> true
        when:
        Long creditlineId = 5
        Long paymentId = 1
        CreditlineViewModel viewModel = paymentService.deletePayment(paymentId,creditlineId)
        then:
        viewModel.currentMessage == "errorMesDelPayment"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.messageDelPayment
        !viewModel.messageAddPayment
        !viewModel.message
    }

    def "Delete Payment with uncorrect input data "() {

        when:
        Long creditlineId = null
        Long paymentId = null
        CreditlineViewModel viewModel = paymentService.deletePayment(paymentId,creditlineId)
        then:
        viewModel.errorDataMessage == "errorAdminMesDelPaym"
        !viewModel.messageDelPayment
        !viewModel.messageAddPayment
        !viewModel.message
    }
}
