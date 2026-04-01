CREATE TABLE IF NOT EXISTS partstatus (
     id INT NOT NULL AUTO_INCREMENT,
     name VARCHAR(255) NULL DEFAULT NULL,
     PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS partcategory (
                                            id INT NOT NULL AUTO_INCREMENT,
                                            name VARCHAR(255) NULL DEFAULT NULL,
                                            PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS unitofmeasure (
          id INT NOT NULL AUTO_INCREMENT,
          name VARCHAR(255) NULL DEFAULT NULL,
          PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS partmaster (
  id INT NOT NULL AUTO_INCREMENT,
  sku VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  partcategory_id INT NOT NULL,
  unitofmeasure_id INT NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_partmaster_partcategory_idx (partcategory_id ASC),
  INDEX fk_partmaster_unitofmeasure_idx (unitofmeasure_id ASC),
  CONSTRAINT fk_partmaster_partcategory
      FOREIGN KEY (partcategory_id)
          REFERENCES partcategory (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
  CONSTRAINT fk_partmaster_unitofmeasure
      FOREIGN KEY (unitofmeasure_id)
          REFERENCES unitofmeasure (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS part (
       id INT NOT NULL AUTO_INCREMENT,
       branch_id INT NOT NULL,
       partmaster_id INT NOT NULL,
       qoh DECIMAL(10,3) DEFAULT 0,
       maxlevel DECIMAL(10,3) DEFAULT 0,
       rop DECIMAL(10,3) DEFAULT 0,
       dolastordered DATE NOT NULL,
       partstatus_id INT NOT NULL,
       remarks VARCHAR(255) DEFAULT NULL,
       deleted BIT(1) DEFAULT 0,
       PRIMARY KEY (id),
       INDEX fk_part_branch_idx (branch_id ASC),
       INDEX fk_part_partstatus_idx (partstatus_id ASC),
       CONSTRAINT fk_part_branch
           FOREIGN KEY (branch_id)
               REFERENCES branch (id)
               ON DELETE NO ACTION
               ON UPDATE NO ACTION,
       CONSTRAINT fk_part_partstatus
           FOREIGN KEY (partstatus_id)
               REFERENCES partstatus (id)
               ON DELETE NO ACTION
               ON UPDATE NO ACTION,
       CONSTRAINT fk_part_partmaster
           FOREIGN KEY (partmaster_id)
               REFERENCES partmaster (id)
               ON DELETE NO ACTION
               ON UPDATE NO ACTION
);
