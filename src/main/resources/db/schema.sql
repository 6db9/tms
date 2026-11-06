USE tms;

-- 1. CLIENTS (IDs: 1, 2, 3)
INSERT IGNORE INTO clients (id, name, phone, email, address, tax_id) VALUES
                                                                         (1, 'Transport Maroc SARL', '0612345678', 'contact@transmaroc.ma', 'Casablanca', 'TAX-111'),
                                                                         (2, 'BTP Construction', '0687654321', 'info@btp.com', 'Rabat', 'TAX-222'),
                                                                         (3, 'SuperMarché Alami', '0699998888', 'alami@mail.com', 'Fès', 'TAX-333');

-- 2. VEHICULES (IDs: 1, 2)
INSERT IGNORE INTO vehicles (id, plate, type, capacity_kg, capacity_m3) VALUES
                                                                            (1, '12345-A-1', 'Camion 20T', 20000.00, 50.00),
                                                                            (2, '67890-B-2', 'Fourgon 3.5T', 3500.00, 15.00);

-- 3. CHAUFFEURS (IDs: 1, 2)
-- Note: Driver 2 has an expired license (2020) to test the F6 qualification rule.
INSERT IGNORE INTO drivers (id, full_name, phone, license_number, license_valid_until) VALUES
                                                                                           (1, 'Ahmed Benali', '0600000001', 'LIC-123', '2030-01-01'),
                                                                                           (2, 'Karim Ziani', '0600000002', 'LIC-456', '2020-01-01');

-- 4. DEMANDES (IDs: 1, 2, 3, 4)
-- Request 1: Validated, with weight (100 * 20 = 2000kg)
-- Request 2: Draft, no weight (should show "Inconnu")
-- Request 3: Validated, ready for planning
-- Request 4: Closed
INSERT IGNORE INTO requests (id, type, client_id, state, load_description, quantity, weight_per_item) VALUES
                                                                                                          (1, 'TRANSPORT_ONLY', 1, 'VALIDATED', 'Palettes de briques', 100, 20.00),
                                                                                                          (2, 'SALE_WITH_DELIVERY', 2, 'DRAFT', 'Ciment en vrac', 50, NULL),
                                                                                                          (3, 'TRANSPORT_ONLY', 3, 'VALIDATED', 'Produits frais', 200, 5.50),
                                                                                                          (4, 'SALE_WITH_DELIVERY', 1, 'CLOSED', 'Matériel électronique', 10, 50.00);

-- 5. MISSIONS (IDs: 1, 2)
-- Mission 1: Planned (ready to be started in F7)
-- Mission 2: Done (to show F8 times)
INSERT IGNORE INTO missions (id, request_id, vehicle_id, driver_id, state, planned_start, planned_end, actual_arrival, actual_start, actual_end) VALUES
                                                                                                                                                     (1, 1, 1, 1, 'PLANNED', '2026-11-10 08:00:00', '2026-11-10 12:00:00', NULL, NULL, NULL),
                                                                                                                                                     (2, 4, 2, 1, 'DONE', '2026-11-01 09:00:00', '2026-11-01 11:00:00', '2026-11-01 09:15:00', '2026-11-01 09:30:00', '2026-11-01 10:15:00');

-- 6. PAIEMENTS (IDs: 1, 2, 3)
-- Payment 1: Announced (waiting for FINANCIER to confirm)
-- Payment 2: Confirmed (to show balance)
-- Payment 3: Rejected
INSERT IGNORE INTO payments (id, request_id, amount, state) VALUES
                                                                (1, 1, 5000.00, 'ANNOUNCED'),
                                                                (2, 4, 10000.00, 'CONFIRMED'),
                                                                (3, 2, 2000.00, 'REJECTED');

-- 7. DEPENSES (IDs: 1, 2)
INSERT IGNORE INTO expenses (id, mission_id, category, amount, expense_date, reference) VALUES
                                                                                            (1, 1, 'Carburant', 1500.00, '2026-11-10', 'FAC-001'),
                                                                                            (2, 2, 'Péage', 150.00, '2026-11-01', 'REC-002');