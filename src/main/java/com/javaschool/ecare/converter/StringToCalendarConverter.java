package com.javaschool.ecare.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringToCalendarConverter implements Converter<String, Calendar> {

    @Override
    public Calendar convert(String date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            return null;
        }
        return cal;
    }



}
