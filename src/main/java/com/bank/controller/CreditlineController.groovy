package com.bank.controller

import com.bank.constant.ViewConstant
import com.bank.model.view.CalculateViewModel
import com.bank.model.view.ClientViewModel
import com.bank.model.dto.CreditlineDTO
import com.bank.model.view.CreditlineViewModel
import com.bank.service.interfaces.CalculateService
import com.bank.service.interfaces.CreditlineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

import java.sql.Date

/**
 * Created by Ник on 19.07.2017.
 */
@Controller
class CreditlineController {

    @Autowired
    private CreditlineService creditlineService

    @Autowired
    CalculateService calculateService

    @RequestMapping(value = "/creditlines", method = RequestMethod.GET)
    String creditlines(Model model) {

        CreditlineViewModel viewModel = creditlineService.getCreditlineCount()
        model.addAttribute("model", viewModel)

        return "creditline/creditlines"
    }

    @RequestMapping(value = "/addNewCreditline", method = RequestMethod.GET)
    String addNewCreditline(@RequestParam("itnClient") String itnClient, Model model) {

        model.addAttribute("itnClient", itnClient)
        model.addAttribute("currencies", [ViewConstant.CURRENCY_USA, ViewConstant.getCURRENCY_UAH(),
                                          ViewConstant.getCURRENCY_EUR(), ViewConstant.getCURRENCY_RUR()])

        return "creditline/addNewCreditline"
    }

    @RequestMapping(value = "/calcPaymentCreditline", method = RequestMethod.POST)
    String calculatePayments(@ModelAttribute CalculateViewModel calculateViewModel, Model model) {

        calculateViewModel = calculateService.calculatePayments(calculateViewModel)
        model.addAttribute("model", calculateViewModel)

        return "shared/calcPayments"
    }

    @RequestMapping(value = "/addCreditline", method = RequestMethod.POST)
    String addCreditline(@ModelAttribute CreditlineDTO creditlineDTO, Model model) {

        ClientViewModel viewModel = creditlineService.addNewCreditline(creditlineDTO)
        model.addAttribute("model", viewModel)
        return "client/client"
    }

    @RequestMapping(value = "/allClientCreditlines", method = RequestMethod.GET)
    String getAllClientCreditlines(@RequestParam("itnClient") String itnClient, Model model) {

        CreditlineViewModel viewModel = creditlineService.getAllClientCreditline(itnClient)
        viewModel.itnClient = itnClient
        model.addAttribute("model", viewModel)
        return "creditline/creditlineList"
    }

    @RequestMapping(value = "/allCurCreditlines", method = RequestMethod.GET)
    String getAllClientCurrentCreditlines(@RequestParam("itnClient") String itnClient, Model model) {

        CreditlineViewModel viewModel = creditlineService.getAllClientCurrentCreditline(itnClient)
        viewModel.itnClient = itnClient
        model.addAttribute("model", viewModel)
        return "creditline/creditlineList"
    }

    @RequestMapping(value = "/showCreditline", method = RequestMethod.GET)
    String showCreditline(@RequestParam("idCredline") Long creditlineId,
                          @RequestParam("itnClient")String itnClient, Model model) {

        CreditlineViewModel viewModel = new CreditlineViewModel()
        viewModel = creditlineService.prepareToShowCreditline(creditlineId, viewModel)
        viewModel.itnClient = itnClient
        model.addAttribute("model", viewModel)
        return "creditline/showCreditline"

    }

    @RequestMapping(value = "/deleteCreditline", method = RequestMethod.POST)
    String deleteCreditline(@RequestParam("creditlineId")Long creditlineId,
                            @RequestParam("itnClient")String itnClient, Model model){

        ClientViewModel viewModel = creditlineService.deleteCreditline(creditlineId, itnClient)
        model.addAttribute("model",viewModel)
        return "client/client"
    }

    @RequestMapping(value = "/closeCreditline", method = RequestMethod.POST)
    String closeCreditline(@RequestParam("creditlineId")Long creditlineId,
                            @RequestParam("itnClient")String itnClient, Model model){

        ClientViewModel viewModel = creditlineService.closeCreditline(creditlineId, itnClient)
        model.addAttribute("model",viewModel)
        return "client/client"
    }

    @RequestMapping(value = "/searchCreditline", method = RequestMethod.POST)
    public String searchCreditline(@RequestParam("searchCriteria") String searchCriteria,
                               @RequestParam("inputDate") Date inputDate, Model model) {

        CreditlineViewModel viewModel = creditlineService.searchCreditline(searchCriteria, inputDate)
        model.addAttribute("model", viewModel)
        return "creditline/creditlines"
    }
}