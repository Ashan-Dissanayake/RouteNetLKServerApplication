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
    ('Colombo head office', 'CLM0001', 'Kirula Rd, Colombo 00500', '117706320', 'clm@sltb.lk', '2025-10-03', 1, '', 1, 1, 0),
    ('Angoda', 'ANG0001', 'WWF7 2H4, Colombo', '117706321', 'ang@sltb.lk', '2025-10-03', 3, '', 1, 9, 0),
    ('Avissawella', 'AVS0001', 'X644 42W, Road, Avissawella', '0362222348', 'avs@sltb.lk', '2025-10-14', 2, '', 1, 1, 1);
