package com.bank.service.interfaces

import com.bank.model.view.OperatorViewModel
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * Created by Ник on 06.07.2017.
 */
interface AccountService extends UserDetailsService{

    /**
     * Метод для получения авторизованного пользователя
     * @return OperatorViewModel Возвращает модель для представления авторизованного пользователя
     */
    OperatorViewModel getAuthorizedUser ()

}