CREATE TABLE IF NOT EXISTS branchtype (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS branchstatus (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS regionaloffice (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS branch (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    code VARCHAR(255) NULL DEFAULT NULL,
    address VARCHAR(255) NULL DEFAULT NULL,
    telephone VARCHAR(255) NULL DEFAULT NULL,
    email VARCHAR(255) NULL DEFAULT NULL,
    docreated DATE NULL DEFAULT NULL,
    branchtype_id INT NOT NULL,
    remarks VARCHAR(255) NULL DEFAULT NULL,
    branchstatus_id INT NOT NULL,
    regionaloffice_id INT NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    INDEX fk_branch_branchtype_idx (branchtype_id ASC),
    INDEX fk_branch_branchstatus_idx (branchstatus_id ASC),
    INDEX fk_branch_regionaloffice_idx (regionaloffice_id ASC),
    CONSTRAINT fk_branch_branchtype
      FOREIGN KEY (branchtype_id)
          REFERENCES branchtype (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
    CONSTRAINT fk_branch_branchstatus
      FOREIGN KEY (branchstatus_id)
          REFERENCES branchstatus (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION,
    CONSTRAINT fk_branch_regionaloffice
      FOREIGN KEY (regionaloffice_id)
          REFERENCES regionaloffice (id)
          ON DELETE NO ACTION
          ON UPDATE NO ACTION
);



CREATE TABLE IF NOT EXISTS codetype (
                                        id INT NOT NULL AUTO_INCREMENT,
                                        name VARCHAR(45) NULL,
                                        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS scope (
                                     id INT NOT NULL AUTO_INCREMENT,
                                     name VARCHAR(45) NULL,
                                     PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS docsequance (
                                           id INT NOT NULL AUTO_INCREMENT,
                                           lastvalue INT NULL,
                                           version INT NULL,
                                           periodkey VARCHAR(45) NULL,
                                           codetype_id INT NOT NULL,
                                           scope_id INT NOT NULL,
                                           PRIMARY KEY (id),

                                           CONSTRAINT fk_docsequance_codetype
                                               FOREIGN KEY (codetype_id)
                                                   REFERENCES codetype(id)
                                                   ON DELETE NO ACTION
                                                   ON UPDATE NO ACTION,

                                           CONSTRAINT fk_docsequance_scope
                                               FOREIGN KEY (scope_id)
                                                   REFERENCES scope(id)
                                                   ON DELETE NO ACTION
                                                   ON UPDATE NO ACTION
);
