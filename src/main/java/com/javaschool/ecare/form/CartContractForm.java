package com.javaschool.ecare.form;

import com.javaschool.ecare.domain.Contract;
import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.domain.Tariff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartContractForm {
    /**
     * Changing contract.
     */
    private Contract contract;

    /**
     * New tariff for the contract.
     */
    private Tariff newTariff;

    private List<CartContractOption> contractOptions;

    /**
     * Default constructor
     */
    public CartContractForm() {
        contractOptions = new ArrayList<>();
    }

    /**
     * Constructor with contract.
     *
     * @param newContract changing contract
     */
    public CartContractForm(final Contract newContract) {
        contract = newContract;
        contractOptions = new ArrayList<>();
    }

    /**
     * Add new option to the cart if it's not there.
     *
     * @param option new option
     */
    public void addNewOption(final Option option) {
        if (!getAvailableOptions().contains(option)) {
            return;
        }

        addOption(option, true);
    }

    /**
     * Delete new option from the cart.
     *
     * @param option deleting option
     */
    public void deleteAddedOption(final Option option) {
        cancelAddOption(option);
    }

    /**
     * Add deactivated option to the cart if it's not there.
     *
     * @param option new option
     */
    public void addDeactivatedOption(final Option option) {
        Set<Option> activeOptions = new HashSet<>(contract.getActivatedOptions());
        activeOptions.removeAll(getDeactivatedOptionsFromList());

        if (!activeOptions.contains(option)) {
            return;
        }
        if (getNewOptionsFromList().contains(option)) {
            return;
        }

        deactivateOption(option, true, false, false);
        renewIncompatibleOptions();
    }

    /**
     * Delete deactivated option from the cart.
     *
     * @param option deleting option
     */
    public void deleteDeactivatedOption(final Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition == null) {
            return;
        }
        if (!optionPosition.isCancelable()) {
            return;
        }

        cancelDeactivateOption(option);
        renewIncompatibleOptions();
    }

    private void addIncompatibleOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition != null && CartOperation.adding.equals(optionPosition.getOperation())) {
            optionPosition.setIncompatible(true);
            deactivateDependingOptions(option);
        }
    }

    private void deleteIncompatibleOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition != null
                && CartOperation.adding.equals(optionPosition.getOperation())
                && optionPosition.isIncompatible()) {
            optionPosition.setIncompatible(false);
            restoreDependingOptions(option);
        }
    }

    private void renewIncompatibleOptions() {
        Set<Option> optionList = new HashSet<>();

        for (CartContractOption optionPosition : contractOptions) {
            if (CartOperation.adding.equals(optionPosition.getOperation())) {
                optionList.add(optionPosition.getOption());
            }
        }

        for (Option option : optionList) {
            if (!hasIncompatibleInFutureList(option)) {
                deleteIncompatibleOption(option);
            } else {
                addIncompatibleOption(option);
            }
        }
    }

    private void addUnsupportedOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition != null) {
            optionPosition.setUnsupported(true);
            if (CartOperation.adding.equals(optionPosition.getOperation())) {
                deleteDependingOptions(option);
            }
        } else {
            deactivateOption(option, false, false, true);
        }
    }

    private void deleteUnsupportedOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition != null && optionPosition.isUnsupported()) {
            optionPosition.setUnsupported(false);
            if (!optionPosition.isAddedByUser()
                    && optionPosition.isCancelable()
                    && CartOperation.deleting.equals(optionPosition.getOperation())) {
                cancelDeactivateOption(option);
            }
        }
    }

    private void renewUnsupportedOptions() {
        Set<Option> availableList;
        if (newTariff != null) {
            availableList = newTariff.getAvailableOptions();
        } else {
            availableList = contract.getTariff().getAvailableOptions();
        }

        Set<Option> optionList = new HashSet<>();
        optionList.addAll(contract.getActivatedOptions());
        for (CartContractOption optionPosition : contractOptions) {
            optionList.add(optionPosition.getOption());
        }

        for (Option option : optionList) {
            if (availableList.contains(option)) {
                deleteUnsupportedOption(option);
            } else {
                addUnsupportedOption(option);
            }
        }
    }

    private void deactivateDependingOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition != null) {
            optionPosition.setDepending(true);
        } else {
            deactivateOption(option, false, true, false);
        }
    }

    private void restoreDependingOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition != null && optionPosition.isDepending()) {
            optionPosition.setDepending(false);
            if (!optionPosition.isAddedByUser() && optionPosition.isCancelable()) {
                contractOptions.remove(optionPosition);
            }
        }

        for (Option dependOption : option.getDependOptions()) {
            restoreDependingOption(dependOption);
        }
    }

    private void addOption(Option option, boolean byUser) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition == null) {
            optionPosition = new CartContractOption(option, CartOperation.adding, byUser, false, false, false);
            contractOptions.add(optionPosition);
            for (Option dependOption : option.getDependOptions()) {
                restoreDependingOption(dependOption);
            }
        }
    }

    private void cancelAddOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition != null) {
            contractOptions.remove(optionPosition);
            deactivateDependingOptions(option);
        }
    }

    private void deactivateOption(Option option, boolean byUser, boolean isDepending, boolean isUnsupported) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition == null) {
            optionPosition = new CartContractOption(option, CartOperation.deleting, byUser, isDepending, isUnsupported, false);
            contractOptions.add(optionPosition);
            deactivateDependingOptions(option);
        }
    }

    private void cancelDeactivateOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        if (optionPosition != null && CartOperation.deleting.equals(optionPosition.getOperation()) && optionPosition.isCancelable()) {
            contractOptions.remove(optionPosition);
            for (Option dependOption : option.getDependOptions()) {
                restoreDependingOption(dependOption);
            }
        }
    }

    private boolean hasMandatoryInFutureList(Option option) {
        Set<Option> mandatoryOptions = new HashSet<>(option.getMandatoryOptions());
        boolean hasMandatoryInFutureList = false;
        for (Option manOption : mandatoryOptions) {
            if (getFutureOptionList().contains(manOption)) {
                hasMandatoryInFutureList = true;
                break;
            }
        }
        if (mandatoryOptions.isEmpty()) {
            hasMandatoryInFutureList = true;
        }
        return hasMandatoryInFutureList;
    }

    private void deactivateDependingOptions(Option option) {
        Set<Option> activeOptions = new HashSet<>(contract.getActivatedOptions());
        activeOptions.addAll(getFutureOptionList());

        for (Option depOption : option.getDependOptions()) {

            if (!hasMandatoryInFutureList(depOption) && activeOptions.contains(depOption)) {
                deactivateDependingOption(depOption);
            }
        }
    }

    private void deleteDependingOptions(Option option) {
        Set<Option> activeOptions = new HashSet<>(contract.getActivatedOptions());

        for (Option depOption : option.getDependOptions()) {
            if (!hasMandatoryInFutureList(depOption)) {
                CartContractOption depOptionPosition = getOptionPosition(depOption);
                if (depOptionPosition != null) {
                    if (CartOperation.deleting.equals(depOptionPosition.getOperation())) {
                        depOptionPosition.setDepending(true);
                    } else if (CartOperation.adding.equals(depOptionPosition.getOperation())) {
                        depOptionPosition.setDepending(true);
                    }
                }
                if (activeOptions.contains(depOption) && depOptionPosition == null) {
                    depOptionPosition = new CartContractOption();
                    depOptionPosition.setOption(depOption);
                    depOptionPosition.setOperation(CartOperation.deleting);
                    depOptionPosition.setAddedByUser(false);
                    depOptionPosition.setDepending(true);

                    contractOptions.add(depOptionPosition);
                    restoreIncompatibleOptions(depOption);
                }
                deleteDependingOptions(depOption);
            }
        }
    }

    private void restoreDependingOptions(Option option) {
        for (Option depOption : option.getDependOptions()) {
            if (hasMandatoryInFutureList(depOption)) {
                CartContractOption depOptionPosition = getOptionPosition(depOption);
                if (depOptionPosition != null) {
                    if (CartOperation.deleting.equals(depOptionPosition.getOperation()) && depOptionPosition.isAddedByUser()) {
                        depOptionPosition.setDepending(false);
                    } else if (CartOperation.deleting.equals(depOptionPosition.getOperation()) && !depOptionPosition.isAddedByUser()) {
                        contractOptions.remove(depOptionPosition);
                        deleteIncompatibleOptions(depOption);
                    } else if (CartOperation.adding.equals(depOptionPosition.getOperation())) {
                        depOptionPosition.setDepending(false);
                    }
                }
                restoreDependingOptions(depOption);
            }
        }
    }

    private boolean hasIncompatibleInFutureList(Option option) {
        Set<Option> incompatibleOptions = new HashSet<>(option.getIncompatibleOptions());
        boolean hasIncompatibleInFutureList = false;

        for (Option incOption : incompatibleOptions) {
            if (getFutureOptionList().contains(incOption)) {
                hasIncompatibleInFutureList = true;
                break;
            }
        }

        return hasIncompatibleInFutureList;
    }

    private void deleteIncompatibleOptions(Option option) {
        for (Option incOption : option.getIncompatibleOptions()) {
            if (hasIncompatibleInFutureList(incOption)) {
                CartContractOption incOptionPosition = getOptionPosition(incOption);
                if (incOptionPosition != null && CartOperation.adding.equals(incOptionPosition.getOperation())) {
                    incOptionPosition.setIncompatible(true);
                }
            }
        }
    }

    private void restoreIncompatibleOptions(Option option) {
        for (Option incompatibleOption : option.getIncompatibleOptions()) {
            if (!hasIncompatibleInFutureList(incompatibleOption)) {
                CartContractOption incOptionPosition = getOptionPosition(incompatibleOption);
                if (incOptionPosition != null && CartOperation.adding.equals(incOptionPosition.getOperation())) {
                    incOptionPosition.setIncompatible(false);
                }
            }
        }
    }

    /**
     * Change contract tariff and deactivate all unsupported options.
     *
     * @param tariff new tariff for the contract
     */
    public void changeTariff(final Tariff tariff) {
        if (contract.getTariff().equals(tariff)) {
            return;
        }

        newTariff = tariff;

        renewUnsupportedOptions();
        renewIncompatibleOptions();
    }

    /**
     * Delete new tariff, remove options from the cart.
     */
    public void deleteNewTariff() {
        if (newTariff == null) {
            return;
        }
        newTariff = null;
        renewUnsupportedOptions();
        renewIncompatibleOptions();
    }

    /**
     * Get list of options that current contract will have after submit
     *
     * @return list of options
     */
    public Set<Option> getFutureOptionList() {
        Set<Option> options = new HashSet<>();
        options.addAll(contract.getActivatedOptions());
        options.addAll(getNewOptionsFromList());
        options.removeAll(getDeactivatedOptionsFromList());
        return options;
    }

    public Set<Option> getNewOptionsFromList() {
        Set<Option> result = new HashSet<>();
        for (CartContractOption optionPosition : contractOptions) {
            if (CartOperation.adding.equals(optionPosition.getOperation())
                    && !optionPosition.isDepending()
                    && !optionPosition.isUnsupported()
                    && !optionPosition.isIncompatible()) {
                result.add(optionPosition.getOption());
            }
        }
        return result;
    }

    public Set<Option> getDeactivatedOptionsFromList() {
        Set<Option> result = new HashSet<>();
        for (CartContractOption optionPosition : contractOptions) {
            if (CartOperation.deleting.equals(optionPosition.getOperation())) {
                result.add(optionPosition.getOption());
            }
        }
        return result;
    }

    /**
     * Get contract that will be saved after submit.
     *
     * @return prototype of new contract
     */
    public Contract getFutureContract() {
        Contract futureContract = new Contract();
        futureContract.setClient(contract.getClient());
        futureContract.setNumber(contract.getNumber());
        futureContract.setTariff(contract.getTariff());
        if (newTariff != null) {
            futureContract.setTariff(newTariff);
        }
        futureContract.setActivatedOptions(getFutureOptionList());

        return futureContract;
    }

    /**
     * Get total cost of connection for all new options.
     *
     * @return total connection cost
     */
    public double getTotalConnectionCost() {
        double totalCost = 0;
        for (Option option : getNewOptionsFromList()) {
            totalCost += option.getConnectionCost();
        }
        return totalCost;
    }

    /**
     * Clear cart for this contract
     */
    public void clearAll() {
        newTariff = null;
        contractOptions = new ArrayList<>();
    }

    public boolean isNewOption(Option option) {
        return getNewOptionsFromList().contains(option);
    }

    public boolean isDeactivatedOption(Option option) {
        return getDeactivatedOptionsFromList().contains(option);
    }

    public boolean isNotCancelable(Option option) {
        CartContractOption position = getOptionPosition(option);
        return position != null && !position.isCancelable();
    }

    public boolean isAvailableOption(Option option) {
        return getAvailableOptions().contains(option);
    }

    public boolean isDependOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        return optionPosition != null && optionPosition.isDepending();
    }

    public boolean isInCart(Option option) {
        return getOptionPosition(option) != null;
    }

    public boolean isUnsupportedOption(Option option) {
        CartContractOption optionPosition = getOptionPosition(option);
        return optionPosition != null && optionPosition.isUnsupported();
    }

    private Set<Option> getAvailableOptions() {
        Set<Option> resultSet = new HashSet<>();

        Tariff currentTariff = contract.getTariff();
        if (newTariff != null) {
            currentTariff = newTariff;
        }

        Set<Option> availableOptions = new HashSet<>(currentTariff.getAvailableOptions());

        resultSet.addAll(currentTariff.getAvailableOptions());

        // remove depending options
        for (Option depOption : availableOptions) {
            Set<Option> mandatoryOptions = new HashSet<>(depOption.getMandatoryOptions());
            boolean depend = true;
            for (Option manOption : mandatoryOptions) {
                if (getFutureOptionList().contains(manOption)) {
                    depend = false;
                    break;
                }
            }

            if (depend && !mandatoryOptions.isEmpty()) {
                resultSet.remove(depOption);
            }
        }

        //remove incompatible options
        for (Option avOption : availableOptions) {
            for (Option incOption : avOption.getIncompatibleOptions()) {
                if (getFutureOptionList().contains(incOption)) {
                    resultSet.remove(avOption);
                }
            }
        }

        return resultSet;
    }

    private CartContractOption getOptionPosition(Option option) {
        for (CartContractOption optionPosition : contractOptions) {
            if (option.equals(optionPosition.getOption())) {
                return optionPosition;
            }
        }
        return null;
    }

    /**
     * Contract getter.
     *
     * @return contract of CartContractForm
     */
    public Contract getContract() {
        return contract;
    }

    /**
     * Contract setter.
     *
     * @param changingContract contract that will be changed after submit
     */
    public void setContract(final Contract changingContract) {
        contract = changingContract;
    }

    /**
     * Tariff getter.
     *
     * @return new tariff of contract
     */
    public Tariff getNewTariff() {
        return newTariff;
    }

//    /**
//     * Deactivated options getter.
//     *
//     * @return list of options that will be deactivated after submit
//     */
//    public Set<Option> getDeactivatedOptions() {
//        Set<Option> resultSet = new HashSet<>();
//        for (Option option : getDeactivatedOptionsFromList()) {
//            if (!isUnsupportedOption(option) && !isDependOption(option)) {
//                resultSet.add(option);
//            }
//        }
//        return resultSet;
//    }
//
//    /**
//     * New options getter.
//     *
//     * @return list of options that will be added after submit
//     */
//    public Set<Option> getNewOptions() {
//        return getNewOptionsFromList();
//    }
//
//    /**
//     * Unsupported options getter.
//     *
//     * @return list of options that will be deactivated after submit with tariff changing
//     */
//    public Set<Option> getUnsupportedOptions() {
//        Set<Option> resultSet = new HashSet<>();
//        for (Option option : getDeactivatedOptionsFromList()) {
//            if (isUnsupportedOption(option)) {
//                resultSet.add(option);
//            }
//        }
//        return resultSet;
//    }
//
//    /**
//     * Depending options getter.
//     *
//     * @return list of options that will be deactivated after submit with mandatory options deleting.
//     */
//    public Set<Option> getDependingOptions() {
//        Set<Option> resultSet = new HashSet<>();
//        for (Option option : getDeactivatedOptionsFromList()) {
//            if (isDependOption(option)) {
//                resultSet.add(option);
//            }
//        }
//        return resultSet;
//    }

    public List<CartContractOption> getContractOptions() {
        return contractOptions;
    }
}
