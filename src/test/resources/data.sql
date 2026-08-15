INSERT INTO `branchstatus` VALUES (1,'Active'),(2,'Suspended'),(3,'Closed');

INSERT INTO `branchtype` VALUES (1,'Central'),(2,'General'),(3,'Sub Depot'),(4,'Workshop Depot');

INSERT INTO `bustype` VALUES (1,'AA'),(2,'A+'),(3,'A'),(4,'B'),(5,'B+'),(6,'C'),(7,'D'),(8,'E');

INSERT INTO `codetype` VALUES (1,'EMPLOYEE'),(2,'DRIVER'),(3,'CONDUCTOR'),(4,'PART_REQUEST'),(5,'GRN'),(6,'VEHICLE_SERVICE'),(7,'BRANCH');

INSERT INTO `conditionrate` VALUES (1,'Excellent'),(2,'Good'),(3,'Fair'),(4,'Poor');

INSERT INTO `crewstatus` VALUES (1,'Eligible'),(2,'Ineligible'),(3,'Active'),(4,'Inactive');

INSERT INTO `department` VALUES (1,'Operations'),(2,'Engineering and Technical'),(3,'Administrative'),(4,'Finance and Revenue'),(5,'Stores Department');

INSERT INTO `designation` VALUES (1,'Driver'),(2,'Conductor'),(3,'Mechanic'),(4,'Depot Manager'),(5,'Assistant Manager'),(6,'Supervisory'),(7,'Clerical');

INSERT INTO `employeestatus` VALUES (1,'Active'),(2,'Suspend'),(3,'Resigned'),(4,'On leave');

INSERT INTO `employeetype` VALUES (1,'Permanent'),(2,'Contract'),(3,'Temporary'),(4,'Probationers'),(5,'Casual');

INSERT INTO `fueltype` VALUES (1,'Petrol'),(2,'Diesel');

INSERT INTO `gender` VALUES (1,'Male'),(2,'Female'),(3,'Other');

INSERT INTO `grnstatus` VALUES (1,'Draft'),(2,'Partially Received'),(3,'Received');

INSERT INTO `incidentstatus` VALUES (1,'Reported'),(2,'In Progress'),(3,'Vehicle Recovery'),(4,'Pending Allocation'),(5,'Resolved'),(6,'Closed');

INSERT INTO `incidenttype` VALUES (1,'Mechanical Breakdown'),(2,'Accident'),(3,'Tyre Puncture'),(4,'Medical '),(5,'Weather');

INSERT INTO `incidentvehicleallocationstatus` VALUES (1,'Assigned'),(2,'In Progress'),(3,'Released'),(4,'Cancelled');

INSERT INTO `licensecategory` VALUES (1,'D'),(2,'D1');

INSERT INTO `make` VALUES (1,'Ashok Leyland'),(2,'Tata'),(3,'Isuzu'),(4,'Mercedes-Benz'),(5,'Metro'),(6,'AEC'),(7,'Hino'),(8,'Mitsubishi'),(9,'Volvo'),(10,'Greatewall'),(11,'Youtong'),(12,'Kinlong');

INSERT INTO `model` VALUES (1,'Ashok Leyland 12M RE',1),(2,'Ashok Leyland Viking 193',1),(3,'Ashok Leyland Viking 210 Turbo',1),(4,'Ashok Leyland Comet Minior',1),(5,'Ashok Leyland Viking 222 Hinopower',1),(6,'Ashok Leyland Stag bus',1),(7,'TATA LP 1510/52',2),(8,'TATA LPO 1313/55',2),(9,'TATA LP 1210/36',2),(10,'TATA LP 1210/52',2),(11,'TATA LP 1510/36',2),(12,'TATA LPO 1313/47',2),(13,'TATA LP 909/36',2);

INSERT INTO `module` VALUES (1,'User'),(2,'Branch'),(3,'Employee'),(4,'Crew'),(5,'Vehicle'),(6,'Route'),(7,'Permit'),(8,'Trip'),(9,'Trip Execution'),(10,'Roster'),(11,'Incident'),(12,'Incident Vehicle Allocation'),(13,'Fare Collection'),(14,'Spare Part'),(15,'Spare Part Request'),(16,'GRN'),(17,'Vehicle Service'),(18,'Dashboard');

INSERT INTO `opcalender` VALUES (1,'Daily',_binary '',_binary '',_binary '',_binary '',_binary '',_binary '',_binary ''),(2,'Weekday Only',_binary '',_binary '',_binary '',_binary '',_binary '',_binary '\0',_binary '\0'),(3,'Weekend Only',_binary '\0',_binary '\0',_binary '\0',_binary '\0',_binary '\0',_binary '',_binary ''),(4,'Mon-Wed-Fri',_binary '',_binary '\0',_binary '',_binary '\0',_binary '',_binary '\0',_binary '\0'),(5,'Tue-Thu-Sat',_binary '\0',_binary '',_binary '\0',_binary '',_binary '\0',_binary '',_binary '\0'),(6,'Sunday Only',_binary '\0',_binary '\0',_binary '\0',_binary '\0',_binary '\0',_binary '\0',_binary '');

INSERT INTO `originterminal` VALUES (1,'pettah','Colombo'),(2,'Rajagiriya','Rajagiriya'),(3,'Kirindiwela','Kirindiwela'),(4,'Sigiriya','Sigiriya'),(5,'Gampaha','Gampaha');

INSERT INTO `partcategory` VALUES (1,'Engine Parts'),(2,'Brake System'),(3,'Electrical Components'),(4,'Suspension Parts'),(5,'Body Parts'),(6,'Lubricants'),(7,'Filters'),(8,'Transmission Parts');

INSERT INTO `partrequeststatus` VALUES (1,'Pending'),(2,'Approved'),(3,'Rejected'),(4,'Completed');

INSERT INTO `partstatus` VALUES (1,'Available'),(2,'Low stock'),(3,'Out of stock'),(4,'Decommissioned');

INSERT INTO `permitestatus` VALUES (1,'Active'),(2,'Expired'),(3,'Suspended'),(4,'Transferred');

INSERT INTO `regionaloffice` VALUES (1,'Colombo'),(2,'Eastern'),(3,'Gampaha'),(4,'Kalutara'),(5,'Kandy'),(6,'Northern'),(7,'Nuwara-Eliya'),(8,'Rajarata'),(9,'Sabaragamuwa'),(10,'Southern'),(11,'Uva'),(12,'Wayamba');

INSERT INTO `role` VALUES (1,'Depot Manager'),(2,'Operations Officer'),(3,'Maintenance Officer'),(4,'Inventory Officer');

INSERT INTO `rostershiftassignmentstatus` VALUES (1,'Draft'),(2,'Proposed'),(3,'Published'),(4,'Confirmed'),(5,'In-Progress'),(6,'Completed'),(7,'Canceled'),(8,'Absent');

INSERT INTO `routefamiliaritylevel` VALUES (1,'Low'),(2,'Medium'),(3,'High');

INSERT INTO `routetype` VALUES (1,'Inter provincial'),(2,'Intra provincial');

INSERT INTO `scope` VALUES (1,'GLOBAL'),(2,'CLM0001'),(3,'KND0001'),(4,'ANG0001');

INSERT INTO `servicetype` VALUES (1,'Normal'),(2,'Semi luxury'),(3,'Luxury'),(4,'Super luxury');

INSERT INTO `shiftstatus` VALUES (1,'Active'),(2,'Inactive');

INSERT INTO `shift` VALUES (1,'Morning Peak','04:00:00','12:00:00',1),(2,'Day Shift','08:00:00','16:00:00',1),(3,'Evening Peak','13:00:00','21:00:00',1),(4,'Night Shift','20:00:00','04:00:00',1);

INSERT INTO `tripexecutionstatus` VALUES (1,'Scheduled'),(2,'Checked In'),(3,'Dispatched'),(5,'Arrived'),(6,'Breakdown'),(8,'Cancelled'),(9,'Completed');

INSERT INTO `tripstatus` VALUES (1,'Draft'),(2,'Active'),(3,'Suspended'),(4,'Discontinued');

INSERT INTO `triptype` VALUES (1,'Daily'),(2,'Weekday'),(3,'Weekend'),(4,'Special'),(5,'Overnight');

INSERT INTO `unitofmeasure` VALUES (1,'Nos'),(2,'Liters'),(3,'Meters'),(4,'Kilograms'),(5,'Sets');

INSERT INTO `userstatus` VALUES (1,'Active'),(2,'Locked'),(3,'Inactive');

INSERT INTO `usertype` VALUES (1,'Internal'),(2,'External'),(3,'System');

INSERT INTO `vehicleservicepriority` VALUES (1,'Critical'),(2,'High'),(3,'Medium'),(4,'Low');

INSERT INTO `vehicleservicestatus` VALUES (1,'Pending'),(2,'In Progress'),(3,'On Hold Parts'),(4,'Complete'),(5,'Cancelled');

INSERT INTO `vehicleservicetype` VALUES (1,'Routing Preventive'),(2,'Breakdown Repair'),(3,'Accident Repair'),(4,'Engine Overhaul'),(5,'Anual Fitness');

INSERT INTO `vehiclestatus` VALUES (1,'Available'),(2,'Allocated'),(3,'In Operation'),(4,'Maintenance'),(5,'Breakdown'),(6,'Decommissioned');
