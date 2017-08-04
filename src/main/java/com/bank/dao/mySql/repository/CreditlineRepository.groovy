package com.bank.dao.mySql.repository

import com.bank.entity.Client
import com.bank.entity.Creditline
import com.bank.enumeration.CreditlineState
import org.springframework.data.jpa.repository.JpaRepository

import java.sql.Date

/**
 * Created by Ник on 19.07.2017.
 */
interface CreditlineRepository extends JpaRepository<Creditline, Long>{

    /**
     * Метод для получения всех кредитных линий клиента
     * @param Client client - клиент
     * @return List<Creditline> - список найденных кредитных линий
     */
    List<Creditline> findAllByClient(Client client)

    /**
     * Метод для получения всех текущих кредитных линий клиента
     * @param Client client - клиент
     * @return List<Creditline> - список найденных кредитных линий
     */
    List<Creditline> findAllByClientAndState(Client client, CreditlineState state)

    /**
     * Метод для получения кредитной линии по дате открытия
     * @param Date openDate - дата открытия кредитной линии
     * @return List<Creditline> - Найденные кредитные линии
     */
    List<Creditline> findAllByOpenDate(Date openDate)

    /**
     * Метод для получения кредитной линии по дате закрытия
     * @param Date closeDate - дата закрытия кредитной линии
     * @return List<Creditline> - Найденные кредитные линии
     */
    List<Creditline> findAllByCloseDate(Date closeDate)
}