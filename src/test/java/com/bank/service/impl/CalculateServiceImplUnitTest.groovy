package com.bank.service.impl

import com.bank.model.view.CalculateViewModel
import spock.lang.Specification

/**
 * Created by Ник on 17.07.2017.
 */
class CalculateServiceImplUnitTest extends Specification {

    CalculateServiceImpl calculateService


   def setup(){
        calculateService = new CalculateServiceImpl()
    }

    def "PrepareHomeViewModel if credit method is differential"() {

        expect:
       CalculateViewModel model = calculateService.calculatePayments(homeViewModel)
        model.overpayment == 0.03
        model.fullCost == 2.03
        model.calcMethod == "diff"
        model.calculatedPayments.size() == 2
        model.calculatedPayments[0].percents == 0.02
        model.calculatedPayments[0].amortization == 1
        model.calculatedPayments[0].monthPayment == 1.02
        model.calculatedPayments[1].percents == 0.01
        model.calculatedPayments[1].amortization == 1
        model.calculatedPayments[1].monthPayment == 1.01

        where:
        homeViewModel = new CalculateViewModel(creditAmount: 2, creditPercent: 10, creditTime: 2, calcMethod: "diff")
    }

    def "PrepareHomeViewModel if credit method is annuity"() {

        expect:
        CalculateViewModel model = calculateService.calculatePayments(homeViewModel)
        model.overpayment == 0.02
        model.fullCost == 2.02
        model.calcMethod == "annuit"
        model.calculatedPayments.size() == 2
        model.calculatedPayments[0].percents == 0.02
        model.calculatedPayments[0].amortization == 0.99
        model.calculatedPayments[0].monthPayment == 1.01
        model.calculatedPayments[1].percents == 0.01
        model.calculatedPayments[1].amortization == 1
        model.calculatedPayments[1].monthPayment == 1.01

        where:
        homeViewModel = new CalculateViewModel(creditAmount: 2, creditPercent: 10, creditTime: 2, calcMethod: "annuit")
    }
}

