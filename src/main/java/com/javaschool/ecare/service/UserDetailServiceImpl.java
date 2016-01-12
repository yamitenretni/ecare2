package com.javaschool.ecare.service;

import com.javaschool.ecare.dao.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(value = "customUserDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private BaseDAO<com.javaschool.ecare.domain.User> userDao;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        UserDetails result = null;

        Map<String, Object> params = new HashMap<>();
        params.put("login", username);

        com.javaschool.ecare.domain.User user;
        List<com.javaschool.ecare.domain.User> userList = userDao.getQueryResult(com.javaschool.ecare.domain.User.FIND_BY_LOGIN, com.javaschool.ecare.domain.User.class, params);
        if (!userList.isEmpty()) {
            user = userList.get(0);
            result = buildUserForAuthentication(user);
        }
        return result;
    }

    private User buildUserForAuthentication(com.javaschool.ecare.domain.User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleName()));
        return new User(user.getLogin(), user.getPassword(), true, true, true, true, authorities);
    }
}
