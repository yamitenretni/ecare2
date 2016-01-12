package com.javaschool.ecare.service;

import com.javaschool.ecare.dao.BaseDAO;
import com.javaschool.ecare.domain.Client;
import com.javaschool.ecare.domain.Contract;
import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.domain.User;
import com.javaschool.ecare.form.CartContractForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class ContractService {

    /**
     * Create DAO for clients.
     */
    @Autowired
    private BaseDAO<Contract> contractDao;

    /**
     * Get service for tariffs.
     */
    @Autowired
    private TariffService tariffService;

    /**
     * Get contract by id from database
     *
     * @param id contract's id
     * @return found contract
     */
    public Contract getById(final long id) {
        return contractDao.getById(Contract.class, id);
    }

    /**
     * Add or update contract in database
     *
     * @param contract contract object
     * @return created or updated contract
     */
    public Contract upsertContract(final Contract contract) {
        Contract updatedContract = contractDao.merge(contract);

        return updatedContract;
    }

    /**
     * Check unique number constraint for contract.
     *
     * @param id     id of checking contract
     * @param number checking number
     * @return true if number is unique
     */
    public boolean hasUniqueNumber(final long id, final String number) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("number", number);
        List<Contract> resultList = contractDao.getQueryResult(Contract.HAS_UNIQUE_NUMBER, Contract.class, params);
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Block contract since current date.
     *
     * @param contract  blocking contract
     * @param blockUser user, who block contract
     * @return blocked contract
     */
    public Contract blockContract(final Contract contract, final User blockUser) {
        Contract blockedContract = contract;
        if (!contract.isBlocked()) {
            contract.setBlockingDate(new Date());
            contract.setBlockingUser(blockUser);

            blockedContract = contractDao.merge(contract);
        }

        return blockedContract;
    }

    /**
     * Unlock given contract.
     *
     * @param contract unlocking contract
     * @return unlocked contract
     */
    public void unlockContract(final Contract contract) {
        if (contract.isBlocked() && !contract.getClient().isBlocked()) {
            contract.setBlockingDate(null);
            contract.setBlockingUser(null);

            contractDao.merge(contract);
        }

    }

    /**
     * Get contracts of the given client.
     *
     * @param client client for the search
     * @return list of client's contracts
     */
    public List<Contract> getByClient(final Client client) {
        Map<String, Object> params = new HashMap<>();
        params.put("client", client);
        List<Contract> resultList = contractDao.getQueryResult(Contract.GET_BY_CLIENT, Contract.class, params);
        return resultList;
    }

    /**
     * Get contract with given number.
     *
     * @param number number of contract
     * @return found contract
     */
    public Contract getByNumber(final String number) {
        Map<String, Object> params = new HashMap<>();
        params.put("number", number);
        List<Contract> resultList = contractDao.getQueryResult(Contract.GET_BY_NUMBER, Contract.class, params);
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    public Set<Option> hasIncompatibleOptions(final Contract contract) {
        Set<Option> result = new HashSet<>();
        Set<Option> activeOptions = new HashSet<>(contract.getActivatedOptions());
        for(Option activeOption : activeOptions) {
            for (Option incOption : activeOption.getIncompatibleOptions()) {
                if (activeOptions.contains(incOption)) {
                    result.add(activeOption);
                    result.add(incOption);
                }
            }
        }
        return result;
    }

    public Set<Option> hasDependOptions(final Contract contract) {
        Set<Option> result = new HashSet<>();
        Set<Option> activeOptions = new HashSet<>(contract.getActivatedOptions());
        for(Option activeOption : activeOptions) {
            Set<Option> mandatoryOptions = new HashSet<>(activeOption.getMandatoryOptions());
            boolean depend = true;
            for (Option manOption : mandatoryOptions) {
                if (activeOptions.contains(manOption)) {
                    depend = false;
                    break;
                }
            }
            if (depend == true && !mandatoryOptions.isEmpty()) {
                result.add(activeOption);
            }
        }
        return result;
    }

//    /**
//     * Get list of available tariffs for the contract and current cart position.
//     *
//     * @param contract     given contract
//     * @param cartContract current cart position
//     * @return list of tariffs
//     */
//    public final List<ContractTariff> getAvailableTariffs(final Contract contract, final CartContractForm cartContract) {
//        List<ContractTariff> resultList = new ArrayList<>();
//        resultList.addAll(TARIFF_SVC.getActiveTariffs());
//        resultList.remove(contract.getTariff());
//        if (cartContract != null && cartContract.getNewTariff() != null) {
//            resultList.remove(cartContract.getNewTariff());
//        }
//        return resultList;
//    }

//    /**
//     * Get list of the options which could be added to the contract with given cart position.
//     *
//     * @param contract     given contract
//     * @param cartContract current cart position
//     * @return list of options
//     */
//    public List<Option> getAvailableOptions(final Contract contract, final CartContractForm cartContract) {
//        List<Option> availableOptions = getAvailableOptionsForTariff(contract, cartContract);
//        List<Option> activeOptions = getActiveOptionsForContract(contract, cartContract);
//
//        availableOptions.removeAll(getIncompatibleOptions(contract, cartContract).keySet());
//        availableOptions.removeAll(getDependOptions(contract, cartContract).keySet());
////        for (ContractOption option : activeOptions) {
////            availableOptions.removeAll(option.getIncompatibleOptions());
////        }
//        availableOptions.removeAll(activeOptions);
//        availableOptions.removeAll(contract.getActivatedOptions());
//
//        return availableOptions;
//    }

//    /**
//     * Get list of options, which disabled for given contracts with current cart position.
//     *
//     * @param contract     given contract object
//     * @param cartContract current cart position
//     * @return map, where key is disable option and value is the list of obstructive options
//     */
//    public final Map<ContractOption, List<ContractOption>> getIncompatibleOptions(final Contract contract, final CartContractForm cartContract) {
//        Map<ContractOption, List<ContractOption>> resultMap = new HashMap<>();
//        List<ContractOption> availableOptions = getAvailableOptionsForTariff(contract, cartContract);
//        List<ContractOption> activeOptions = getActiveOptionsForContract(contract, cartContract);
//        List<ContractOption> incompatibleOptions = new ArrayList<>();
//
//        for (ContractOption activeOption : activeOptions) {
//            incompatibleOptions.addAll(activeOption.getIncompatibleOptions());
//        }
//
//        incompatibleOptions.retainAll(availableOptions);
//        incompatibleOptions.removeAll(contract.getActivatedOptions());
//
//        for (ContractOption incOption : incompatibleOptions) {
//            List<ContractOption> obstructiveOptions = new ArrayList<>(incOption.getIncompatibleOptions());
//            obstructiveOptions.retainAll(activeOptions);
//            resultMap.put(incOption, obstructiveOptions);
//        }
//
//        return resultMap;
//    }

//    /**
//     * Get list of options, which disabled for given contracts with current cart position.
//     *
//     * @param contract     given contract object
//     * @param cartContract current cart position
//     * @return map, where key is disable option and value is the list of mandatory options
//     */
//    public final Map<ContractOption, List<ContractOption>> getDependOptions(final Contract contract, final CartContractForm cartContract) {
//        Map<ContractOption, List<ContractOption>> resultMap = new HashMap<>();
//        List<ContractOption> availableOptions = getAvailableOptionsForTariff(contract, cartContract);
//        List<ContractOption> activeOptions = getActiveOptionsForContract(contract, cartContract);
//        List<ContractOption> dependOptions = new ArrayList<>();
//
//        for (ContractOption availableOption : availableOptions) {
//            boolean isDepend = true;
//            Set<ContractOption> mandatoryOptions = availableOption.getMandatoryOptions();
//            if (mandatoryOptions.size() == 0) {
//                isDepend = false;
//            }
//            else {
//                for (ContractOption mandatoryOption : mandatoryOptions) {
//                    if (activeOptions.contains(mandatoryOption)) {
//                        isDepend = false;
//                        break;
//                    }
//                }
//            }
//            if (isDepend) {
//                dependOptions.add(availableOption);
//            }
//        }
//
//        for (ContractOption depOption : dependOptions) {
//            List<ContractOption> mandatoryOptions = new ArrayList<>(depOption.getMandatoryOptions());
//            mandatoryOptions.retainAll(availableOptions);
//            resultMap.put(depOption, mandatoryOptions);
//        }
//
//        return resultMap;
//    }



//    private List<Option> getAvailableOptionsForTariff(final Contract contract, final CartContractForm cartContract) {
//        List<Option> availableOptions = new ArrayList<>();
//        if (cartContract != null && cartContract.getNewTariff() != null) {
//            availableOptions.addAll(cartContract.getNewTariff().getAvailableOptions());
//        }
//        else {
//            availableOptions.addAll(contract.getTariff().getAvailableOptions());
//        }
//
//        return availableOptions;
//    }
//
//    private List<Option> getActiveOptionsForContract(final Contract contract, final CartContractForm cartContract) {
//        List<Option> activeOptions = new ArrayList<>();
//        if (cartContract != null) {
//            activeOptions.addAll(cartContract.getFutureOptionList());
//
//        }
//        else {
//            activeOptions.addAll(contract.getActivatedOptions());
//        }
//
//        return activeOptions;
//    }
}
