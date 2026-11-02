package com.tms.service;

import com.tms.dao.RequestDAO;
import com.tms.model.Request;
import com.tms.model.RequestState;
import com.tms.model.Role;
import com.tms.util.Session;

import java.sql.SQLException;
import java.util.List;

public class RequestService {

    private final RequestDAO dao = new RequestDAO();

    public void createRequest(Request r) throws Exception {
        Session.requireRole(Role.ADMIN); // or COMMERCIAL
        r.setState(RequestState.DRAFT);
        dao.insert(r);
    }

    public void validateRequest(Request r) throws Exception {
        Session.requireRole(Role.ADMIN); // or COORD
        if (r.getState() != RequestState.DRAFT) {
            throw new Exception("Seule une demande en brouillon peut être validée.");
        }
        dao.updateState(r.getId(), RequestState.VALIDATED);
    }

    public void cancelRequest(Request r) throws Exception {
        Session.requireRole(Role.ADMIN); // or COMMERCIAL
        if (r.getState() == RequestState.CLOSED) {
            throw new Exception("Une demande clôturée ne peut pas être annulée.");
        }
        dao.updateState(r.getId(), RequestState.CANCELLED);
    }

    public List<Request> listRequests() throws SQLException {
        Session.touch();
        return dao.findAll();
    }
}