-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate the table to remove old data and reset AUTO_INCREMENT
TRUNCATE TABLE partrequeststatus;
TRUNCATE TABLE partrequest;
TRUNCATE TABLE partrequestitem;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO partrequeststatus (name) VALUES
     ('Pending'),
     ('Approved'),
     ('Rejected'),
     ('Completed');

INSERT INTO partrequest
(branch_id, number, dorequested, partrequeststatus_id, remarks)
VALUES
    (1, 'PR-CLM0001-202603-0001', '2026-03-01', 1, 'Routine maintenance parts'),
    (1, 'PR-CLM0001-202603-0002', '2026-03-02', 2, 'Urgent brake replacement'),
    (2, 'PR-ANG0001-202603-0001', '2026-03-03', 3, 'Invalid quantity request'),
    (1, 'PR-CLM0001-202603-0003', '2026-03-04', 4, 'Stock replenishment completed');

-- Approved PO
INSERT INTO partrequest (branch_id, number, dorequested, partrequeststatus_id, remarks)
VALUES (1, 'PR-CLM0001-202604-0001', '2026-04-01', 2, 'Partial Receipt Testing');


INSERT INTO partrequestitem
(partrequest_id, part_id, quantity)
VALUES

-- PR-001 Normal pending
(1, 1, 5),
(1, 3, 2),

-- PR-002 Approved
(2, 3, 10),

-- PR-003 Rejected
(3, 5, 100),

-- PR-004 Completed
(4, 1, 8),
(4, 5, 4);


INSERT INTO partrequestitem (partrequest_id, part_id, quantity)
VALUES (5, 6, 20);



#     (4, 'PR-CLM0002-202603-0001', '2026-03-05', 1, 'Monthly service requirement'),
#     (1, 'PR-CLM0001-202603-0004', '2026-03-06', 2, 'Bulk order for depot'),
#     (2, 'PR-ANG0001-202603-0002', '2026-03-07', 1, 'Test deleted record');

-- PR-005 Multiple items
# (5, 1, 6),
# (5, 3, 3),
# (5, 5, 12),

-- PR-006 Large quantity
# (6, 1, 50),

-- PR-007 Deleted parent request
# (7, 3, 1);
