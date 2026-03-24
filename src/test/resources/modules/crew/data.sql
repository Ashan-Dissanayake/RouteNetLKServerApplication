SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE licensecategory;
TRUNCATE TABLE crewstatus;
TRUNCATE TABLE routefamiliaritylevel;
TRUNCATE TABLE driver;
TRUNCATE TABLE conductor;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO licensecategory (name) VALUES
                                       ('B'),
                                       ('C1'),
                                       ('E');

INSERT INTO crewstatus (name) VALUES
                                  ('Eligible'),
                                  ('Ineligible'),
                                  ('Active'),
                                  ('Inactive');


INSERT INTO routefamiliaritylevel (name) VALUES
                                             ('Low'),
                                             ('Medium'),
                                             ('High');

INSERT INTO driver (
    employee_id, number,licensenumber,dolicenseissued,dolicenseexpired,domedicalissued,domedicalexpired,
    licensecategory_id,
    crewstatus_id,
    routefamiliaritylevel_id
) VALUES
      (1, 'DRV-0001', 'B12345678902','2025-07-12', '2027-12-27','2025-07-01', '2026-12-31',1, 1, 2);
