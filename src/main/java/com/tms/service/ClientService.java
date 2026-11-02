package com.tms.service;

import com.tms.dao.ClientDAO;
import com.tms.model.Client;
import com.tms.model.Role;
import com.tms.util.Session;

import java.sql.SQLException;
import java.util.List;

public class ClientService {

    private final ClientDAO dao = new ClientDAO();

    public void createClient(Client c) throws Exception {
        Session.requireRole(Role.ADMIN); // Or Role.COMMERCIAL if allowed
        if (dao.hasDuplicateTaxId(c.getTaxId())) {
            throw new Exception("Un client actif avec ce numéro fiscal existe déjà.");
        }
        dao.insert(c);
    }

    public void updateClient(Client c) throws Exception {
        Session.requireRole(Role.ADMIN);
        dao.update(c);
    }

    public List<Client> listClients() throws SQLException {
        // Everyone logged in can view clients
        Session.touch();
        return dao.findAllActive();
    }
}