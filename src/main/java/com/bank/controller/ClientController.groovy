package com.bank.controller

import com.bank.model.dto.ClientDTO
import com.bank.model.view.ClientViewModel
import com.bank.service.interfaces.ClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * Created by Ник on 17.07.2017.
 */
@Controller
class ClientController {

    @Autowired
    private ClientService clientService

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    String allClients(Model model) {

        ClientViewModel viewModel = clientService.getClientCount()
        model.addAttribute("model", viewModel)

        return "client/clients"
    }

    @RequestMapping(value = "/addNewClient", method = RequestMethod.GET)
    String addNewClient(Model model) {

        ClientViewModel viewModel = new ClientViewModel()
        model.addAttribute("model", viewModel)

        return "client/addNewClient"
    }

    @RequestMapping(value = "/addClient", method = RequestMethod.POST)
    public String addCl(@ModelAttribute ClientDTO client, Model model) {

        ClientViewModel viewModel = clientService.addNewClient(client)
        model.addAttribute("model", viewModel)
        return "client/addNewClient"
    }

    @RequestMapping(value = "/searchClient", method = RequestMethod.POST)
    public String searchClient(@RequestParam("searchCriteria") String searchCriteria,
                               @RequestParam("inputData") String inputData, Model model) {

        ClientViewModel viewModel = clientService.searchClient(searchCriteria, inputData)
        model.addAttribute("model", viewModel)
        return "client/clients"
    }

    @RequestMapping(value = "/deleteClient", method = RequestMethod.POST)
    public String deleteClient(@RequestParam("itnClient") String itnClient, Model model) {

        ClientViewModel viewModel = clientService.deleteClientByItn(itnClient)
        model.addAttribute("model", viewModel)
        return "client/clients"
    }

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public String client(@RequestParam("itnClient") String itnClient, Model model) {

        ClientViewModel viewModel = clientService.getClientByItn(itnClient)
        model.addAttribute("model", viewModel)
        return "client/client"
    }

    @RequestMapping(value = "/changeClient", method = RequestMethod.GET)
    public String changeClient(@RequestParam("itnClient") String itnClient, Model model) {

        ClientViewModel viewModel = clientService.getClientByItn(itnClient)
        model.addAttribute("model", viewModel)
        return "client/changeClient"
    }

    @RequestMapping(value = "/updateClient", method = RequestMethod.POST)
    public String updateClient(@ModelAttribute ClientDTO clientDTO, Model model) {

        ClientViewModel viewModel = clientService.updateClient(clientDTO)
        model.addAttribute("model", viewModel)
        return "client/client"
    }
}