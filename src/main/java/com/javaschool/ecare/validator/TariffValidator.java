package com.javaschool.ecare.validator;

import com.javaschool.ecare.domain.Tariff;
import com.javaschool.ecare.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class TariffValidator implements Validator {
    private TariffService tariffService;

    @Autowired
    public void setTariffService(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Tariff.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Tariff tariff = (Tariff) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "Name is required");
        if (!tariffService.hasUniqueName(tariff.getId(), tariff.getName())) {
            errors.rejectValue("name", "notUniqueValue", new Object[]{"'name'"}, "Name must be unique");
        }
    }
}
