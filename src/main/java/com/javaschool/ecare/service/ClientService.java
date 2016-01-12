package com.javaschool.ecare.service;

import com.javaschool.ecare.dao.BaseDAO;
import com.javaschool.ecare.domain.Client;
import com.javaschool.ecare.domain.Contract;
import com.javaschool.ecare.domain.Tariff;
import com.javaschool.ecare.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ClientService {
    @Autowired
    private BaseDAO<Client> clientDao;

    @Autowired
    private ContractService contractService;


    /**
     * Get client by id from database
     *
     * @param id client's id
     * @return found client
     */
    public Client getById(final long id) {
        return clientDao.getById(Client.class, id);
    }

    /**
     * Add or update client
     *
     * @param client client object
     * @return created or updated client
     */
    public Client upsertClient(final Client client) {
        Client updatedClient = clientDao.merge(client);

        return updatedClient;
    }


    /**
     * Check unique passport constraint for client.
     *
     * @param id       id of checking client
     * @param passport checking passport
     * @return true if client doesn't violate unique constraints
     */
    public boolean hasUniquePassport(final long id, final String passport) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("passport", passport);

        List<Client> resultList = clientDao.getQueryResult(Client.HAS_UNIQUE_PASSPORT, Client.class, params);
        if (resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Get client by user.
     *
     * @param user given user
     * @return client linked with given user
     */
    public Client getByUser(final User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);

        List<Client> resultList = clientDao.getQueryResult(Client.GET_BY_USER, Client.class, params);
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }

    }

    /**
     * Add link between client and user in database
     *
     * @param clientId client's id in database
     * @param user     object contains user's data
     */
    public void addUser(final long clientId, final User user) {
        Client client = getById(clientId);
        client.setUser(user);

        clientDao.merge(client);
    }

    /**
     * Update contract list of client in database
     *
     * @param clientId  client's id in database
     * @param contracts list of contracts
     */
    public void updateContractList(final long clientId, final List<Contract> contracts) {
        Client client = getById(clientId);
        client.setContracts(contracts);

        clientDao.merge(client);
    }

    /**
     * Block client and all his contracts.
     *
     * @param client    blocking client
     * @param blockUser user, who block client
     */
    public void blockClient(final Client client, final User blockUser) {
        if (!client.isBlocked()) {
            client.setBlockingDate(new Date());

            for (Contract contract : client.getContracts()) {
                contractService.blockContract(contract, blockUser);
            }

            clientDao.merge(client);
        }
    }

    public void unlockClient(final Client client) {
        if (client.isBlocked()) {
            client.setBlockingDate(null);

            clientDao.merge(client);

            for (Contract contract : client.getContracts()) {
                if (!client.getUser().equals(contract.getBlockingUser())) {
                    contractService.unlockContract(contract);
                }
            }
        }
    }

    /**
     * Get all clients from database
     *
     * @return list of clients
     */
    public List<Client> getClients() {
        return clientDao.getAll(Client.class);
    }

    public List<Client> getByTariff(final Tariff tariff) {
        Map<String, Object> params = new HashMap<>();
        params.put("tariff", tariff);
        return clientDao.getQueryResult(Client.GET_BY_TARIFF, Client.class, params);
    }
}
