package com.javaschool.ecare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "options")
@NamedQueries({
        @NamedQuery(name = Option.GET_ACTIVE,
                query = "select o from Option o where o.isDeleted = false"),
        @NamedQuery(name = Option.HAS_UNIQUE_NAME,
                query = "select o from Option o where o.name = :name and o.id <> :id"),
        @NamedQuery(name = Option.GET_INCOMPATIBLE_OPTIONS,
                query = "select o from Option o left join o.incompatibleOptions io where io = :option")
})
public class Option extends BaseEntity {
    public static final String GET_ACTIVE = "Option.getActive";
    public static final String HAS_UNIQUE_NAME = "Option.hasUniqueName";
    public static final String GET_INCOMPATIBLE_OPTIONS = "Option.getIncompatibleOptions";

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "connection_cost")
    private double connectionCost = 0;

    @Column(name = "regular_cost")
    private double regularCost = 0;

    @Column(name = "deleted")
    private boolean isDeleted = false;

    @ManyToMany
    @JoinTable(name = "incompatible_options",
            joinColumns = {@JoinColumn(name = "option_id")},
            inverseJoinColumns = {@JoinColumn(name = "incomp_option_id")})
    private Set<Option> incompatibleOptions = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "mandatory_options",
            joinColumns = {@JoinColumn(name = "option_id")},
            inverseJoinColumns = {@JoinColumn(name = "mand_option_id")})
    private Set<Option> mandatoryOptions = new HashSet<>();

    @ManyToMany(mappedBy = "mandatoryOptions")
    private Set<Option> dependOptions = new HashSet<>();


    public Option() {
    }

    public Option(final String newName, final double newConnectionCost, final double newRegularCost) {
        name = newName;
        connectionCost = newConnectionCost;
        regularCost = newRegularCost;
    }

    public String getName() {
        return name;
    }

    public void setName(final String newName) {
        name = newName;
    }

    public double getConnectionCost() {
        return connectionCost;
    }

    public void setConnectionCost(final double newConnectionCost) {
        connectionCost = newConnectionCost;
    }

    public double getRegularCost() {
        return regularCost;
    }

    public void setRegularCost(final double newRegularCost) {
        regularCost = newRegularCost;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(final boolean deleted) {
        isDeleted = deleted;
    }

    @JsonIgnore
    public Set<Option> getIncompatibleOptions() {
        return incompatibleOptions;
    }

    public void setIncompatibleOptions(Set<Option> incompatibleOptions) {
        if (incompatibleOptions == null) {
            incompatibleOptions = new HashSet<>();
        }
        this.incompatibleOptions = incompatibleOptions;
    }

    @JsonIgnore
    public Set<Option> getMandatoryOptions() {
        return mandatoryOptions;
    }

    public void setMandatoryOptions(Set<Option> mandatoryOptions) {
        if (mandatoryOptions == null) {
            mandatoryOptions = new HashSet<>();
        }
        this.mandatoryOptions = mandatoryOptions;
    }

    @JsonIgnore
    public Set<Option> getDependOptions() {
        return dependOptions;
    }

    public void setDependOptions(Set<Option> dependOptions) {
        if (dependOptions == null) {
            dependOptions = new HashSet<>();
        }
        this.dependOptions = dependOptions;
    }
}
