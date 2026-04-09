CREATE TABLE IF NOT EXISTS grnstatus (
     id INT NOT NULL AUTO_INCREMENT,
     name VARCHAR(255) NULL DEFAULT NULL,
     PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS grn (
       id INT NOT NULL AUTO_INCREMENT,
       branch_id INT NOT NULL,
       number VARCHAR(255) NOT NULL,
       doreceived DATE NOT NULL,
       partrequest_id INT NOT NULL,
       grnstatus_id INT NOT NULL,
       remarks VARCHAR(255) DEFAULT NULL,
       PRIMARY KEY (id),
       INDEX fk_grn_branch_idx (branch_id ASC),
       INDEX fk_partrequest_idx (partrequest_id ASC),
       INDEX fk_grnstatus_idx (grnstatus_id ASC),
       CONSTRAINT fk_grn_branch
           FOREIGN KEY (branch_id)
               REFERENCES branch (id)
               ON DELETE NO ACTION
               ON UPDATE NO ACTION,
       CONSTRAINT fk_grn_partrequest
           FOREIGN KEY (partrequest_id)
               REFERENCES partrequest (id)
               ON DELETE NO ACTION
               ON UPDATE NO ACTION,
       CONSTRAINT fk_grn_grnstatus
           FOREIGN KEY (grnstatus_id)
               REFERENCES grnstatus (id)
               ON DELETE NO ACTION
               ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS grnpartrequestitem (
   id INT NOT NULL AUTO_INCREMENT,
   grn_id INT NOT NULL,
   partrequestitem_id INT NOT NULL,
   quantity DECIMAL(10,3) DEFAULT NULL,

   PRIMARY KEY (id),

   INDEX fk_grnpartrequestitem_grn_idx (grn_id ASC),
   INDEX fk_grnpartrequestitem_partrequestitem_idx (partrequestitem_id ASC),

   CONSTRAINT fk_grnpartrequestitem_partrequestitem
       FOREIGN KEY (partrequestitem_id)
           REFERENCES partrequestitem (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_grnpartrequestitem_grn
       FOREIGN KEY (grn_id)
           REFERENCES grn (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION
);
