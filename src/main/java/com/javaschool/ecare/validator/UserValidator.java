package com.javaschool.ecare.validator;

import com.javaschool.ecare.domain.User;
import com.javaschool.ecare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private Pattern pattern;
    private Matcher matcher;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "error.login", "E-mail is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.password", "Password is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "error.firstName", "First name is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "error.lastName", "Last name is required");

        if (!(user.getLogin() != null && user.getLogin().isEmpty())) {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(user.getLogin());
            if (!matcher.matches()) {
                errors.rejectValue("login", "login.incorrect",
                        "E-mail is incorrect");
            }

            String login = user.getLogin();
            if (!(login != null && "".equals(login))) {
                if (!userService.hasUniqueLogin(user.getId(), login)) {
                    errors.rejectValue("login", "notUniqueValue", new Object[]{"'login'"}, "E-mail must be unique");
                }
            }
        }
    }
}
