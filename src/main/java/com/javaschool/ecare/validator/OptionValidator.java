package com.javaschool.ecare.validator;

import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Set;

public class OptionValidator implements Validator{
    private OptionService optionService;

    public boolean supports(Class<?> aClass) {
        return Option.class.equals(aClass);
    }

    @Autowired
    public void setOptionService(OptionService optionService) {
        this.optionService = optionService;
    }

    @Override
    public void validate(Object o, Errors errors) {
        Option option = (Option) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "Name is required");
        if (!optionService.hasUniqueName(option.getId(), option.getName())) {
            errors.rejectValue("name", "notUniqueValue", new Object[]{"'name'"}, "Name must be unique");
        }
        Set<Option> crossingOptions = optionService.hasCrossingOptions(option);
        if (!crossingOptions.isEmpty()) {
            for (Option crossOption : crossingOptions) {
                option.getMandatoryOptions().remove(crossOption);
            }
            errors.rejectValue("mandatoryOptions", "crossingOptions", new Object[]{"'mandatoryOptions'"}, "Incompatible options can not be mandatory");
        }
    }
}
