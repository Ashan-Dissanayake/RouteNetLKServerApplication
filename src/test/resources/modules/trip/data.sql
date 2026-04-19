-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE triptype;
TRUNCATE TABLE originterminal;
TRUNCATE TABLE tripstatus;
TRUNCATE TABLE overridestatus;
TRUNCATE TABLE trip;
TRUNCATE TABLE tripvehicleoverride;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

insert into triptype (name) values ('Daily'),('Weekday'),('Weekend'),('Special');

insert into originterminal (name) values ('pettah'),('Rajagiriya'),('Kirindiwela'),
                                         ('Sigiriya'),('Gampaha');

insert into overridestatus (name) values ('Active'),('Cancelled');

insert into tripstatus (name)
values ('Planned'),('Ready'),('Need vehicle override'),('In progress'),
       ('Delayed'),('Suspended'),('Completed'),('Cancelled');


-- Ensure FOREIGN_KEY_CHECKS are handled if truncating before this

insert into trip (branch_id, triptype_id, permite_id, doservice, todepature, toarrival, notrip, tripstatus_id, originterminal_id)
values
    -- Trip 1: Tomorrow at 08:00 (Used for Gap Violation Test)
    (1, 2, 1, DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '08:00:00', '10:00:00', 1, 2, 1),

    -- Trip 2: Two days from now (Normal)
    (1, 1, 1, DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '08:30:00', '12:30:00', 1, 2, 1),

    -- Trip 3: Tomorrow at 09:00 (Used for Solver Suggestion - Linked to Permit 2/Vehicle 4)
    (1, 2, 2, DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '09:00:00', '11:00:00', 1, 3, 1),

    -- Trip 4: Today (In progress scenario)
    (1, 2, 1, CURRENT_DATE, '12:00:00', '14:00:00', 2, 4, 1),

    -- Trip 5: Future trip (Status 4 - Delayed)
    (1, 2, 1, DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '14:00:00', '16:00:00', 3, 4, 1),

    -- Trip 6: Future trip (Status 7 - Completed)
    (1, 2, 1, DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), '06:00:00', '08:00:00', 4, 7, 1),

    -- Trip 7: Tomorrow at 10:00 (Status 3 - Need vehicle override)
    (1, 2, 2, DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '10:00:00', '12:00:00', 2, 3, 1),

    -- Trip 8: Tomorrow at 09:00 (Status 2 - Ready)
    (1, 2, 1, DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '09:00:00', '13:00:00', 5, 2, 1);
