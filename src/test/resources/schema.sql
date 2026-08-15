
CREATE TABLE `branchstatus` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `name` varchar(255) DEFAULT NULL,
                                PRIMARY KEY (`id`)
);

CREATE TABLE `branchtype` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `regionaloffice` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `name` varchar(45) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
);

CREATE TABLE `branch` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) DEFAULT NULL,
                          `code` varchar(255) DEFAULT NULL,
                          `address` varchar(255) DEFAULT NULL,
                          `telephone` varchar(255) DEFAULT NULL,
                          `email` varchar(255) DEFAULT NULL,
                          `docreated` date NOT NULL,
                          `branchtype_id` int NOT NULL,
                          `remarks` varchar(255) DEFAULT NULL,
                          `branchstatus_id` int NOT NULL,
                          `deleted` bit(1) DEFAULT NULL,
                          `regionaloffice_id` int NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `fk_bramch_branchstatus1_idx` (`branchstatus_id`),
                          KEY `fk_branch_branchtype1_idx` (`branchtype_id`),
                          KEY `fk_branch_regionaloffice1_idx` (`regionaloffice_id`),
                          CONSTRAINT `fk_bramch_branchstatus1` FOREIGN KEY (`branchstatus_id`) REFERENCES `branchstatus` (`id`),
                          CONSTRAINT `fk_branch_branchtype1` FOREIGN KEY (`branchtype_id`) REFERENCES `branchtype` (`id`),
                          CONSTRAINT `fk_branch_regionaloffice1` FOREIGN KEY (`regionaloffice_id`) REFERENCES `regionaloffice` (`id`)
);

CREATE TABLE `bustype` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `name` varchar(45) DEFAULT NULL,
                           PRIMARY KEY (`id`)
);

CREATE TABLE `codetype` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(45) DEFAULT NULL,
                            PRIMARY KEY (`id`)
);

CREATE TABLE `conditionrate` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `name` varchar(45) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
);

CREATE TABLE `crewstatus` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(45) DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `department` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(45) DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `designation` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `name` varchar(45) DEFAULT NULL,
                               PRIMARY KEY (`id`)
);

CREATE TABLE `scope` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(45) DEFAULT NULL,
                         PRIMARY KEY (`id`)
);

CREATE TABLE `docsequance` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `lastvalue` int DEFAULT NULL,
                               `version` int DEFAULT NULL,
                               `periodkey` varchar(45) DEFAULT NULL,
                               `codetype_id` int NOT NULL,
                               `scope_id` int NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `fk_docsequance_codetype1_idx` (`codetype_id`),
                               KEY `fk_docsequance_scope1_idx` (`scope_id`),
                               CONSTRAINT `fk_docsequance_codetype1` FOREIGN KEY (`codetype_id`) REFERENCES `codetype` (`id`),
                               CONSTRAINT `fk_docsequance_scope1` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`id`)
);

CREATE TABLE `employeestatus` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `name` varchar(45) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
);

CREATE TABLE `employeetype` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `name` varchar(45) DEFAULT NULL,
                                PRIMARY KEY (`id`)
);

CREATE TABLE `gender` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(45) DEFAULT NULL,
                          PRIMARY KEY (`id`)
);

CREATE TABLE `employee` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `number` char(10) NOT NULL,
                            `fullname` varchar(45) NOT NULL,
                            `callingname` varchar(45) NOT NULL,
                            `nic` char(12) NOT NULL,
                            `gender_id` int NOT NULL,
                            `mobile` char(10) NOT NULL,
                            `email` varchar(45) NOT NULL,
                            `address` varchar(45) NOT NULL,
                            `emergencycontact` char(10) NOT NULL,
                            `image` longblob,
                            `branch_id` int NOT NULL,
                            `department_id` int NOT NULL,
                            `designation_id` int NOT NULL,
                            `employeetype_id` int NOT NULL,
                            `doj` date NOT NULL,
                            `employeestatus_id` int NOT NULL,
                            `deleted` bit(1) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `number_UNIQUE` (`number`),
                            UNIQUE KEY `nic_UNIQUE` (`nic`),
                            UNIQUE KEY `mobile_UNIQUE` (`mobile`),
                            UNIQUE KEY `email_UNIQUE` (`email`),
                            KEY `fk_employee_gender1_idx` (`gender_id`),
                            KEY `fk_employee_employeetype1_idx` (`employeetype_id`),
                            KEY `fk_employee_designation1_idx` (`designation_id`),
                            KEY `fk_employee_employeestatus1_idx` (`employeestatus_id`),
                            KEY `fk_employee_department1_idx` (`department_id`),
                            KEY `fk_employee_branch1_idx` (`branch_id`),
                            CONSTRAINT `fk_employee_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                            CONSTRAINT `fk_employee_department1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
                            CONSTRAINT `fk_employee_designation1` FOREIGN KEY (`designation_id`) REFERENCES `designation` (`id`),
                            CONSTRAINT `fk_employee_employeestatus1` FOREIGN KEY (`employeestatus_id`) REFERENCES `employeestatus` (`id`),
                            CONSTRAINT `fk_employee_employeetype1` FOREIGN KEY (`employeetype_id`) REFERENCES `employeetype` (`id`),
                            CONSTRAINT `fk_employee_gender1` FOREIGN KEY (`gender_id`) REFERENCES `gender` (`id`)
);

CREATE TABLE `licensecategory` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `name` varchar(45) DEFAULT NULL,
                                   PRIMARY KEY (`id`)
);

CREATE TABLE `routefamiliaritylevel` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `name` varchar(45) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
);

CREATE TABLE `userstatus` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(45) DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `usertype` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(45) DEFAULT NULL,
                            PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `employee_id` int NOT NULL,
                        `username` varchar(45) DEFAULT NULL,
                        `password` varchar(45) DEFAULT NULL,
                        `usertype_id` int NOT NULL,
                        `userstatus_id` int NOT NULL,
                        `accountlocked` bit(1) DEFAULT NULL,
                        `recoverycode` varchar(45) DEFAULT NULL,
                        `recoverycodeexpiration` timestamp NULL DEFAULT NULL,
                        `recoverycodeused` bit(1) DEFAULT NULL,
                        `remarks` text,
                        PRIMARY KEY (`id`),
                        KEY `fk_user_employee1_idx` (`employee_id`),
                        KEY `fk_user_usertype1_idx` (`usertype_id`),
                        KEY `fk_user_userstatus1_idx` (`userstatus_id`),
                        CONSTRAINT `fk_user_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
                        CONSTRAINT `fk_user_userstatus1` FOREIGN KEY (`userstatus_id`) REFERENCES `userstatus` (`id`),
                        CONSTRAINT `fk_user_usertype1` FOREIGN KEY (`usertype_id`) REFERENCES `usertype` (`id`)
);

CREATE TABLE `conductor` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `employee_id` int DEFAULT NULL,
                             `number` char(12) NOT NULL,
                             `domedicalissued` date DEFAULT NULL,
                             `domedicalexpired` date DEFAULT NULL,
                             `routefamiliaritylevel_id` int NOT NULL,
                             `crewstatus_id` int NOT NULL,
                             `totaldutyminute` int DEFAULT NULL,
                             `user_id` int DEFAULT NULL,
                             `branch_id` INT NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `number_UNIQUE` (`number`),
                             UNIQUE KEY `employee_id_UNIQUE` (`employee_id`),
                             KEY `fk_conductor_routefamiliaritylevel1_idx` (`routefamiliaritylevel_id`),
                             KEY `fk_conductor_crewstatus1_idx` (`crewstatus_id`),
                             KEY `fk_conductor_employee1_idx` (`employee_id`),
                             KEY `fk_conductor_user1_idx` (`user_id`),
                             KEY `fk_conductor_branch1_idx` (`branch_id`),
                             CONSTRAINT `fk_conductor_crewstatus1` FOREIGN KEY (`crewstatus_id`) REFERENCES `crewstatus` (`id`),
                             CONSTRAINT `fk_conductor_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
                             CONSTRAINT `fk_conductor_routefamiliaritylevel1` FOREIGN KEY (`routefamiliaritylevel_id`) REFERENCES `routefamiliaritylevel` (`id`),
                             CONSTRAINT `fk_conductor_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                             CONSTRAINT `fk_conductor_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`)
);

CREATE TABLE `driver` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `employee_id` int DEFAULT NULL,
                          `number` char(12) NOT NULL,
                          `licensenumber` char(12) NOT NULL,
                          `dolicenseissued` date DEFAULT NULL,
                          `dolicenseexpired` date DEFAULT NULL,
                          `domedicalissued` date DEFAULT NULL,
                          `domedicalexpired` date DEFAULT NULL,
                          `licensecategory_id` int NOT NULL,
                          `crewstatus_id` int NOT NULL,
                          `routefamiliaritylevel_id` int NOT NULL,
                          `totaldutyminute` int DEFAULT NULL,
                          `user_id` int DEFAULT NULL,
                          `branch_id` int DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `number_UNIQUE` (`number`),
                          UNIQUE KEY `licensenumber_UNIQUE` (`licensenumber`),
                          UNIQUE KEY `employee_id_UNIQUE` (`employee_id`),
                          KEY `fk_driver_licensecategory1_idx` (`licensecategory_id`),
                          KEY `fk_driver_crewstatus1_idx` (`crewstatus_id`),
                          KEY `fk_driver_employee1_idx` (`employee_id`),
                          KEY `fk_driver_routefamiliaritylevel1_idx` (`routefamiliaritylevel_id`),
                          KEY `fk_driver_user1_idx` (`user_id`),
                          KEY `fk_driver_branch1_idx` (`branch_id`),
                          CONSTRAINT `fk_driver_crewstatus1` FOREIGN KEY (`crewstatus_id`) REFERENCES `crewstatus` (`id`),
                          CONSTRAINT `fk_driver_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
                          CONSTRAINT `fk_driver_licensecategory1` FOREIGN KEY (`licensecategory_id`) REFERENCES `licensecategory` (`id`),
                          CONSTRAINT `fk_driver_routefamiliaritylevel1` FOREIGN KEY (`routefamiliaritylevel_id`) REFERENCES `routefamiliaritylevel` (`id`),
                          CONSTRAINT `fk_driver_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                          CONSTRAINT `fk_driver_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`)

);

CREATE TABLE `fueltype` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(45) DEFAULT NULL,
                            PRIMARY KEY (`id`)
);

CREATE TABLE `grnstatus` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `name` varchar(45) DEFAULT NULL,
                             PRIMARY KEY (`id`)
);

CREATE TABLE `incidentstatus` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `name` varchar(45) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
);

CREATE TABLE `incidenttype` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `name` varchar(45) DEFAULT NULL,
                                PRIMARY KEY (`id`)
);

CREATE TABLE `incidentvehicleallocationstatus` (
                                                   `id` int NOT NULL AUTO_INCREMENT,
                                                   `name` varchar(45) DEFAULT NULL,
                                                   PRIMARY KEY (`id`)
);

CREATE TABLE `make` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(45) DEFAULT NULL,
                        PRIMARY KEY (`id`)
);

CREATE TABLE `model` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(45) DEFAULT NULL,
                         `make_id` int NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `fk_model_make1_idx` (`make_id`),
                         CONSTRAINT `fk_model_make1` FOREIGN KEY (`make_id`) REFERENCES `make` (`id`)
);

CREATE TABLE `module` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(45) DEFAULT NULL,
                          PRIMARY KEY (`id`)
);

CREATE TABLE `opcalender` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(45) DEFAULT NULL,
                              `mon` bit(1) DEFAULT NULL,
                              `tue` bit(1) DEFAULT NULL,
                              `wed` bit(1) DEFAULT NULL,
                              `thu` bit(1) DEFAULT NULL,
                              `fri` bit(1) DEFAULT NULL,
                              `sat` bit(1) DEFAULT NULL,
                              `sun` bit(1) DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `operation` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `displayname` varchar(45) DEFAULT NULL,
                             `operation` varchar(45) DEFAULT NULL,
                             `module_id` int DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             KEY `fk_operation_module1_idx` (`module_id`),
                             CONSTRAINT `fk_operation_module1` FOREIGN KEY (`module_id`) REFERENCES `module` (`id`)
);

CREATE TABLE `originterminal` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `name` varchar(45) DEFAULT NULL,
                                  `city` varchar(45) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
);

CREATE TABLE `partcategory` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `name` varchar(45) DEFAULT NULL,
                                PRIMARY KEY (`id`)
);

CREATE TABLE `partrequeststatus` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `name` varchar(45) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
);

CREATE TABLE `partstatus` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(45) DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `permitestatus` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `name` varchar(45) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
);

CREATE TABLE `routetype` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `name` varchar(45) DEFAULT NULL,
                             PRIMARY KEY (`id`)
);

CREATE TABLE `servicetype` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `name` varchar(45) DEFAULT NULL,
                               PRIMARY KEY (`id`)
);

CREATE TABLE `shiftstatus` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `name` varchar(45) DEFAULT NULL,
                               PRIMARY KEY (`id`)
);

CREATE TABLE `shift` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(45) DEFAULT NULL,
                         `tostart` time DEFAULT NULL,
                         `toend` time DEFAULT NULL,
                         `shiftstatus_id` int NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `fk_shift_shiftstatus1_idx` (`shiftstatus_id`),
                         CONSTRAINT `fk_shift_shiftstatus1` FOREIGN KEY (`shiftstatus_id`) REFERENCES `shiftstatus` (`id`)
);

CREATE TABLE `tripstatus` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(45) DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `triptype` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(45) DEFAULT NULL,
                            PRIMARY KEY (`id`)
);

CREATE TABLE `unitofmeasure` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `name` varchar(45) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
);

CREATE TABLE `vehicleservicepriority` (
                                          `id` int NOT NULL AUTO_INCREMENT,
                                          `name` varchar(45) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
);

CREATE TABLE `vehicleservicestatus` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `name` varchar(45) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
);

CREATE TABLE `vehicleservicetype` (
                                      `id` int NOT NULL AUTO_INCREMENT,
                                      `name` varchar(45) DEFAULT NULL,
                                      PRIMARY KEY (`id`)
);

CREATE TABLE `vehiclestatus` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `name` varchar(45) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
);

CREATE TABLE `partmaster` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `sku` varchar(45) DEFAULT NULL,
                              `name` varchar(45) DEFAULT NULL,
                              `partcategory_id` int NOT NULL,
                              `unitofmeasure_id` int NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `fk_partmaster_partcategory1_idx` (`partcategory_id`),
                              KEY `fk_partmaster_unitofmeasure1_idx` (`unitofmeasure_id`),
                              CONSTRAINT `fk_partmaster_partcategory1` FOREIGN KEY (`partcategory_id`) REFERENCES `partcategory` (`id`),
                              CONSTRAINT `fk_partmaster_unitofmeasure1` FOREIGN KEY (`unitofmeasure_id`) REFERENCES `unitofmeasure` (`id`)
);

CREATE TABLE `part` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `branch_id` int NOT NULL,
                        `partmaster_id` int NOT NULL,
                        `qoh` decimal(10,3) DEFAULT NULL,
                        `maxlevel` decimal(10,3) DEFAULT NULL,
                        `rop` decimal(10,3) DEFAULT NULL,
                        `dolastordered` date DEFAULT NULL,
                        `remarks` text,
                        `partstatus_id` int NOT NULL,
                        `deleted` bit(1) DEFAULT NULL,
                        `user_id` int DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `fk_part_partstatus1_idx` (`partstatus_id`),
                        KEY `fk_part_branch1_idx` (`branch_id`),
                        KEY `fk_part_partmaster1_idx` (`partmaster_id`),
                        KEY `fk_part_user1_idx` (`user_id`),
                        CONSTRAINT `fk_part_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                        CONSTRAINT `fk_part_partmaster1` FOREIGN KEY (`partmaster_id`) REFERENCES `partmaster` (`id`),
                        CONSTRAINT `fk_part_partstatus1` FOREIGN KEY (`partstatus_id`) REFERENCES `partstatus` (`id`),
                        CONSTRAINT `fk_part_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `partrequest` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `branch_id` int NOT NULL,
                               `number` varchar(45) DEFAULT NULL,
                               `dorequested` date DEFAULT NULL,
                               `remarks` varchar(45) DEFAULT NULL,
                               `partrequeststatus_id` int NOT NULL,
                               `user_id` int DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               KEY `fk_request_branch1_idx` (`branch_id`),
                               KEY `fk_partrequest_partrequeststatus1_idx` (`partrequeststatus_id`),
                               KEY `fk_partrequest_user1_idx` (`user_id`),
                               CONSTRAINT `fk_partrequest_partrequeststatus1` FOREIGN KEY (`partrequeststatus_id`) REFERENCES `partrequeststatus` (`id`),
                               CONSTRAINT `fk_partrequest_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                               CONSTRAINT `fk_request_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`)
);

CREATE TABLE `partrequestitem` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `partrequest_id` int NOT NULL,
                                   `part_id` int NOT NULL,
                                   `quantity` decimal(10,3) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `fk_partrequest_has_part_part1_idx` (`part_id`),
                                   KEY `fk_partrequest_has_part_partrequest1_idx` (`partrequest_id`),
                                   CONSTRAINT `fk_partrequest_has_part_part1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
                                   CONSTRAINT `fk_partrequest_has_part_partrequest1` FOREIGN KEY (`partrequest_id`) REFERENCES `partrequest` (`id`)
);

CREATE TABLE `grn` (
                       `id` int NOT NULL AUTO_INCREMENT,
                       `branch_id` int NOT NULL,
                       `partrequest_id` int NOT NULL,
                       `number` varchar(45) DEFAULT NULL,
                       `doreceived` date DEFAULT NULL,
                       `remarks` text,
                       `grnstatus_id` int NOT NULL,
                       `user_id` int DEFAULT NULL,
                       PRIMARY KEY (`id`),
                       KEY `fk_grn_branch1_idx` (`branch_id`),
                       KEY `fk_grn_grnstatus1_idx` (`grnstatus_id`),
                       KEY `fk_grn_partrequest1_idx` (`partrequest_id`),
                       KEY `fk_grn_user1_idx` (`user_id`),
                       CONSTRAINT `fk_grn_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                       CONSTRAINT `fk_grn_grnstatus1` FOREIGN KEY (`grnstatus_id`) REFERENCES `grnstatus` (`id`),
                       CONSTRAINT `fk_grn_partrequest1` FOREIGN KEY (`partrequest_id`) REFERENCES `partrequest` (`id`),
                       CONSTRAINT `fk_grn_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `grnpartrequestitem` (
                                      `id` int NOT NULL AUTO_INCREMENT,
                                      `grn_id` int NOT NULL,
                                      `partrequestitem_id` int NOT NULL,
                                      `quantity` decimal(10,3) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `fk_grn_has_partrequestitem_partrequestitem1_idx` (`partrequestitem_id`),
                                      KEY `fk_grn_has_partrequestitem_grn1_idx` (`grn_id`),
                                      CONSTRAINT `fk_grn_has_partrequestitem_grn1` FOREIGN KEY (`grn_id`) REFERENCES `grn` (`id`),
                                      CONSTRAINT `fk_grn_has_partrequestitem_partrequestitem1` FOREIGN KEY (`partrequestitem_id`) REFERENCES `partrequestitem` (`id`)
);

CREATE TABLE `role` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(45) DEFAULT NULL,
                        PRIMARY KEY (`id`)
);

CREATE TABLE `privilege` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `authority` varchar(45) DEFAULT NULL,
                             `role_id` int NOT NULL,
                             `module_id` int NOT NULL,
                             `operation_id` int NOT NULL,
                             PRIMARY KEY (`id`),
                             KEY `fk_privilege_role1_idx` (`role_id`),
                             KEY `fk_privilege_module1_idx` (`module_id`),
                             KEY `fk_privilege_operation1_idx` (`operation_id`),
                             CONSTRAINT `fk_privilege_module1` FOREIGN KEY (`module_id`) REFERENCES `module` (`id`),
                             CONSTRAINT `fk_privilege_operation1` FOREIGN KEY (`operation_id`) REFERENCES `operation` (`id`),
                             CONSTRAINT `fk_privilege_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
);

CREATE TABLE `roster` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `branch_id` int NOT NULL,
                          `dostartofweek` date DEFAULT NULL,
                          `doendofweek` date DEFAULT NULL,
                          `deleted` bit(1) DEFAULT NULL,
                          `user_id` int DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `fk_roster_branch1_idx` (`branch_id`),
                          KEY `fk_roster_user1_idx` (`user_id`),
                          CONSTRAINT `fk_roster_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                          CONSTRAINT `fk_roster_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `rostershift` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `roster_id` int NOT NULL,
                               `shift_id` int NOT NULL,
                               `doshift` date DEFAULT NULL,
                               `designation_id` int NOT NULL,
                               `requiredemployeecount` int DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               KEY `fk_roster_has_shift_shift1_idx` (`shift_id`),
                               KEY `fk_roster_has_shift_roster1_idx` (`roster_id`),
                               KEY `fk_rostershift_designation1_idx` (`designation_id`),
                               CONSTRAINT `fk_roster_has_shift_roster1` FOREIGN KEY (`roster_id`) REFERENCES `roster` (`id`),
                               CONSTRAINT `fk_roster_has_shift_shift1` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`id`),
                               CONSTRAINT `fk_rostershift_designation1` FOREIGN KEY (`designation_id`) REFERENCES `designation` (`id`)
);

CREATE TABLE `rostershiftassignmentstatus` (
                                               `id` int NOT NULL AUTO_INCREMENT,
                                               `name` varchar(45) DEFAULT NULL,
                                               PRIMARY KEY (`id`)
);

CREATE TABLE `rostershiftassignment` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `rostershift_id` int NOT NULL,
                                         `employee_id` int DEFAULT NULL,
                                         `rostershiftassignmentstatus_id` int NOT NULL,
                                         PRIMARY KEY (`id`),
                                         KEY `fk_rostershift_has_employee_employee1_idx` (`employee_id`),
                                         KEY `fk_rostershift_has_employee_rostershift1_idx` (`rostershift_id`),
                                         KEY `fk_rostershiftassignment_rostershiftassignmentstatus1_idx` (`rostershiftassignmentstatus_id`),
                                         CONSTRAINT `fk_rostershift_has_employee_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
                                         CONSTRAINT `fk_rostershift_has_employee_rostershift1` FOREIGN KEY (`rostershift_id`) REFERENCES `rostershift` (`id`),
                                         CONSTRAINT `fk_rostershiftassignment_rostershiftassignmentstatus1` FOREIGN KEY (`rostershiftassignmentstatus_id`) REFERENCES `rostershiftassignmentstatus` (`id`)
);

CREATE TABLE `route` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `number` varchar(45) DEFAULT NULL,
                         `origin` varchar(45) DEFAULT NULL,
                         `destination` varchar(45) DEFAULT NULL,
                         `distancekm` decimal(5,1) DEFAULT NULL,
                         `routetype_id` int NOT NULL,
                         `mingapminutes` int DEFAULT NULL,
                         `waypoints` json DEFAULT NULL,
                         `user_id` int DEFAULT NULL,
                         `requiredroutefamiliaritylevel_id` int NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `fk_route_routetype1_idx` (`routetype_id`),
                         KEY `fk_route_user1_idx` (`user_id`),
                         KEY `fk_route_routefamiliaritylevel1_idx` (`requiredroutefamiliaritylevel_id`),
                         CONSTRAINT `fk_route_routefamiliaritylevel1` FOREIGN KEY (`requiredroutefamiliaritylevel_id`) REFERENCES `routefamiliaritylevel` (`id`),
                         CONSTRAINT `fk_route_routetype1` FOREIGN KEY (`routetype_id`) REFERENCES `routetype` (`id`),
                         CONSTRAINT `fk_route_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `routebranch` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `branch_id` int NOT NULL,
                               `route_id` int NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `fk_route_has_branch_branch1_idx` (`branch_id`),
                               KEY `fk_route_has_branch_route1_idx` (`route_id`),
                               CONSTRAINT `fk_route_has_branch_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                               CONSTRAINT `fk_route_has_branch_route1` FOREIGN KEY (`route_id`) REFERENCES `route` (`id`)
);

CREATE TABLE `ticketmachine` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `name` varchar(45) DEFAULT NULL,
                                 `branch_id` int NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `fk_ticketmachine_branch1_idx` (`branch_id`),
                                 CONSTRAINT `fk_ticketmachine_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`)
);

CREATE TABLE `vehicle` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `branch_id` int NOT NULL,
                           `number` char(7) NOT NULL,
                           `model_id` int NOT NULL,
                           `bustype_id` int NOT NULL,
                           `mileage` int DEFAULT NULL,
                           `fueltype_id` int NOT NULL,
                           `conditionrate_id` int NOT NULL,
                           `vehiclestatus_id` int NOT NULL,
                           `remarks` varchar(45) DEFAULT NULL,
                           `deleted` bit(1) DEFAULT NULL,
                           `user_id` int DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `number_UNIQUE` (`number`),
                           KEY `fk_vehicle_conditionrate1_idx` (`conditionrate_id`),
                           KEY `fk_vehicle_vehiclestatus1_idx` (`vehiclestatus_id`),
                           KEY `fk_vehicle_fueltype1_idx` (`fueltype_id`),
                           KEY `fk_vehicle_branch1_idx` (`branch_id`),
                           KEY `fk_vehicle_bustype1_idx` (`bustype_id`),
                           KEY `fk_vehicle_model1_idx` (`model_id`),
                           KEY `fk_vehicle_user1_idx` (`user_id`),
                           CONSTRAINT `fk_vehicle_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                           CONSTRAINT `fk_vehicle_bustype1` FOREIGN KEY (`bustype_id`) REFERENCES `bustype` (`id`),
                           CONSTRAINT `fk_vehicle_conditionrate1` FOREIGN KEY (`conditionrate_id`) REFERENCES `conditionrate` (`id`),
                           CONSTRAINT `fk_vehicle_fueltype1` FOREIGN KEY (`fueltype_id`) REFERENCES `fueltype` (`id`),
                           CONSTRAINT `fk_vehicle_model1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`),
                           CONSTRAINT `fk_vehicle_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                           CONSTRAINT `fk_vehicle_vehiclestatus1` FOREIGN KEY (`vehiclestatus_id`) REFERENCES `vehiclestatus` (`id`)
);

CREATE TABLE `permite` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `branch_id` int NOT NULL,
                           `route_id` int NOT NULL,
                           `number` char(16) DEFAULT NULL,
                           `vehicle_id` int NOT NULL,
                           `doissued` date DEFAULT NULL,
                           `doexpired` date DEFAULT NULL,
                           `notripsperday` int DEFAULT NULL,
                           `permitestatus_id` int NOT NULL,
                           `servicetype_id` int NOT NULL,
                           `deleted` bit(1) DEFAULT NULL,
                           `user_id` int DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `fk_permite_branch1_idx` (`branch_id`),
                           KEY `fk_permite_permitestatus1_idx` (`permitestatus_id`),
                           KEY `fk_permite_servicetype1_idx` (`servicetype_id`),
                           KEY `fk_permite_vehicle1_idx` (`vehicle_id`),
                           KEY `fk_permite_route1_idx` (`route_id`),
                           KEY `fk_permite_user1_idx` (`user_id`),
                           CONSTRAINT `fk_permite_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                           CONSTRAINT `fk_permite_permitestatus1` FOREIGN KEY (`permitestatus_id`) REFERENCES `permitestatus` (`id`),
                           CONSTRAINT `fk_permite_route1` FOREIGN KEY (`route_id`) REFERENCES `route` (`id`),
                           CONSTRAINT `fk_permite_servicetype1` FOREIGN KEY (`servicetype_id`) REFERENCES `servicetype` (`id`),
                           CONSTRAINT `fk_permite_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                           CONSTRAINT `fk_permite_vehicle1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`)
);

CREATE TABLE `trip` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `branch_id` int NOT NULL,
                        `triptype_id` int NOT NULL,
                        `permite_id` int NOT NULL,
                        `todepature` time DEFAULT NULL,
                        `toarrival` time DEFAULT NULL,
                        `breakminutes` int DEFAULT NULL,
                        `remarks` varchar(45) DEFAULT NULL,
                        `tripstatus_id` int NOT NULL,
                        `originterminal_id` int NOT NULL,
                        `user_id` int DEFAULT NULL,
                        `opcalender_id` int NOT NULL,
                        PRIMARY KEY (`id`),
                        KEY `fk_trip_permite1_idx` (`permite_id`),
                        KEY `fk_trip_triptype1_idx` (`triptype_id`),
                        KEY `fk_trip_branch1_idx` (`branch_id`),
                        KEY `fk_trip_tripstatus1_idx` (`tripstatus_id`),
                        KEY `fk_trip_originterminal1_idx` (`originterminal_id`),
                        KEY `fk_trip_user1_idx` (`user_id`),
                        KEY `fk_trip_opcalender1_idx` (`opcalender_id`),
                        CONSTRAINT `fk_trip_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                        CONSTRAINT `fk_trip_opcalender1` FOREIGN KEY (`opcalender_id`) REFERENCES `opcalender` (`id`),
                        CONSTRAINT `fk_trip_originterminal1` FOREIGN KEY (`originterminal_id`) REFERENCES `originterminal` (`id`),
                        CONSTRAINT `fk_trip_permite1` FOREIGN KEY (`permite_id`) REFERENCES `permite` (`id`),
                        CONSTRAINT `fk_trip_tripstatus1` FOREIGN KEY (`tripstatus_id`) REFERENCES `tripstatus` (`id`),
                        CONSTRAINT `fk_trip_triptype1` FOREIGN KEY (`triptype_id`) REFERENCES `triptype` (`id`),
                        CONSTRAINT `fk_trip_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `tripexecutionstatus` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `name` varchar(45) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
);

CREATE TABLE `tripexecution` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `branch_id` int NOT NULL,
                                 `trip_id` int NOT NULL,
                                 `vehicle_id` int DEFAULT NULL,
                                 `driver_id` int DEFAULT NULL,
                                 `conductor_id` int DEFAULT NULL,
                                 `doservice` date DEFAULT NULL,
                                 `toactualdeparture` time DEFAULT NULL,
                                 `toactualarrival` time DEFAULT NULL,
                                 `startodometer` int DEFAULT NULL,
                                 `endodometer` int DEFAULT NULL,
                                 `passengercount` int DEFAULT NULL,
                                 `tripno` int DEFAULT NULL,
                                 `remarks` varchar(45) DEFAULT NULL,
                                 `tripexecutionstatus_id` int NOT NULL,
                                 `user_id` int DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `fk_tripexecution_trip1_idx` (`trip_id`),
                                 KEY `fk_tripexecution_vehicle1_idx` (`vehicle_id`),
                                 KEY `fk_tripexecution_branch1_idx` (`branch_id`),
                                 KEY `fk_tripexecution_driver1_idx` (`driver_id`),
                                 KEY `fk_tripexecution_conductor1_idx` (`conductor_id`),
                                 KEY `fk_tripexecution_tripexecutionstatus1_idx` (`tripexecutionstatus_id`),
                                 KEY `fk_tripexecution_user1_idx` (`user_id`),
                                 CONSTRAINT `fk_tripexecution_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                                 CONSTRAINT `fk_tripexecution_conductor1` FOREIGN KEY (`conductor_id`) REFERENCES `conductor` (`id`),
                                 CONSTRAINT `fk_tripexecution_driver1` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`),
                                 CONSTRAINT `fk_tripexecution_trip1` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`),
                                 CONSTRAINT `fk_tripexecution_tripexecutionstatus1` FOREIGN KEY (`tripexecutionstatus_id`) REFERENCES `tripexecutionstatus` (`id`),
                                 CONSTRAINT `fk_tripexecution_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                                 CONSTRAINT `fk_tripexecution_vehicle1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`)
);

CREATE TABLE `farecollection` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `branch_id` int NOT NULL,
                                  `tripexecution_id` int NOT NULL,
                                  `ticketmachine_id` int NOT NULL,
                                  `totaltickets` int DEFAULT NULL,
                                  `cashcollected` decimal(10,2) DEFAULT NULL,
                                  `digitalpayments` decimal(10,2) DEFAULT NULL,
                                  `isreconciled` bit(1) DEFAULT NULL,
                                  `tocollected` time DEFAULT NULL,
                                  `user_id` int DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `fk_farecollection_branch1_idx` (`branch_id`),
                                  KEY `fk_farecollection_ticketmachine1_idx` (`ticketmachine_id`),
                                  KEY `fk_farecollection_tripexecution1_idx` (`tripexecution_id`),
                                  KEY `fk_farecollection_user1_idx` (`user_id`),
                                  CONSTRAINT `fk_farecollection_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                                  CONSTRAINT `fk_farecollection_ticketmachine1` FOREIGN KEY (`ticketmachine_id`) REFERENCES `ticketmachine` (`id`),
                                  CONSTRAINT `fk_farecollection_tripexecution1` FOREIGN KEY (`tripexecution_id`) REFERENCES `tripexecution` (`id`),
                                  CONSTRAINT `fk_farecollection_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `incident` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `branch_id` int NOT NULL,
                            `tripexecution_id` int NOT NULL,
                            `incidenttype_id` int NOT NULL,
                            `regionalarea_id` int NOT NULL,
                            `toreported` time DEFAULT NULL,
                            `doreported` date DEFAULT NULL,
                            `odometeratincident` int DEFAULT NULL,
                            `remarks` varchar(45) DEFAULT NULL,
                            `incidentstatus_id` int NOT NULL,
                            `user_id` int DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `fk_incident_incidenttype1_idx` (`incidenttype_id`),
                            KEY `fk_incident_incidentstatus1_idx` (`incidentstatus_id`),
                            KEY `fk_incident_tripexecution1_idx` (`tripexecution_id`),
                            KEY `fk_incident_regionaloffice1_idx` (`regionalarea_id`),
                            KEY `fk_incident_user1_idx` (`user_id`),
                            KEY `fk_incident_branch1_idx` (`branch_id`),
                            CONSTRAINT `fk_incident_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                            CONSTRAINT `fk_incident_incidentstatus1` FOREIGN KEY (`incidentstatus_id`) REFERENCES `incidentstatus` (`id`),
                            CONSTRAINT `fk_incident_incidenttype1` FOREIGN KEY (`incidenttype_id`) REFERENCES `incidenttype` (`id`),
                            CONSTRAINT `fk_incident_regionaloffice1` FOREIGN KEY (`regionalarea_id`) REFERENCES `regionaloffice` (`id`),
                            CONSTRAINT `fk_incident_tripexecution1` FOREIGN KEY (`tripexecution_id`) REFERENCES `tripexecution` (`id`),
                            CONSTRAINT `fk_incident_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `incidentvehicleallocation` (
                                             `id` int NOT NULL AUTO_INCREMENT,
                                             `incident_id` int NOT NULL,
                                             `vehicle_id` int NOT NULL,
                                             `providedbranch_id` int NOT NULL,
                                             `incidentvehicleallocationstatus_id` int NOT NULL,
                                             `doassigned` datetime DEFAULT NULL,
                                             `doreleased` datetime DEFAULT NULL,
                                             `user_id` int DEFAULT NULL,
                                             PRIMARY KEY (`id`),
                                             KEY `fk_incidentvehicleallocation_incident1_idx` (`incident_id`),
                                             KEY `fk_incidentvehicleallocation_vehicle1_idx` (`vehicle_id`),
                                             KEY `fk_incidentvehicleallocation_branch1_idx` (`providedbranch_id`),
                                             KEY `fk_incidentvehicleallocation_incidentvehicleallocationstatu_idx` (`incidentvehicleallocationstatus_id`),
                                             KEY `fk_incidentvehicleallocation_user1_idx` (`user_id`),
                                             CONSTRAINT `fk_incidentvehicleallocation_branch1` FOREIGN KEY (`providedbranch_id`) REFERENCES `branch` (`id`),
                                             CONSTRAINT `fk_incidentvehicleallocation_incident1` FOREIGN KEY (`incident_id`) REFERENCES `incident` (`id`),
                                             CONSTRAINT `fk_incidentvehicleallocation_incidentvehicleallocationstatus1` FOREIGN KEY (`incidentvehicleallocationstatus_id`) REFERENCES `incidentvehicleallocationstatus` (`id`),
                                             CONSTRAINT `fk_incidentvehicleallocation_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                                             CONSTRAINT `fk_incidentvehicleallocation_vehicle1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`)
);

CREATE TABLE `userrole` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `user_id` int NOT NULL,
                            `role_id` int NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `fk_user_has_role_role1_idx` (`role_id`),
                            KEY `fk_user_has_role_user1_idx` (`user_id`),
                            CONSTRAINT `fk_user_has_role_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
                            CONSTRAINT `fk_user_has_role_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `vehicleservice` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `branch_id` int NOT NULL,
                                  `number` varchar(45) DEFAULT NULL,
                                  `vehicle_id` int NOT NULL,
                                  `vehicleservicetype_id` int NOT NULL,
                                  `incident_id` int DEFAULT NULL,
                                  `vehicleservicestatus_id` int NOT NULL,
                                  `vehicleservicepriority_id` int NOT NULL,
                                  `docreated` date DEFAULT NULL,
                                  `user_id` int DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `fk_vehicleservice_vehicle1_idx` (`vehicle_id`),
                                  KEY `fk_vehicleservice_vehicleservicetype1_idx` (`vehicleservicetype_id`),
                                  KEY `fk_vehicleservice_vehicleservicepriority1_idx` (`vehicleservicepriority_id`),
                                  KEY `fk_vehicleservice_incident1_idx` (`incident_id`),
                                  KEY `fk_vehicleservice_branch1_idx` (`branch_id`),
                                  KEY `fk_vehicleservice_vehicleservicestatus1_idx` (`vehicleservicestatus_id`),
                                  KEY `fk_vehicleservice_user1_idx` (`user_id`),
                                  CONSTRAINT `fk_vehicleservice_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                                  CONSTRAINT `fk_vehicleservice_incident1` FOREIGN KEY (`incident_id`) REFERENCES `incident` (`id`),
                                  CONSTRAINT `fk_vehicleservice_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                                  CONSTRAINT `fk_vehicleservice_vehicle1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
                                  CONSTRAINT `fk_vehicleservice_vehicleservicepriority1` FOREIGN KEY (`vehicleservicepriority_id`) REFERENCES `vehicleservicepriority` (`id`),
                                  CONSTRAINT `fk_vehicleservice_vehicleservicestatus1` FOREIGN KEY (`vehicleservicestatus_id`) REFERENCES `vehicleservicestatus` (`id`),
                                  CONSTRAINT `fk_vehicleservice_vehicleservicetype1` FOREIGN KEY (`vehicleservicetype_id`) REFERENCES `servicetype` (`id`)
);

CREATE TABLE `vehicleserviceexecution` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `branch_id` int NOT NULL,
                                           `vehicleservice_id` int NOT NULL,
                                           `dostarted` date DEFAULT NULL,
                                           `doend` date DEFAULT NULL,
                                           `remarks` text,
                                           `startodometer` int DEFAULT NULL,
                                           `nextserviceinkm` int DEFAULT NULL,
                                           `maintechnician_id` int NOT NULL,
                                           `user_id` int DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           KEY `fk_vehicleserviceschedule_vehicleservice1_idx` (`vehicleservice_id`),
                                           KEY `fk_vehicleserviceschedule_employee1_idx` (`maintechnician_id`),
                                           KEY `fk_vehicleserviceschedule_branch1_idx` (`branch_id`),
                                           KEY `fk_vehicleserviceschedule_user1_idx` (`user_id`),
                                           CONSTRAINT `fk_vehicleserviceschedule_branch1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
                                           CONSTRAINT `fk_vehicleserviceschedule_employee1` FOREIGN KEY (`maintechnician_id`) REFERENCES `employee` (`id`),
                                           CONSTRAINT `fk_vehicleserviceschedule_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                                           CONSTRAINT `fk_vehicleserviceschedule_vehicleservice1` FOREIGN KEY (`vehicleservice_id`) REFERENCES `vehicleservice` (`id`)
);

CREATE TABLE `vehicleservicepart` (
                                      `id` int NOT NULL AUTO_INCREMENT,
                                      `vehicleservice_id` int NOT NULL,
                                      `part_id` int NOT NULL,
                                      `quantity` decimal(10,3) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `fk_vehicleservice_has_part_part1_idx` (`part_id`),
                                      KEY `fk_vehicleservice_has_part_vehicleservice1_idx` (`vehicleservice_id`),
                                      CONSTRAINT `fk_vehicleservice_has_part_part1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
                                      CONSTRAINT `fk_vehicleservice_has_part_vehicleservice1` FOREIGN KEY (`vehicleservice_id`) REFERENCES `vehicleservice` (`id`)
);
