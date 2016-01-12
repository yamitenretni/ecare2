package com.javaschool.ecare.listener;

import com.javaschool.ecare.domain.User;
import com.javaschool.ecare.service.ClientService;
import com.javaschool.ecare.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class AuthenticationListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private HttpSession session;

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent event) {
        UserDetails userDetails = (UserDetails)event.getAuthentication().getPrincipal();
        User currentUser = userService.getByLogin(userDetails.getUsername());
        session.setAttribute("currentUser", currentUser);
    }
}
