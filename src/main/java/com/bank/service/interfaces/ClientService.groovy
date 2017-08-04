package com.bank.service.interfaces

import com.bank.model.dto.ClientDTO
import com.bank.model.view.ClientViewModel

/**
 * Created by Ник on 17.07.2017.
 */
interface ClientService {

    /**
     * Метод для нахождения количества зарегистрированых клиентов
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel getClientCount()

    /**
     * Метод для добавления нового клиента
     * @param ClientDTO client - клиент для регистрации
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel addNewClient(ClientDTO client)

    /**
     * Метод для нахождения зарегистрированых клиентов по выбранным критериям и заданным данным
     * @param String searchCriteria - заданные критерии поиска клиента
     * @param String inputData - данные для поиска клиента
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel searchClient(String searchCriteria, String inputData)

    /**
     * Метод для удаления клиента по инн
     * @param String itnClient - идентификационный налоговый номер клиента
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel deleteClientByItn(String itnClient)

    /**
     * Метод для нахождения зарегистрированого клиента по инн
     * @param String itnClient - идентификационный налоговый номер клиента
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel getClientByItn(String itnClient)

    /**
     * Метод для обновления зарегистрированого клиента по инн
     * @param ClientDTO clientDTO - клиент с изменёнными данными
     * @return ClientViewModel Возвращает модель для отображения
     */
    ClientViewModel updateClient(ClientDTO clientDTO)
}