package com.bank.service.impl

import com.bank.constant.ViewConstant
import com.bank.dao.ClientDAO
import com.bank.entity.Client
import com.bank.model.dto.ClientDTO
import com.bank.model.view.ClientViewModel
import org.hibernate.HibernateException
import spock.lang.Specification

/**
 * Created by Ник on 27.07.2017.
 */
class ClientServiceImplUnitTest extends Specification {

    ClientDAO clientDAO
    ClientServiceImpl clientService

    def setup(){
        clientService = new ClientServiceImpl()
        clientDAO = Stub(ClientDAO)
        clientService.setClientDAO(clientDAO)
        clientDAO.findByItn(_)>> {String itnClient ->
            if(itnClient=="121212"){
                return new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")
            }else{
                null
            }
        }

    }

    def "GetClientCount"() {

        given:
        clientDAO.findClientCount()>> 2
        when:
        ClientViewModel viewModel = clientService.clientCount
        then:
        viewModel.clientCount == 2
    }

    def "Add New Client with correct input data"() {

        given:
        clientDAO.save(_)>>new Client(itn: "121212", name: "TestClient", id: 1, surname: "Testovich", phoneNumber: "123123")
        when:
        ClientDTO clientDTO = new ClientDTO(itn: "1111", name: "TestClient", surname: "Testovich", phoneNumber: "123123" )
        ClientViewModel viewModel = clientService.addNewClient(clientDTO)
        then:
        viewModel.messageType == ViewConstant.MESS_SUCCESS
        viewModel.currentMessage == "successMes"
        viewModel.message
    }

    def "Add New Client with correct input data if client already exist"() {

        when:
        ClientDTO clientDTO = new ClientDTO(itn: "121212", name: "TestClient", surname: "Testovich", phoneNumber: "123123" )
        ClientViewModel viewModel = clientService.addNewClient(clientDTO)
        then:
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.currentMessage == "errorExistMes"
        viewModel.message
    }

    def "Add New Client with uncorrect input data "() {

        when:
        ClientDTO clientDTO = new ClientDTO()
        ClientViewModel viewModel = clientService.addNewClient(clientDTO)
        then:
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.currentMessage == "errorMes"
        viewModel.message
    }

    def "Search Client if search criteria: 'name'"() {

        given:
        clientDAO.findByName(_)>> [new Client(itn: "1111", name: "TestClient", surname: "Testovich", phoneNumber: "123123"),
                                   new Client(itn: "2222", name: "TestClient", surname: "Testovich2", phoneNumber: "123123")]
        when:
        String searchCriteria = "name"
        String inputData = "TestClient"
        ClientViewModel viewModel = clientService.searchClient(searchCriteria,inputData)
        then:
        viewModel.clients.size() == 2
        viewModel.clients[0].itn == "1111"
        viewModel.clients[0].surname == "Testovich"
        viewModel.clients[1].itn == "2222"
        viewModel.clients[1].surname == "Testovich2"
        viewModel.answer
    }

    def "Search Client if search criteria: 'surname'"() {

        given:
        clientDAO.findBySurname(_)>> [new Client(itn: "1111", name: "TestClient", surname: "Testovich", phoneNumber: "123123"),
                                   new Client(itn: "2222", name: "TestClient2", surname: "Testovich", phoneNumber: "123123")]
        when:
        String searchCriteria = "surname"
        String inputData = "Testovich"
        ClientViewModel viewModel = clientService.searchClient(searchCriteria,inputData)
        then:
        viewModel.clients.size() == 2
        viewModel.clients[0].itn == "1111"
        viewModel.clients[0].name == "TestClient"
        viewModel.clients[1].itn == "2222"
        viewModel.clients[1].name == "TestClient2"
        viewModel.answer
    }

    def "Search Client if search criteria: 'phone'"() {

        given:
        clientDAO.findByPhoneNumber(_)>> [new Client(itn: "1111", name: "TestClient", surname: "Testovich", phoneNumber: "123123"),
                                      new Client(itn: "2222", name: "TestClient2", surname: "Testovich", phoneNumber: "123123")]
        when:
        String searchCriteria = "phone"
        String inputData = "123123"
        ClientViewModel viewModel = clientService.searchClient(searchCriteria,inputData)
        then:
        viewModel.clients.size() == 2
        viewModel.clients[0].itn == "1111"
        viewModel.clients[0].name == "TestClient"
        viewModel.clients[1].itn == "2222"
        viewModel.clients[1].name == "TestClient2"
        viewModel.answer
    }

    def "Search Client if search criteria: 'itn'"() {

        when:
        String searchCriteria = "itn"
        String inputData = "121212"
        ClientViewModel viewModel = clientService.searchClient(searchCriteria,inputData)
        then:
        viewModel.clients.size() == 1
        viewModel.clients[0].itn == "121212"
        viewModel.clients[0].name == "TestClient"
        viewModel.answer
    }

    def "Search Client if search criteria: 'notExist'"() {

        when:
        String searchCriteria = "notExist"
        String inputData = "121212"
        ClientViewModel viewModel = clientService.searchClient(searchCriteria,inputData)
        then:
        viewModel.clients.size() == 0
        viewModel.answer
    }

    def "Search Client if uncorrect input data"() {

        when:
        String searchCriteria = null
        String inputData = 1111
        ClientViewModel viewModel = clientService.searchClient(searchCriteria,inputData)
        then:
        !viewModel.clients
        !viewModel.answer
        viewModel.currentMessage == "errorMesSearch"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Delete Client By Itn with correct input data"() {

        given:
        clientDAO.deleteClient(_)>>true
        when:
        String itnClient = "121212"
        ClientViewModel viewModel = clientService.deleteClientByItn(itnClient)
        then:
        viewModel.currentMessage == "successMesDel"
        viewModel.messageType == ViewConstant.MESS_SUCCESS
        viewModel.message
    }

    def "Delete Client By Itn with correct input data if fail delete client"() {

        given:
        clientDAO.deleteClient(_)>>{throw new HibernateException("Fail delete client")}
        when:
        String itnClient = "121212"
        ClientViewModel viewModel = clientService.deleteClientByItn(itnClient)
        then:
        viewModel.currentMessage == "errorMesDel"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Delete Client By Itn with uncorrect input data"() {

        when:
        String itnClient = null
        ClientViewModel viewModel = clientService.deleteClientByItn(itnClient)
        then:
        viewModel.errorDataMessage == "errorAdminMesDelClient"
        !viewModel.message
    }

    def "Delete Client By Itn if client not found by itn"() {

        when:
        String itnClient = "111"
        ClientViewModel viewModel = clientService.deleteClientByItn(itnClient)
        then:
        viewModel.currentMessage == "errorMesDel"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Get Client By Itn with correct input data"() {

        when:
        String itnClient = "121212"
        ClientViewModel viewModel = clientService.getClientByItn(itnClient)
        then:
        viewModel.clients.size() == 1
        viewModel.clients[0].surname == "Testovich"
        viewModel.clients[0].name == "TestClient"
        !viewModel.message
    }

    def "Get Client By Itn with uncorrect input data"() {

        when:
        String itnClient = null
        ClientViewModel viewModel = clientService.getClientByItn(itnClient)
        then:
        viewModel.errorDataMessage == "errorAdminMesFindClient"
        !viewModel.message
    }

    def "Get Client By Itn if client with itn not exist"() {

        when:
        String itnClient = "1111"
        ClientViewModel viewModel = clientService.getClientByItn(itnClient)
        then:
        viewModel.errorDataMessage == "errorAdminMesFindClient"
        !viewModel.message
    }

    def "Update Client with correct input data"() {

        given:
        clientDAO.updateClient(_)>>new Client(itn: "121212",id:1, name: "TestClient", surname: "Testovich", phoneNumber: "123123" )
        when:
        ClientDTO clientDTO = new ClientDTO(itn: "121212", name: "TestClient", surname: "Testovich", phoneNumber: "123123" )
        ClientViewModel viewModel = clientService.updateClient(clientDTO)
        then:
        viewModel.currentMessage == "successMesChange"
        viewModel.messageType == ViewConstant.MESS_SUCCESS
        viewModel.message
    }

    def "Update Client with correct input data and fail update client"() {

        given:
        clientDAO.updateClient(_)>>{throw new HibernateException("Fail update client")}
        when:
        ClientDTO clientDTO = new ClientDTO(itn: "121212", name: "TestClient", surname: "Testovich", phoneNumber: "123123" )
        ClientViewModel viewModel = clientService.updateClient(clientDTO)
        then:
        viewModel.currentMessage == "errorMesChange"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }

    def "Update Client with uncorrect input data "() {

        when:
        ClientDTO clientDTO = new ClientDTO()
        ClientViewModel viewModel = clientService.updateClient(clientDTO)
        then:
        viewModel.errorDataMessage == "errorAdminMesUpdateClient"
        !viewModel.message
    }

    def "Update Client with correct input data if client not find by itn"() {

        when:
        ClientDTO clientDTO = new ClientDTO(itn: "111", name: "TestClient", surname: "Testovich", phoneNumber: "123123" )
        ClientViewModel viewModel = clientService.updateClient(clientDTO)
        then:
        viewModel.currentMessage == "errorMesChange"
        viewModel.messageType == ViewConstant.MESS_ERROR
        viewModel.message
    }
}
