-- Complete data.sql for Roster UAT Testing
-- Includes all necessary data for OptaPlanner to generate feasible solutions

-- branchstatus
INSERT INTO branchstatus (name) VALUES ('Active'),('Suspended'),('Closed');

-- branchtype
INSERT INTO branchtype (name) VALUES ('Central'),('General'),('Sub Depot'),('Workshop Depot');

-- regionaloffice
INSERT INTO regionaloffice (name) VALUES
                                      ('Colombo'),('Eastern'),('Gampaha'),('Kalutara'),('Kandy'),('Northern'),('Nuwara-Eliya'),('Rajarata'),('Sabaragamuwa'),('Southern'),('Uva'),('Wayamba');

INSERT INTO branch
(name, code, address, telephone, email, docreated, branchtype_id, remarks, branchstatus_id, regionaloffice_id, deleted)
VALUES
    ('Colombo head office', 'CLM0001', 'Kirula Rd, Colombo 00500', '117706320', 'clm@sltb.lk', '2025-10-03', 1, '', 1, 1, 0),
    ('Angoda', 'ANG0001', 'WWF7 2H4, Colombo', '117706321', 'ang@sltb.lk', '2025-10-03', 3, '', 1, 9, 0),
    ('Avissawella', 'AVS0001', 'X644 42W, Road, Avissawella', '362222348', 'avs@sltb.lk', '2025-10-14', 2, '', 2, 1, 0),
    ('Homagama', 'HMG0001', 'R2V6 9RR Bus Depot, Homagama', '117706330', 'hmg@sltb.lk', '2025-10-14', 2, '', 3, 1, 1),
    ('Kesbewa Deport', 'KSB0001', 'Piliyandala', '117706360', 'ksb@sltb.lk', '2025-10-15', 3, '', 3, 1, 1);

-- employeestatus
INSERT INTO employeestatus (name) VALUES
                                      ('Active'),('Suspend'),('Resigned'),('On leave');

-- gender
INSERT INTO gender (name) VALUES
                              ('Male'),('Female'),('Other');

-- department
INSERT INTO department (name) VALUES
                                  ('Operations (Traffic)'),('Engineering and Technical'),('Administrative'),
                                  ('Finance and Revenue'),('Stores Department');

-- designation
INSERT INTO designation (name) VALUES
                                   ('Driver'),('Conductor'),('Mechanic'),('Depot Manager'),
                                   ('Assistant Manager'),('Supervisory'),('Clerical');

-- employeetype
INSERT INTO employeetype (name) VALUES
                                    ('Permanent'),('Contract'),('Temporary'),('Probationers'),('Casual');

-- Original Employees
INSERT INTO employee
(number, fullname, callingname, nic, gender_id, mobile, email, address, emergencycontact, image, branch_id, department_id, designation_id, employeetype_id, doj, employeestatus_id, deleted)
VALUES
    ('EMPCLM0001', 'Sunil Perera', 'Sunil', '200046000000', 1, '771234567', 'sunil.EMPCLM0001@sltb.lk', 'No 12, Maradana, Colombo 10', '712345678', NULL, 1, 1, 1, 1, '2015-03-12', 1, 0),
    ('EMPCLM0002', 'Kumari Fernando', 'Kumari', '857621345V', 2, '758765432', 'kumari.EMPCLM0002@sltb.lk', 'No 45, Borella, Colombo 8', '767895432', NULL, 1, 3, 1, 2, '2018-07-24', 1, 0),
    ('EMPANG0001', 'Rohan Jayasuriya', 'Rohan', '199179000000', 1, '714456789', 'rohan.EMPANG0001@sltb.lk', 'No 21, Angoda', '775678945', NULL, 2, 1, 1, 1, '2016-09-10', 4, 0),
    ('EMPANG0002', 'Nirosha Weerasinghe', 'Nirosha', '945622345V', 2, '702345678', 'nirosha.EMPANG0002@sltb.lk', 'No 32, Ambathale Road, Angoda', '713345678', NULL, 2, 1, 1, 1, '2020-11-15', 1, 0),
    ('EMPANG0003', 'Pradeep Silva', 'Pradeep', '200046000000', 1, '762233456', 'pradeep.EMPANG0003@sltb.lk', 'No 78, Mulleriyawa', '774561234', NULL, 2, 2, 1, 3, '2019-06-05', 4, 0),
    ('EMPAVS0001', 'Chaminda Ranasinghe', 'Chaminda', '871235678V', 1, '777891234', 'chaminda.EMPAVS0001@sltb.lk', 'No 34, Avissawella', '712349999', NULL, 4, 1, 2, 1, '2014-01-22', 1, 0),
    ('EMPAVS0002', 'Harsha Abeykoon', 'Harsha', '199946000000', 1, '701239876', 'harsha.EMPAVS0002@sltb.lk', 'No 11, Puwakpitiya, Avissawella', '751112223', NULL, 4, 2, 2, 3, '2021-02-01', 3, 1),
    ('EMPAVS0003', 'Anoma Jayawardena', 'Anoma', '926578901V', 2, '754453321', 'anoma.EMPAVS0003@sltb.lk', 'No 56, Avissawella Town', '775551234', NULL, 4, 3, 2, 4, '2022-08-13', 1, 0),
    ('EMPANG0004', 'Kasun Rathnayake', 'Kasun', '199879000000', 1, '716677889', 'kasun.EMPANG0004@sltb.lk', 'No 20, Homagama', '721234567', NULL, 2, 1, 2, 1, '2017-05-20', 1, 0),
    ('EMPANG0005', 'Dilini Samarasinghe', 'Dilini', '987654321V', 2, '779988776', 'dilini.EMPANG0005@sltb.lk', 'No 25, Galawilawatta, Homagama', '769998887', NULL, 2, 1, 2, 2, '2019-12-15', 4, 0),
    ('EMPANG0006', 'Priyantha Liyanage', 'Priyantha', '198946000000', 1, '701234567', 'priyantha.EMPANG0006@sltb.lk', 'No 45, Godagama', '712223344', NULL, 2, 2, 3, 5, '2023-07-01', 1, 0),
    ('EMPAVS0005', 'Rasika Dissanayake', 'Rasika', '751235678V', 2, '756677889', 'rasika.EMPAVS0005@sltb.lk', 'No 89, Meegoda', '771112233', NULL, 4, 4, 7, 3, '2016-10-30', 4, 0),
    ('EMPAVS0006', 'Sameera Fernando', 'Sameera', '200057000000', 1, '718899000', 'sameera.EMPAVS0006@sltb.lk', 'No 2, Kesbewa', '751234567', NULL, 4, 1, 1, 1, '2013-06-18', 1, 0),
    ('EMPCLM0010', 'Ishara Wickramasinghe', 'Ishara', '956712345V', 2, '709988776', 'ishara.EMPCLM0010@sltb.lk', 'No 6, Piliyandala', '718887776', NULL, 1, 1, 2, 1, '2018-02-12', 1, 0),
    ('EMPANG0007', 'Ajith Abeyratne', 'Ajith', '200146000000', 1, '766678899', 'ajith.EMPANG0007@sltb.lk', 'No 88, Kesbewa South', '776677788', NULL, 2, 3, 5, 2, '2020-10-09', 1, 0),
    ('EMPAVS0007', 'Thilini Madushani', 'Thilini', '946578901V', 2, '757788990', 'thilini.EMPAVS0007@sltb.lk', 'No 11, Bandaragama', '725566778', NULL, 4, 4, 7, 3, '2021-03-25', 1, 0),
    ('EMPCLM0003', 'Mahesh Gunawardena', 'Mahesh', '803456789V', 1, '719988776', 'mahesh.EMPCLM0003@sltb.lk', 'No 5, Borella, Colombo 8', '709988776', NULL, 1, 3, 5, 1, '2011-01-11', 1, 0),
    ('EMPCLM0004', 'Nadeesha Abeywardena', 'Nadeesha', '199946000000', 2, '726677889', 'nadeesha.EMPCLM0004@sltb.lk', 'No 56, Dehiwala', '713344556', NULL, 1, 3, 6, 4, '2022-05-05', 4, 0),
    ('EMPCLM0005', 'Suresh Perera', 'Suresh', '198735000000', 1, '706677889', 'suresh.EMPCLM0005@sltb.lk', 'No 22, Mount Lavinia', '778899000', NULL, 1, 2, 3, 5, '2023-01-10', 1, 0),
    ('EMPCLM0006', 'Heshani Silva', 'Heshani', '976543210V', 2, '766677788', 'heshani.EMPCLM0006@sltb.lk', 'No 7, Rajagiriya', '751236789', NULL, 1, 4, 2, 3, '2019-11-18', 1, 0),
    ('EMPCLM0007', 'Dilshan Perera', 'Dilshan', '200335000000', 1, '771112334', 'dilshan.EMPCLM0007@sltb.lk', 'No 15, Maradana, Colombo 10', '712334556', NULL, 1, 1, 1, 1, '2022-05-22', 1, 0),
    ('EMPCLM0008', 'Jane Doe', 'Jane', '200124000000', 1, '772222222', 'jane.EMPCLM0008@sltb.lk', 'No 2, Colombo', '712345679', NULL, 1, 1, 1, 1, '2021-07-31', 1, 0),
    ('EMPCLM0009', 'Priya Silva', 'Priya', '200656000000', 2, '773333333', 'priya.EMPCLM0009@sltb.lk', 'No 4, Colombo', '712345681', NULL, 1, 3, 5, 1, '2011-07-31', 1, 0),
    ('EMPAVS0004', 'Nadeesha Perera', 'Nadeesha', '200557000000', 2, '701112233', 'nadeesha.EMPAVS0004@sltb.lk', 'No 40, Avissawella', '751223344', NULL, 4, 3, 5, 1, '2011-01-19', 1, 0),
    ('EMPAVS0008', 'Nishantha Peris Gunarathne', 'Nishantha', '199912000000', 1, '775551011', 'nishantha.EMPAVS0008@sltb.lk', 'Perera Rd, Negombo', '771438876', NULL, 4, 1, 2, 2, '2025-12-02', 1, 0);

-- ==================== ADDITIONAL EMPLOYEES FOR UAT 7 ====================
-- Add more ACTIVE employees for Branch 1 (Colombo) to ensure OptaPlanner success

INSERT INTO employee
(number, fullname, callingname, nic, gender_id, mobile, email, address, emergencycontact, image, branch_id, department_id, designation_id, employeetype_id, doj, employeestatus_id, deleted)
VALUES
    -- More Drivers (designation_id = 1)
    ('EMPCLM0011', 'Amal Dissanayake', 'Amal', '199001012345', 1, '771234568', 'amal.EMPCLM0011@sltb.lk', 'No 10, Colombo', '712345680', NULL, 1, 1, 1, 1, '2020-01-15', 1, 0),
    ('EMPCLM0012', 'Buddhika Silva', 'Buddhika', '199102022345', 1, '771234569', 'buddhika.EMPCLM0012@sltb.lk', 'No 11, Colombo', '712345681', NULL, 1, 1, 1, 1, '2020-02-20', 1, 0),
    ('EMPCLM0013', 'Chandana Perera', 'Chandana', '199203032345', 1, '771234570', 'chandana.EMPCLM0013@sltb.lk', 'No 13, Colombo', '712345682', NULL, 1, 1, 1, 1, '2020-03-25', 1, 0),
    ('EMPCLM0014', 'Dinesh Fernando', 'Dinesh', '199304042345', 1, '771234571', 'dinesh.EMPCLM0014@sltb.lk', 'No 14, Colombo', '712345683', NULL, 1, 1, 1, 1, '2020-04-30', 1, 0),
    ('EMPCLM0015', 'Eranga Wickrama', 'Eranga', '199405052345', 1, '771234572', 'eranga.EMPCLM0015@sltb.lk', 'No 16, Colombo', '712345684', NULL, 1, 1, 1, 1, '2020-05-10', 1, 0),

    -- More Conductors (designation_id = 2)
    ('EMPCLM0016', 'Fathima Nizar', 'Fathima', '199506062345', 2, '771234573', 'fathima.EMPCLM0016@sltb.lk', 'No 17, Colombo', '712345685', NULL, 1, 1, 2, 1, '2020-06-15', 1, 0),
    ('EMPCLM0017', 'Gayan Rathnayake', 'Gayan', '199607072345', 1, '771234574', 'gayan.EMPCLM0017@sltb.lk', 'No 18, Colombo', '712345686', NULL, 1, 1, 2, 1, '2020-07-20', 1, 0),
    ('EMPCLM0018', 'Hemantha Silva', 'Hemantha', '199708082345', 1, '771234575', 'hemantha.EMPCLM0018@sltb.lk', 'No 19, Colombo', '712345687', NULL, 1, 1, 2, 1, '2020-08-25', 1, 0),
    ('EMPCLM0019', 'Iroshini Perera', 'Iroshini', '199809092345', 2, '771234576', 'iroshini.EMPCLM0019@sltb.lk', 'No 20, Colombo', '712345688', NULL, 1, 1, 2, 1, '2020-09-30', 1, 0),
    ('EMPCLM0020', 'Janaka Fernando', 'Janaka', '199910102345', 1, '771234577', 'janaka.EMPCLM0020@sltb.lk', 'No 21, Colombo', '712345689', NULL, 1, 1, 2, 1, '2020-10-15', 1, 0);

-- Make and Model data
INSERT INTO make (name) VALUES
                            ('Ashok Leyland'),
                            ('Tata'),
                            ('Isuzu'),
                            ('Mercedes-Benz'),
                            ('Metro'),
                            ('AEC'),
                            ('Hino'),
                            ('Mitsubishi'),
                            ('Volvo'),
                            ('Greatewall'),
                            ('Youtong'),
                            ('Kinlong');

INSERT INTO model (name, make_id) VALUES
                                      ('Ashok Leyland 12M RE', 1),
                                      ('Ashok Leyland Viking 193', 1),
                                      ('Ashok Leyland Viking 210 Turbo', 1),
                                      ('Ashok Leyland Comet Minior', 1),
                                      ('Ashok Leyland Viking 222 Hinopower', 1),
                                      ('Ashok Leyland Stag bus', 1),
                                      ('TATA LP 1510/52', 2),
                                      ('TATA LPO 1313/55', 2),
                                      ('TATA LP 1210/36', 2),
                                      ('TATA LP 1210/52', 2),
                                      ('TATA LP 1510/36', 2),
                                      ('TATA LPO 1313/47', 2),
                                      ('TATA LP 909/36', 2);

INSERT INTO conditionrate (name) VALUES
                                     ('Excellent'),
                                     ('Good'),
                                     ('Fair'),
                                     ('Poor');

INSERT INTO vehiclestatus (name) VALUES
                                     ('Available'),
                                     ('Allocated'),
                                     ('In Operation'),
                                     ('Maintenance'),
                                     ('Breakdown'),
                                     ('Decommissioned');

INSERT INTO fueltype (name) VALUES
                                ('Petrol'),
                                ('Diesel');

INSERT INTO bustype(name) values ('AA'),('A+'),('A'),('B'),('B+'),('C'),('D'),('E');

INSERT INTO vehicle
(number,model_id,bustype_id,mileage, fueltype_id, conditionrate_id, remarks, vehiclestatus_id, deleted, branch_id)
VALUES
    ('ND-9167',1,6,180000,2, 4, NULL, 1, 0, 1),
    ('NA-7845',2,1,200000,2, 3, NULL, 1, 0, 1),
    ('ND-5623',2,1,80000,2, 4, NULL, 4, 0, 1),
    ('NB-4392',3,1,220000,2, 2, NULL, 5, 1, 1),
    ('NA-1111', 2, 1,50000, 2, 2, null, 1, 0, 1),
    ('NA-2222', 2, 1,60000, 2, 3, null, 1, 0, 1);

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

-- Original Drivers
INSERT INTO driver (
    employee_id,
    number,
    licensenumber,
    dolicenseissued,
    dolicenseexpired,
    domedicalissued,
    domedicalexpired,
    licensecategory_id,
    crewstatus_id,
    routefamiliaritylevel_id
) VALUES
      (1, 'DRV-2025-002', 'B12345678902',
       '2022-12-10', '2027-12-27',
       '2025-07-01', '2026-12-31',
       1, 1, 2),

      (3, 'DRV-2025-003', 'C12345678903',
       '2021-11-30', '2026-11-30',
       '2024-10-15', '2026-04-15',
       2, 1, 1),

      (4, 'DRV-2025-004', 'C12345678904',
       '2022-08-20', '2027-08-20',
       '2025-09-10', '2027-03-10',
       2, 1, 3),

      (5, 'DRV-2025-005', 'B12345678905',
       '2023-01-10', '2028-01-10',
       '2025-01-05', '2026-07-05',
       1, 1, 2);

-- ==================== ADDITIONAL DRIVERS FOR UAT 7 ====================
INSERT INTO driver (
    employee_id,
    number,
    licensenumber,
    dolicenseissued,
    dolicenseexpired,
    domedicalissued,
    domedicalexpired,
    licensecategory_id,
    crewstatus_id,
    routefamiliaritylevel_id
) VALUES
-- Employee 2 (EMPCLM0002 - Kumari)
(2, 'DRV-2025-006', 'B12345678906',
 '2022-01-15', '2027-01-15',
 '2025-01-01', '2026-07-01',
 1, 1, 2),

-- Employee 21 (EMPCLM0007 - Dilshan)
(21, 'DRV-2025-007', 'C12345678907',
 '2022-05-22', '2027-05-22',
 '2025-05-22', '2026-11-22',
 2, 1, 3),

-- Employee 22 (EMPCLM0008 - Jane)
(22, 'DRV-2025-008', 'B12345678908',
 '2021-07-31', '2026-07-31',
 '2025-07-31', '2027-01-31',
 1, 1, 2),

-- Employee 26 (EMPCLM0011 - Amal)
(26, 'DRV-2025-011', 'B12345678911',
 '2020-01-15', '2030-01-15',
 '2025-01-15', '2026-07-15',
 1, 1, 2),

-- Employee 27 (EMPCLM0012 - Buddhika)
(27, 'DRV-2025-012', 'C12345678912',
 '2020-02-20', '2030-02-20',
 '2025-02-20', '2026-08-20',
 2, 1, 3),

-- Employee 28 (EMPCLM0013 - Chandana)
(28, 'DRV-2025-013', 'B12345678913',
 '2020-03-25', '2030-03-25',
 '2025-03-25', '2026-09-25',
 1, 1, 2),

-- Employee 29 (EMPCLM0014 - Dinesh)
(29, 'DRV-2025-014', 'C12345678914',
 '2020-04-30', '2030-04-30',
 '2025-04-30', '2026-10-30',
 2, 1, 3),

-- Employee 30 (EMPCLM0015 - Eranga)
(30, 'DRV-2025-015', 'B12345678915',
 '2020-05-10', '2030-05-10',
 '2025-05-10', '2026-11-10',
 1, 1, 2);

-- Original Conductors
INSERT INTO conductor (
    employee_id,
    number,
    domedicalissued,
    domedicalexpired,
    crewstatus_id,
    routefamiliaritylevel_id
)values
     (9,'CON-2025-001','2025-01-01','2026-06-30',3,1),
     (11,'CON-2025-002','2025-02-01','2026-07-31',3,2),
     (14,'CON-2025-004','2025-08-01','2027-02-01',3,1);

-- ==================== ADDITIONAL CONDUCTORS FOR UAT 7 ====================
INSERT INTO conductor (
    employee_id,
    number,
    domedicalissued,
    domedicalexpired,
    crewstatus_id,
    routefamiliaritylevel_id
) VALUES
      -- Employee 20 (EMPCLM0006 - Heshani)
      (20,'CON-2025-003','2025-03-01','2026-09-01',3,2),

      -- Employee 31 (EMPCLM0016 - Fathima)
      (31,'CON-2025-005','2025-06-15','2026-12-15',3,1),

      -- Employee 32 (EMPCLM0017 - Gayan)
      (32,'CON-2025-006','2025-07-20','2027-01-20',3,2),

      -- Employee 33 (EMPCLM0018 - Hemantha)
      (33,'CON-2025-007','2025-08-25','2027-02-25',3,1),

      -- Employee 34 (EMPCLM0019 - Iroshini)
      (34,'CON-2025-008','2025-09-30','2027-03-30',3,2),

      -- Employee 35 (EMPCLM0020 - Janaka)
      (35,'CON-2025-009','2025-10-15','2027-04-15',3,1);

-- Route data
INSERT INTO routetype (name) VALUES ('Inter provincial'), ('Intra provincial');

INSERT INTO scheduletype (name) VALUES ('Normal'), ('Special');

INSERT INTO route (number,origin,destination,distancekm,scheduletype_id,routetype_id,mingapminutes)VALUES
                                                                                                       ('4-7','Colombo','Puttalam',137.2,1,1,30),
                                                                                                       ('5','Colombo','Kurunegala',95.8,1,1,30),
                                                                                                       ('6','Colombo','Kurunegala',93.4,1,1,30),
                                                                                                       ('49','Colombo','Trincomalee',258.0,1,1,45),
                                                                                                       ('92-4','Colombo','Kuliyapitiya',86.1,1,1,30),
                                                                                                       ('103-3','Pettah','Borella',4.0,1,2,15),
                                                                                                       ('130','Fort','I.D.H.',10.5,1,2,20),
                                                                                                       ('130-1','Fort','Kolonnawa',7.3,1,2,20),
                                                                                                       ('152-1','Pettah','I.D.H.',9.8,1,2,20),
                                                                                                       ('175','Town Hall','Kohilawatte',11.0,1,2,20),
                                                                                                       ('175-1','Kollupitiya','I.D.H.',10.1,1,2,20),
                                                                                                       ('175-2','Town Hall','I.D.H.',7.9,1,2,20),
                                                                                                       ('175-3','Borella','I.D.H.',6.3,1,2,20),
                                                                                                       ('187-3','Colombo','Katunayake',35.1,1,2,25),
                                                                                                       ('200','Gampaha','Pettah',29.3,1,2,25),
                                                                                                       ('201-4','Yakkala','Gampaha',4.8,1,2,15),
                                                                                                       ('240','Colombo','Negombo',36.1,1,2,30),
                                                                                                       ('896','Trincomalee','Hot Wells',10.0,1,2,20);

INSERT INTO permitestatus (name) values ('Active'),('Expired'),('Suspended'),('Transferred');

INSERT INTO servicetype (name) values ('Normal'),('Semi luxury'),('Luxury'),('Super luxury');

INSERT INTO permite(number,vehicle_id,doissued,doexpired,branch_id,permitestatus_id,servicetype_id,route_id,deleted) VALUES
                                                                                                                         ('2696',1,'2003-01-13','2027-07-24', 1,1,1,1,0),
                                                                                                                         ('2697',3,'2003-01-13','2027-07-24', 1,1,1,1,0),
                                                                                                                         ('ANG-NA7845-103-3',2,'2002-05-01','2026-05-12', 1,1,1,6,0);

insert into triptype (name) values ('Daily'),('Weekday'),('Weekend'),('Special');

insert into originterminal (name) values ('pettah'),('Rajagiriya'),('Kirindiwela'),('Sigiriya'),('Gampaha');

insert into overridestatus (name) values ('Active'),('Cancelled');

insert into tripstatus (name)
values ('Planned'),('Ready'),('Need vehicle override'),('In progress'),('Delayed'),('Suspended'),('Completed'),('Cancelled');

insert into trip (branch_id,triptype_id,permite_id,doservice,todepature,toarrival,notrip,tripstatus_id,originterminal_id) values
                                                                                                                              (1,2,1,'2026-02-16','08:00:00','10:00:00',1,2,1),
                                                                                                                              (1,1,1,'2026-03-09','08:30:00','12:30:00',1,2,1),
                                                                                                                              (1,2,2,'2026-02-15','09:00:00','11:00:00',1,3,1),
                                                                                                                              (1,2,1,'2026-02-15','12:00:00','14:00:00',2,1,1),
                                                                                                                              (1,2,1,'2026-02-14','14:00:00','16:00:00',3,4,1),
                                                                                                                              (1,2,1,'2026-02-14','06:00:00','08:00:00',4,7,1),
                                                                                                                              (1,2,2,'2026-02-16','10:00:00','12:00:00',2,3,1),
                                                                                                                              (1,2,1,'2026-02-16','09:00:00','13:00:00',5,2,1);

-- ==================== ROSTER MODULE DATA ====================

INSERT INTO rosterstatus (name) VALUES
                                    ('Draft'),('Locked'),('Archived');

INSERT INTO shiftrosterassignmentstatus (name) VALUES
                                                   ('Suggested'),('Confirmed'),('Rejected');

INSERT INTO shiftstatus (name) VALUES
                                   ('Active'),('Inactive');

INSERT INTO role (name) VALUES
                            ('Driver'),('Conductor');

INSERT INTO shift (branch_id, name, tostart, toend, maxhours, shiftstatus_id) VALUES
                                                                                  (1, 'Morning Shift', '06:00:00', '14:00:00', 8, 1),
                                                                                  (1, 'Evening Shift', '14:00:00', '22:00:00', 8, 1),
                                                                                  (2, 'Evening Shift', '14:00:00', '22:00:00', 8, 1),
                                                                                  (1, 'Night Shift', '22:00:00', '06:00:00', 8, 1);

-- Rosters for testing
-- Roster 1: DRAFT (for UAT 1, 4, 5, 7, 9, 10, 11, 16, 17)
INSERT INTO roster (branch_id, dostartofweek, doendofweek, rosterstatus_id, deleted) VALUES
    (1, '2026-03-02', '2026-03-08', 1, 0);

-- Roster 2: LOCKED with mixed confirmations (for UAT 6, 13, 15, 19)
INSERT INTO roster (branch_id, dostartofweek, doendofweek, rosterstatus_id, deleted) VALUES
    (1, '2026-03-09', '2026-03-15', 2, 0);

-- Roster 3: DRAFT with NO assignments (for UAT 12 - empty roster)
INSERT INTO roster (branch_id, dostartofweek, doendofweek, rosterstatus_id, deleted) VALUES
    (1, '2026-03-16', '2026-03-22', 1, 0);

-- Roster 4: LOCKED with ALL CONFIRMED (for UAT 14 - archive success)
INSERT INTO roster (branch_id, dostartofweek, doendofweek, rosterstatus_id, deleted) VALUES
    (1, '2026-03-23', '2026-03-29', 2, 0);

-- Roster 5: DRAFT for branch with insufficient employees (for UAT 8)
INSERT INTO roster (branch_id, dostartofweek, doendofweek, rosterstatus_id, deleted) VALUES
    (2, '2026-03-30', '2026-04-05', 1, 0);

-- Roster 6: DRAFT for deletion test (for UAT 18)
INSERT INTO roster (branch_id, dostartofweek, doendofweek, rosterstatus_id, deleted) VALUES
    (1, '2026-02-16', '2026-04-12', 1, 0);

-- Assignments for Roster 1 (SUGGESTED)
INSERT INTO shiftrosterassignment (shift_id, roster_id, doassigned, role_id, employee_id, shiftrosterassignmentstatus_id) VALUES
                                                                                                                              (1, 1, '2026-03-02', 1, 1, 1), -- Morning Driver (for UAT 9 - approve)
                                                                                                                              (1, 1, '2026-03-02', 2, 9, 1), -- Morning Conductor (for UAT 10 - reject)
                                                                                                                              (2, 1, '2026-03-02', 1, 2, 1); -- Evening Driver

-- Assignments for Roster 2 (LOCKED, mixed)
INSERT INTO shiftrosterassignment (shift_id, roster_id, doassigned, role_id, employee_id, shiftrosterassignmentstatus_id) VALUES
                                                                                                                              (1, 2, '2026-03-09', 1, 1, 2), -- CONFIRMED
                                                                                                                              (1, 2, '2026-03-09', 2, 9, 1), -- SUGGESTED (for UAT 15 - blocks archival)
                                                                                                                              (2, 2, '2026-03-09', 1, 2, 2); -- CONFIRMED

-- Assignments for Roster 4 (ALL CONFIRMED for archival)
INSERT INTO shiftrosterassignment (shift_id, roster_id, doassigned, role_id, employee_id, shiftrosterassignmentstatus_id) VALUES
              (1, 4, '2026-02-16', 1, 1, 2), -- CONFIRMED
              (1, 4, '2026-03-23', 2, 9, 2); -- CONFIRME



INSERT INTO tripallocationstatus (name) VALUES
                                            ('Pending'),
                                            ('Suggested'),
                                            ('Confirmed'),
                                            ('Rejected');

