-- ============================================================
-- TRIP REPOSITORY TEST DATA
-- ============================================================


-- ============================================================
-- 1. BRANCH
-- ============================================================

INSERT INTO branch (
    id,
    name,
    code,
    address,
    telephone,
    email,
    docreated,
    remarks,
    branchtype_id,
    branchstatus_id,
    regionaloffice_id,
    deleted
)
VALUES (
           9001,
           'Trip Repository Test Branch',
           'TRT9001',
           'Test Address',
           '0119000001',
           'trip9001@test.com',
           CURRENT_DATE,
           'Trip repository test branch',
           2,
           1,
           1,
           FALSE
       );


-- ============================================================
-- 2. EMPLOYEE
-- ============================================================

INSERT INTO employee (
    id,
    number,
    fullname,
    callingname,
    nic,
    mobile,
    email,
    address,
    emergencycontact,
    doj,
    gender_id,
    branch_id,
    department_id,
    designation_id,
    employeetype_id,
    employeestatus_id,
    deleted
)
VALUES (
           9001,
           'EMP9001',
           'Trip Repository Tester',
           'Tester',
           '900000001V',
           '0779000001',
           'employee9001@test.com',
           'Test Address',
           '0719000001',
           CURRENT_DATE,
           1,
           9001,
           1,
           4,
           1,
           1,
           FALSE
       );


-- ============================================================
-- 3. USER
-- ============================================================

INSERT INTO `user` (
    id,
    username,
    password,
    accountlocked,
    recoverycode,
    recoverycodeexpiration,
    recoverycodeused,
    remarks,
    employee_id,
    usertype_id,
    userstatus_id
)
VALUES (
           9001,
           'triptest9001',
           'test-password',
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Trip repository test user',
           9001,
           1,
           1
       );


-- ============================================================
-- 4. ROUTE - NORMAL ROUTE
-- ============================================================
-- routetype_id = 1
-- Inter provincial according to master data
--
-- Used for:
-- findByPermite_Route_Id()
-- findByPermite_Id()
-- existsActiveTrip()
-- ============================================================

INSERT INTO route (
    id,
    number,
    origin,
    destination,
    distancekm,
    routetype_id,
    mingapminutes,
    waypoints,
    requiredroutefamiliaritylevel_id
)
VALUES (
           9001,
           'TRT9001',
           'Pettah',
           'Gampaha',
           35.0,
           1,
           10,
           NULL,
           1
       );


-- ============================================================
-- 5. ROUTE - ROUTETYPE 2
-- ============================================================
-- NOTE:
-- existsInterprovincialTripInShift() currently checks:
--     r.routetype.id = 2
--
-- Therefore this route is intentionally type 2 so that
-- the repository query returns TRUE.
-- ============================================================

INSERT INTO route (
    id,
    number,
    origin,
    destination,
    distancekm,
    routetype_id,
    mingapminutes,
    waypoints,
    requiredroutefamiliaritylevel_id
)
VALUES (
           9002,
           'TRT9002',
           'Pettah',
           'Kandy',
           115.0,
           2,
           10,
           NULL,
           1
       );


-- ============================================================
-- 6. VEHICLE
-- ============================================================

INSERT INTO vehicle (
    id,
    branch_id,
    number,
    model_id,
    bustype_id,
    mileage,
    fueltype_id,
    conditionrate_id,
    vehiclestatus_id,
    remarks,
    deleted,
    user_id
)
VALUES (
           9001,
           9001,
           'TRT9001',
           1,
           1,
           50000,
           2,
           1,
           1,
           'Trip repository test vehicle',
           FALSE,
           9001
       );


-- ============================================================
-- 7. PERMIT - ROUTE 9001
-- ============================================================

INSERT INTO permite (
    id,
    branch_id,
    route_id,
    number,
    vehicle_id,
    doissued,
    doexpired,
    notripsperday,
    permitestatus_id,
    servicetype_id,
    deleted,
    user_id
)
VALUES (
           9001,
           9001,
           9001,
           'TRTPERMIT9001',
           9001,
           '2026-01-01',
           '2027-01-01',
           10,
           1,
           1,
           FALSE,
           9001
       );


-- ============================================================
-- 8. PERMIT - ROUTE 9002
-- ============================================================

INSERT INTO permite (
    id,
    branch_id,
    route_id,
    number,
    vehicle_id,
    doissued,
    doexpired,
    notripsperday,
    permitestatus_id,
    servicetype_id,
    deleted,
    user_id
)
VALUES (
           9002,
           9001,
           9002,
           'TRTPERMIT9002',
           9001,
           '2026-01-01',
           '2027-01-01',
           10,
           1,
           1,
           FALSE,
           9001
       );


-- ============================================================
-- 9. TRIP 9001
-- ============================================================
-- Active trip
-- Permit 9001 -> Route 9001
-- Origin terminal 1
-- Departure 08:00
-- Arrival 10:00
-- Shift 1
--
-- Used by most derived-query tests.
-- ============================================================

INSERT INTO trip (
    id,
    branch_id,
    triptype_id,
    permite_id,
    todepature,
    toarrival,
    breakminutes,
    remarks,
    tripstatus_id,
    originterminal_id,
    opcalender_id,
    user_id,
    shift_id
)
VALUES (
           9001,
           9001,
           1,
           9001,
           '08:00:00',
           '10:00:00',
           10,
           'Active trip repository test trip',
           2,
           1,
           1,
           9001,
           1
       );


-- ============================================================
-- 10. TRIP 9002
-- ============================================================
-- Active trip
-- Same permit
-- Different origin terminal
-- Different departure/arrival
--
-- Gives findByPermite_Id() multiple results.
-- ============================================================

INSERT INTO trip (
    id,
    branch_id,
    triptype_id,
    permite_id,
    todepature,
    toarrival,
    breakminutes,
    remarks,
    tripstatus_id,
    originterminal_id,
    opcalender_id,
    user_id,
    shift_id
)
VALUES (
           9002,
           9001,
           1,
           9001,
           '10:00:00',
           '12:00:00',
           10,
           'Second active trip repository test trip',
           2,
           5,
           1,
           9001,
           2
       );


-- ============================================================
-- 11. TRIP 9003
-- ============================================================
-- Active trip
-- Permit 9002 -> Route 9002
-- routetype = 2
-- Departure 09:00
-- Shift 2 = 08:00 - 16:00
--
-- Used by:
-- existsInterprovincialTripInShift()
-- countDistinctPermitsForShift()
-- ============================================================

INSERT INTO trip (
    id,
    branch_id,
    triptype_id,
    permite_id,
    todepature,
    toarrival,
    breakminutes,
    remarks,
    tripstatus_id,
    originterminal_id,
    opcalender_id,
    user_id,
    shift_id
)
VALUES (
           9003,
           9001,
           1,
           9002,
           '09:00:00',
           '11:00:00',
           10,
           'Route type 2 trip repository test trip',
           2,
           1,
           1,
           9001,
           2
       );


-- ============================================================
-- 12. TRIP 9004
-- ============================================================
-- NON-ACTIVE trip
--
-- Used to verify status filtering.
-- ============================================================

INSERT INTO trip (
    id,
    branch_id,
    triptype_id,
    permite_id,
    todepature,
    toarrival,
    breakminutes,
    remarks,
    tripstatus_id,
    originterminal_id,
    opcalender_id,
    user_id,
    shift_id
)
VALUES (
           9004,
           9001,
           1,
           9001,
           '13:00:00',
           '15:00:00',
           10,
           'Draft trip repository test trip',
           1,
           1,
           1,
           9001,
           3
       );
