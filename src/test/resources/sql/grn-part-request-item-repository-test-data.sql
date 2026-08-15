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
    regionaloffice_id
)
VALUES (
           9001,
           'Repository Test Branch',
           'TST9001',
           'Test Address',
           '0119000001',
           'branch9001@test.com',
           CURRENT_DATE,
           'Repository test branch',
           2,
           1,
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
           'Repository Test User',
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
           'testuser9001',
           'test-password',
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Repository test user',
           9001,
           1,
           1
       );


-- ============================================================
-- 4. PART MASTER
-- ============================================================
-- Use IDs that already exist in data.sql:
-- partcategory_id = 1
-- unitofmeasure_id = 1
--
-- If your data.sql uses different IDs, replace these two values.

INSERT INTO partmaster (
    id,
    sku,
    name,
    partcategory_id,
    unitofmeasure_id
)
VALUES (
           9001,
           'TEST-PART-9001',
           'Repository Test Part',
           1,
           1
       );


-- ============================================================
-- 5. PART
-- ============================================================

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
           9001,
           9001,
           9001,
           100.000,
           200.000,
           50.000,
           CURRENT_DATE,
           'Repository test part',
           1,
           FALSE,
           9001
       );


-- ============================================================
-- 6. PART REQUEST
-- ============================================================

INSERT INTO partrequest (
    id,
    number,
    dorequested,
    remarks,
    branch_id,
    partrequeststatus_id,
    user_id
)
VALUES (
           9001,
           'PR9001',
           CURRENT_DATE,
           'Repository test part request',
           9001,
           1,
           9001
       );


-- ============================================================
-- 7. PART REQUEST ITEM
-- ============================================================

INSERT INTO partrequestitem (
    id,
    quantity,
    partrequest_id,
    part_id
)
VALUES (
           9001,
           100.00,
           9001,
           9001
       );


-- ============================================================
-- 8. GRN - RECEIVED
-- ============================================================

INSERT INTO grn (
    id,
    number,
    doreceived,
    remarks,
    branch_id,
    partrequest_id,
    grnstatus_id,
    user_id
)
VALUES (
           9001,
           'GRN9001',
           CURRENT_DATE,
           'Received GRN - Repository Test',
           9001,
           9001,
           3,
           9001
       );


-- ============================================================
-- 9. GRN - DRAFT
-- ============================================================

INSERT INTO grn (
    id,
    number,
    doreceived,
    remarks,
    branch_id,
    partrequest_id,
    grnstatus_id,
    user_id
)
VALUES (
           9002,
           'GRN9002',
           CURRENT_DATE,
           'Draft GRN - Repository Test',
           9001,
           9001,
           1,
           9001
       );


-- ============================================================
-- 10. GRN PART REQUEST ITEM - RECEIVED
-- ============================================================

INSERT INTO grnpartrequestitem (
    id,
    quantity,
    grn_id,
    partrequestitem_id
)
VALUES (
           9001,
           60.00,
           9001,
           9001
       );


-- ============================================================
-- 11. GRN PART REQUEST ITEM - DRAFT
-- ============================================================

INSERT INTO grnpartrequestitem (
    id,
    quantity,
    grn_id,
    partrequestitem_id
)
VALUES (
           9002,
           40.00,
           9002,
           9001
       );


-- ============================================================
-- 12. SECOND RECEIVED GRN
-- ============================================================

INSERT INTO grn (
    id,
    number,
    doreceived,
    remarks,
    branch_id,
    partrequest_id,
    grnstatus_id,
    user_id
)
VALUES (
           9003,
           'GRN9003',
           CURRENT_DATE,
           'Second Received GRN - Repository Test',
           9001,
           9001,
           3,
           9001
       );


-- ============================================================
-- 13. SECOND RECEIVED GRN PART REQUEST ITEM
-- ============================================================

INSERT INTO grnpartrequestitem (
    id,
    quantity,
    grn_id,
    partrequestitem_id
)
VALUES (
           9003,
           25.00,
           9003,
           9001
       );
