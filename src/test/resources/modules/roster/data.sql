SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE rostershiftassignment;
TRUNCATE TABLE rostershift;
TRUNCATE TABLE shift;
TRUNCATE TABLE roster;
TRUNCATE TABLE rostershiftassignmentstatus;
TRUNCATE TABLE shiftstatus;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO rostershiftassignmentstatus (id, name) VALUES
   (1, 'Suggested'),
   (2, 'Confirmed'),
   (3, 'Rejected');

INSERT INTO shiftstatus (id, name) VALUES
   (1, 'Active'),
   (2, 'Inactive');

INSERT INTO roster (id, branch_id, dostartofweek, doendofweek, deleted) VALUES
    (1, 1, '2026-04-14', '2026-04-20', 0),
    (2, 1, '2026-04-14', '2026-04-20', 0),
    (3, 1, '2026-03-01', '2026-03-07', 0),
    (4, 2, '2026-04-14', '2026-04-20', 0),
    (5, 1, '2026-04-14', '2026-04-20', 0);


INSERT INTO shift (
    name,
    tostart,
    toend,
    notrips,
    maxhours,
    breakminutes,
    issplitshift,
    shiftstatus_id
) VALUES

      ('Morning Peak',        '04:00:00', '12:00:00', NULL, 8, 30, 0, 1),
      ('Day Shift',           '08:00:00', '16:00:00', NULL, 8, 60, 0, 1),
      ('Evening Peak',        '13:00:00', '21:00:00', NULL, 8, 30, 0, 1),
      ('Night Shift',         '20:00:00', '04:00:00', NULL, 8, 60, 0, 1),
      ('Split Office Shift',  '06:00:00', '18:00:00', NULL, 12, 240, 1, 1),
      ('Short Distance Turn', '06:00:00', '10:00:00', NULL, 4, 15, 0, 1);


INSERT INTO rostershift (
    id,
    roster_id,
    shift_id,
    doshift,
    designation_id
) VALUES
      (1, 2, 1, '2026-04-14', 1),
      (2, 2, 1, '2026-04-14', 2);


INSERT INTO rostershift VALUES
                            (3, 1, 1, '2026-04-14', 1),
                            (4, 1, 1, '2026-04-14', 2);

INSERT INTO rostershift VALUES
                            (5, 5, 1, '2026-04-14', 1),
                            (6, 5, 1, '2026-04-14', 2),
                            (7, 5, 2, '2026-04-14', 1),
                            (8, 5, 2, '2026-04-14', 2),
                            (9, 5, 3, '2026-04-14', 1),
                            (10,5, 3, '2026-04-14', 2);


INSERT INTO rostershiftassignment (
    id,
    rostershift_id,
    employee_id,
    rostershiftassignmentstatus_id
) VALUES

-- Roster 2
(1, 1, 1, 1),
(2, 2, 4, 2),

-- Roster 1
(3, 3, 1, 1),
(4, 4, 4, 2),

-- Roster 5
(5, 5, 1, 2),
(6, 6, 4, 2),
(7, 7, 2, 2),
(8, 8, 5, 2),
(9, 9, 3, 2),
(10,10,6, 2);
