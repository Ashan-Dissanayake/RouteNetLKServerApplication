-- ============================================================
-- PRIVILEGE REPOSITORY TEST DATA
-- ============================================================


-- ============================================================
-- 1. ROLE
-- ============================================================

INSERT INTO role (
    id,
    name
)
VALUES (
           9001,
           'Privilege Test Role'
       );

INSERT INTO role (
    id,
    name
)
VALUES (
           9002,
           'Second Test Role'
       );


-- ============================================================
-- 2. MODULE
-- ============================================================

INSERT INTO module (
    id,
    name
)
VALUES (
           9001,
           'Privilege Test Module'
       );

INSERT INTO module (
    id,
    name
)
VALUES (
           9002,
           'Second Test Module'
       );


-- ============================================================
-- 3. OPERATION
-- ============================================================

INSERT INTO operation (
    id,
    displayname,
    operation,
    module_id
)
VALUES (
           9001,
           'View Test',
           'view',
           9001
       );

INSERT INTO operation (
    id,
    displayname,
    operation,
    module_id
)
VALUES (
           9002,
           'Add Test',
           'add',
           9001
       );

INSERT INTO operation (
    id,
    displayname,
    operation,
    module_id
)
VALUES (
           9003,
           'Update Test',
           'update',
           9002
       );


-- ============================================================
-- 4. PRIVILEGE
-- ============================================================
-- Role 9001
-- Module 9001
-- Operation 9001

INSERT INTO privilege (
    id,
    authority,
    role_id,
    module_id,
    operation_id
)
VALUES (
           9001,
           'privilege-test-view',
           9001,
           9001,
           9001
       );


-- ============================================================
-- 5. PRIVILEGE
-- ============================================================
-- Role 9001
-- Module 9001
-- Operation 9002

INSERT INTO privilege (
    id,
    authority,
    role_id,
    module_id,
    operation_id
)
VALUES (
           9002,
           'privilege-test-add',
           9001,
           9001,
           9002
       );


-- ============================================================
-- 6. PRIVILEGE
-- ============================================================
-- Role 9001
-- Module 9002
-- Operation 9003

INSERT INTO privilege (
    id,
    authority,
    role_id,
    module_id,
    operation_id
)
VALUES (
           9003,
           'privilege-test-update',
           9001,
           9002,
           9003
       );


-- ============================================================
-- 7. PRIVILEGE
-- ============================================================
-- Different role.
-- Used to verify role filtering.

INSERT INTO privilege (
    id,
    authority,
    role_id,
    module_id,
    operation_id
)
VALUES (
           9004,
           'second-role-view',
           9002,
           9001,
           9001
       );
