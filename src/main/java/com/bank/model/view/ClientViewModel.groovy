package com.bank.model.view

import com.bank.model.dto.ClientDTO

/**
 * Created by Ник on 17.07.2017.
 */
class ClientViewModel extends BaseViewModel{

    /**
     * Количество клиентов банка
     */
    Integer clientCount

    /**
     * Список клиентов
     */
    List<ClientDTO> clients

}
