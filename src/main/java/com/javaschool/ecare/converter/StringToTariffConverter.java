package com.javaschool.ecare.converter;

import com.javaschool.ecare.domain.Tariff;
import com.javaschool.ecare.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Лена on 27.12.2015.
 */
public class StringToTariffConverter implements Converter<String, Tariff> {

    @Autowired
    TariffService tariffService;

    @Override
    public Tariff convert(String id) {
        if ("".equals(id)) {
            return null;
        }
        return tariffService.getById(Long.parseLong(id));
    }
}
