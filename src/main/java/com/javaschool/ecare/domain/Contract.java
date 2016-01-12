package com.javaschool.ecare.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.validation.annotation.Validated;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Contract of client
 */
@Entity
@Table(name = "contracts")
@NamedQueries({
        @NamedQuery(name = Contract.HAS_UNIQUE_NUMBER,
                query = "select c from Contract c where c.id <> :id and c.number = :number"),
        @NamedQuery(name = Contract.GET_BY_CLIENT,
                query = "select c from Contract c where c.client = :client"),
        @NamedQuery(name = Contract.GET_BY_NUMBER,
                query = "select c from Contract c where c.number = :number")
})
public class Contract extends BaseEntity {
    public static final String HAS_UNIQUE_NUMBER = "Contract.hasUniqueNumber";
    public static final String GET_BY_CLIENT = "Contract.getByClient";
    public static final String GET_BY_NUMBER = "Contract.getByNumber";

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    @Column(name = "number", unique = true)
    private String number;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @ManyToMany
    @JoinTable(name = "contract_options",
            joinColumns = {@JoinColumn(name = "contract_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_id")})
    private Set<Option> activatedOptions = new HashSet<>();

    @Column(name = "blocking_date")
    private Date blockingDate;

    @ManyToOne
    @JoinColumn(name = "blocking_user")
    private User blockingUser;

    public Contract() {
        client = new Client();
    }

    public Contract(Contract contract) {
        number = contract.getNumber();
        tariff = contract.getTariff();
        activatedOptions = contract.getActivatedOptions();
    }

    public Contract(final Client newClient, final String newNumber, final Tariff newTariff, final Set<Option> newActivatedOptions) {
        client = newClient;
        number = newNumber;
        tariff = newTariff;
        activatedOptions = newActivatedOptions;
    }

    public boolean isBlocked() {
        if (blockingDate == null && blockingUser == null) {
            return false;
        }
        return true;
    }

    public boolean isBlockedByClient() {
        if (isBlocked()) {
        return client.getUser().equals(blockingUser);
        }
        else {
            return false;
        }
    }

    public boolean isBlockedByOperator() {
        if (isBlocked()) {
            return Role.ROLE_EMPLOYEE.equals(blockingUser.getRole());
        }
        else {
            return false;
        }
    }

    public double getTotalRegularCost() {
        double totalCost = tariff.getRegularCost();
        for (Option activeOption : activatedOptions) {
            totalCost += activeOption.getRegularCost();
        }

        return totalCost;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client newClient) {
        client = newClient;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String newNumber) {
        number = newNumber;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(final Tariff newTariff) {
        tariff = newTariff;
    }

    public Set<Option> getActivatedOptions() {
        return activatedOptions;
    }

    public void setActivatedOptions(final Set<Option> newActivatedOptions) {
        activatedOptions = newActivatedOptions;
    }

    public Date getBlockingDate() {
        return blockingDate;
    }

    public void setBlockingDate(final Date newBlockingDate) {
        blockingDate = newBlockingDate;
    }

    public User getBlockingUser() {
        return blockingUser;
    }

    public void setBlockingUser(final User newBlockingUser) {
        blockingUser = newBlockingUser;
    }


}
