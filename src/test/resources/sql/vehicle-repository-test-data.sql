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
    9001,
    'Vehicle Repository Test Branch 1',
    'VRT9001',
    'Test Address 1',
    '0119000001',
    'vehiclebranch9001@test.com',
    CURRENT_DATE,
    2,
    'Vehicle repository test branch',
    1,
    FALSE,
    1
),
(
    9002,
    'Vehicle Repository Test Branch 2',
    'NB-1212',
    'Test Address 2',
    '0119000002',
    'vehiclebranch9002@test.com',
    CURRENT_DATE,
    2,
    'Vehicle repository secondary branch',
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
    9001,
    'EMP9001',
    'Vehicle Repository Tester',
    'Tester',
    '900000001V',
    1,
    '0779000001',
    'vehicleemployee9001@test.com',
    'Test Address',
    '0719000001',
    9001,
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
    9001,
    9001,
    'vehicletest9001',
    'test-password',
    1,
    1,
    FALSE,
    NULL,
    NULL,
    FALSE,
    'Vehicle repository test user'
);


-- ============================================================
-- 4. VEHICLE - BRANCH 9001
-- ============================================================
-- Expected from findByBranch_Id(9001).

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
    'VRT9001',
    1,
    1,
    50000,
    2,
    1,
    1,
    'Vehicle repository test vehicle 1',
    FALSE,
    9001
);


-- ============================================================
-- 5. VEHICLE - BRANCH 9001
-- ============================================================
-- Expected from findByBranch_Id(9001).

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
    9002,
    9001,
    'VRT9002',
    1,
    1,
    60000,
    2,
    2,
    1,
    'Vehicle repository test vehicle 2',
    FALSE,
    9001
);


-- ============================================================
-- 6. VEHICLE - DIFFERENT BRANCH
-- ============================================================
-- This vehicle must NOT be returned by:
--
-- findByBranch_Id(9001)
--
-- because it belongs to branch 9002.

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
    9004,
    9002,
    'NB-1221',
    1,
    1,
    70000,
    2,
    3,
    1,
    '',
    FALSE,
    9001
);
