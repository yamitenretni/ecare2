package com.javaschool.ecare.service;

import com.javaschool.ecare.dao.BaseDAO;
import com.javaschool.ecare.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Лена on 26.12.2015.
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private BaseDAO<User> userDao;

    public User addUser(final User user) {
        User updatedUser = userDao.merge(user);

        return updatedUser;
    }

    /**
     * Check unique login constraint for user.
     *
     * @param id    id of checking user
     * @param login checking login
     * @return true if user doesn't violate unique constraints
     */
    public boolean hasUniqueLogin(final long id, final String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("login", login);
        List<User> resultList = userDao.getQueryResult(User.HAS_UNIQUE_LOGIN, User.class, params);
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<User> getUsers() {
        List<User> users = userDao.getAll(User.class);
        return users;
    }

    public User getById(final long userId) {
        return userDao.getById(User.class, userId);
    }

    public User getByLogin(final String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return userDao.getQueryResult(User.FIND_BY_LOGIN, User.class, params).get(0);
    }

    public long checkUser(String login, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        params.put("password", User.getMd5(password));

        List<User> resultList = userDao.getQueryResult(User.CHECK, User.class, params);

        if (!resultList.isEmpty()) {
            return resultList.get(0).getId();
        }
        return 0L;
    }
}
