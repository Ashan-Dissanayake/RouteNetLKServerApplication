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

    ('Avissawella', 'AVS0001', 'X644 42W, Road, Avissawella', '0362222348', 'avs@sltb.lk', '2025-10-14', 2, '', 1, 1, 1),

    ('Colombo Central Depot', 'CLM0002', 'Olcott Mawatha, Colombo', '0117706401', 'clm@sltb.lk', '2025-11-01', 1, '', 1, 1, 0),

    ('Colombo General Depot', 'CLM0003', 'Maradana Road, Colombo', '0117706402', 'clm@sltb.lk', '2025-11-05', 2, '', 2, 1, 0),

    ('Colombo Sub Depot', 'CLM0004', 'Dematagoda, Colombo', '0117706403', 'clm@sltb.lk', '2025-11-10', 3, '', 3, 1, 0),

    ('Colombo Workshop Depot', 'CLM0005', 'Kelaniya Road, Colombo', '0117706404', 'clm@sltb.lk', '2025-11-15', 4, '', 1, 1, 0),

    ('Ampara Central Depot', 'AMP0001', 'Main Street, Ampara', '0632223401', 'amp@sltb.lk', '2025-11-20', 1, '', 2, 2, 0),

    ('Gampaha General Depot', 'GMP0001', 'Colombo Road, Gampaha', '0332223402', 'gmp@sltb.lk', '2025-11-25', 2, '', 3, 3, 0),

    ('Kalutara Sub Depot', 'KLT0001', 'Nagoda Road, Kalutara', '0342223403', 'klt@sltb.lk', '2025-12-01', 3, '', 1, 4, 0),

    ('Kandy Workshop Depot', 'KDY0001', 'Peradeniya Road, Kandy', '0812223404', 'kdy@sltb.lk', '2025-12-05', 4, '', 2, 5, 0),

    ('Jaffna Central Depot', 'JFN0001', 'Hospital Road, Jaffna', '0212223405', 'jfn@sltb.lk', '2025-12-10', 1, '', 3, 6, 0),

    ('Nuwara Eliya General Depot', 'NEL0001', 'Badulla Road, Nuwara Eliya', '0522223406', 'nel@sltb.lk', '2025-12-15', 2, '', 1, 7, 0),

    ('Anuradhapura Sub Depot', 'ANP0001', 'Airport Road, Anuradhapura', '0252223407', 'anp@sltb.lk', '2025-12-20', 3, '', 2, 8, 0),

    ('Ratnapura Workshop Depot', 'RTP0001', 'Colombo Road, Ratnapura', '0452223408', 'rtp@sltb.lk', '2025-12-25', 4, '', 3, 9, 0);

-- codetype
# INSERT INTO codetype (name) VALUES
#                                 ('EMPLOYEE'),
#                                 ('DRIVER'),
#                                 ('CONDUCTOR'),
#                                 ('PART_REQUEST'),
#                                 ('GRN'),
#                                 ('VEHICLE_SERVICE'),
#                                 ('BRANCH');
#
# insert into scope (name) values ('GLOBAL'),('CLM'),('ANG'),('KND');
