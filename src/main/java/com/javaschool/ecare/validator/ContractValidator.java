package com.javaschool.ecare.validator;

import com.javaschool.ecare.domain.Client;
import com.javaschool.ecare.domain.Contract;
import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.service.ClientService;
import com.javaschool.ecare.service.ContractService;
import com.javaschool.ecare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Set;

public class ContractValidator implements Validator {

    private ContractService contractService;

    private ClientValidator clientValidator;

    private UserValidator userValidator;

    private ClientService clientService;

    private UserService userService;

    @Autowired
    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    @Autowired
    public void setClientValidator(ClientValidator clientValidator) {
        this.clientValidator = clientValidator;
    }

    @Autowired
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Contract.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Contract contract = (Contract) o;
        clientValidator = new ClientValidator();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tariff", "error.tariff", "Tariff is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "number", "error.number", "Number is required");
        if (!contractService.hasUniqueNumber(contract.getId(), contract.getNumber())) {
            errors.rejectValue("number", "notUniqueValue", new Object[]{"'number'"}, "Number must be unique");
        }
        Client client = contract.getClient();
        if (client != null) {
            try {
                errors.pushNestedPath("client");
                clientValidator.setUserValidator(userValidator);
                clientValidator.setClientService(clientService);
                clientValidator.setUserService(userService);
                ValidationUtils.invokeValidator(clientValidator, client, errors);
            }
            finally {
                errors.popNestedPath();
            }

        }

    }
}
