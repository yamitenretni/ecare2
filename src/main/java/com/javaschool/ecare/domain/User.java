package com.javaschool.ecare.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = User.FIND_BY_LOGIN, query = "select u from User u where u.login = :login"),
        @NamedQuery(name = User.CHECK,
                query = "select u from User u where u.login = :login and u.password = :password"),
        @NamedQuery(name = User.HAS_UNIQUE_LOGIN,
                query = "select u from User u where u.id <> :id and u.login = :login")
})
public class User extends BaseEntity {
    public static final String CHECK = "User.checkUser";
    public static final String HAS_UNIQUE_LOGIN = "User.hasUniqueLogin";
    public static final String FIND_BY_LOGIN = "User.findByLogin";

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "role")
    private Role role;

    public User() {
    }

    public User(final String newLogin, final String newPassword, final Role newRole) {
        login = newLogin;
        password = getMd5(newPassword);
        role = newRole;
    }

    @JsonIgnore
    public String getRoleName() {
        return role.name();
    }

    public static String getMd5(final String s) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        byte[] digest = md.digest(s.getBytes());
        BigInteger bigInt = new BigInteger(1, digest);
        String hashText = bigInt.toString(16);

        // add leading zeroes, which removed by java
        while(hashText.length() < 32 ){
            hashText = "0" + hashText;
        }
        return hashText;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String newLogin) {
        login = newLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String newPassword) {
        password = getMd5(newPassword);
    }

    /**
     * first name getter.
     *
     * @return client's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * first name setter.
     *
     * @param newFirstName client's first name
     */
    public void setFirstName(final String newFirstName) {
        firstName = newFirstName;
    }

    /**
     * last name getter.
     *
     * @return client's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * last name setter.
     *
     * @param newLastName client's last name
     */
    public void setLastName(final String newLastName) {
        lastName = newLastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role newRole) {
        role = newRole;
    }
}
