package com.bank.dao

import com.bank.entity.Client
import com.bank.entity.Creditline

import java.sql.Date

/**
 * Created by Ник on 19.07.2017.
 */
interface CreditlineDAO {

    /**
     * Метод для поиска количества всех кредитных линий
     * @return Integer - количество найденных кредитных линий
     */
    Integer findCreditlineCount()

    /**
     * Метод для сохранения кредитной линии
     * @param Creditline creditline - кредитная линия для сохранения
     * @return Creditline - Сохранённая кредитная линия
     */
    Creditline save(Creditline creditline)

    /**
     * Метод для получения всех кредитных линий клиента
     * @param Client client - клиент
     * @return List<Creditline> - список найденных кредитных линий
     */
    List<Creditline> getAllClientCreditline(Client client)

    /**
     * Метод для получения всех текущих кредитных линий клиента
     * @param Client client - клиент
     * @return List<Creditline> - список найденных кредитных линий
     */
    List<Creditline> getAllClientCurrentCreditline(Client client)

    /**
     * Метод для получения кредитной линии по идентификатору
     * @param Long creditlineId - идентификатор кредитной линии
     * @return Creditline - Найденная кредитная линия
     */
    Creditline getById(Long creditlineId)

    /**
     * Метод для удаления кредитной линии
     * @param Long creditlineId - идентификатор кредитной линии
     * @return true - если кредитная линия удалена успешно false - если нет
     */
    boolean delete(Long creditlineId)

    /**
     * Метод для получения кредитной линии по дате открытия
     * @param Date openDate - дата открытия кредитной линии
     * @return List<Creditline> - Найденные кредитные линии
     */
   List<Creditline> getByOpenDate(Date openDate)

    /**
     * Метод для получения кредитной линии по дате закрытия
     * @param Date closeDate - дата закрытия кредитной линии
     * @return List<Creditline> - Найденные кредитные линии
     */
    List<Creditline> getByCloseDate(Date closeDate)
}