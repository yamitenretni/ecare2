package com.javaschool.ecare.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Лена on 27.12.2015.
 */
public class StringToDateConverter implements Converter<String, Date> {
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @Override
    public Date convert(String stringDate) {
        Date resultDate = null;
        try {
            resultDate = format.parse(stringDate);
        } catch (ParseException e) {
        }
        return resultDate;
    }
}
