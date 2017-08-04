package com.bank.model.view

import com.bank.entity.Account

/**
 * Created by Ник on 12.07.2017.
 */
class OperatorViewModel {

    /**
     * Логин пользователя
     */
     String username

    /**
     * Имя пользователя
     */
     String fullName


    static final OperatorViewModel buildFromAccount(Account account){
        return new OperatorViewModel(
                username: account.username,
                fullName: account.fullName
        )
    }
}
