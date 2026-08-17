-- ============================================================
-- PERMIT REPOSITORY TEST DATA
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
           'Permit Repository Test Branch',
           'PRT9001',
           'Test Address',
           '0119000001',
           'permit9001@test.com',
           CURRENT_DATE,
           'Permit repository test branch',
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
           'Permit Repository Tester',
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
           'permittest9001',
           'test-password',
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Permit repository test user',
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
           'PRT9001',
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
           'PRT9001',
           1,
           1,
           50000,
           2,
           1,
           1,
           'Permit repository test vehicle',
           FALSE,
           9001
       );


-- ============================================================
-- 6. PERMIT - BEFORE DATE
-- ============================================================
-- Used by:
-- findByPermitestatus_NameAndDoexpiredBefore()

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
           9101,
           9001,
           9001,
           '12901',
           9001,
           '2025-01-01',
           '2025-12-31',
           10,
           1,
           1,
           FALSE,
           9001
       );


-- ============================================================
-- 7. PERMIT - BETWEEN RANGE #1
-- ============================================================
-- Used by:
-- findByPermitestatus_NameAndDoexpiredBetween()

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
           9102,
           9001,
           9001,
           '12902',
           9001,
           '2026-01-01',
           '2026-06-15',
           10,
           1,
           1,
           FALSE,
           9001
       );


-- ============================================================
-- 8. PERMIT - BETWEEN RANGE #2
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
           9103,
           9001,
           9001,
           '12903',
           9001,
           '2026-02-01',
           '2026-07-15',
           10,
           1,
           1,
           FALSE,
           9001
       );


-- ============================================================
-- 9. DIFFERENT STATUS
-- ============================================================
-- Used to verify status-name filtering.

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
           9104,
           9001,
           9001,
           '12904',
           9001,
           '2024-01-01',
           '2025-01-01',
           10,
           2,
           1,
           FALSE,
           9001
       );
