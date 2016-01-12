package com.javaschool.ecare.converter;

import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

/**
 * Created by Лена on 16.12.2015.
 */
public class StringToOptionConverter implements Converter<String, Option> {
    @Autowired
    OptionService optionService;

    @Override
    public Option convert(String id) {
        return optionService.getById(Long.parseLong(id));
    }
}
