package com.javaschool.ecare.form;

import com.javaschool.ecare.domain.Option;

import java.util.Set;

public class ContractAvailableOptionForm {
    Option option;
    Set<Option> incompatibleList;
    Set<Option> mandatoryList;

    public ContractAvailableOptionForm(Option option, Set<Option> incompatibleList, Set<Option> mandatoryList) {
        this.option = option;
        this.incompatibleList = incompatibleList;
        this.mandatoryList = mandatoryList;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Set<Option> getIncompatibleList() {
        return incompatibleList;
    }

    public void setIncompatibleList(Set<Option> incompatibleList) {
        this.incompatibleList = incompatibleList;
    }

    public Set<Option> getMandatoryList() {
        return mandatoryList;
    }

    public void setMandatoryList(Set<Option> mandatoryList) {
        this.mandatoryList = mandatoryList;
    }

    public boolean isAvailable() {
        return incompatibleList.isEmpty() && mandatoryList.isEmpty();
    }
}
