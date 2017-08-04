package com.bank.service.impl

import com.bank.model.view.CalculateViewModel
import com.bank.service.interfaces.CalculateService
import com.bank.utils.AnnuityCredit
import com.bank.utils.CalculatedPaymentModel
import com.bank.utils.DifferentialCredit
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

/**
 * Created by Ник on 14.07.2017.
 */
@Slf4j
@Service
class CalculateServiceImpl implements CalculateService{

    @Override
    CalculateViewModel calculatePayments(CalculateViewModel calculateViewModel) {

        log.debug("Prepare view model for calculated payments")
        if (calculateViewModel.creditAmount && calculateViewModel.creditTime && calculateViewModel.creditPercent) {

            if(calculateViewModel.calcMethod == "annuit") {
                calculateViewModel = annuityPayments(calculateViewModel)
            }else if(calculateViewModel.calcMethod == "diff"){
                calculateViewModel = differentialPayments(calculateViewModel)
            }
            calculateViewModel.calcTable = true


        }
        return calculateViewModel
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private CalculateViewModel differentialPayments(CalculateViewModel calculateViewModel) {
        DifferentialCredit differentialCredit = new DifferentialCredit()
        List<CalculatedPaymentModel> calcPaymentsList =
                differentialCredit.calcPayments(calculateViewModel.creditAmount, calculateViewModel.creditTime, calculateViewModel.creditPercent)

        calculateViewModel.fullCost = differentialCredit.calcFullCost(calcPaymentsList)
        calculateViewModel.overpayment = differentialCredit.calcOverpayment(calcPaymentsList, calculateViewModel.creditAmount)
        calculateViewModel.calculatedPayments = calcPaymentsList
        return calculateViewModel
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private CalculateViewModel annuityPayments(CalculateViewModel calculateViewModel) {
        AnnuityCredit annuityCredit = new AnnuityCredit()
        List<CalculatedPaymentModel> calcPaymentsList =
                annuityCredit.calcPayments(calculateViewModel.creditAmount, calculateViewModel.creditTime, calculateViewModel.creditPercent)

        calculateViewModel.fullCost = annuityCredit.calcFullCost(calcPaymentsList)
        calculateViewModel.overpayment = annuityCredit.calcOverpayment(calcPaymentsList, calculateViewModel.creditAmount)
        calculateViewModel.calculatedPayments = calcPaymentsList
        return calculateViewModel
    }
}
