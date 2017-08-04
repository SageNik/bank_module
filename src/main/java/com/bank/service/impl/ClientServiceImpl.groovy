package com.bank.service.impl

import com.bank.dao.ClientDAO
import com.bank.entity.Client
import com.bank.model.dto.ClientDTO
import com.bank.model.view.BaseViewModel
import com.bank.model.view.ClientViewModel
import com.bank.service.interfaces.ClientService
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional


/**
 * Created by Ник on 17.07.2017.
 */
@Slf4j
@Service
class ClientServiceImpl implements ClientService {

    @Autowired
    ClientDAO clientDAO

    @Override
    ClientViewModel getClientCount() {

        log.debug("Getting count of registered clients")
        ClientViewModel model = new ClientViewModel()
        model.clientCount = clientDAO.findClientCount()
        log.debug("Found registered clients: [${model.clientCount}]")
        return model
    }

    @Override
    @Transactional
    ClientViewModel addNewClient(ClientDTO clientDTO) {

        ClientViewModel viewModel = new ClientViewModel()
        if (!StringUtils.isBlank(clientDTO.name) && !StringUtils.isBlank(clientDTO.surname)
                && !StringUtils.isBlank(clientDTO.phoneNumber) && !StringUtils.isBlank(clientDTO.itn)) {
            log.debug("Try to save new client with itn: [${clientDTO.itn}]")

            viewModel = save(clientDTO, viewModel)
        } else {
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorMes",viewModel)as ClientViewModel
            log.debug("Fail save new client with itn: [${clientDTO.itn}]")
        }
        return viewModel
    }

    private ClientViewModel save(ClientDTO clientDTO, ClientViewModel viewModel) {

        Client existClient = clientDAO.findByItn(clientDTO.itn)
        if (!existClient) {
            Client savedClient = clientDAO.save(new Client(clientDTO))
            viewModel = BaseViewModel.writeSuccessMessToViewModel("successMes", viewModel)as ClientViewModel
            log.debug("Success save new client with name:[${savedClient.name}] and itn:[${savedClient.itn}]")
        } else {
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorExistMes", viewModel)as ClientViewModel
            log.debug("Save failed. Client with itn:[${clientDTO.itn}] already exist")
        }
        return viewModel
    }

    @Override
    ClientViewModel searchClient(String searchCriteria, String inputData) {

        ClientViewModel viewModel = new ClientViewModel()
        if (!StringUtils.isBlank(searchCriteria) && !StringUtils.isBlank(inputData)) {
            log.debug("Serch client by criteria:[${searchCriteria}] and input data:[${inputData}]")

            viewModel = writeClientsToViewModel(searchCriteria, inputData, viewModel)
            viewModel.answer = true
            log.debug("Success found [${viewModel.clients.size()}] client(s)")

        } else {
            log.debug("Failed search client by criteria:[${searchCriteria}] and input data:[${inputData}]")
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorMesSearch",viewModel)as ClientViewModel
        }
        viewModel.clientCount = clientDAO.findClientCount()
        return viewModel
    }

    private ClientViewModel writeClientsToViewModel(String searchCriteria, String inputData, viewModel) {

        List<Client> clients = switchCriteria(searchCriteria, inputData)
        viewModel.clients = new ArrayList<>()
        clients.each { viewModel.clients.add(ClientDTO.buildFromClient(it)) }
        return viewModel
    }

    private List<Client> switchCriteria(String searchCriteria, String inputData) {

        List<Client> clients = new ArrayList<>()
        switch (searchCriteria) {

            case "name":
                clients = clientDAO.findByName(inputData.trim())
                break
            case "surname":
                clients = clientDAO.findBySurname(inputData.trim())
                break
            case "phone":
                clients = clientDAO.findByPhoneNumber(inputData.trim())
                break
            case "itn":
                clients.add(clientDAO.findByItn(inputData.trim()))
                break
            default: break
        }
      return clients
    }

    @Override
    @Transactional
    ClientViewModel deleteClientByItn(String itnClient) {

        ClientViewModel viewModel = new ClientViewModel()
        if(!StringUtils.isBlank(itnClient)) {
            log.debug("Try to delete client by itn: [${itnClient}]")

            viewModel = delete(itnClient, viewModel)
            log.debug("Client with itn: [${itnClient}] success deleted")
        }else{
            viewModel.errorDataMessage = "errorAdminMesDelClient"
            log.debug("Fail delete client ")
        }
        viewModel.clientCount = clientDAO.findClientCount()
        return viewModel
    }

    private ClientViewModel delete(String itnClient, ClientViewModel viewModel) {

        try {
            Client client = findCurrentClient(itnClient)
            clientDAO.deleteClient(client)
            viewModel = BaseViewModel.writeSuccessMessToViewModel("successMesDel", viewModel)as ClientViewModel
        }catch(Exception ignore){
            viewModel = BaseViewModel.writeErrorMessToViewModel("errorMesDel", viewModel)as ClientViewModel
            log.debug("Fail delete client with itn: [${itnClient}]")
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
    ClientViewModel getClientByItn(String itnClient) {

        ClientViewModel viewModel = new ClientViewModel()
        if(!StringUtils.isBlank(itnClient)) {
            try {
                log.debug("Try to find client by itn:[${itnClient}]")
                Client client = findCurrentClient(itnClient)

                viewModel.clients = new ArrayList<>()
                viewModel.clients.add(ClientDTO.buildFromClient(client))
                log.debug("The client with itn: [${itnClient}] success found")
                return viewModel
            } catch (Exception ignore) {
                log.debug("Fail to find client ")
                viewModel.errorDataMessage = "errorAdminMesFindClient"
            }
        }else{
            log.debug("Fail to find client")
            viewModel.errorDataMessage = "errorAdminMesFindClient"
        }
        return viewModel
    }

    @Override
    @Transactional
    ClientViewModel updateClient(ClientDTO clientDTO) {

        ClientViewModel viewModel = new ClientViewModel()
        if (!StringUtils.isBlank(clientDTO.name) && !StringUtils.isBlank(clientDTO.surname)
                && !StringUtils.isBlank(clientDTO.phoneNumber) && !StringUtils.isBlank(clientDTO.itn)) {
            log.debug("Try to update client with itn: [${clientDTO.itn}]")

            viewModel = update(clientDTO, viewModel)
        }else{
            viewModel.errorDataMessage = "errorAdminMesUpdateClient"
            log.debug("Fail update client ")
        }
        return viewModel
    }

    private ClientViewModel update(ClientDTO clientDTO, ClientViewModel viewModel) {

       try{
           Client client = findCurrentClient(clientDTO.itn)
           client.name = clientDTO.name
           client.surname = clientDTO.surname
           client.phoneNumber = clientDTO.phoneNumber
           client.itn = clientDTO.itn

            clientDAO.updateClient(client)
           viewModel.clients = new ArrayList<>()
           viewModel.clients.add(ClientDTO.buildFromClient(client))
           viewModel = BaseViewModel.writeSuccessMessToViewModel("successMesChange", viewModel)as ClientViewModel

       }catch(Exception ignore){
           log.debug("Fail update client with itn: [${clientDTO.itn}]")
           viewModel = BaseViewModel.writeErrorMessToViewModel("errorMesChange", viewModel)as ClientViewModel
       }
        return viewModel
    }
}