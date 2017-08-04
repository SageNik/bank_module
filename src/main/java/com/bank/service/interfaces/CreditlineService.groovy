package com.bank.service.interfaces

import com.bank.model.view.ClientViewModel
import com.bank.model.dto.CreditlineDTO
import com.bank.model.view.CreditlineViewModel

import java.sql.Date

/**
 * Created by Ник on 19.07.2017.
 */
interface CreditlineService {

    /**
     * Метод для нахождения количества зарегистрированых кредитных линий
     * @return CreditlineViewModel Возвращает модель для отображения
     */
    CreditlineViewModel getCreditlineCount()

    /**
     * Метод для добавления новой кредитной линии клиенту
     * @param CreditlineDTO creditlineDTO - кредитная линия для регистрации
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel addNewCreditline(CreditlineDTO creditlineDTO)

    /**
     * Метод для получения всех кредитных линий клиента по его инн
     * @param String itnClient - инн клиента
     * @return CreditlineViewModel Возвращает модель для отображения
     */
    CreditlineViewModel getAllClientCreditline(String itnClient)

    /**
     * Метод для получения всех текущих (не закрытых) кредитных линий клиента по его инн
     * @param String itnClient - инн клиента
     * @return CreditlineViewModel Возвращает модель для отображения
     */
    CreditlineViewModel getAllClientCurrentCreditline(String itnClient)

    /**
     * Метод для подготовки отображения кредитной линии по идентификатору
     * @param Long creditlineId - идентификатор кредитной линии
     * @param CreditlineViewModel viewModel - модель для отображения
     * @return CreditlineViewModel Возвращает модель для отображения
     */
    CreditlineViewModel prepareToShowCreditline(Long creditlineId, CreditlineViewModel viewModel)

    /**
     * Метод для удаления кредитной линии
     * @param Long creditlineId - идентификатор кредитной линии
     * @param String itnClient - инн клиента, владельца кредитной линии
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel deleteCreditline(Long creditlineId, String itnClient)

    /**
     * Метод для закрытия кредитной линии
     * @param Long creditlineId - идентификатор кредитной линии
     * @param String itnClient - инн клиента, владельца кредитной линии
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel closeCreditline(Long creditlineId, String itnClient)

    /**
     * Метод для нахождения зарегистрированых редитных линий по выбранным критериям и заданным данным
     * @param String searchCriteria - заданные критерии поиска клиента
     * @param Date inputDate - данные для поиска клиента
     * @return CreditlineViewModel Возвращает модель для отображения
     */
    CreditlineViewModel searchCreditline(String searchCriteria, Date inputDate)
}