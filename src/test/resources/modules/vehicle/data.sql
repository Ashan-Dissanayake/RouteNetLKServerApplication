-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE make;
TRUNCATE TABLE model;
TRUNCATE TABLE conditionrate;
TRUNCATE TABLE vehiclestatus;
TRUNCATE TABLE fueltype;
TRUNCATE TABLE bustype;

TRUNCATE TABLE vehicle;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

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
    ('ND-1217',1,6,10000,2, 2, NULL, 1, 0, 1),
    ('NE-1217',1,6,10000,2, 2, NULL, 1, 0, 1),
    ('NG-1110',1,6,10000,2, 2, NULL, 1, 0, 1),
    ('NB-5566', 1, 6, 12000, 2, 2, 'In Shop', 4, 0, 1);
