package com.bank.dao.impl

import com.bank.dao.CreditlineDAO
import com.bank.dao.mySql.repository.CreditlineRepository
import com.bank.entity.Client
import com.bank.entity.Creditline
import com.bank.enumeration.CreditlineState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import java.sql.Date

/**
 * Created by Ник on 19.07.2017.
 */
@Repository
class MySqlCreditlineDAO implements CreditlineDAO{

   @Autowired
   private CreditlineRepository creditlineRepository

    @Override
    Integer findCreditlineCount() {
        return creditlineRepository.count()
    }

    @Override
    Creditline save(Creditline creditline) {
        return creditlineRepository.save(creditline)
    }

    @Override
    List<Creditline> getAllClientCreditline(Client client) {
        return creditlineRepository.findAllByClient(client)
    }

    @Override
    List<Creditline> getAllClientCurrentCreditline(Client client) {
        return creditlineRepository.findAllByClientAndState(client, CreditlineState.OPENED)
    }

    @Override
    Creditline getById(Long creditlineId) {
        return creditlineRepository.findOne(creditlineId)
    }

    @Override
    boolean delete(Long creditlineId) {
        return creditlineRepository.delete(creditlineId)
    }

    @Override
    List<Creditline> getByOpenDate(Date openDate) {
        return creditlineRepository.findAllByOpenDate(openDate)
    }

    @Override
    List<Creditline> getByCloseDate(Date closeDate) {
        return creditlineRepository.findAllByCloseDate(closeDate)
    }
}
