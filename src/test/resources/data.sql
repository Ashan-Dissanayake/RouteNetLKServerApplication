-- branchstatus
INSERT INTO branchstatus (name) VALUES ('Active'),('Suspended'),('Closed');

-- branchtype
INSERT INTO branchtype (name) VALUES ('Central'),('General'),('Sub Depot'),('Workshop Depot');

-- regionaloffice
INSERT INTO regionaloffice (name) VALUES
('Colombo'),('Eastern'),('Gampaha'),('Kalutara'),('Kandy'),('Northern'),('Nuwara-Eliya'),('Rajarata'),('Sabaragamuwa'),('Southern'),('Uva'),('Wayamba');

-- branch
INSERT INTO branch (name, code, address, telephone, email, docreated, branchtype_id, remarks, branchstatus_id,regionaloffice_id, deleted) VALUES
('Colombo head office', 'CLM0001', 'Kirula Rd, Colombo 00500', '117706320', 'clm@sltb.lk', '03-10-25', 1, '', 1, 1,0),
('Angoda', 'ANG0001', 'WWF7 2H4, Colombo', '117706321', 'ang@sltb.lk', '03-10-25', 3, '', 1,9, 0),
('Avissawella', 'AVS0001', 'X644 42W, Road, Avissawella', '362222348', 'avs@sltb.lk', '14-10-25', 2, '', 2,1, 0),
('Homagama', 'HMG0001', 'R2V6 9RR Bus Depot, Homagama', '117706330', 'hmg@sltb.lk', '14-10-25', 2, '', 3, 1,1),
('Kesbewa Deport', 'KSB0001', 'Piliyandala', '117706360', 'ksb@sltb.lk', '15-10-25', 3, '', 3, 1,1);


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
    ('EMPCLM0010', 'Ishara Wickramasinghe', 'Ishara', '956712345V', 2, '709988776', 'ishara.EMPCLM0010@sltb.lk', 'No 6, Piliyandala', '718887776', NULL, 1, 1, 2, 1, '2018-02-12', 4, 0),
    ('EMPANG0007', 'Ajith Abeyratne', 'Ajith', '200146000000', 1, '766678899', 'ajith.EMPANG0007@sltb.lk', 'No 88, Kesbewa South', '776677788', NULL, 2, 3, 5, 2, '2020-10-09', 1, 0),
    ('EMPAVS0007', 'Thilini Madushani', 'Thilini', '946578901V', 2, '757788990', 'thilini.EMPAVS0007@sltb.lk', 'No 11, Bandaragama', '725566778', NULL, 4, 4, 7, 3, '2021-03-25', 1, 0),
    ('EMPCLM0003', 'Mahesh Gunawardena', 'Mahesh', '803456789V', 1, '719988776', 'mahesh.EMPCLM0003@sltb.lk', 'No 5, Borella, Colombo 8', '709988776', NULL, 1, 3, 5, 1, '2011-01-11', 1, 0),
    ('EMPCLM0004', 'Nadeesha Abeywardena', 'Nadeesha', '199946000000', 2, '726677889', 'nadeesha.EMPCLM0004@sltb.lk', 'No 56, Dehiwala', '713344556', NULL, 1, 3, 6, 4, '2022-05-05', 4, 0),
    ('EMPCLM0005', 'Suresh Perera', 'Suresh', '198735000000', 1, '706677889', 'suresh.EMPCLM0005@sltb.lk', 'No 22, Mount Lavinia', '778899000', NULL, 1, 2, 3, 5, '2023-01-10', 1, 0),
    ('EMPCLM0006', 'Heshani Silva', 'Heshani', '976543210V', 2, '766677788', 'heshani.EMPCLM0006@sltb.lk', 'No 7, Rajagiriya', '751236789', NULL, 1, 4, 7, 3, '2019-11-18', 1, 0),
    ('EMPCLM0007', 'Dilshan Perera', 'Dilshan', '200335000000', 1, '771112334', 'dilshan.EMPCLM0007@sltb.lk', 'No 15, Maradana, Colombo 10', '712334556', NULL, 1, 1, 1, 1, '2022-05-22', 1, 0),
    ('EMPCLM0008', 'Jane Doe', 'Jane', '200124000000', 1, '772222222', 'jane.EMPCLM0008@sltb.lk', 'No 2, Colombo', '712345679', NULL, 1, 1, 1, 1, '2021-07-31', 1, 0),
    ('EMPCLM0009', 'Priya Silva', 'Priya', '200656000000', 2, '773333333', 'priya.EMPCLM0009@sltb.lk', 'No 4, Colombo', '712345681', NULL, 1, 3, 5, 1, '2011-07-31', 1, 0),
    ('EMPAVS0004', 'Nadeesha Perera', 'Nadeesha', '200557000000', 2, '701112233', 'nadeesha.EMPAVS0004@sltb.lk', 'No 40, Avissawella', '751223344', NULL, 4, 3, 5, 1, '2011-01-19', 1, 0),
    ('EMPAVS0008', 'Nishantha Peris Gunarathne', 'Nishantha', '199912000000', 1, '775551011', 'nishantha.EMPAVS0008@sltb.lk', 'Perera Rd, Negombo', '771438876', NULL, 4, 1, 2, 2, '2025-12-02', 1, 0);



INSERT INTO make ( name) VALUES
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
(' Ashok Leyland 12M RE', 1),
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
('Decommissioned');

INSERT INTO fueltype (name) VALUES
('Petrol'),
('Diesel');

INSERT INTO bustype(name) values ('AA'),('A+'),('A'),('B'),('B+'),('C'),('D'),('E');

INSERT INTO vehicle
(number,model_id,bustype_id,mileage, fueltype_id, conditionrate_id, remarks, vehiclestatus_id, deleted, branch_id)
VALUES
    ( 'ND-1217',1,1,120000,2, 4, NULL, 1, 0,1),
    ( 'ND-9167',1,6,180000,2, 4, NULL, 1, 0,2),
    ( 'NA-7845',2,1,200000,2, 3, NULL, 2, 0,2),
    ( 'ND-5623',2,1,80000,2, 4, NULL, 1, 0,3),
    ( 'NB-4392',3,1,220000,2, 2, NULL, 2, 1,4),
    ( 'NE-9981',2,1,50000, 2, 4, NULL, 1, 0,5),
    ( 'NB-3456',3,1,150000,2, 4, NULL, 2, 1,1),
    ( 'NA-1123',1,1,250000,2, 1, NULL, 3, 0, 2),
    ( 'NC-7784',3,1,130000,2, 4, NULL, 1, 0, 3),
    ( 'NA-3345',3,1,300000,2, 2, NULL, 3, 0, 3),
    ( 'ND-5566',5,1,90000,2, 4, NULL, 1, 1, 5),
    ( 'NC-8899',3,1,170000,2, 3, NULL, 2, 0, 5),
    ( 'ND-1290',5,3,7654, 2, 2, NULL, 1, 0, 3),
    ( 'ND-1299',9,4,7654,2, 3, NULL, 5, 1, 2);


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
-- Row 1
(1, 'DRV-2025-002', 'B12345678902',
 '2022-12-10', '2025-12-27',
 '2025-07-01', '2025-12-31',
 1, 1, 2),

-- Row 2
(3, 'DRV-2025-003', 'C12345678903',
 '2021-11-30', '2024-11-30',
 '2024-10-15', '2025-04-15',
 2, 1, 1),

-- Row 3
(4, 'DRV-2025-004', 'C12345678904',
 '2022-08-20', '2025-08-20',
 '2025-09-10', '2026-03-10',
 2, 1, 3),

-- Row 4
(5, 'DRV-2025-005', 'B12345678905',
 '2023-01-10', '2026-01-10',
 '2025-01-05', '2025-07-05',
 1, 1, 2);

INSERT INTO conductor (
    employee_id,
    number,
    domedicalissued,
    domedicalexpired,
    crewstatus_id,
    routefamiliaritylevel_id
)values
     (9,'CON-2025-001','2025-01-01','2025-06-30',3,1),
     (11,'CON-2025-002','2025-02-01','2025-07-31',2,2),
     (14,'CON-2025-004','2025-08-01','2026-02-01',3,1);


INSERT INTO routetype (name) VALUES ('Inter provincial'), ('Intra provincial');

INSERT INTO scheduletype (name) VALUES ('Normal'), ('Special');

INSERT INTO route (number,origin,destination,distancekm,scheduletype_id,routetype_id)VALUES
    ('4-7','Colombo','Puttalam',137.2,1,1),
    ('5','Colombo','Kurunegala',95.8,1,1),
    ('6','Colombo','Kurunegala',93.4,1,1),
    ('49','Colombo','Trincomalee',258.0	,1,1),
    ('92-4','Colombo','Kuliyapitiya',86.1,1,1),
    ('103-3','Pettah','Borella',	4.0,1,2),
    ('130','Fort','I.D.H.',10.5,1,2),
    ('130-1','Fort','Kolonnawa',7.3,1,2),
    ('152-1','Pettah','I.D.H.',9.8,1,2),
    ('175','Town Hall','Kohilawatte',11.0,1,2),
    ('175-1','Kollupitiya','I.D.H.',10.1,1,2),
    ('175-2','Town Hall','I.D.H.',7.9,1,2),
    ('175-3','Borella','I.D.H.',6.3,1,2),
    ('187-3','Colombo','Katunayake',35.1,1,2),
    ('200','Gampaha','Pettah',29.3,1,2),
    ('201-4','Yakkala','Gampaha',4.8,1,2),
    ('240','Colombo','Negombo',36.1,1,2),
    ('896','Trincomalee','Hot Wells',10.0,1,2);

INSERT INTO permitestatus (name) values ('Active'),('Expired'),('Suspended'),('Transferred');

INSERT INTO servicetype (name) values ('Normal'),('Semi luxury'),('Luxury'),('Super luxury');

INSERT INTO permite(number,vehicle_id,doissued,doexpired,branch_id,permitestatus_id,servicetype_id,route_id,deleted) VALUES
    ('2696',2,'2003-01-13','2027-07-24',2,1,1,1,0),
    ('ANG-NA7845-103-3',3,'2002-05-01','2026-05-12',2,1,1,6,0);



