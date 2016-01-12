package com.javaschool.ecare.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Client of mobile operator.
 */
@Entity
@Table(name = "clients")
@NamedQueries({
        @NamedQuery(name = Client.HAS_UNIQUE_PASSPORT,
                query = "select c from Client c where c.id <> :id and c.passportData = :passport"),
        @NamedQuery(name = Client.GET_BY_USER,
                query = "select c from Client c where c.user = :user"),
        @NamedQuery(name = Client.GET_BY_TARIFF,
                query = "select c from Client c left join c.contracts ct where ct.tariff = :tariff")
})
public class Client extends BaseEntity {
    public static final String HAS_UNIQUE_PASSPORT = "Client.hasUniquePassport";
    public static final String GET_BY_USER = "Client.getByUser";
    public static final String GET_BY_TARIFF = "Client.getByTariff";

    /**
     * User which connected with the client.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * List of contracts (phones) of the client.
     */
    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<Contract> contracts = new ArrayList<>();

    /**
     * Date of blocking.
     */
    @Column(name = "blocking_date")
//    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private Date blockingDate;

    /**
     * Client's birth date.
     */
    @Column(name = "birth_date")
//    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private Calendar birthDate;

    /**
     * Client's passport: all data in one line.
     */
    @Column(name = "passport_data", unique = true)
    private String passportData;

    /**
     * Client's address: all data in one line.
     */
    @Column(name = "address")
    private String address;

    /**
     * The blank constructor for the client.
     */
    public Client() {
        user = new User();
    }

    /**
     * The main constructor for the client.
     *
     * @param newUser         user for the new client
     * @param newContracts    list of contracts (phones)
     * @param newBirthDate    date of birth
     * @param newPassportData passport (one line)
     * @param newAddress      address (one line)
     */
    public Client(final User newUser,
                  final List<Contract> newContracts,
                  final Calendar newBirthDate,
                  final String newPassportData,
                  final String newAddress) {
        user = newUser;
        contracts = newContracts;
        birthDate = newBirthDate;
        passportData = newPassportData;
        address = newAddress;
    }

    public boolean isBlocked() {
        if (blockingDate == null) {
            return false;
        }
        return true;
    }

    @JsonIgnore
    public List<Contract> getActiveContracts() {
        List<Contract> activeContracts = new ArrayList<>();
        for (Contract contract: contracts) {
            if (!contract.isBlocked()) {
                activeContracts.add(contract);
            }
        }
        return activeContracts;
    }

    /**
     * user getter.
     *
     * @return client's linked user
     */
    public User getUser() {
        return user;
    }

    /**
     * user setter.
     *
     * @param newUser user which will be linked to client
     */
    public void setUser(final User newUser) {
        user = newUser;
    }

    /**
     * contracts getter.
     *
     * @return list of client's contracts
     */
    public List<Contract> getContracts() {
        return contracts;
    }

    /**
     * contracts setter.
     *
     * @param newContracts list of contracts
     */
    public void setContracts(final List<Contract> newContracts) {
        contracts = newContracts;
    }

    /**
     * birth date getter.
     *
     * @return client's birth date
     */
    public Calendar getBirthDate() {
        return birthDate;
    }

    /**
     * birth date setter.
     *
     * @param newBirthDate client's birth date
     */
    public void setBirthDate(final Calendar newBirthDate) {
        birthDate = newBirthDate;
    }

    /**
     * passport data getter.
     *
     * @return client's passport data
     */
    public String getPassportData() {
        return passportData;
    }

    /**
     * passport data setter.
     *
     * @param newPassportData client's passport data
     */
    public void setPassportData(final String newPassportData) {
        passportData = newPassportData;
    }

    /**
     * address getter.
     *
     * @return client's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * address setter.
     *
     * @param newAddress client's address
     */
    public void setAddress(final String newAddress) {
        address = newAddress;
    }

    /**
     * blocking date getter.
     *
     * @return blocking date
     */
    public Date getBlockingDate() {
        return blockingDate;
    }

    /**
     * blocking date setter.
     *
     * @param newBlockingDate blocking date
     */
    public void setBlockingDate(final Date newBlockingDate) {
        blockingDate = newBlockingDate;
    }


}
