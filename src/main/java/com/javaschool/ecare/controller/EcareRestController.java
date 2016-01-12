package com.javaschool.ecare.controller;

import com.javaschool.ecare.domain.Client;
import com.javaschool.ecare.domain.Tariff;
import com.javaschool.ecare.service.ClientService;
import com.javaschool.ecare.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EcareRestController {
    @Autowired
    ClientService clientService;

    @Autowired
    TariffService tariffService;

    @RequestMapping("/api/tariff/{tariffId}/clients")
    public List<Client> getClients(@PathVariable() long tariffId) {
        return clientService.getByTariff(tariffService.getById(tariffId));
    }

    @RequestMapping("/api/tariff")
    public List<Tariff> getTariffs() {
        return tariffService.getActiveTariffs();
    }
}
