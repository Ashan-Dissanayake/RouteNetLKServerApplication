-- ============================================================
-- 1. BRANCHES
-- ============================================================

INSERT INTO branch (
    id,
    name,
    code,
    address,
    telephone,
    email,
    docreated,
    branchtype_id,
    remarks,
    branchstatus_id,
    deleted,
    regionaloffice_id
)
VALUES
(
    9101,
    'Vehicle Service Test Branch 1',
    'VST9101',
    'Vehicle Service Test Address 1',
    '0119100001',
    'vehicleservicebranch9101@test.com',
    CURRENT_DATE,
    2,
    'Vehicle service repository primary test branch',
    1,
    FALSE,
    1
),
(
    9102,
    'Vehicle Service Test Branch 2',
    'VST9102',
    'Vehicle Service Test Address 2',
    '0119100002',
    'vehicleservicebranch9102@test.com',
    CURRENT_DATE,
    2,
    'Vehicle service repository secondary test branch',
    1,
    FALSE,
    1
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
    gender_id,
    mobile,
    email,
    address,
    emergencycontact,
    branch_id,
    department_id,
    designation_id,
    employeetype_id,
    doj,
    employeestatus_id,
    deleted
)
VALUES (
    9101,
    'EMP9101',
    'Vehicle Service Repository Tester',
    'Tester',
    '910000001V',
    1,
    '0779100001',
    'vehicleserviceemployee9101@test.com',
    'Test Address',
    '0719100001',
    9101,
    1,
    4,
    1,
    CURRENT_DATE,
    1,
    FALSE
);


-- ============================================================
-- 3. USER
-- ============================================================

INSERT INTO `user` (
    id,
    employee_id,
    username,
    password,
    usertype_id,
    userstatus_id,
    accountlocked,
    recoverycode,
    recoverycodeexpiration,
    recoverycodeused,
    remarks
)
VALUES (
    9101,
    9101,
    'vehicleservicetest9101',
    'test-password',
    1,
    1,
    FALSE,
    NULL,
    NULL,
    FALSE,
    'Vehicle service repository test user'
);


-- ============================================================
-- 4. VEHICLE - PRIMARY TEST BRANCH
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
VALUES
(
    9101,
    9101,
    'VST9101',
    1,
    1,
    50000,
    2,
    1,
    1,
    'Vehicle service test vehicle 1',
    FALSE,
    9101
),
(
    9102,
    9101,
    'VST9102',
    1,
    1,
    60000,
    2,
    2,
    1,
    'Vehicle service test vehicle 2',
    FALSE,
    9101
);


-- ============================================================
-- 5. VEHICLE - DIFFERENT BRANCH
-- ============================================================
-- Not used by the maintenance metric records.
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
    9104,
    9102,
    'VST9104',
    1,
    1,
    70000,
    2,
    3,
    1,
    'Vehicle from secondary test branch',
    FALSE,
    9101
);


-- ============================================================
-- 6. VEHICLE SERVICE - WEEK 32
-- ============================================================
-- 2026-08-10 -> 2026-08-16
--
-- Expected:
-- completedServices = 2
-- pendingBacklog    = 1
-- ============================================================

INSERT INTO vehicleservice (
    id,
    branch_id,
    number,
    vehicle_id,
    vehicleservicetype_id,
    incident_id,
    vehicleservicestatus_id,
    vehicleservicepriority_id,
    docreated,
    user_id
)
VALUES
    (
        9501,
        9101,
        'VS-9501',
        9101,
        1,
        NULL,
        4,
        1,
        '2026-08-10',
        9101
    ),
    (
        9502,
        9101,
        'VS-9502',
        9102,
        2,
        NULL,
        4,
        2,
        '2026-08-12',
        9101
    ),
    (
        9503,
        9101,
        'VS-9503',
        9101,
        1,
        NULL,
        1,
        3,
        '2026-08-14',
        9101
    );

-- ============================================================
-- 7. VEHICLE SERVICE - WEEK 31
-- ============================================================
-- Dates:
-- 2026-08-03
-- 2026-08-05
-- 2026-08-07
--
-- MySQL WEEK() returns these dates as Week 31.
--
-- Expected:
-- completedServices = 1
-- pendingBacklog    = 2
-- ============================================================

INSERT INTO vehicleservice (
    id,
    branch_id,
    number,
    vehicle_id,
    vehicleservicetype_id,
    incident_id,
    vehicleservicestatus_id,
    vehicleservicepriority_id,
    docreated,
    user_id
)
VALUES
    (
        9511,
        9101,
        'VS-9511',
        9101,
        1,
        NULL,
        4,
        1,
        '2026-08-03',
        9101
    ),
    (
        9512,
        9101,
        'VS-9512',
        9102,
        3,
        NULL,
        1,
        2,
        '2026-08-05',
        9101
    ),
    (
        9513,
        9101,
        'VS-9513',
        9101,
        4,
        NULL,
        1,
        3,
        '2026-08-07',
        9101
    );

-- ============================================================
-- 8. VEHICLE SERVICE - WEEK 30
-- ============================================================
-- Date:
-- 2026-07-27
--
-- Expected:
-- completedServices = 0
-- pendingBacklog    = 1
-- ============================================================

INSERT INTO vehicleservice (
    id,
    branch_id,
    number,
    vehicle_id,
    vehicleservicetype_id,
    incident_id,
    vehicleservicepriority_id,
    vehicleservicestatus_id,
    docreated,
    user_id
)
VALUES (
           9521,
           9101,
           'VS-9521',
           9101,
           4,
           NULL,
           4,
           1,
           '2026-07-27',
           9101
       );
-- ============================================================
-- 9. VEHICLE SERVICE - WEEK 29
-- ============================================================
-- Date:
-- 2026-07-20
--
-- Expected:
-- completedServices = 1
-- pendingBacklog    = 0
-- ============================================================

INSERT INTO vehicleservice (
    id,
    branch_id,
    number,
    vehicle_id,
    vehicleservicetype_id,
    incident_id,
    vehicleservicestatus_id,
    vehicleservicepriority_id,
    docreated,
    user_id
)
VALUES (
           9531,
           9101,
           'VS-9531',
           9102,
           1,
           NULL,
           4,
           2,
           '2026-07-20',
           9101
       );

-- ============================================================
-- 10. VEHICLE SERVICE - WEEK 28
-- ============================================================
-- Dates:
-- 2026-07-13
-- 2026-07-17
--
-- Expected:
-- completedServices = 1
-- pendingBacklog    = 1
-- ============================================================

INSERT INTO vehicleservice (
    id,
    branch_id,
    number,
    vehicle_id,
    vehicleservicetype_id,
    incident_id,
    vehicleservicestatus_id,
    vehicleservicepriority_id,
    docreated,
    user_id
)
VALUES
    (
        9541,
        9101,
        'VS-9541',
        9101,
        2,
        NULL,
        4,
        1,
        '2026-07-13',
        9101
    ),
    (
        9542,
        9101,
        'VS-9542',
        9102,
        3,
        NULL,
        1,
        3,
        '2026-07-17',
        9101
    );


-- ============================================================
-- 11. VEHICLE SERVICE - WEEK 27
-- ============================================================
-- Dates:
-- 2026-07-06
-- 2026-07-10
--
-- Expected:
-- completedServices = 2
-- pendingBacklog    = 0
-- ============================================================

INSERT INTO vehicleservice (
    id,
    branch_id,
    number,
    vehicle_id,
    vehicleservicetype_id,
    incident_id,
    vehicleservicepriority_id,
    vehicleservicestatus_id,
    docreated,
    user_id
)
VALUES
    (
        9551,
        9101,
        'VS-9551',
        9101,
        4,
        NULL,
        1,
        4,
        '2026-07-06',
        9101
    ),
    (
        9552,
        9101,
        'VS-9552',
        9102,
        3,
        NULL,
        2,
        4,
        '2026-07-10',
        9101
    );


-- ============================================================
-- 12. VEHICLE SERVICE - WEEK 26
-- ============================================================
-- Date:
-- 2026-06-29
--
-- Expected:
-- completedServices = 0
-- pendingBacklog    = 1
-- ============================================================

INSERT INTO vehicleservice (
    id,
    branch_id,
    number,
    vehicle_id,
    vehicleservicetype_id,
    incident_id,
    vehicleservicepriority_id,
    vehicleservicestatus_id,
    docreated,
    user_id
)
VALUES (
           9561,
           9101,
           'VS-9561',
           9101,
           1,
           NULL,
           3,
           1,
           '2026-06-29',
           9101
       );
