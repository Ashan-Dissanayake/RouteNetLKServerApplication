-- ====== Branch References ======
CREATE TABLE branchstatus (
                              id INT PRIMARY KEY,
                              name VARCHAR(45) NOT NULL
);

CREATE TABLE branchtype (
                            id INT PRIMARY KEY,
                            name VARCHAR(45) NOT NULL
);

CREATE TABLE regionaloffice (
                                id INT PRIMARY KEY,
                                name VARCHAR(45) NOT NULL
);

CREATE TABLE branch (
                        id INT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        branchstatus_id INT NOT NULL,
                        branchtype_id INT NOT NULL,
                        regionaloffice_id INT NOT NULL,
                        docreated DATE NOT NULL,
                        deleted BOOLEAN NOT NULL,
                        remarks VARCHAR(255),
                        FOREIGN KEY (branchstatus_id) REFERENCES branchstatus(id),
                        FOREIGN KEY (branchtype_id) REFERENCES branchtype(id),
                        FOREIGN KEY (regionaloffice_id) REFERENCES regionaloffice(id)
);

-- ====== Vehicle References ======
CREATE TABLE vehiclestatus (
                               id INT PRIMARY KEY,
                               name VARCHAR(45) NOT NULL
);

CREATE TABLE bustype (
                         id INT PRIMARY KEY,
                         name VARCHAR(45) NOT NULL
);

CREATE TABLE vehicle (
                         id INT PRIMARY KEY,
                         branch_id INT NOT NULL,
                         number CHAR(7) NOT NULL,
                         bustype_id INT NOT NULL,
                         vehiclestatus_id INT NOT NULL,
                         remarks VARCHAR(255),
                         deleted BOOLEAN  NOT NULL,
                         FOREIGN KEY (branch_id) REFERENCES branch(id),
                         FOREIGN KEY (bustype_id) REFERENCES bustype(id),
                         FOREIGN KEY (vehiclestatus_id) REFERENCES vehiclestatus(id)
);

-- ====== Trip & Incident ======
CREATE TABLE tripstatus (
                            id INT PRIMARY KEY,
                            name VARCHAR(45) NOT NULL
);

CREATE TABLE triptype (
                          id INT PRIMARY KEY,
                          name VARCHAR(45) NOT NULL
);

CREATE TABLE originterminal (
                                id INT PRIMARY KEY,
                                name VARCHAR(45) NOT NULL
);

CREATE TABLE permite (
                         id INT PRIMARY KEY,
                         number CHAR(16) NOT NULL,
                         vehicle_id INT NOT NULL,
                         branch_id INT NOT NULL,
                         FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
                         FOREIGN KEY (branch_id) REFERENCES branch(id)
);

CREATE TABLE trip (
                      id INT PRIMARY KEY,
                      branch_id INT NOT NULL,
                      triptype_id INT NOT NULL,
                      permite_id INT NOT NULL,
                      doservice DATE NOT NULL,
                      tripstatus_id INT NOT NULL,
                      originterminal_id INT NOT NULL,
                      FOREIGN KEY (branch_id) REFERENCES branch(id),
                      FOREIGN KEY (triptype_id) REFERENCES triptype(id),
                      FOREIGN KEY (permite_id) REFERENCES permite(id),
                      FOREIGN KEY (tripstatus_id) REFERENCES tripstatus(id),
                      FOREIGN KEY (originterminal_id) REFERENCES originterminal(id)
);

CREATE TABLE incidenttype (
                               id INT PRIMARY KEY,
                               name VARCHAR(45) NOT NULL
);

CREATE TABLE incidentstatus (
                                id INT PRIMARY KEY,
                                name VARCHAR(45) NOT NULL
);

CREATE TABLE incident (
                          id INT PRIMARY KEY,
                          trip_id INT NOT NULL,
                          incidenttype_id INT NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          description VARCHAR(255),
                          location VARCHAR(255),
                          reported_time TIMESTAMP NOT NULL,
                          resolved_time TIMESTAMP,
                          FOREIGN KEY (trip_id) REFERENCES trip(id),
                          FOREIGN KEY (incidenttype_id) REFERENCES incidenttype(id)
);

CREATE TABLE incidentvehicleallocation (
                                             id INT PRIMARY KEY,
                                             incident_id INT NOT NULL,
                                             vehicle_id INT NOT NULL,
                                             providingbranch_id INT NOT NULL,
                                             allocation_type VARCHAR(30) NOT NULL,
                                             status VARCHAR(30) NOT NULL,
                                             doassigned TIMESTAMP NOT NULL,
                                             doreleased TIMESTAMP,
                                             remarks VARCHAR(255),
                                             FOREIGN KEY (incident_id) REFERENCES incident(id),
                                             FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
                                             FOREIGN KEY (providingbranch_id) REFERENCES branch(id)
);
