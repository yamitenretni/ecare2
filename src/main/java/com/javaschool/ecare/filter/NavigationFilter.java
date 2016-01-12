package com.javaschool.ecare.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NavigationFilter implements Filter {

    private static Map<String, String> urlMap;

    static {
        urlMap = new LinkedHashMap<>();
        urlMap.put("/staff/tariff/add", "addTariff");
        urlMap.put("/staff/tariff/", "Tariff");
        urlMap.put("/staff/tariff", "allTariffs");
        urlMap.put("/staff/tariff", "allTariffs");
        urlMap.put("/staff/option/add", "addOption");
        urlMap.put("/staff/option/", "Option");
        urlMap.put("/staff/option", "allOptions");
        urlMap.put("/staff/client/add", "addClient");
        urlMap.put("/staff/contract", "Client");
        urlMap.put("/staff/client/", "Client");
        urlMap.put("/staff/client", "allClients");
        urlMap.put("/my/tariff", "myTariffs");
        urlMap.put("/my/contract", "myContracts");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String path = req.getServletPath();

        for (String url : urlMap.keySet()) {
            if(path.contains(url)) {
                req.setAttribute("position", urlMap.get(url));
                break;
            }
        }

        filterChain.doFilter(req, resp);

    }

    @Override
    public void destroy() {

    }
}
