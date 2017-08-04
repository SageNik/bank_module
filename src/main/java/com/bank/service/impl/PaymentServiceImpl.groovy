package com.bank.service.impl

import com.bank.dao.ClientDAO
import com.bank.dao.CreditlineDAO
import com.bank.dao.PaymentDAO
import com.bank.entity.Client
import com.bank.entity.Creditline
import com.bank.entity.Payment
import com.bank.model.dto.PaymentDTO
import com.bank.model.view.BaseViewModel
import com.bank.model.view.CreditlineViewModel
import com.bank.model.view.PaymentViewModel
import com.bank.service.interfaces.PaymentService
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional
import java.sql.Date

/**
 * Created by Ник on 21.07.2017.
 */
@Slf4j
@Service
class PaymentServiceImpl implements PaymentService{

    @Autowired
     PaymentDAO paymentDAO
    @Autowired
     CreditlineDAO creditlineDAO
    @Autowired
     ClientDAO clientDAO

    @Override
    PaymentViewModel getAllClientPayments(String itnClient) {

        PaymentViewModel viewModel = new PaymentViewModel()
        if(!StringUtils.isBlank(itnClient)) {
            viewModel = getClientPayments(itnClient, viewModel)
        }else{
            viewModel.errorDataMessage = "errorAdminMesGetPaym"
            log.debug("Fail get all payments for client")
        }
        return viewModel
    }

    private PaymentViewModel getClientPayments(String itnClient, PaymentViewModel viewModel) {
        try {
            log.debug("Try to get all payments by client with itn:[${itnClient}]")
            Client client = findCurrentClient(itnClient)
            List<Creditline> creditlines = creditlineDAO.getAllClientCreditline(client)

            viewModel = findAllPaymentsByCreditlines(creditlines)
            Collections.sort(viewModel.payments)
            viewModel.itnClient = itnClient
            log.debug("The payments with size: [${viewModel.payments.size()}] success found")
        } catch (Exception ignore) {
            viewModel = BaseViewModel.writeErrorMessToViewModel("failGetPaymentsByClient", viewModel) as PaymentViewModel
            log.debug("Fail to get all payments by credit lines for client with itn:[${itnClient}]")
        }
        return viewModel
    }

    private PaymentViewModel findAllPaymentsByCreditlines(List<Creditline> creditlines) {
        PaymentViewModel viewModel = new PaymentViewModel()
        viewModel.payments = new ArrayList<>()
        creditlines.each {
            List<Payment> payments = paymentDAO.getAllByCreditline(it)
            payments.each { viewModel.payments.add(PaymentDTO.buildFromPayment(it)) }
        }
       return viewModel
    }

    private Client findCurrentClient(String itnClient) {
        Client currentClient = clientDAO.findByItn(itnClient)
        if (currentClient) {
            return currentClient
        } else {
            log.debug("Fail to find client with itn:[${itnClient}]")
            throw new EntityNotFoundException("The client with itn:[${itnClient}] not found")
        }
    }

    @Override
    @Transactional
    CreditlineViewModel addPayment(Long creditlineId, BigDecimal amount) {

        CreditlineViewModel viewModel = new CreditlineViewModel()
        if(creditlineId && amount){
            viewModel = addNewPayment(amount, creditlineId, viewModel)
            viewModel.messageAddPayment = true
            viewModel.message = false
        }else {
            log.debug("Fail to add payment amount: [${amount}] to creditline with id: [${creditlineId}]")
            viewModel.errorDataMessage = "errorAdminMesAddPaym"
        }
        return viewModel
    }

    private CreditlineViewModel addNewPayment(BigDecimal amount, Long creditlineId, CreditlineViewModel viewModel) {

        try {
            log.debug("Try to add payment amount: [${amount}] to creditline with id: [${creditlineId}]")

            Creditline creditline = getCurrentCreditline(creditlineId)
            Payment payment = new Payment()
            payment.date = new Date(new java.util.Date().getTime())
            payment.value = amount*100
            payment.creditline = creditline

            paymentDAO.addNewPayment(payment)
            log.debug("The payment amount: [${amount}] to creditline with id: [${creditlineId}] success added")
            viewModel = BaseViewModel.writeSuccessMessToViewModel("successMesAddPayment", viewModel)as CreditlineViewModel
        } catch (Exception ignore) {
            log.debug("Fail to add payment amount: [${amount}] to creditline with id: [${creditlineId}]")
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorMesAddPayment", viewModel)as CreditlineViewModel
        }
        return viewModel
    }

    private Creditline getCurrentCreditline(Long creditlineId) {
        Creditline currentCreditline = creditlineDAO.getById(creditlineId)
        if (currentCreditline) {
            return currentCreditline
        } else {
            log.debug("The credit line with id: [${creditlineId}] not found")
            throw new EntityNotFoundException("The credit line with id: [${creditlineId}] not found")
        }
    }

    @Override
    @Transactional
    CreditlineViewModel deletePayment(Long paymentId,Long creditlineId) {

        CreditlineViewModel viewModel = new CreditlineViewModel()
        if(paymentId && creditlineId) {
            viewModel = delete(paymentId, creditlineId, viewModel)
            viewModel.messageDelPayment = true
            viewModel.message = false
        }else{
            log.debug("Fail to delete payment with id: [${paymentId}]")
            viewModel.errorDataMessage = "errorAdminMesDelPaym"
        }
        return viewModel
    }

    private CreditlineViewModel delete(Long paymentId, Long creditlineId, CreditlineViewModel viewModel) {
        try {
            log.debug("Try to delete payment with id: [${paymentId}]")
            Creditline creditline = getCurrentCreditline(creditlineId)
            Payment currentPayment = getCurrentPayment(paymentId)

            paymentDAO.deletePayment(paymentId)
            viewModel = BaseViewModel.writeSuccessMessToViewModel("successMesDelPayment", viewModel) as CreditlineViewModel
            log.debug("The payment with id: [${paymentId}] success deleted")
        } catch (Exception ignore) {
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorMesDelPayment", viewModel) as CreditlineViewModel
            log.debug("Fail to delete payment with id: [${paymentId}]")
        }
        return viewModel
    }

    private Payment getCurrentPayment(Long paymentId) {
        Payment currentPayment = paymentDAO.getById(paymentId)
        if (currentPayment) {
            return currentPayment
        } else {
            log.debug("The payment with id: [${paymentId}] not found")
            throw new EntityNotFoundException("The payment with id: [${paymentId}] not found")
        }
    }
}
