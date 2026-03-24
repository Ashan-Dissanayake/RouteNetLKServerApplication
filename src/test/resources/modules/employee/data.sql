SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE employeestatus;
TRUNCATE TABLE employeetype;
TRUNCATE TABLE designation;
TRUNCATE TABLE department;
TRUNCATE TABLE gender;
TRUNCATE TABLE employee;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- employeestatus
INSERT INTO employeestatus (name) VALUES
                                      ('Active'),('Suspend'),('Resigned'),('On leave');

-- gender
INSERT INTO gender (name) VALUES
                              ('Male'),('Female'),('Other');

-- department
INSERT INTO department (name) VALUES
                                  ('Operations'),('Engineering and Technical'),('Administrative'),
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
    ('EMP0001', 'Sunil Perera','Sunil','200012345678',1,'0712345678','sunil.emp0001@sltb.lk','No 12,kandy Rd,Colombo','0771234567',NULL,1,1,1,1,'2015-03-12',1,0),
    ('EMP0002', 'Kumari Fernando', 'Kumari', '857621345V', 2, '0758765432', 'kumari.emp0002@sltb.lk', 'No 45, Borella, Colombo 8', '0767895432', NULL, 1, 3, 2, 2, '2018-07-24', 1, 0),
    ('EMP0003', 'Rohan Jayasuriya', 'Rohan', '199179000000', 1, '0714456789', 'rohan.emp0003@sltb.lk', 'No 21, Angoda', '0775678945', NULL, 2, 1, 1, 1, '2016-09-10', 4, 0),
    ('EMP0004', 'Chaminda Ranasinghe', 'Chaminda', '871235678V', 1, '0777891234', 'chaminda.emp0004@sltb.lk', 'No 34, Avissawella', '0712349999', NULL, 3, 1, 1, 1, '2014-01-22', 1, 0),
    ('EMP0005', 'Harsha Abeykoon', 'Harsha', '199946000000', 1, '0701239876', 'harsha.emp0005@sltb.lk', 'No 11, Puwakpitiya, Avissawella', '0751112223', NULL, 3, 2, 2, 3, '2021-02-01', 3, 0),
    ('EMP0006', 'Mahesh Gunawardena', 'Mahesh', '803456789V', 1, '0719988776', 'mahesh.emp0006@sltb.lk', 'No 5, Borella, Colombo 8', '0709988776', NULL, 1, 3, 1, 1, '2011-01-11', 1, 0);
