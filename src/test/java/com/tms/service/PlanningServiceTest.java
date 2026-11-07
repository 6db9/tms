package com.tms.service;

import com.tms.dao.MissionDAO;
import com.tms.dao.RequestDAO;
import com.tms.model.*;
import com.tms.util.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PlanningServiceTest {

    @Test
    public void testConflictDetected() throws Exception {
        // 1. FAKE LOGIN
        User fakeUser = new User();
        fakeUser.getRoles().add(Role.ADMIN);
        Session.start(fakeUser);

        // 2. Setup mocks
        MissionDAO mockMissionDAO = Mockito.mock(MissionDAO.class);
        RequestDAO mockRequestDAO = Mockito.mock(RequestDAO.class);
        PlanningService service = new PlanningService(mockMissionDAO, mockRequestDAO);

        // Fake valid request
        Request req = new Request();
        req.setState(RequestState.VALIDATED);

        // Fake vehicle and driver
        Vehicle v = new Vehicle(); v.setId(1);
        Driver d = new Driver(); d.setId(1);
        d.setLicenseValidUntil(java.time.LocalDate.now().plusYears(1)); // Valid license

        LocalDateTime start = LocalDateTime.of(2026, 11, 1, 10, 0); // 10h
        LocalDateTime end = LocalDateTime.of(2026, 11, 1, 12, 0);   // 12h

        // Tell Mockito to simulate a conflict
        when(mockMissionDAO.hasConflict(anyInt(), anyInt(), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(true);

        // The service should throw an Exception due to the conflict
        Exception exception = assertThrows(Exception.class, () -> {
            service.planMission(req, v, d, start, end);
        });

        assertTrue(exception.getMessage().contains("Conflit"));
    }
}