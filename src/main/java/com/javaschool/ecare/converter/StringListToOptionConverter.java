package com.javaschool.ecare.converter;

import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Лена on 16.12.2015.
 */
public class StringListToOptionConverter implements Converter<String[], Set<Option>> {
    @Autowired
    OptionService optionService;

    @Override
    public Set<Option> convert(String[] idListString) {
        Set<Option> resultSet = new HashSet<>();
        for(String id : idListString) {
            resultSet.add(optionService.getById(Long.parseLong(id)));
        }
        return resultSet;
    }

}
