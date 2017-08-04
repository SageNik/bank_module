package com.bank.controller

import com.bank.service.interfaces.CalculateService
import com.bank.model.view.CalculateViewModel
import com.bank.model.view.OperatorViewModel
import com.bank.service.interfaces.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam


/**
 * Created by Ник on 07.07.2017.
 */
@Controller
class HomeController {

    @Autowired
    AccountService accountService

    @Autowired
    CalculateService calculateService

    @RequestMapping(value ="/", method = RequestMethod.GET)
    public String goToHome() {

        return "redirect:/home"
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(@RequestParam ("error")Optional<String> error, Model model) {

        model.addAttribute("login_error", error)
        return "home"
    }

    @RequestMapping(value = "/home-operator", method = RequestMethod.GET)
    public String homeOperator(Model model) {

     OperatorViewModel operatorViewModel = accountService.getAuthorizedUser()

        model.addAttribute("authUser", operatorViewModel)

        return "home-operator"
    }

    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public String homePayments(@ModelAttribute CalculateViewModel homeModel,
                               @RequestParam ("error")Optional<String> error, Model model) {

        homeModel = calculateService.calculatePayments(homeModel)
        model.addAttribute("model", homeModel)
        model.addAttribute("login_error", error)

        return "home"
    }
}
