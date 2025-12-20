-- branchstatus
INSERT INTO branchstatus (name) VALUES ('Active'),('Suspended'),('Closed');

-- branchtype
INSERT INTO branchtype (name) VALUES ('Head'),('Region'),('Local Sub Depot'),('Workshop Depot');

-- province
INSERT INTO province (name) VALUES
('Western'),('Central'),('Southern'),('Northern'),('Eastern'),
('North Western'),('North Central'),('Uva'),('Sabaragamuwa');

-- district
INSERT INTO district (name, province_id) VALUES
('Colombo', 1),('Gampaha', 1),('Kalutara', 1),('Kandy', 2),('Matale', 2),
('Nuwara Eliya', 2),('Galle', 3),('Matara', 3),('Hambantota', 3),('Jaffna', 4),
('Kilinochchi', 4),('Mullaitivu', 4),('Vavuniya', 4),('Mannar', 4),('Trincomalee', 5),
('Batticaloa', 5),('Ampara', 5),('Kurunegala', 6),('Puttalam', 6),('Anuradhapura', 7),
('Polonnaruwa', 7),('Badulla', 8),('Monaragala', 8),('Ratnapura', 9),('Kegalle', 9);

-- branch
INSERT INTO branch (name, code, address, telephone, email, docreated, branchtype_id, remarks, branchstatus_id, deleted) VALUES
('Colombo head office', 'CLM0001', 'Kirula Rd, Colombo 00500', '117706320', 'clm@sltb.lk', '03-10-25', 1, '', 1, 0),
('Angoda', 'ANG0001', 'WWF7 2H4, Colombo', '117706321', 'ang@sltb.lk', '03-10-25', 3, '', 1, 0),
('Avissawella', 'AVS0001', 'X644 42W, Road, Avissawella', '362222348', 'avs@sltb.lk', '14-10-25', 2, '', 2, 0),
('Homagama', 'HMG0001', 'R2V6 9RR Bus Depot, Homagama', '117706330', 'hmg@sltb.lk', '14-10-25', 2, '', 3, 1),
('Kesbewa Deport', 'KSB0001', 'Piliyandala', '117706360', 'ksb@sltb.lk', '15-10-25', 3, '', 3, 1);

-- branchcoverage
INSERT INTO branchcoverage (branch_id, district_id) VALUES
(2, 3),(2, 4),(2, 1),(4, 20),(4, 22),(4, 10),(4, 8),(4, 6),(4, 1),(4, 2),
(4, 3),(4, 24),(5, 1),(5, 2),(5, 3),(3, 1),(3, 2),(3, 3),(3, 4),(3, 5),
(3, 18),(3, 25),(3, 24),(3, 22),(3, 20),(1, 1),(1, 2),(1, 3),(1, 4),(1, 5),
(1, 6),(1, 7),(1, 8),(1, 9),(1, 10),(1, 11),(1, 12),(1, 13),(1, 14),(1, 15),
(1, 16),(1, 17),(1, 18),(1, 19),(1, 20),(1, 21),(1, 22),(1, 23),(1, 24),(1, 25);


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

INSERT INTO make ( name, airconditioned) VALUES
('Ashok Leyland Viking 193', 0),
('Ashok Leyland Viking 210 Turbo', 0),
('Ashok Leyland Viking 222', 0),
('Ashok Leyland Lynx', 0),
('Tata LP 12.10/42', 0),
('Tata LP 15.10/52', 0),
('Isuzu BF50', 0),
('Isuzu MT 111L', 0),
('Isuzu ELR500', 0),
('Hino (repowered with Ashok Leyland engine)', 0),
('Mitsubishi UMP', 0),
('Leyland Tiger TL 11', 0),
('Leyland MCW double decker', 0),
('Volvo B7RLE', 1),
('Fiat 642', 0);

INSERT INTO seatingcapacity (amount, make_id) VALUES
(42, 1),
(44, 2),
(49, 1),
(54, 2),
(58, 3),
(42, 14);

INSERT INTO servicetype (name) VALUES
('Passenger'),
('Recovery');

INSERT INTO conditionrate (name) VALUES
('Excellent'),
('Good'),
('Fair'),
('Poor'),
('Critical');

INSERT INTO vehiclestatus (name) VALUES
('Available'),
('In Service'),
('Under Maintenance'),
('Out of Service'),
('Decommissioned'),
('Reserved');

INSERT INTO fueltype (name) VALUES
('Petrol'),
('Diesel');

INSERT INTO vehicle
(code, number, yom, dob, mileage, chasisnumber, enginenumber, fueltype_id, conditionrate_id, remarks, servicetype_id, vehiclestatus_id, deleted, employee_id, branch_id, seatingcapacity_id)
VALUES
    ('BS-ALV00001', 'ND-1217', 2018, '2018-01-15', 120000, 'KLWT712345ABC6789', 'VLK193A1B2C3', 2, 5, NULL, 1, 1, 0, 1, 1, 1),
    ('BS-ALV00002', 'ND-9167', 2016, '2016-03-10', 180000, 'JLY/AB12345', 'ABC123XYZ78K', 2, 4, NULL, 1, 1, 0, 2, 2, 1),
    ('BS-ALV00003', 'NA-7845', 2015, '2015-07-22', 200000, 'MHX98765CD4321', 'AB123CD.123456', 1, 3, NULL, 1, 2, 0, 3, 2, 2),
    ('BS-ALV00004', 'ND-5623', 2019, '2019-05-05', 80000, 'TBN123456EF', 'ASH123B4C5D6', 2, 5, NULL, 1, 1, 0, 4, 3, 2),
    ('BS-ALV00005', 'NB-4392', 2014, '2014-11-30', 220000, 'KLX/56789GH12', 'LMN456PQR12X', 1, 2, NULL, 1, 2, 1, 5, 4, 3),
    ('BS-ALV00006', 'NE-9981', 2020, '2020-02-20', 50000, 'MNB34567JK890', 'XY456ZT.654321', 2, 5, NULL, 1, 1, 0, 6, 5, 2),
    ('BS-ALV00007', 'NB-3456', 2017, '2017-09-15', 150000, 'JKL/23456AB78', 'SLB987X6Y7Z8', 1, 4, NULL, 1, 2, 1, 6, 1, 2),
    ('BS-ALV00008', 'NA-1123', 2013, '2013-06-10', 250000, 'HJK98765CD3210', 'XYZ789JKL45M', 2, 1, NULL, 1, 3, 0, 8, 2, 2),
    ('BS-ALV00009', 'NC-7784', 2018, '2018-12-01', 130000, 'QWE/34567FG89', 'PQ321RS.111222', 1, 4, NULL, 1, 1, 0, 9, 3, 3),
    ('BS-ALV00010', 'NA-3345', 2012, '2012-03-25', 300000, 'ASD67890HJ123', 'VIK456M7N8O9', 2, 2, NULL, 1, 3, 0, 10, 3, 3),
    ('BS-ALV00011', 'ND-5566', 2019, '2019-08-18', 90000, 'ZXC/12345KL67', 'DEF321GHY67P', 1, 5, NULL, 1, 1, 1, 1, 5, 1),
    ('BS-ALV00012', 'NC-8899', 2016, '2016-10-05', 170000, 'RTY98765MN432', 'CD654EF.333444', 2, 3, NULL, 1, 2, 0, 2, 5, 4),
    ('BS-VLV00014', 'ND-1290', 2017, '2017-10-13', 7654, 'YV3BE7RLEAB012344', 'D7E290L0001I', 2, 2, NULL, 1, 1, 0, 5, 3, 1),
    ('BS-VLV00013', 'ND-1299', 2017, '2017-10-03', 7654, 'YV3BE7RLEAB012345', 'D7E290L0001A', 2, 3, NULL, 1, 5, 1, 6, 2, 6);

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

INSERT INTO allowedbustype (name) VALUES
('Normal'),
('Semi Luxury'),
('Luxury'),
('Super Luxury');

INSERT INTO driver (employee_id, number, licensenumber, dolicenseissued, dolicenseexpired, domedicalissued, domedicalexpired, licensecategory_id, crewstatus_id, routefamiliaritylevel_id) VALUES
(2, 'DRV-2025-002', 'B12345678902', '2015-03-15', '2025-03-15', '2024-02-28', '2025-02-28', 1, 2, 2),
(3, 'DRV-2025-003', 'C12345678903', '2014-11-30', '2024-11-30', '2023-10-15', '2024-10-15', 2, 3, 1),
(4, 'DRV-2025-004', 'C12345678904', '2018-08-20', '2028-08-20', '2026-09-10', '2027-09-10', 2, 1, 3),
(5, 'DRV-2025-005', 'B12345678905', '2016-01-10', '2026-01-10', '2025-01-05', '2026-01-05', 1, 1, 2);

INSERT INTO licensecategoryallowedbustype (licensecategory_id, allowedbustype_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4);

