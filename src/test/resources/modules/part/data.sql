-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE partcategory;
TRUNCATE TABLE partstatus;
TRUNCATE TABLE unitofmeasure;
TRUNCATE TABLE partmaster;
TRUNCATE TABLE part;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO partcategory (name) VALUES ('Engine Parts'),('Brake System'),('Electrical Components'), ('Suspension Parts'),
        ('Body Parts'),('Lubricants'), ('Filters'), ('Transmission Parts');

INSERT INTO partstatus (name) VALUES ('Available'),('Low stock'),('Out of stock'), ('Decommissioned');

INSERT INTO unitofmeasure (name) VALUES ('Nos'), ('Liters'),('Meters'),('Kilograms'),('Sets');

INSERT INTO partmaster (sku, name, partcategory_id, unitofmeasure_id) VALUES
           ('ENG-001','Engine Oil Filter',1,1),
           ('ENG-002','Fan Belt',1,1),
           ('BRK-001','Brake Pad Set',2,5),
           ('BRK-002','Brake Drum',2,1),
           ('ELE-001','Head Light Bulb',3,1),
           ('ELE-002','Battery 12V',3,1),
           ('SUS-001','Leaf Spring',4,1),
           ('LUB-001','Engine Oil 15W40',6,2),
           ('FIL-001','Air Filter',7,1),
           ('FIL-002','Fuel Filter',7,1);

INSERT INTO part
(branch_id, partmaster_id,qoh,maxlevel,rop,dolastordered, partstatus_id, remarks, deleted)
VALUES

-- 1 Normal stock (baseline update success)
(1,1,45,100,30,'2026-02-10',1,'Ashok Leyland buses',0),

-- 2 Normal stock (different part)
(1,3,25,80,20,'2026-01-20',1,'Front brake pads',0),

-- 3 High stock scenario
(1,5,75,150,50,'2026-02-05',1,'24V bus headlight',0),

-- 4 Low quantity but still AVAILABLE
(1,7,6,20,5,'2026-02-01',1,'Rear suspension',0),

-- 5 Large stock item
(1,8,200,500,150,'2026-02-12',1,'Diesel engine oil',0),

-- 6 Different branch record
(2,2,28,90,20,'2026-02-08',1,'Fan Belt - Branch 2',0),

-- 7 LOW_STOCK scenario (QOH < ROP)
(1,1,18,100,20,'2026-02-10',2,'Low stock test item',0),

-- 8 OUT_OF_STOCK scenario (QOH = 0)
(1,3,0,80,20,'2026-02-10',3,'Out of stock test item',0),

-- 9 DECOMMISSIONED scenario
(1,5,10,50,20,'2026-02-10',4,'Decommissioned test item',0),

-- 10 Deleted record (soft delete validation)
(1,7,12,40,10,'2026-02-10',1,'Deleted test item',1),

-- 11 Boundary condition (maxlevel == qoh)
(1,8,50,50,20,'2026-02-10',1,'Boundary stock test',0);

# INSERT INTO part (branch_id, partmaster_id, qoh, maxlevel, rop, dolastordered, partstatus_id, remarks, deleted) VALUES
#              (1,1,45,100,30,'2026-02-10',1,'Ashok Leyland buses',0),
#              (1,3,25,80,20,'2026-01-20',1,'Front brake pads',0),
#              (1,5,75,150,50,'2026-02-05',1,'24V bus headlight',0),
#              (1,7,6,20,5,'2026-02-01',1,'Rear suspension',0),
#              (1,8,200,500,150,'2026-02-12',1,'Diesel engine oil',0),
#              (2,2,28,90,20,'2026-02-08',1,'Fan Belt',0);


#              (1,2,60,120,40,'2026-02-15',1,'Standard fan belt',0),
#              (1,4,12,40,10,'2026-01-18',1,'Rear brake drum',0),
#              (1,6,10,25,8,'2026-01-25',1,'Bus starting battery',0),
#              (1,9,35,100,25,'2026-02-03',1,'Engine air filter',0),
