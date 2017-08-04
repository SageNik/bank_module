package com.bank.model.dto

import com.bank.entity.Creditline
import com.bank.enumeration.CreditlineState

import java.sql.Date

/**
 * Created by Ник on 20.07.2017.
 */
class CreditlineDTO {

     Long id

     String itnClient

     String type

     Date openDate

     Date closeDate

     int duration

     Long amount

     int percent

     String currency

     CreditlineState state

     final static CreditlineDTO buildFromCreditline(Creditline creditline){

          return new CreditlineDTO(
                  id: creditline.id,
                  itnClient: creditline.client.itn,
                  type: creditline.type,
                  openDate: creditline.openDate,
                  closeDate: creditline.closeDate,
                  duration: creditline.monthDuration,
                  amount: creditline.amount,
                  percent: creditline.percent,
                  currency: creditline.currency,
                  state: creditline.state
          )
     }

}
