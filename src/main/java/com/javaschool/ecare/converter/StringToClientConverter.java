package com.javaschool.ecare.converter;

import com.javaschool.ecare.domain.Client;
import com.javaschool.ecare.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Лена on 07.01.2016.
 */
public class StringToClientConverter implements Converter<String, Client> {
    @Autowired
    ClientService clientService;

    @Override
    public Client convert(String s) {
        return clientService.getById(Long.parseLong(s));
    }
}
