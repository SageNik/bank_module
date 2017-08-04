package com.bank.dao

import com.bank.entity.Client


/**
 * Created by Ник on 17.07.2017.
 */
interface ClientDAO {

    /**
     * Метод для поиска количества всех клиентов
     * @return Integer - количество найденных клиентов
     */
    Integer findClientCount()

    /**
     * Метод для сохранения клиента
     * @param Client client - клиент для сохранения
     * @return Client - Сохранённый клиент
     */
    Client save(Client client)

    /**
     * Метод для нахождения клиента по инн
     * @param String clientItn - идентификационный налоговый номер клиента
     * @return Client - найденный клиент
     */
    Client findByItn(String clientItn)

    /**
     * Метод для нахождения клиента по имени
     * @param String clientName - имя клиента
     * @return List<Client> - список найденных клиентов
     */
    List<Client> findByName(String clientName)

    /**
     * Метод для нахождения клиента по фамилии
     * @param String clientSurname - фамилия клиента
     * @return List<Client> - список найденных клиентов
     */
    List<Client> findBySurname(String clientSurname)

    /**
     * Метод для нахождения клиента по номеру телефона
     * @param String clientPhoneNumber - номер телефона клиента
     * @return List<Client> - список найденных клиентов
     */
    List<Client> findByPhoneNumber(String clientPhoneNumber)

    /**
     * Метод для удаления клиента
     * @param Client client - клиент для удаления
     * @return  tru- если клиент удалён, false - если нет
     */
    boolean deleteClient(Client client)

    /**
     * Метод для обновления клиента
     * @param Client client - клиент для удаления
     * @return  Client - обновлённый клиент
     */
    Client updateClient(Client client)

}