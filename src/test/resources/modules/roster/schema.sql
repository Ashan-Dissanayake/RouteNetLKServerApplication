CREATE TABLE IF NOT EXISTS shiftstatus (
   id INT NOT NULL AUTO_INCREMENT,
   name VARCHAR(45),
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rostershiftassignmentstatus (
   id INT NOT NULL AUTO_INCREMENT,
   name VARCHAR(45),
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roster (
    id INT NOT NULL AUTO_INCREMENT,
    branch_id INT NOT NULL,
    dostartofweek DATE,
    doendofweek DATE,
    deleted BIT(1),

    PRIMARY KEY (id),

    INDEX fk_roster_branch_idx (branch_id),

    CONSTRAINT fk_roster_branch
      FOREIGN KEY (branch_id)
          REFERENCES branch (id)
);

CREATE TABLE IF NOT EXISTS shift (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(45),
    tostart TIME,
    toend TIME,
    notrips INT,
    maxhours INT,
    breakminutes INT,
    issplitshift BIT(1),
    shiftstatus_id INT NOT NULL,

    PRIMARY KEY (id),

    INDEX fk_shift_shiftstatus_idx (shiftstatus_id),

    CONSTRAINT fk_shift_shiftstatus
    FOREIGN KEY (shiftstatus_id)
     REFERENCES shiftstatus (id)
);

CREATE TABLE IF NOT EXISTS rostershift (
   id INT NOT NULL AUTO_INCREMENT,
   roster_id INT NOT NULL,
   shift_id INT NOT NULL,
   doshift DATE,
   designation_id INT NOT NULL,

   PRIMARY KEY (id),

   INDEX fk_rostershift_roster_idx (roster_id),
   INDEX fk_rostershift_shift_idx (shift_id),
   INDEX fk_rostershift_designation_idx (designation_id),

   CONSTRAINT fk_rostershift_roster
       FOREIGN KEY (roster_id)
           REFERENCES roster (id),

   CONSTRAINT fk_rostershift_shift
       FOREIGN KEY (shift_id)
           REFERENCES shift (id),

   CONSTRAINT fk_rostershift_designation
       FOREIGN KEY (designation_id)
           REFERENCES designation (id)
);

CREATE TABLE IF NOT EXISTS rostershiftassignment (
     id INT NOT NULL AUTO_INCREMENT,
     rostershift_id INT NOT NULL,
     employee_id INT,
     rostershiftassignmentstatus_id INT NOT NULL,

     PRIMARY KEY (id),

     INDEX fk_assignment_rostershift_idx (rostershift_id),
     INDEX fk_assignment_employee_idx (employee_id),
     INDEX fk_assignment_status_idx (rostershiftassignmentstatus_id),

     CONSTRAINT fk_assignment_rostershift
         FOREIGN KEY (rostershift_id)
             REFERENCES rostershift (id),

     CONSTRAINT fk_assignment_employee
         FOREIGN KEY (employee_id)
             REFERENCES employee (id),

     CONSTRAINT fk_assignment_status
         FOREIGN KEY (rostershiftassignmentstatus_id)
             REFERENCES rostershiftassignmentstatus (id)
);
