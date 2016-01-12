package com.javaschool.ecare.service;

import com.javaschool.ecare.dao.BaseDAO;
import com.javaschool.ecare.domain.Tariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TariffService {

    @Autowired
    private BaseDAO<Tariff> tariffDao;

    public Tariff upsertTariff(final Tariff tariff) {
        Tariff updatedTariff = tariffDao.merge(tariff);

        return updatedTariff;
    }

    public List<Tariff> getAll() {
        return tariffDao.getAll(Tariff.class);
    }

    /**
     * Check unique name constraint for tariff.
     *
     * @param id   id of checking tariff
     * @param name checking name
     * @return true if tariff doesn't violate unique constraints
     */
    public boolean hasUniqueName(final long id, final String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        List<Tariff> resultList = tariffDao.getQueryResult(Tariff.HAS_UNIQUE_NAME, Tariff.class, params);
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Tariff> getActiveTariffs() {
        List<Tariff> tariffs = tariffDao.getQueryResult(Tariff.GET_ACTIVE, Tariff.class, new HashMap<String, Object>());
        return tariffs;
    }

    public Tariff getById(final long id) {
        return tariffDao.getById(Tariff.class, id);
    }

    public void deleteTariff(final long id) {
        Tariff tariff = getById(id);
        tariff.setDeleted(true);

        tariffDao.merge(tariff);
    }
}
