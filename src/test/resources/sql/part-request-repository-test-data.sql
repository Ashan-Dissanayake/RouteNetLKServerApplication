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
           9101,
           'Part Request Test Branch',
           'PRT9101',
           'Test Address',
           '0119100001',
           'partrequest9101@test.com',
           CURRENT_DATE,
           'Part request repository test branch',
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
           9101,
           'EMP9101',
           'Part Request Repository Tester',
           'Tester',
           '910000001V',
           '0779100001',
           'employee9101@test.com',
           'Test Address',
           '0719100001',
           CURRENT_DATE,
           1,
           9101,
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
           9101,
           'partrequesttest9101',
           'test-password',
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Part request repository test user',
           9101,
           1,
           1
       );


-- ============================================================
-- 4. PART MASTER
-- ============================================================
-- partcategory_id and unitofmeasure_id come from data.sql

INSERT INTO partmaster (
    id,
    sku,
    name,
    partcategory_id,
    unitofmeasure_id
)
VALUES (
           9101,
           'SKU-PR-9101',
           'Test Brake Pad',
           1,
           1
       );


-- ============================================================
-- 5. PART
-- ============================================================
-- partstatus_id comes from data.sql

INSERT INTO part (
    id,
    branch_id,
    partmaster_id,
    qoh,
    maxlevel,
    rop,
    dolastordered,
    remarks,
    partstatus_id,
    deleted,
    user_id
)
VALUES (
           9101,
           9101,
           9101,
           100.000,
           200.000,
           20.000,
           NULL,
           'Part request repository test part',
           1,
           FALSE,
           9101
       );


-- ============================================================
-- 6. PART REQUEST
-- ============================================================
-- partrequeststatus_id comes from data.sql

INSERT INTO partrequest (
    id,
    branch_id,
    number,
    dorequested,
    remarks,
    partrequeststatus_id,
    user_id
)
VALUES (
           9101,
           9101,
           'PR9101',
           '2026-08-15',
           'Part request repository test request',
           1,
           9101
       );


-- ============================================================
-- 7. PART REQUEST ITEM
-- ============================================================

INSERT INTO partrequestitem (
    id,
    partrequest_id,
    part_id,
    quantity
)
VALUES (
           9101,
           9101,
           9101,
           5.000
       );
