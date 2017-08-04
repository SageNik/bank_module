package com.bank.dao

import com.bank.BankModuleApplicationTests
import com.bank.entity.Client
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Stepwise

/**
 * Created by Ник on 31.07.2017.
 */
@Stepwise
class ClientDAOTestIT extends BankModuleApplicationTests {

    @Autowired
    private ClientDAO clientDAO

    Client client
    Integer firstCount

    def setup(){
         client = new Client( name: "TestName", surname: "TestSurname", itn: "121212", phoneNumber: "123123")
         firstCount = clientDAO.findClientCount()
    }

    def "Save client and check client count"() {

        when:
        Client savedClient = clientDAO.save(client)
        Integer count = clientDAO.findClientCount()
        then:
        savedClient.itn == client.itn
        savedClient.name == client.name
        savedClient.surname == client.surname
        savedClient.phoneNumber == client.phoneNumber
        count - firstCount == 1
}


    def "Find client by Itn"() {

        when:
        String itnClient = "121212"
        Client foundClient = clientDAO.findByItn(itnClient)
        then:
        foundClient.itn == "121212"
        foundClient.name == "TestName"
        foundClient.surname == "TestSurname"
        foundClient.phoneNumber == "123123"
    }

    def "Find client by Itn if client with itn not exist"() {

        when:
        String itnClient = "0000"
        Client foundClient = clientDAO.findByItn(itnClient)
        then:
        !foundClient
    }

    def "Find client by Name"() {

        when:
        String nameClient = "TestName"
        List<Client> foundClients = clientDAO.findByName(nameClient)
        then:
        !foundClients.isEmpty()
        foundClients[0].itn == "121212"
        foundClients[0].name == "TestName"
        foundClients[0].surname == "TestSurname"
        foundClients[0].phoneNumber == "123123"
    }

    def "Find client by Name if client with name not exist"() {

        when:
        String nameClient = "0000"
        List<Client> foundClients = clientDAO.findByName(nameClient)
        then:
        foundClients.isEmpty()
    }

    def "Find client by Surname"() {

        when:
        String surnameClient = "TestSurname"
        List<Client> foundClients = clientDAO.findBySurname(surnameClient)
        then:
        !foundClients.isEmpty()
        foundClients[0].itn == "121212"
        foundClients[0].name == "TestName"
        foundClients[0].surname == "TestSurname"
        foundClients[0].phoneNumber == "123123"
    }

    def "Find client by Surname if client with surname not exist"() {

        when:
        String surnameClient = "0000"
        List<Client> foundClients = clientDAO.findBySurname(surnameClient)
        then:
        foundClients.isEmpty()
    }

    def "Find client by PhoneNumber"() {

        when:
        String phoneClient = "123123"
        List<Client> foundClients = clientDAO.findByPhoneNumber(phoneClient)
        then:
        !foundClients.isEmpty()
        foundClients[0].itn == "121212"
        foundClients[0].name == "TestName"
        foundClients[0].surname == "TestSurname"
        foundClients[0].phoneNumber == "123123"
    }

    def "Find client by PhoneNumber if client with phoneNumber not exist"() {

        when:
        String phoneClient = "0000"
        List<Client> foundClients = clientDAO.findByPhoneNumber(phoneClient)
        then:
        foundClients.isEmpty()
    }

    def "Update Client"() {

        when:
        Client client = clientDAO.findByItn("121212")
        client.name = "TestTest"
        Client udatedClient = clientDAO.updateClient(client)
        then:
        udatedClient.itn == "121212"
        udatedClient.name == "TestTest"
        udatedClient.surname == "TestSurname"
        udatedClient.phoneNumber == "123123"
    }

    def "Delete Client"() {

        when:
        Client deleteClient = clientDAO.findByItn("121212")
        Boolean isDeleted = clientDAO.deleteClient(deleteClient)
        then:
        !isDeleted
        !clientDAO.findByItn("121212")
    }
}
