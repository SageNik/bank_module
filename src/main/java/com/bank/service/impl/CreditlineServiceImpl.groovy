package com.bank.service.impl

import com.bank.dao.ClientDAO
import com.bank.dao.CreditlineDAO
import com.bank.dao.PaymentDAO
import com.bank.entity.Client
import com.bank.entity.Creditline
import com.bank.entity.Payment
import com.bank.enumeration.CreditlineState
import com.bank.model.FullPaymentModel
import com.bank.model.dto.ClientDTO
import com.bank.model.dto.PaymentDTO
import com.bank.model.view.BaseViewModel
import com.bank.model.view.CalculateViewModel
import com.bank.model.view.ClientViewModel
import com.bank.model.dto.CreditlineDTO
import com.bank.model.view.CreditlineViewModel
import com.bank.service.interfaces.CalculateService
import com.bank.service.interfaces.CreditlineService
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional
import java.sql.Date

/**
 * Created by Ник on 19.07.2017.
 */
@Slf4j
@Service
class CreditlineServiceImpl implements CreditlineService {

    @Autowired
    CreditlineDAO creditlineDAO
    @Autowired
    ClientDAO clientDAO
    @Autowired
    PaymentDAO paymentDAO
    @Autowired
    CalculateService calculateService

    @Override
    CreditlineViewModel getCreditlineCount() {

        log.debug("Getting count of registered credit lines")
        CreditlineViewModel viewModel = new CreditlineViewModel()
        viewModel.creditlineCount = creditlineDAO.findCreditlineCount()
        log.debug("Found registered credit lines: [${viewModel.creditlineCount}]")
        return viewModel
    }

    @Override
    @Transactional
    ClientViewModel addNewCreditline(CreditlineDTO creditlineDTO) throws EntityNotFoundException {

        ClientViewModel viewModel = new ClientViewModel()
        if (!StringUtils.isBlank(creditlineDTO.itnClient) && creditlineDTO.amount && creditlineDTO.duration) {
            log.debug("Try to add new creditline to client with itn: [${creditlineDTO.itnClient}]")

            viewModel = save(creditlineDTO)
        } else {
            log.debug("Fail to save creditline for client. Not correct input data")
            viewModel.errorDataMessage = "errorAdminMesAdd"
        }
        return viewModel
    }

    private ClientViewModel save(CreditlineDTO creditlineDTO) {
        ClientViewModel viewModel = new ClientViewModel()
        try {
            Client currentClient = findCurrentClient(creditlineDTO.itnClient)
            viewModel.clients = new ArrayList<>()
            viewModel.clients.add(ClientDTO.buildFromClient(currentClient))

            creditlineDAO.save(new Creditline(creditlineDTO, currentClient))
            viewModel = BaseViewModel.writeSuccessMessToViewModel("successMesAddCredlin", viewModel) as ClientViewModel
            log.debug("The creditline for client with itn:[${creditlineDTO.itnClient}] success saved")
        } catch (Exception ignore) {
            viewModel = BaseViewModel.writeErrorMessToViewModel("failAddCredlin", viewModel) as ClientViewModel
            log.debug("Fail to save creditline for client with itn:[${creditlineDTO.itnClient}]")
        }
        return viewModel
    }

    @Override
    CreditlineViewModel getAllClientCreditline(String itnClient) {

        CreditlineViewModel viewModel = new CreditlineViewModel()
        if (!StringUtils.isBlank(itnClient)) {
            List<Creditline> creditlines = new ArrayList<>()
            creditlines = findClientCreditlines(itnClient, viewModel, creditlines)
            return writeCreditlinesToViewModel(creditlines, viewModel)
        } else {
            log.debug("Fail to get all creditlines for client with itn:[${itnClient}]")
            return BaseViewModel.writeErrorMessToViewModel("failGetClientCredlins", viewModel) as CreditlineViewModel
        }
    }

    private List<Creditline> findClientCreditlines(String itnClient, CreditlineViewModel viewModel, List<Creditline> creditlines) {
        try {
            log.debug("Try to get all creditline for client with itn: [${itnClient}]")
            Client currentClient = findCurrentClient(itnClient)
            creditlines = creditlineDAO.getAllClientCreditline(currentClient)
            log.debug("The credit lines with size: [${creditlines.size()}]  was success found ")
        } catch (Exception ignore) {
            log.debug("Fail to get all creditlines for client with itn:[${itnClient}]")
            BaseViewModel.writeErrorMessToViewModel("failGetClientCredlins", viewModel) as CreditlineViewModel
        }
        return creditlines
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private CreditlineViewModel writeCreditlinesToViewModel(List<Creditline> creditlines, CreditlineViewModel viewModel) {
        viewModel.creditlines = new ArrayList<>()
        creditlines.each {
            viewModel.creditlines.add(CreditlineDTO.buildFromCreditline(it))
        }
        return viewModel
    }

    @Override
    CreditlineViewModel getAllClientCurrentCreditline(String itnClient) {

        CreditlineViewModel viewModel = new CreditlineViewModel()
        if (!StringUtils.isBlank(itnClient)) {
            List<Creditline> creditlines = new ArrayList<>()
            creditlines = findClientCurrentCreditlines(itnClient, viewModel, creditlines)
            return writeCreditlinesToViewModel(creditlines, viewModel)
        } else {
            log.debug("Fail to get all current credit lines for client with itn:[${itnClient}]")
            return BaseViewModel.writeErrorMessToViewModel("failGetClientCurrentCreditlines", viewModel) as CreditlineViewModel
        }
    }

    private List<Creditline> findClientCurrentCreditlines(String itnClient, CreditlineViewModel viewModel, List<Creditline> creditlines) {
        try {
            log.debug("Try to get all current credit line for client with itn: [${itnClient}]")
            Client currentClient = findCurrentClient(itnClient)
            creditlines = creditlineDAO.getAllClientCurrentCreditline(currentClient)
            log.debug("The credit lines with size: [${creditlines.size()}]  was success found ")
        } catch (Exception ignore) {
            log.debug("Fail to get all current creditlines for client with itn:[${itnClient}]")
            BaseViewModel.writeErrorMessToViewModel("failGetClientCurrentCreditlines", viewModel) as CreditlineViewModel
        }
        return creditlines
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
    CreditlineViewModel prepareToShowCreditline(Long creditlineId, CreditlineViewModel viewModel) {

        if (creditlineId && viewModel) {
            viewModel = prepare(creditlineId, viewModel)
        } else {
            viewModel.errorDataMessage = "errorAdminMesShow"
            log.debug("Fail prepare credit line to show")
        }
        return viewModel
    }

    private CreditlineViewModel prepare(Long creditlineId, CreditlineViewModel viewModel) {
        try {
            log.debug("Try to prepare credit line with id: [${creditlineId}] to show")
            viewModel = getCreditlineById(creditlineId, viewModel)
            CalculateViewModel calcModel = calculateService.calculatePayments(CalculateViewModel.buildFromCreditlineViewModel(viewModel))

            List<PaymentDTO> paymentDTOs = getPayments(creditlineId)
            List<FullPaymentModel> fullPayments = FullPaymentModel.buildFromPaymentsAndCalculatePayments(paymentDTOs, calcModel.calculatedPayments)

            viewModel.fullPayments = fullPayments
            viewModel.fullCost = calcModel.fullCost
            viewModel.overpayment = calcModel.overpayment
            if (paymentDTOs.size() != calcModel.calculatedPayments.size()) {
                viewModel.recommendPay = calcModel.calculatedPayments[paymentDTOs.size()].monthPayment
            }
        } catch (Exception ignore) {
            BaseViewModel.writeErrorMessToViewModel("errorMesShowCreditline", viewModel) as CreditlineViewModel
        }
        return viewModel
    }

    private List<PaymentDTO> getPayments(Long creditlineId) {
        List<Payment> payments = paymentDAO.getAllByCreditlineId(creditlineId)
        List<PaymentDTO> paymentDTOs = new ArrayList<>()
        payments.each { paymentDTOs.add(PaymentDTO.buildFromPayment(it)) }
        return paymentDTOs
    }

    private CreditlineViewModel getCreditlineById(Long creditlineId, CreditlineViewModel viewModel) {

        log.debug("Find credit line by id: [${creditlineId}]")
        Creditline currentCreditline = findCreditline(creditlineId)
        viewModel.creditlines = new ArrayList<>()
        viewModel.creditlines.add(CreditlineDTO.buildFromCreditline(currentCreditline))
        return viewModel
    }

    private Creditline findCreditline(Long creditlineId) {

        Creditline creditline = creditlineDAO.getById(creditlineId)
        if (creditline) {
            return creditline
        } else {
            log.debug("Fail to find creditline with id:[${creditlineId}]")
            throw new EntityNotFoundException("The creditline with id:[${creditlineId}] not found")
        }
    }

    @Override
    @Transactional
    ClientViewModel deleteCreditline(Long creditlineId, String itnClient) {

        ClientViewModel viewModel = new ClientViewModel()
        if (creditlineId && !StringUtils.isBlank(itnClient)) {
            viewModel = delete(creditlineId, viewModel, itnClient)
        } else {
            log.debug("Fail delete credit line ")
            viewModel.errorDataMessage = "errorAdminMesDel"
        }
        return viewModel
    }

    private ClientViewModel delete(Long creditlineId, ClientViewModel viewModel, String itnClient) {
        try {
            log.debug("Try to delete credit line with id: [${creditlineId}]")
            viewModel = addClientToViewModel(itnClient, viewModel)

            creditlineDAO.delete(creditlineId)
            viewModel = BaseViewModel.writeSuccessMessToViewModel("successMesDelCredlin", viewModel) as ClientViewModel
            log.debug("The credit line with id: [${creditlineId}] success deleted")
        } catch (Exception ignore) {
            log.debug("Fail delete credit line with id: [${creditlineId}]")
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorMesDelCredlin", viewModel) as ClientViewModel
        }
        return viewModel
    }

    @Override
    @Transactional
    ClientViewModel closeCreditline(Long creditlineId, String itnClient)  {

        ClientViewModel viewModel = new ClientViewModel()
        if (creditlineId && !StringUtils.isBlank(itnClient)) {
            viewModel = close(creditlineId, viewModel, itnClient)
        } else {
            log.debug("Fail delete credit line ")
            viewModel.errorDataMessage = "errorAdminMesClos"
        }
        return viewModel
    }

    private ClientViewModel close(Long creditlineId, ClientViewModel viewModel, String itnClient) {
        try {
            log.debug("Try to close credit line with id: [${creditlineId}]")
            viewModel = addClientToViewModel(itnClient, viewModel)

            Creditline currentCreditline = findCreditline(creditlineId)
            currentCreditline.state = CreditlineState.CLOSED
            currentCreditline.closeDate = new Date(new java.util.Date().getTime())

            creditlineDAO.save(currentCreditline)
            viewModel = BaseViewModel.writeSuccessMessToViewModel("successMesClosCredlin", viewModel) as ClientViewModel
            log.debug("The credit line with id: [${creditlineId}] success closed")
        } catch (Exception ignore) {
            log.debug("Fail close credit line with id: [${creditlineId}]")
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorMesClosCredlin", viewModel) as ClientViewModel
        }
        return viewModel
    }

    private ClientViewModel addClientToViewModel(String itnClient, ClientViewModel viewModel) {
        Client client = findCurrentClient(itnClient)
        viewModel.clients = new ArrayList<>()
        viewModel.clients.add(ClientDTO.buildFromClient(client))
        return viewModel
    }

    @Override
    CreditlineViewModel searchCreditline(String searchCriteria, Date inputDate) {

        CreditlineViewModel viewModel = new CreditlineViewModel()
        if (!StringUtils.isBlank(searchCriteria) && inputDate) {
            log.debug("Search creditline by search criteria: [${searchCriteria}] and input data: [${inputDate}]")

            viewModel = search(searchCriteria, inputDate, viewModel)
            viewModel.answer = true
        } else {
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorMesSearchCreditline", viewModel) as CreditlineViewModel
        }
        return viewModel
    }

    private CreditlineViewModel search(String searchCriteria, Date inputDate, CreditlineViewModel viewModel) {
        List<Creditline> creditlines = new ArrayList<>()
        switch (searchCriteria) {
            case "openDate":
                creditlines = creditlineDAO.getByOpenDate(inputDate)
                break
            case "closeDate":
                creditlines = creditlineDAO.getByCloseDate(inputDate)
                break
            default: break
        }
        viewModel.creditlines = new ArrayList<>()
        creditlines.each {
            viewModel.creditlines.add(CreditlineDTO.buildFromCreditline(it))
        }
        return viewModel
    }

}
