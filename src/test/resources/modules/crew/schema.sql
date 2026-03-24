CREATE TABLE IF NOT EXISTS licensecategory (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS routefamiliaritylevel (
                                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                                     name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS crewstatus (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS driver (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      employee_id INT UNIQUE,
                                      number CHAR(12) NOT NULL,
                                      licensenumber CHAR(12) NOT NULL,
                                      dolicenseissued DATE,
                                      dolicenseexpired DATE,
                                      domedicalissued DATE,
                                      domedicalexpired DATE,
                                      licensecategory_id INT NOT NULL,
                                      crewstatus_id INT NOT NULL,
                                      routefamiliaritylevel_id INT NOT NULL,
                                      CONSTRAINT fk_driver_employee FOREIGN KEY (employee_id) REFERENCES employee(id),
                                      CONSTRAINT fk_driver_licensecategory FOREIGN KEY (licensecategory_id) REFERENCES licensecategory(id),
                                      CONSTRAINT fk_driver_crewstatus FOREIGN KEY (crewstatus_id) REFERENCES crewstatus(id),
                                      CONSTRAINT fk_driver_routefamiliaritylevel FOREIGN KEY (routefamiliaritylevel_id) REFERENCES routefamiliaritylevel(id)
);

CREATE TABLE IF NOT EXISTS conductor (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         employee_id INT UNIQUE,
                                         number CHAR(12) NOT NULL,
                                         domedicalissued DATE,
                                         domedicalexpired DATE,
                                         routefamiliaritylevel_id INT NOT NULL,
                                         crewstatus_id INT NOT NULL,
                                         CONSTRAINT fk_conductor_employee FOREIGN KEY (employee_id) REFERENCES employee(id),
                                         CONSTRAINT fk_conductor_routefamiliaritylevel FOREIGN KEY (routefamiliaritylevel_id) REFERENCES routefamiliaritylevel(id),
                                         CONSTRAINT fk_conductor_crewstatus FOREIGN KEY (crewstatus_id) REFERENCES crewstatus(id)
);
