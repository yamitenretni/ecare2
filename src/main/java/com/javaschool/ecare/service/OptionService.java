package com.javaschool.ecare.service;

import com.javaschool.ecare.dao.BaseDAO;
import com.javaschool.ecare.domain.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class OptionService {

    @Autowired
    private BaseDAO<Option> optionDao;

    @PostConstruct
    public void init() {
        return;
    }

    /**
     * Get option by id from database
     *
     * @param id option's id
     * @return found option
     */
    public Option getById(final long id) {
        Option option = optionDao.getById(Option.class, id);
        return option;
    }

    /**
     * Add or update option in database.
     *
     * @param option updating option
     * @return updated option
     */
    public Option upsertOption(final Option option) {
        Option updatedOption = optionDao.merge(option);

        updateIncompatibleOptions(updatedOption);

        return updatedOption;
    }

    /**
     * Check unique name constraint for option.
     *
     * @param id   id of checking option
     * @param name checking name
     * @return true if option doesn't violate unique constraints
     */
    public boolean hasUniqueName(final long id, final String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        List<Option> resultList = optionDao.getQueryResult(Option.HAS_UNIQUE_NAME, Option.class, params);
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Delete option with the specified id
     *
     * @param id option's id
     */
    public void deleteOption(final long id) {
        Option option = getById(id);
        if (option != null) {
            option.setDeleted(true);

            optionDao.merge(option);
        }
    }

    public List<Option> getAll() {
        return optionDao.getAll(Option.class);
    }

    /**
     * Returns all active (not deleted) options from database
     *
     * @return list of options
     */
    public List<Option> getActiveOptions() {
        List<Option> options = optionDao.getQueryResult(Option.GET_ACTIVE, Option.class, new HashMap<String, Object>());
        return options;
    }

    public Set<Option> hasCrossingOptions(Option option) {
        Set<Option> result = new HashSet<>();
        if (option.getMandatoryOptions().isEmpty()) {
            return result;
        }
        if (option.getIncompatibleOptions().isEmpty()) {
            return result;
        }
        result.addAll(option.getIncompatibleOptions());
        result.retainAll(option.getMandatoryOptions());

        return result;
    }

    private void updateIncompatibleOptions(Option option) {
        Map<String, Object> params = new HashMap<>();
        params.put("option", option);
        List<Option> connectedOptions = optionDao.getQueryResult(Option.GET_INCOMPATIBLE_OPTIONS, Option.class, params);

        if (option.getIncompatibleOptions() == null) {
            option.setIncompatibleOptions(new HashSet<Option>());
        }

        Set<Option> oldIncOptions = new HashSet<>(connectedOptions);
        oldIncOptions.removeAll(option.getIncompatibleOptions());

        for (Option oldOption : oldIncOptions) {
            Set<Option> newIncOptionList = oldOption.getIncompatibleOptions();
            newIncOptionList.remove(option);
            oldOption.setIncompatibleOptions(newIncOptionList);
            optionDao.merge(oldOption);
        }

        Set<Option> newIncOptions = new HashSet<>(option.getIncompatibleOptions());
        newIncOptions.removeAll(connectedOptions);

        for (Option newOption : newIncOptions) {
            Set<Option> newIncOptionList = newOption.getIncompatibleOptions();
            newIncOptionList.add(option);
            newOption.setIncompatibleOptions(newIncOptionList);
            optionDao.merge(newOption);
        }

    }
}
