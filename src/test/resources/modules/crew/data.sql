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
      (1, 'DRV-0001', 'B12345678902','2025-07-12', '2027-12-27','2025-07-01', '2026-12-31',1, 1, 2),
      (2, 'DRV-0002', 'C17917813024','2025-02-13', '2033-02-11','2024-07-04', '2024-12-31',2, 1, 1),
      (3, 'DRV-0003', 'C18785695809','2025-07-25', '2033-07-23','2023-08-23', '2024-02-19',2, 4, 1);
#       (4, 'DRV-0004', 'B65182022557','2024-12-15', '2032-12-13','2025-11-01', '2026-04-30',1, 1,3); remove for roster test

INSERT INTO conductor (
    employee_id, number,domedicalissued,domedicalexpired,crewstatus_id,routefamiliaritylevel_id
) VALUES
      (4, 'CON-0001', '2026-04-12', '2026-10-12',1, 1),
      (5, 'CON-0002', '2026-04-12', '2026-10-12',1, 1),
      (6, 'CON-0003' ,'2026-04-12', '2026-10-12',1, 1);
