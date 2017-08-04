package com.bank.model.dto

import com.bank.entity.Client
import groovy.transform.ToString

/**
 * Created by Ник on 17.07.2017.
 */
@ToString
class ClientDTO {

     String name

     String surname

     String itn

     String phoneNumber

    static final ClientDTO buildFromClient(Client client){

        return new ClientDTO(
                name: client.name,
                surname: client.surname,
                itn: client.itn,
                phoneNumber: client.phoneNumber
        )
    }
}
