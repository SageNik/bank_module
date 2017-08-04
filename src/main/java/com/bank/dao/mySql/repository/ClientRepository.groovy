package com.bank.dao.mySql.repository

import com.bank.entity.Client
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Ник on 17.07.2017.
 */
interface ClientRepository extends JpaRepository<Client,Long>{

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
}