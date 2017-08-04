package com.bank.service.interfaces

import com.bank.model.view.CalculateViewModel

/**
 * Created by Ник on 14.07.2017.
 */
interface CalculateService {

    /**
     * Метод для подготовки модели представления рассчёта платежей по кредиту
     * @param CalculateViewModel homeViewModel - модель представления рассчёта платежей по кредиту
     * @return CalculateViewModel Возвращает модель представления рассчёта платежей по кредиту
     */
    CalculateViewModel calculatePayments(CalculateViewModel homeViewModel)
}