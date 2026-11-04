package com.tms.service;

import com.tms.dao.MissionDAO;
import com.tms.dao.RequestDAO;
import com.tms.model.*;
import com.tms.util.Session;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class PlanningService {

    private final MissionDAO missionDAO = new MissionDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    public void planMission(Request request, Vehicle vehicle, Driver driver, java.time.LocalDateTime start, java.time.LocalDateTime end) throws Exception {
        Session.requireRole(Role.ADMIN);

        if (request.getState() != RequestState.VALIDATED) {
            throw new Exception("Seule une demande validée peut être planifiée.");
        }
        if (start == null || end == null) {
            throw new Exception("Les dates de début et de fin sont obligatoires.");
        }
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new Exception("La date de fin doit être après la date de début.");
        }

        // Qualification rule: Driver license must be valid at the mission date
        if (driver.getLicenseValidUntil() != null && driver.getLicenseValidUntil().isBefore(start.toLocalDate())) {
            throw new Exception("Le permis du chauffeur n'est pas valide à cette date.");
        }

        // Conflict rule
        Timestamp sqlStart = Timestamp.valueOf(start);
        Timestamp sqlEnd = Timestamp.valueOf(end);
        if (missionDAO.hasConflict(vehicle.getId(), driver.getId(), sqlStart, sqlEnd)) {
            throw new Exception("Conflit : Le véhicule ou le chauffeur est déjà occupé sur ce créneau.");
        }

        // If all passes, create the mission
        Mission m = new Mission();
        m.setRequest(request);
        m.setVehicle(vehicle);
        m.setDriver(driver);
        m.setState(MissionState.PLANNED);
        m.setPlannedStart(start);
        m.setPlannedEnd(end);

        missionDAO.insert(m);
    }

    public List<Request> getValidatedRequests() throws SQLException {

        return requestDAO.findAll().stream()
                .filter(r -> r.getState() == RequestState.VALIDATED)
                .toList();
    }

    public List<Mission> listMissions() throws SQLException {
        Session.touch();
        return missionDAO.findAll();
    }

    public void markInProgress(Mission m) throws Exception {
        Session.requireRole(Role.ADMIN);
        if (m.getState() != MissionState.PLANNED) throw new Exception("La mission doit être Planifiée.");
        missionDAO.updateProgress(m.getId(), MissionState.IN_PROGRESS, m.getActualArrival(), m.getActualStart(), m.getActualEnd());
    }

    public void markArrived(Mission m) throws Exception {
        Session.requireRole(Role.ADMIN);
        if (m.getState() != MissionState.IN_PROGRESS) throw new Exception("La mission doit être En cours.");
        missionDAO.updateProgress(m.getId(), MissionState.ARRIVED, java.time.LocalDateTime.now(), m.getActualStart(), m.getActualEnd());
    }

    public void startUnloading(Mission m) throws Exception {
        Session.requireRole(Role.ADMIN);
        if (m.getState() != MissionState.ARRIVED && m.getState() != MissionState.WAITING)
            throw new Exception("La mission doit être Arrivée ou En attente.");
        missionDAO.updateProgress(m.getId(), MissionState.UNLOADING, m.getActualArrival(), java.time.LocalDateTime.now(), m.getActualEnd());
    }

    public void markDone(Mission m) throws Exception {
        Session.requireRole(Role.ADMIN);
        if (m.getState() != MissionState.UNLOADING) throw new Exception("La mission doit être En déchargement.");
        missionDAO.updateProgress(m.getId(), MissionState.DONE, m.getActualArrival(), m.getActualStart(), java.time.LocalDateTime.now());
    }
}