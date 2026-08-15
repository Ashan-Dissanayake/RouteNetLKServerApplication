-- ============================================================
-- 1. BRANCH
-- ============================================================

INSERT INTO branch
(
    id,
    name,
    code,
    address,
    telephone,
    email,
    docreated,
    branchtype_id,
    branchstatus_id,
    regionaloffice_id
)
VALUES
    (
        9001,
        'Test Colombo Depot',
        'TST9001',
        'Test Address',
        '0119000001',
        'branch9001@test.com',
        CURRENT_DATE,
        2,
        1,
        1
    );


-- ============================================================
-- 2. EMPLOYEE
-- ============================================================

INSERT INTO employee
(
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
    employeestatus_id
)
VALUES
    (
        9001,
        'EMP9001',
        'Test Auditor',
        'Auditor',
        '900000001V',
        '0779000001',
        'auditor@test.com',
        'Test Address',
        '0719000001',
        CURRENT_DATE,
        1,
        9001,
        1,
        4,
        1,
        1
    );


-- ============================================================
-- 3. USER
-- ============================================================

INSERT INTO user
(
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
VALUES
    (
        9001,
        'fare_test_user',
        'test-password',
        false,
        NULL,
        NULL,
        false,
        'FareCollection repository test user',
        9001,
        1,
        1
    );


-- ============================================================
-- 4. VEHICLE
-- ============================================================

INSERT INTO vehicle
(
    id,
    number,
    mileage,
    remarks,
    fueltype_id,
    conditionrate_id,
    vehiclestatus_id,
    branch_id,
    bustype_id,
    model_id,
    user_id,
    deleted
)
VALUES
    (
        9001,
        'NG-1253',
        50000,
        'Test Vehicle',
        2,
        1,
        1,
        9001,
        3,
        1,
        9001,
        false
    );


-- ============================================================
-- 5. ROUTE
-- ============================================================

INSERT INTO route
(
    id,
    number,
    origin,
    destination,
    distancekm,
    mingapminutes,
    routetype_id,
    requiredroutefamiliaritylevel_id
)
VALUES
    (
        9001,
        'RT9001',
        'Colombo',
        'Kandy',
        115,
        10,
        2,
        1
    );


-- ============================================================
-- 6. PERMIT
-- ============================================================

INSERT INTO permite
(
    id,
    number,
    doissued,
    doexpired,
    notripsperday,
    vehicle_id,
    branch_id,
    permitestatus_id,
    servicetype_id,
    route_id,
    user_id
)
VALUES
    (
        9001,
        'PERM9001',
        CURRENT_DATE,
        DATE_ADD(CURRENT_DATE, INTERVAL 1 YEAR),
        10,
        9001,
        9001,
        1,
        1,
        9001,
        9001
    );


-- ============================================================
-- 7. TRIP
-- ============================================================

INSERT INTO trip
(
    id,
    todepature,
    toarrival,
    remarks,
    breakminutes,
    triptype_id,
    branch_id,
    permite_id,
    tripstatus_id,
    originterminal_id,
    user_id,
    opcalender_id,
    shift_id
)
VALUES
    (
        9001,
        '08:00:00',
        '11:00:00',
        'Fare repository test trip',
        15,
        1,
        9001,
        9001,
        2,
        1,
        9001,
        1,
        2
    );


-- ============================================================
-- 8. TRIP EXECUTION
-- ============================================================

INSERT INTO tripexecution
(
    id,
    doservice,
    toactualdeparture,
    toactualarrival,
    startodometer,
    endodometer,
    passengercount,
    tripno,
    remarks,
    branch_id,
    trip_id,
    vehicle_id,
    tripexecutionstatus_id,
    user_id
)
VALUES
    (
        9001,
        CURRENT_DATE,
        '08:05:00',
        '11:05:00',
        50000,
        50115,
        80,
        1,
        'Test trip execution',
        9001,
        9001,
        9001,
        9,
        9001
    );


-- ============================================================
-- 9. TICKET MACHINE
-- ============================================================

INSERT INTO ticketmachine
(
    id,
    name,
    branch_id
)
VALUES
    (
        9001,
        'TEST-MACHINE-9001',
        9001
    );


-- ============================================================
-- 10. FARE COLLECTION
-- ============================================================

INSERT INTO farecollection
(
    id,
    totaltickets,
    cashcollected,
    digitalpayments,
    isreconciled,
    tocollected,
    branch_id,
    tripexecution_id,
    ticketmachine_id,
    user_id
)
VALUES
    (
        9001,
        100,
        7500.00,
        2500.00,
        true,
        '11:15:00',
        9001,
        9001,
        9001,
        9001
    );
