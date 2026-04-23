-- Core Branch Tables [cite: 55]
CREATE TABLE branchstatus (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255));
CREATE TABLE branchtype (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255));
CREATE TABLE regionaloffice (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));

CREATE TABLE branch (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255),
                        code VARCHAR(255),
                        address VARCHAR(255),
                        telephone VARCHAR(255),
                        email VARCHAR(255),
                        docreated DATE NOT NULL,
                        branchtype_id INT NOT NULL,
                        remarks VARCHAR(255),
                        branchstatus_id INT NOT NULL,
                        deleted BIT(1),
                        regionaloffice_id INT NOT NULL
);

-- Core Employee & Crew Tables [cite: 59, 58, 56, 57]
CREATE TABLE designation (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE TABLE employeestatus (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE TABLE employeetype (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE TABLE gender (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE TABLE department (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE TABLE crewstatus (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE TABLE routefamiliaritylevel (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));

CREATE TABLE employee (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          number CHAR(10) NOT NULL,
                          fullname VARCHAR(45) NOT NULL,
                          callingname VARCHAR(45) NOT NULL,
                          nic CHAR(12) NOT NULL,
                          gender_id INT NOT NULL,
                          mobile CHAR(10) NOT NULL,
                          email VARCHAR(45) NOT NULL,
                          address VARCHAR(45) NOT NULL,
                          emergencycontact CHAR(10) NOT NULL,
                          image LONGBLOB,
                          branch_id INT NOT NULL,
                          department_id INT NOT NULL,
                          designation_id INT NOT NULL,
                          employeetype_id INT NOT NULL,
                          doj DATE NOT NULL,
                          employeestatus_id INT NOT NULL,
                          deleted BIT(1)
);

-- Roster & Trip Tables [cite: 69, 70, 72, 73]
CREATE TABLE rostershiftassignmentstatus (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE TABLE tripcrewallocationstatus (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE TABLE tripstatus (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));

CREATE TABLE rostershiftassignment (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       rostershift_id INT NOT NULL,
                                       employee_id INT,
                                       rostershiftassignmentstatus_id INT NOT NULL
);

CREATE TABLE trip (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      branch_id INT NOT NULL,
                      triptype_id INT NOT NULL,
                      permite_id INT NOT NULL,
                      doservice DATE,
                      todepature TIME,
                      toarrival TIME,
                      remarks VARCHAR(45),
                      notrip INT,
                      tripstatus_id INT NOT NULL,
                      originterminal_id INT NOT NULL
);

CREATE TABLE tripcrewallocation (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    trip_id INT NOT NULL,
                                    rostershiftassignment_id INT NOT NULL,
                                    toallocated TIME,
                                    remarks VARCHAR(45),
                                    tripcrewallocationstatus_id INT NOT NULL
);
