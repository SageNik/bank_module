package com.bank.controller

import com.bank.model.view.CreditlineViewModel
import com.bank.model.view.PaymentViewModel
import com.bank.service.interfaces.CreditlineService
import com.bank.service.interfaces.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * Created by Ник on 21.07.2017.
 */
@Controller
class PaymentController {

    @Autowired
    private PaymentService paymentService
    @Autowired
    private CreditlineService creditlineService

    @RequestMapping(value = "/allPayments", method = RequestMethod.GET)
    String getAllClientPayments(@RequestParam("itnClient")String itnClient, Model model){

        PaymentViewModel viewModel = paymentService.getAllClientPayments(itnClient)
        model.addAttribute("model",viewModel)
        return "payment/payments"
    }

    @RequestMapping(value = "/addPayment", method = RequestMethod.POST)
    String addPayment(@RequestParam("creditlineId")Long creditlineId,
                      @RequestParam("itnClient")String itnClient,
                      @RequestParam("amount") BigDecimal amount, Model model){

        CreditlineViewModel viewModel = paymentService.addPayment(creditlineId, amount)
        viewModel = creditlineService.prepareToShowCreditline(creditlineId, viewModel)

        viewModel.itnClient = itnClient
        model.addAttribute("model", viewModel)
        return "creditline/showCreditline"
    }

    @RequestMapping(value = "/deletePayment", method = RequestMethod.POST)
    String deletePayment(@RequestParam("creditlineId")Long creditlineId,
                         @RequestParam("paymentId")Long paymentId,
                         @RequestParam("itnClient")String itnClient, Model model){

        CreditlineViewModel viewModel = paymentService.deletePayment(paymentId,creditlineId)
        viewModel = creditlineService.prepareToShowCreditline(creditlineId, viewModel)

        viewModel.itnClient = itnClient
        model.addAttribute("model", viewModel)
        return "creditline/showCreditline"
    }
}
