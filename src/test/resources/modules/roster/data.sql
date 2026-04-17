SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE rostershiftassignment;
TRUNCATE TABLE shift;
TRUNCATE TABLE roster;
TRUNCATE TABLE role;
TRUNCATE TABLE rostershiftassignmentstatus;
TRUNCATE TABLE shiftstatus;
TRUNCATE TABLE rosterstatus;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO role (id, name) VALUES
    (1, 'Driver'),
    (2, 'Conductor');

INSERT INTO rosterstatus (id, name) VALUES
    (1, 'Draft'),
    (2, 'Locked'),
    (3, 'Archived');

INSERT INTO rostershiftassignmentstatus (id, name) VALUES
       (1, 'Suggested'),
       (2, 'Confirmed'),
       (3, 'Rejected');

INSERT INTO shiftstatus (id, name) VALUES
       (1, 'Active'),
       (2, 'Inactive');


INSERT INTO roster (id, branch_id, dostartofweek, doendofweek, rosterstatus_id, deleted) VALUES
         (1, 1, '2026-04-14', '2026-04-20', 2, 0),  -- LOCKED  → generate happy path
         (2, 1, '2026-04-14', '2026-04-20', 1, 0),  -- DRAFT   → approve/reject happy path
         (3, 1, '2026-03-01', '2026-03-07', 3, 0),  -- ARCHIVED → all operations fail
         (4, 2, '2026-04-14', '2026-04-20', 2, 0),  -- LOCKED, branch 2, no shifts
         (5, 1, '2026-04-14', '2026-04-20', 2, 0);  -- LOCKED  → all slots pre-filled (Option A)

INSERT INTO shift (id, branch_id, name, tostart, toend, maxhours, shiftstatus_id) VALUES
        (1, 1, 'Morning Shift', '06:00:00', '14:00:00', 8, 1),
        (2, 1, 'Evening Shift', '14:00:00', '22:00:00', 8, 1),
        (3, 1, 'Night Shift',   '22:00:00', '06:00:00', 8, 1);


-- ── Assignments ───────────────────────────────────────────────────────

-- Roster 2 (DRAFT) — approve/reject scenarios
INSERT INTO rostershiftassignment (id, shift_id, roster_id, doassigned, role_id, employee_id, shiftrosterassignmentstatus_id) VALUES
    (1, 1, 2, '2026-04-14', 1, 1, 1),  -- SUGGESTED → approve happy path
    (2, 1, 2, '2026-04-14', 2, 4, 2);  -- CONFIRMED → reject must fail (cannot reject Confirmed)

-- Roster 1 (LOCKED) — clear suggestions scenario
INSERT INTO rostershiftassignment (id, shift_id, roster_id, doassigned, role_id, employee_id, shiftrosterassignmentstatus_id) VALUES
    (3, 1, 1, '2026-04-14', 1, 1, 1),  -- SUGGESTED → cleared by clearAllSuggestions
    (4, 1, 1, '2026-04-14', 2, 4, 2);  -- CONFIRMED → must NOT be cleared

-- Roster 5 (LOCKED) — all 42 slots pre-filled (7 days × 3 shifts × 2 roles)
-- Used to verify Option A: generate produces 0 new suggestions when all slots filled

INSERT INTO rostershiftassignment (shift_id, roster_id, doassigned, role_id, employee_id, shiftrosterassignmentstatus_id) VALUES
    -- Day 1: 2026-04-14
    (1, 5, '2026-04-14', 1, 1, 2), (1, 5, '2026-04-14', 2, 4, 2),
    (2, 5, '2026-04-14', 1, 2, 2), (2, 5, '2026-04-14', 2, 5, 2),
    (3, 5, '2026-04-14', 1, 3, 2), (3, 5, '2026-04-14', 2, 6, 2),
    -- Day 2: 2026-04-15
    (1, 5, '2026-04-15', 1, 1, 2), (1, 5, '2026-04-15', 2, 4, 2),
    (2, 5, '2026-04-15', 1, 2, 2), (2, 5, '2026-04-15', 2, 5, 2),
    (3, 5, '2026-04-15', 1, 3, 2), (3, 5, '2026-04-15', 2, 6, 2),
    -- Day 3: 2026-04-16
    (1, 5, '2026-04-16', 1, 1, 2), (1, 5, '2026-04-16', 2, 4, 2),
    (2, 5, '2026-04-16', 1, 2, 2), (2, 5, '2026-04-16', 2, 5, 2),
    (3, 5, '2026-04-16', 1, 3, 2), (3, 5, '2026-04-16', 2, 6, 2),
    -- Day 4: 2026-04-17
    (1, 5, '2026-04-17', 1, 1, 2), (1, 5, '2026-04-17', 2, 4, 2),
    (2, 5, '2026-04-17', 1, 2, 2), (2, 5, '2026-04-17', 2, 5, 2),
    (3, 5, '2026-04-17', 1, 3, 2), (3, 5, '2026-04-17', 2, 6, 2),
    -- Day 5: 2026-04-18
    (1, 5, '2026-04-18', 1, 1, 2), (1, 5, '2026-04-18', 2, 4, 2),
    (2, 5, '2026-04-18', 1, 2, 2), (2, 5, '2026-04-18', 2, 5, 2),
    (3, 5, '2026-04-18', 1, 3, 2), (3, 5, '2026-04-18', 2, 6, 2),
    -- Day 6: 2026-04-19
    (1, 5, '2026-04-19', 1, 1, 2), (1, 5, '2026-04-19', 2, 4, 2),
    (2, 5, '2026-04-19', 1, 2, 2), (2, 5, '2026-04-19', 2, 5, 2),
    (3, 5, '2026-04-19', 1, 3, 2), (3, 5, '2026-04-19', 2, 6, 2),
    -- Day 7: 2026-04-20
    (1, 5, '2026-04-20', 1, 1, 2), (1, 5, '2026-04-20', 2, 4, 2),
    (2, 5, '2026-04-20', 1, 2, 2), (2, 5, '2026-04-20', 2, 5, 2),
    (3, 5, '2026-04-20', 1, 3, 2), (3, 5, '2026-04-20', 2, 6, 2);

