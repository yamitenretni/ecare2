package com.javaschool.ecare.form;

import com.javaschool.ecare.domain.Contract;
import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.domain.Tariff;
import com.javaschool.ecare.service.ContractService;
import com.javaschool.ecare.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartForm {

    @Autowired
    private TariffService tariffService;

    @Autowired
    private ContractService contractService;

    /**
     * List of contracts with changes.
     */
    private List<CartContractForm> cartContractForms;

    @PostConstruct
    public void init() {
        return;
    }

    /**
     * Default constructor.
     */
    public CartForm() {
        cartContractForms = new ArrayList<>();
    }

    /**
     * Get CartContractForm by Contract or create, if it's not exist.
     *
     * @param contract contract for search
     * @return found or created CartContractForm
     */
    public CartContractForm getCartContractForm(final Contract contract) {
        CartContractForm cartContractForm = contains(contract);

        if (cartContractForm == null) {
            cartContractForm = new CartContractForm(contract);
            cartContractForms.add(cartContractForm);
        }
        return cartContractForm;
    }

    /**
     * Getter for list of changing contracts. Removes empty position before returning.
     *
     * @return list of contracts with changes
     */
    public List<CartContractForm> getCartContractForms() {
        Iterator it = cartContractForms.iterator();
        while (it.hasNext()) {
            CartContractForm position = (CartContractForm) it.next();
            if (position.getNewTariff() == null && position.getContractOptions().isEmpty()) {
                it.remove();
            } else {
                Contract contract = contractService.getById(position.getContract().getId());
                if (contract.isBlocked()) {
                    it.remove();
                }
            }
        }

        return cartContractForms;
    }

    /**
     * Remove given position from cart.
     *
     * @param cartContractForm removing position
     */
    public void deleteCartContractForm(CartContractForm cartContractForm) {
        cartContractForms.remove(cartContractForm);
    }

    /**
     * Get list of available tariffs for the contract and current cart position.
     *
     * @param contract given contract
     * @return list of tariffs
     */
    public List<Tariff> getAvailableTariffs(final Contract contract) {
        List<Tariff> resultList = new ArrayList<>();
        resultList.addAll(tariffService.getActiveTariffs());
        resultList.remove(contract.getTariff());
        CartContractForm cartContractForm = contains(contract);
        if (cartContractForm != null && cartContractForm.getNewTariff() != null) {
            resultList.remove(cartContractForm.getNewTariff());
        }
        return resultList;
    }

    public List<ContractAvailableOptionForm> getAllAvailableOptions(final Contract contract) {
        List<ContractAvailableOptionForm> resultList = new ArrayList<>();
        Set<Option> optionSet = new HashSet<>(getAvailableOptionsForContract(contract));
        optionSet.removeAll(getActiveOptionsForContract(contract));
        optionSet.removeAll(contract.getActivatedOptions());

        for (Option option : optionSet) {
            resultList.add(new ContractAvailableOptionForm(option,
                    getIncompatibleOptionsFor(contract, option),
                    getMandatoryOptionsForDepend(contract, option)));
        }
        return resultList;
    }

    public List<Option> getContractDeactivatedOptions(final Contract contract) {
        List<Option> resultList = new ArrayList<>();
        CartContractForm position = contains(contract);
        if (position != null) {
            resultList.addAll(position.getDeactivatedOptionsFromList());
        }
        return resultList;
    }

    public Set<Option> getIncompatibleOptionsFor(Contract contract, Option option) {
        Set<Option> result = new HashSet<>(getActiveOptionsForContract(contract));
        result.retainAll(option.getIncompatibleOptions());

        return result;
    }

    public Set<Option> getMandatoryOptionsForDepend(Contract contract, Option option) {
        Set<Option> result = new HashSet<>(getAvailableOptionsForContract(contract));
        result.retainAll(option.getMandatoryOptions());
        boolean removed = result.removeAll(getActiveOptionsForContract(contract));

        if (removed) {
            return new HashSet<>();
        } else {
            return result;
        }
    }

    private CartContractForm contains(final Contract contract) {
        for (CartContractForm cartContract : cartContractForms) {
            if (cartContract.getContract().equals(contract)) {
                return cartContract;
            }
        }
        return null;
    }

    private List<Option> getAvailableOptionsForContract(final Contract contract) {
        List<Option> availableOptions = new ArrayList<>();
        CartContractForm cartContractForm = contains(contract);
        Tariff currentTariff = null;
        if (cartContractForm != null) {
            currentTariff = cartContractForm.getNewTariff();

        }
        if (currentTariff == null) {
            currentTariff = contract.getTariff();
        }

        availableOptions.addAll(currentTariff.getAvailableOptions());

        return availableOptions;
    }

    private List<Option> getActiveOptionsForContract(final Contract contract) {
        List<Option> activeOptions = new ArrayList<>();
        CartContractForm cartContractForm = contains(contract);
        if (cartContractForm != null) {
            activeOptions.addAll(cartContractForm.getFutureOptionList());
        } else {
            activeOptions.addAll(contract.getActivatedOptions());
        }

        return activeOptions;
    }
}
