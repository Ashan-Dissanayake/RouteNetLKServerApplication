-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE scope;
TRUNCATE TABLE codetype;
TRUNCATE TABLE docsequance;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- codetype
INSERT INTO codetype (name) VALUES
                                ('EMPLOYEE'),
                                ('DRIVER'),
                                ('CONDUCTOR'),
                                ('PART_REQUEST'),
                                ('GRN'),
                                ('VEHICLE_SERVICE'),
                                ('BRANCH');

insert into scope (name) values ('GLOBAL'),('CLM0001'),('ANG0001'),('KND0001');
