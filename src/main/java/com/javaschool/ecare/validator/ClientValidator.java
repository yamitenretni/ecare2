package com.javaschool.ecare.validator;

import com.javaschool.ecare.domain.Client;
import com.javaschool.ecare.domain.User;
import com.javaschool.ecare.service.ClientService;
import com.javaschool.ecare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.PostConstruct;

public class ClientValidator implements Validator {
    private ClientService clientService;

    private UserValidator userValidator;

    private UserService userService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        return;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Client.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Client client = (Client) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthDate", "error.birthDate", "Birth date is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "error.address", "Address is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passportData", "error.passportData", "Passport is required");

        if (!(client.getPassportData() != null && client.getPassportData().isEmpty())) {
            if (!clientService.hasUniquePassport(client.getId(), client.getPassportData())) {
                errors.rejectValue("passportData", "notUniqueValue", new Object[]{"'passportData'"}, "Passport must be unique");
            }
        }
        User user = client.getUser();
        if (user != null) {
            try {
                errors.pushNestedPath("user");
                userValidator.setUserService(userService);
                ValidationUtils.invokeValidator(userValidator, user, errors);
            }
            finally {
                errors.popNestedPath();
            }
        }
    }
}
