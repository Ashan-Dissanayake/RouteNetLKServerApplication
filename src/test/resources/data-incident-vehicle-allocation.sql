-- ====== Branch Status / Type / Regional Office ======
INSERT INTO branchstatus(id, name) VALUES (1, 'ACTIVE');
INSERT INTO branchtype(id, name) VALUES (1, 'DEPOT');
INSERT INTO regionaloffice(id, name) VALUES (1, 'Colombo Office');

-- ====== Branch ======
INSERT INTO branch(id, name, branchstatus_id, branchtype_id, regionaloffice_id, docreated, deleted, remarks)
VALUES (1, 'Colombo Depot', 1, 1, 1, CURRENT_DATE, 0, 'Main Depot');

-- ====== Vehicle ======
INSERT INTO vehiclestatus(id, name) VALUES (1, 'ACTIVE');
INSERT INTO bustype(id, name) VALUES (1, 'AC_BUS');

INSERT INTO vehicle(id, branch_id, number, bustype_id, vehiclestatus_id, remarks, deleted)
VALUES (201, 1, 'V001', 1, 1, 'Active Bus', 0);

-- ====== Permit & Trip ======
INSERT INTO permite(id, number, vehicle_id, branch_id) VALUES (301, 'PERMIT001', 201, 1);
INSERT INTO tripstatus(id, name) VALUES (1, 'Scheduled');
INSERT INTO triptype(id, name) VALUES (1, 'Daily');
INSERT INTO originterminal(id, name) VALUES (1, 'Colombo Depot');

INSERT INTO trip(id, branch_id, triptype_id, permite_id, doservice, tripstatus_id, originterminal_id)
VALUES (401, 1, 1, 301, CURRENT_DATE, 1, 1);

-- ====== Incident Types ======
INSERT INTO incidenttype(id, name) VALUES (1, 'Breakdown'), (2, 'Accident');

-- ====== Incident Status ======
INSERT INTO incidentstatus(id, name) VALUES (1, 'Reported'), (2, 'In progress');

-- ====== Incident ======
INSERT INTO incident(id, trip_id, incident_type_id, status, description, location, reported_time)
VALUES (501, 401, 1, 'REPORTED', 'Bus breakdown', 'Kandy Road', NOW());

-- ====== Incident Vehicle Allocation ======
INSERT INTO incidentvehicleallocation(id, incident_id, vehicle_id, providing_branch_id, allocation_type, status, assigned_time)
VALUES (601, 501, 201, 1, 'PRIMARY', 'ASSIGNED', NOW());
