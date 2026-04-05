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
