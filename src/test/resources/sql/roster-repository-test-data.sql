-- ============================================================
-- ROSTER REPOSITORY TEST DATA
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
           'Roster Repository Test Branch',
           'RRT9001',
           'Test Address',
           '0119000001',
           'roster9001@test.com',
           CURRENT_DATE,
           'Roster repository test branch',
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
           'Roster Repository Tester',
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
           'rostertest9001',
           'test-password',
           FALSE,
           NULL,
           NULL,
           FALSE,
           'Roster repository test user',
           9001,
           1,
           1
       );


-- ============================================================
-- 4. ROSTER - MAIN OVERLAPPING ROSTER
-- ============================================================
-- Existing roster:
-- 2026-08-10 -> 2026-08-16
--
-- Used to test overlapping date ranges.


INSERT INTO roster (
    id,
    branch_id,
    dostartofweek,
    doendofweek,
    deleted,
    user_id
)
VALUES (
           9001,
           9001,
           '2026-08-10',
           '2026-08-16',
           FALSE,
           9001
       );


-- ============================================================
-- 5. ROSTER - NON-OVERLAPPING ROSTER
-- ============================================================
-- Completely before the main roster.

INSERT INTO roster (
    id,
    branch_id,
    dostartofweek,
    doendofweek,
    deleted,
    user_id
)
VALUES (
           9002,
           9001,
           '2026-07-13',
           '2026-07-19',
           FALSE,
           9001
       );
