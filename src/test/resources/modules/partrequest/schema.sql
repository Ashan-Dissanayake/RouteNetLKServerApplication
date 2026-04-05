CREATE TABLE IF NOT EXISTS partrequeststatus (
     id INT NOT NULL AUTO_INCREMENT,
     name VARCHAR(255) NULL DEFAULT NULL,
     PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS partrequest (
       id INT NOT NULL AUTO_INCREMENT,
       branch_id INT NOT NULL,
       number VARCHAR(255) NOT NULL,
       dorequested DATE NOT NULL,
       partrequeststatus_id INT NOT NULL,
       remarks VARCHAR(255) DEFAULT NULL,
       PRIMARY KEY (id),
       INDEX fk_partrequest_branch_idx (branch_id ASC),
       INDEX fk_part_parrequesttstatus_idx (partrequeststatus_id ASC),
       CONSTRAINT fk_partrequest_branch
           FOREIGN KEY (branch_id)
               REFERENCES branch (id)
               ON DELETE NO ACTION
               ON UPDATE NO ACTION,
       CONSTRAINT fk_partrequest_partrequeststatus
           FOREIGN KEY (partrequeststatus_id)
               REFERENCES partstatus (id)
               ON DELETE NO ACTION
               ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS partrequestitem (
   id INT NOT NULL AUTO_INCREMENT,
   partrequest_id INT NOT NULL,
   part_id INT NOT NULL,
   quantity DECIMAL(10,3) DEFAULT NULL,

   PRIMARY KEY (id),

   INDEX fk_partrequestitem_part_idx (part_id ASC),
   INDEX fk_partrequestitem_partrequest_idx (partrequest_id ASC),

   CONSTRAINT fk_partrequestitem_partrequest
       FOREIGN KEY (partrequest_id)
           REFERENCES partrequest (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_partrequestitem_part
       FOREIGN KEY (part_id)
           REFERENCES part (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION
);
