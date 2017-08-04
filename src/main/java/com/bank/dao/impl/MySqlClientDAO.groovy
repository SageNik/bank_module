package com.bank.dao.impl

import com.bank.dao.ClientDAO
import com.bank.dao.mySql.repository.ClientRepository
import com.bank.entity.Client
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by Ник on 17.07.2017.
 */
@Repository
class MySqlClientDAO implements ClientDAO{

   @Autowired
   private ClientRepository clientRepository

    @Override
    Integer findClientCount() {
        return clientRepository.count()
    }

    @Override
    Client save(Client client) {
        return clientRepository.save(client)
    }

    @Override
    Client findByItn(String clientItn) {
        return clientRepository.findByItn(clientItn)
    }

    @Override
    List<Client> findByName(String clientName) {
        return clientRepository.findByName(clientName)
    }

    @Override
    List<Client> findBySurname(String clientSurname) {
        return clientRepository.findBySurname(clientSurname)
    }

    @Override
    List<Client> findByPhoneNumber(String clientPhoneNumber) {
        return clientRepository.findByPhoneNumber(clientPhoneNumber)
    }

    @Override
    boolean deleteClient(Client client) {
        return clientRepository.delete(client)
    }

    @Override
    Client updateClient(Client client) {
        return clientRepository.save(client)
    }
}
