package com.bank.model.view

import com.bank.model.dto.PaymentDTO

/**
 * Created by Ник on 21.07.2017.
 */
class PaymentViewModel extends BaseViewModel{

    /**
     * Список платежей клиента
     */
    List<PaymentDTO> payments

    /**
     * Инн текущего клиента
     */
    String itnClient
}
