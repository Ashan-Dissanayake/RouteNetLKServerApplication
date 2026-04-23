-- Initial Master Data [cite: 24-28]
INSERT INTO branchstatus (id, name) VALUES (1, 'Active');
INSERT INTO branchtype (id, name) VALUES (1, 'Central');
INSERT INTO regionaloffice (id, name) VALUES (1, 'Colombo');
INSERT INTO branch (id, name, code, docreated, branchtype_id, branchstatus_id, regionaloffice_id, deleted)
VALUES (1, 'Colombo Head Office', 'CLM0001', '2025-10-03', 1, 1, 1, 0);

INSERT INTO designation (id, name) VALUES (1, 'Driver'), (2, 'Conductor');
INSERT INTO employeestatus (id, name) VALUES (1, 'Active');
INSERT INTO employeetype (id, name) VALUES (1, 'Permanent');
INSERT INTO gender (id, name) VALUES (1, 'Male');
INSERT INTO department (id, name) VALUES (1, 'Operations');

-- Employee & Roster Setup [cite: 29-30]
INSERT INTO employee (id, number, fullname, callingname, nic, gender_id, mobile, email, address, emergencycontact, branch_id, department_id, designation_id, employeetype_id, doj, employeestatus_id, deleted)
VALUES (1, 'EMP0001', 'Sunil Perera', 'Sunil', '200012345678', 1, '0712345678', 'sunil@sltb.lk', 'Kandy Rd', '0771234567', 1, 1, 1, 1, '2015-03-12', 1, 0);

INSERT INTO rostershiftassignmentstatus (id, name) VALUES (1, 'Available');
INSERT INTO rostershiftassignment (id, rostershift_id, employee_id, rostershiftassignmentstatus_id) VALUES (101, 1, 1, 1);

-- Trip & Status
INSERT INTO tripstatus (id, name) VALUES (1, 'Ready');
INSERT INTO tripcrewallocationstatus (id, name) VALUES (1, 'Proposed'), (2, 'Allocated'), (3, 'Confirmed'), (4, 'Rejected');

INSERT INTO trip (id, branch_id, triptype_id, permite_id, tripstatus_id, originterminal_id, doservice)
VALUES (50, 1, 1, 1, 1, 1, '2026-04-21');
