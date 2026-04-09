-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE grnstatus;
TRUNCATE TABLE grn;
TRUNCATE TABLE grnpartrequestitem;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

insert into grnstatus (name) values ('Draft'),('Partially Received'),('Received');

-- 1. Insert GRN Headers (Status: 1=Draft, 2=Partially Received, 3=Received)

-- Draft GRN for Branch 1
INSERT INTO grn (branch_id, partrequest_id, number, doreceived, remarks, grnstatus_id)
VALUES (1, 2, 'GRN-CLM0001-202604-0001', '2026-04-05', 'Initial delivery draft', 1);

-- Partially Received GRN for Branch 1
INSERT INTO grn (branch_id, partrequest_id, number, doreceived, remarks, grnstatus_id)
VALUES (1, 2, 'GRN-CLM0001-202604-0002', '2026-04-06', 'Half shipment received', 2);

-- Fully Received GRN for Branch 2
INSERT INTO grn (branch_id, partrequest_id, number, doreceived, remarks, grnstatus_id)
VALUES (2, 3, 'GRN-ANG0001-202604-0001', '2026-04-07', 'Urgent parts arrived', 3);


-- 2. Insert GRN Items (Linking to PartRequestItem IDs from your previous data)

-- Items for GRN 0001 (Draft)
INSERT INTO grnpartrequestitem (grn_id, partrequestitem_id, quantity)
VALUES (1, 3, 10.000); -- Expected 10 for PR 2

-- Items for GRN 0002 (Partially Received)
INSERT INTO grnpartrequestitem (grn_id, partrequestitem_id, quantity)
VALUES (2, 3, 4.000); -- Received 4 out of 10

-- Items for GRN 0001 - Branch 2 (Fully Received)
INSERT INTO grnpartrequestitem (grn_id, partrequestitem_id, quantity)
VALUES (3, 4, 100.000); -- Received full amount
