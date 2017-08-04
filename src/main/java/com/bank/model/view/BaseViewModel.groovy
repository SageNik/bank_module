package com.bank.model.view

import com.bank.constant.ViewConstant

/**
 * Created by Ник on 19.07.2017.
 */
abstract class BaseViewModel {

    /**
     * Флаг наличия сообщения
     */
    boolean message

    /**
     * Текущее сообщение для отображения
     */
    String currentMessage

    /**
     * Тип текущего сообщениея для отображения
     */
    String messageType

    /**
     * Флаг наличия ответа
     */
    boolean answer

    /**
     * Сообщение ошибки данных
     */
    String errorDataMessage


    final static BaseViewModel writeErrorMessToViewModel(String errorMessage, BaseViewModel viewModel) {
        viewModel.message = true
        viewModel.currentMessage = errorMessage
        viewModel.messageType = ViewConstant.MESS_ERROR
        return viewModel
    }

    final static BaseViewModel writeSuccessMessToViewModel(String successMessage, BaseViewModel viewModel) {
        viewModel.message = true
        viewModel.currentMessage = successMessage
        viewModel.messageType = ViewConstant.MESS_SUCCESS
        return viewModel
    }
}
