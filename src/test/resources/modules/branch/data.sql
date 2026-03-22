-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE branchstatus;
TRUNCATE TABLE branchtype;
TRUNCATE TABLE regionaloffice;
TRUNCATE TABLE branch;


TRUNCATE TABLE scope;
TRUNCATE TABLE codetype;
TRUNCATE TABLE docsequance;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- branchstatus
INSERT INTO branchstatus (name) VALUES ('Active'),('Suspended'),('Closed');

-- branchtype
INSERT INTO branchtype (name) VALUES ('Central'),('General'),('Sub Depot'),('Workshop Depot');

-- regionaloffice
INSERT INTO regionaloffice (name) VALUES
                                      ('Colombo'),('Eastern'),('Gampaha'),('Kalutara'),('Kandy'),('Northern'),
                                      ('Nuwara-Eliya'),('Rajarata'),('Sabaragamuwa'),('Southern'),('Uva'),('Wayamba');

-- branch
INSERT INTO branch
(name, code, address, telephone, email, docreated, branchtype_id, remarks, branchstatus_id, regionaloffice_id, deleted)
VALUES
    ('Colombo head office', 'CLM0001', 'Kirula Rd, Colombo 00500', '0117706320', 'clm@sltb.lk', '2025-10-03', 1, '', 1, 1, 0),
    ('Angoda', 'ANG0001', 'WWF7 2H4, Colombo', '0117706321', 'ang@sltb.lk', '2025-10-03', 3, '', 1, 9, 0),
    ('Avissawella', 'AVS0001', 'X644 42W, Road, Avissawella', '0362222348', 'avs@sltb.lk', '2025-10-14', 2, '', 1, 1, 1);

-- codetype
INSERT INTO codetype (name) VALUES
                                ('EMPLOYEE'),
                                ('DRIVER'),
                                ('CONDUCTOR'),
                                ('PART_REQUEST'),
                                ('GRN'),
                                ('VEHICLE_SERVICE'),
                                ('BRANCH');

insert into scope (name) values ('GLOBAL')
