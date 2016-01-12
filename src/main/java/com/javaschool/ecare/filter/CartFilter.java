package com.javaschool.ecare.filter;

import com.javaschool.ecare.domain.Contract;
import com.javaschool.ecare.domain.Role;
import com.javaschool.ecare.domain.User;
import com.javaschool.ecare.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CartFilter implements Filter {

    @Autowired
    ContractService contractService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        String actionPath = req.getServletPath();
        String refPath = req.getHeader("referer");

        Pattern cartUrlPattern = Pattern.compile("^/cart/(\\d+)/.*");
        Matcher cartUrlMatcher = cartUrlPattern.matcher(actionPath);

//        ContractService contractSvc = new ContractService();

        if (cartUrlMatcher.matches()) {
            long contractId = Long.parseLong(cartUrlMatcher.group(1));
            Contract contract = contractService.getById(contractId);

            String userRole = currentUser.getRoleName();
            User contractUser = contract.getClient().getUser();

            if (("ROLE_EMPLOYEE".equals(userRole) || contractUser.getId() == currentUser.getId()) && !contract.isBlocked()) {
                filterChain.doFilter(req, resp);
            }
            else {
                if (refPath == null) {
                    refPath = req.getContextPath() + "/my/contract";
                }
                resp.sendRedirect(refPath);
            }
        }
        else {
            filterChain.doFilter(req, resp);
        }



    }

    @Override
    public void destroy() {

    }
}
