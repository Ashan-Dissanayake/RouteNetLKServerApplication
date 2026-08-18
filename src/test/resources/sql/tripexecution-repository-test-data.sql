-- ============================================================
-- TRIP EXECUTION REPOSITORY TEST DATA
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
           'Trip Execution Repository Test Branch',
           'TER9001',
           'Test Address',
           '0119000001',
           'tripexecution9001@test.com',
           CURRENT_DATE,
           'Trip execution repository test branch',
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
           'Trip Execution Repository Tester',
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
           'tripexecutiontest9001',
           'test-password',
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Trip execution repository test user',
           9001,
           1,
           1
       );


-- ============================================================
-- 4. ROUTE
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
           'TER9001',
           'Pettah',
           'Gampaha',
           35.0,
           1,
           10,
           NULL,
           1
       );


-- ============================================================
-- 5. VEHICLE
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
           'TER9001',
           1,
           1,
           50000,
           2,
           1,
           1,
           'Trip execution repository test vehicle',
           FALSE,
           9001
       );


-- ============================================================
-- 6. PERMIT
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
           'TERPERMIT9001',
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
-- 7. TRIP
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
           'Trip execution repository test trip',
           2,
           1,
           1,
           9001,
           1
       );


-- ============================================================
-- 8. TRIP EXECUTION - COMPLETED
-- ============================================================
-- Used by:
-- findAllByTrip_Id()
-- findByDoserviceAndBranch_Id()
-- findByDoserviceAndBranch_IdAndDriverIsNull()
-- findByTripexecutionstatus_Name()
--
-- status 9 = Completed
-- ============================================================

INSERT INTO tripexecution (
    id,
    branch_id,
    trip_id,
    vehicle_id,
    driver_id,
    conductor_id,
    doservice,
    toactualdeparture,
    toactualarrival,
    startodometer,
    endodometer,
    passengercount,
    tripno,
    remarks,
    tripexecutionstatus_id,
    user_id
)
VALUES (
           9001,
           9001,
           9001,
           9001,
           NULL,
           NULL,
           '2026-08-10',
           '08:00:00',
           '10:00:00',
           10000,
           10050,
           50,
           1,
           'Completed trip execution',
           9,
           9001
       );


-- ============================================================
-- 9. TRIP EXECUTION - BREAKDOWN
-- ============================================================
-- Same trip and same date.
--
-- status 6 = Breakdown
-- ============================================================

INSERT INTO tripexecution (
    id,
    branch_id,
    trip_id,
    vehicle_id,
    driver_id,
    conductor_id,
    doservice,
    toactualdeparture,
    toactualarrival,
    startodometer,
    endodometer,
    passengercount,
    tripno,
    remarks,
    tripexecutionstatus_id,
    user_id
)
VALUES (
           9002,
           9001,
           9001,
           9001,
           NULL,
           NULL,
           '2026-08-10',
           '11:00:00',
           '13:00:00',
           10100,
           10150,
           40,
           2,
           'Breakdown trip execution',
           6,
           9001
       );


-- ============================================================
-- 10. TRIP EXECUTION - DIFFERENT DATE
-- ============================================================
-- Used to prove date filtering.
--
-- status 9 = Completed
-- ============================================================

INSERT INTO tripexecution (
    id,
    branch_id,
    trip_id,
    vehicle_id,
    driver_id,
    conductor_id,
    doservice,
    toactualdeparture,
    toactualarrival,
    startodometer,
    endodometer,
    passengercount,
    tripno,
    remarks,
    tripexecutionstatus_id,
    user_id
)
VALUES (
           9003,
           9001,
           9001,
           9001,
           NULL,
           NULL,
           '2026-08-11',
           '08:00:00',
           '10:00:00',
           10200,
           10250,
           45,
           1,
           'Different date execution',
           9,
           9001
       );


-- ============================================================
-- 11. TRIP EXECUTION - DISPATCHED TODAY
-- ============================================================
-- Used by:
-- countActiveTripsByBranch()
--
-- status 3 = Dispatched
-- doservice = CURRENT_DATE
-- ============================================================

INSERT INTO tripexecution (
    id,
    branch_id,
    trip_id,
    vehicle_id,
    driver_id,
    conductor_id,
    doservice,
    toactualdeparture,
    toactualarrival,
    startodometer,
    endodometer,
    passengercount,
    tripno,
    remarks,
    tripexecutionstatus_id,
    user_id
)
VALUES (
           9004,
           9001,
           9001,
           9001,
           NULL,
           NULL,
           CURRENT_DATE,
           '08:00:00',
           '10:00:00',
           10300,
           10350,
           55,
           1,
           'Dispatched trip for dashboard test',
           3,
           9001
       );
