package com.bank.model

import com.bank.model.dto.PaymentDTO
import com.bank.utils.CalculatedPaymentModel

/**
 * Created by Ник on 24.07.2017.
 */
class FullPaymentModel {

     PaymentDTO payment
     CalculatedPaymentModel calculatedPayment

    final static List<FullPaymentModel> buildFromPaymentsAndCalculatePayments(List<PaymentDTO> payList,
                                                                              List<CalculatedPaymentModel> calcPayList){
        List<FullPaymentModel> result = new ArrayList<FullPaymentModel>()
        for(int i=0;i<calcPayList.size();i++) {
            FullPaymentModel fullPayment = new FullPaymentModel()
            fullPayment.calculatedPayment = calcPayList[i]
            if(i<payList.size() && payList.get(i)!=null){
                fullPayment.payment = payList[i]
            }
            result.add(fullPayment)
        }
        return result
    }
}
