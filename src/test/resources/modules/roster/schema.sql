CREATE TABLE IF NOT EXISTS rosterstatus (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS shiftstatus (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rostershiftassignmentstatus (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS role (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roster (
    id INT NOT NULL AUTO_INCREMENT,
    branch_id INT NOT NULL,
    dostartofweek DATE NULL DEFAULT NULL,
    doendofweek DATE NULL DEFAULT NULL,
    rosterstatus_id INT NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,

    PRIMARY KEY (id),

    INDEX fk_roster_branch_idx (branch_id),
    INDEX fk_roster_rosterstatus_idx (rosterstatus_id),

    CONSTRAINT fk_roster_branch
    FOREIGN KEY (branch_id)
      REFERENCES branch (id),

    CONSTRAINT fk_roster_rosterstatus
    FOREIGN KEY (rosterstatus_id)
      REFERENCES rosterstatus (id)
);

CREATE TABLE IF NOT EXISTS shift (
     id INT NOT NULL AUTO_INCREMENT,
     branch_id INT NOT NULL,
     name VARCHAR(255),
     tostart TIME,
     toend TIME,
     maxhours INT,
     shiftstatus_id INT NOT NULL,

     PRIMARY KEY (id),

     INDEX fk_shift_branch_idx (branch_id),
     INDEX fk_shift_shiftstatus_idx (shiftstatus_id),

     CONSTRAINT fk_shift_branch
         FOREIGN KEY (branch_id)
             REFERENCES branch (id),

     CONSTRAINT fk_shift_shiftstatus
         FOREIGN KEY (shiftstatus_id)
             REFERENCES shiftstatus (id)
);

CREATE TABLE IF NOT EXISTS rostershiftassignment (
     id INT NOT NULL AUTO_INCREMENT,
     shift_id INT NOT NULL,
     roster_id INT NOT NULL,
     doassigned DATE,
     role_id INT NOT NULL,
     employee_id INT NOT NULL,
     shiftrosterassignmentstatus_id INT NOT NULL,

     PRIMARY KEY (id),

     INDEX fk_shiftrosterassignment_shift_idx (shift_id),
     INDEX fk_shiftrosterassignment_roster_idx (roster_id),
     INDEX fk_shiftrosterassignment_role_idx (role_id),
     INDEX fk_shiftrosterassignment_employee_idx (employee_id),
     INDEX fk_shiftrosterassignment_status_idx (shiftrosterassignmentstatus_id),

     CONSTRAINT fk_shiftrosterassignment_shift
         FOREIGN KEY (shift_id)
             REFERENCES shift (id),

     CONSTRAINT fk_shiftrosterassignment_roster
         FOREIGN KEY (roster_id)
             REFERENCES roster (id),

     CONSTRAINT fk_shiftrosterassignment_role
         FOREIGN KEY (role_id)
             REFERENCES role (id),

     CONSTRAINT fk_shiftrosterassignment_employee
         FOREIGN KEY (employee_id)
             REFERENCES employee (id),

     CONSTRAINT fk_shiftrosterassignment_status
         FOREIGN KEY (shiftrosterassignmentstatus_id)
             REFERENCES rostershiftassignmentstatus (id)
);
