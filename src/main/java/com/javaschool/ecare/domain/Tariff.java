package com.javaschool.ecare.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tariffs", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@NamedQueries({
        @NamedQuery(name = Tariff.GET_ACTIVE,
                query = "select t from Tariff t where t.isDeleted = false"),
        @NamedQuery(name = Tariff.HAS_UNIQUE_NAME,
                query = "select t from Tariff t where t.name = :name and t.id <> :id")
})
public class Tariff extends BaseEntity {
    public static final String GET_ACTIVE = "Tariff.getAllActive";
    public static final String HAS_UNIQUE_NAME = "Tariff.hasUniqueName";

    @Column(name = "name")
    private String name;

    @Column(name = "regular_cost")
    private double regularCost = 0;

    @ManyToMany
    @JoinTable(name = "tariff_options",
            joinColumns = {@JoinColumn(name = "tariff_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_id")})
    private Set<Option> availableOptions = new HashSet<>();

    @Column(name = "deleted")
    private boolean isDeleted = false;

    public Tariff() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String newName) {
        name = newName;
    }

    public double getRegularCost() {
        return regularCost;
    }

    public void setRegularCost(final double newRegularCost) {
        regularCost = newRegularCost;
    }

    public Set<Option> getAvailableOptions() {
        return availableOptions;
    }

    public void setAvailableOptions(final Set<Option> newAvailableOptions) {
        availableOptions = newAvailableOptions;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
