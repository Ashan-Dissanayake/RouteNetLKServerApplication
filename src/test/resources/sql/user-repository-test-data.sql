-- ============================================================
-- USER REPOSITORY TEST DATA
-- ============================================================


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
VALUES (
           9001,
           'User Repository Test Branch',
           'URT9001',
           'Test Address 9001',
           '0119000001',
           'userbranch9001@test.com',
           CURRENT_DATE,
           2,
           'User repository test branch',
           1,
           FALSE,
           1
       );

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
VALUES (
           9002,
           'User Repository Empty Branch',
           'URT9002',
           'Test Address 9002',
           '0119000002',
           'userbranch9002@test.com',
           CURRENT_DATE,
           2,
           'Branch without users',
           1,
           FALSE,
           1
       );


-- ============================================================
-- 2. EMPLOYEES
-- ============================================================

-- Employee belonging to branch 9001
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
           'User Repository Tester One',
           'Tester One',
           '900000001V',
           1,
           '0779000001',
           'employee9001@test.com',
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


-- Employee belonging to branch 9001
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
           9002,
           'EMP9002',
           'User Repository Tester Two',
           'Tester Two',
           '900000002V',
           1,
           '0779000002',
           'employee9002@test.com',
           'Test Address',
           '0719000002',
           9001,
           1,
           4,
           1,
           CURRENT_DATE,
           1,
           FALSE
       );


-- Employee without a user
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
           9003,
           'EMP9003',
           'User Repository No User',
           'No User',
           '900000003V',
           1,
           '0779000003',
           'employee9003@test.com',
           'Test Address',
           '0719000003',
           9001,
           1,
           4,
           1,
           CURRENT_DATE,
           1,
           FALSE
       );


-- ============================================================
-- 3. USERS
-- ============================================================

-- Normal unlocked user
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
           'usertest9001',
           'test-password',
           1,
           1,
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Normal test user'
       );


-- Locked user
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
           9002,
           9002,
           'usertest9002',
           'test-password',
           1,
           1,
           TRUE,
           NULL,
           NULL,
           FALSE,
           'Locked test user'
       );


-- User with username used for duplicate username test
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
           9004,
           9002,
           'duplicate-user',
           'test-password',
           1,
           1,
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Duplicate username test user'
       );
