-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE routetype;
TRUNCATE TABLE scheduletype;
TRUNCATE TABLE route;
TRUNCATE TABLE servicetype;
TRUNCATE TABLE permitestatus;
TRUNCATE TABLE permite;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO routetype (name) VALUES ('Inter provincial'), ('Intra provincial');

INSERT INTO scheduletype (name) VALUES ('Normal'), ('Special');

INSERT INTO route (number,origin,destination,distancekm,scheduletype_id,routetype_id,mingapminutes)VALUES
               ('4-7','Colombo','Puttalam',137.2,1,1,30),
               ('5','Colombo','Kurunegala',95.8,1,1,30),
               ('201-4','Yakkala','Gampaha',4.8,1,2,15),
               ('240','Colombo','Negombo',36.1,1,2,30),
               ('896','Trincomalee','Hot Wells',10.0,1,2,20);

INSERT INTO permitestatus (name) values ('Active'),('Expired'),('Suspended'),('Transferred');

INSERT INTO servicetype (name) values ('Normal'),('Semi luxury'),('Luxury'),('Super luxury');

INSERT INTO permite(number,vehicle_id,doissued,doexpired,branch_id,permitestatus_id,servicetype_id,route_id,deleted) VALUES
            ('2696',1,'2003-01-13','2027-07-24', 1,1,1,1,0),
            ('ANG-NA7845-103-3',2,'2002-05-01','2026-05-12', 1,1,1,4,0);
#             ('1865',3,'2002-05-01','2026-05-12', 1,1,1,2,0);
