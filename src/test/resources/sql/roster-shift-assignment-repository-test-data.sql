-- ============================================================
-- ROSTER SHIFT ASSIGNMENT REPOSITORY TEST DATA
-- ============================================================
-- Existing master/reference data is loaded from data.sql:
-- branchstatus
-- branchtype
-- department
-- designation
-- employeestatus
-- employeetype
-- gender
-- shiftstatus
-- rostershiftassignmentstatus
-- usertype
-- userstatus
-- etc.
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
           'Roster Assignment Test Branch',
           'RST9001',
           'Test Address',
           '0119000001',
           'roster@test.com',
           CURRENT_DATE,
           'Roster shift assignment repository test branch',
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
           'Roster Assignment Tester',
           'Tester',
           '900000001V',
           '0779000001',
           'employee9001@roster.test',
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
           'Roster shift assignment repository test user',
           9001,
           1,
           1
       );


-- ============================================================
-- 4. ROSTER
-- ============================================================
-- Used by:
-- findUnassignedByRosterId()
-- findByRosterId()

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
-- 5. ROSTER SHIFT #1
-- ============================================================
-- Morning shift
-- Used to test ordering by doshift + shift.tostart

INSERT INTO rostershift (
    id,
    roster_id,
    shift_id,
    doshift,
    designation_id,
    requiredemployeecount
)
VALUES (
           9001,
           9001,
           1,
           '2026-08-10',
           1,
           2
       );


-- ============================================================
-- 6. ROSTER SHIFT #2
-- ============================================================
-- Day shift
-- Used to verify ordering.

INSERT INTO rostershift (
    id,
    roster_id,
    shift_id,
    doshift,
    designation_id,
    requiredemployeecount
)
VALUES (
           9002,
           9001,
           2,
           '2026-08-11',
           1,
           1
       );


-- ============================================================
-- 7. ASSIGNMENT #1 - UNASSIGNED
-- ============================================================
-- employee_id = NULL
-- Used by findUnassignedByRosterId()

INSERT INTO rostershiftassignment (
    id,
    rostershift_id,
    employee_id,
    rostershiftassignmentstatus_id
)
VALUES (
           9001,
           9001,
           NULL,
           1
       );


-- ============================================================
-- 8. ASSIGNMENT #2 - ASSIGNED
-- ============================================================
-- Used to verify unassigned query excludes assigned records.
-- Also used by updateEmployeeAndStatusDirectly().

INSERT INTO rostershiftassignment (
    id,
    rostershift_id,
    employee_id,
    rostershiftassignmentstatus_id
)
VALUES (
           9002,
           9001,
           9001,
           2
       );


-- ============================================================
-- 9. ASSIGNMENT #3 - ANOTHER ROSTER SHIFT
-- ============================================================
-- Used by:
-- findByRosterId()
-- countAssignmentsByRosterShiftId()

INSERT INTO rostershiftassignment (
    id,
    rostershift_id,
    employee_id,
    rostershiftassignmentstatus_id
)
VALUES (
           9003,
           9002,
           NULL,
           1
       );
