CREATE TABLE IF NOT EXISTS routetype (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS scheduletype (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS servicetype (
   id INT NOT NULL AUTO_INCREMENT,
   name VARCHAR(255) NULL DEFAULT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS permitestatus (
     id INT NOT NULL AUTO_INCREMENT,
     name VARCHAR(255) NULL DEFAULT NULL,
     PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS route (
 id INT NOT NULL AUTO_INCREMENT,
 number VARCHAR(255) NULL DEFAULT NULL,
 origin VARCHAR(255) NULL DEFAULT NULL,
 destination VARCHAR(255) NULL DEFAULT NULL,
 distancekm DECIMAL(5,1) NULL DEFAULT NULL,
 scheduletype_id INT NOT NULL,
 routetype_id INT NOT NULL,
 mingapminutes INT NULL DEFAULT NULL,

 PRIMARY KEY (id),

 INDEX fk_route_scheduletype_idx (scheduletype_id ASC),
 INDEX fk_route_routetype_idx (routetype_id ASC),

 CONSTRAINT fk_route_scheduletype
     FOREIGN KEY (scheduletype_id)
         REFERENCES scheduletype (id)
         ON DELETE NO ACTION
         ON UPDATE NO ACTION,

 CONSTRAINT fk_route_routetype
     FOREIGN KEY (routetype_id)
         REFERENCES routetype (id)
         ON DELETE NO ACTION
         ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS permite (
   id INT NOT NULL AUTO_INCREMENT,
   number CHAR(16) NULL DEFAULT NULL,
   vehicle_id INT NOT NULL,
   doissued DATE NULL DEFAULT NULL,
   doexpired DATE NULL DEFAULT NULL,
   branch_id INT NOT NULL,
   permitestatus_id INT NOT NULL,
   servicetype_id INT NOT NULL,
   route_id INT NOT NULL,
   deleted TINYINT(1) NOT NULL DEFAULT 0,

   PRIMARY KEY (id),

   INDEX fk_permite_vehicle_idx (vehicle_id ASC),
   INDEX fk_permite_branch_idx (branch_id ASC),
   INDEX fk_permite_permitestatus_idx (permitestatus_id ASC),
   INDEX fk_permite_servicetype_idx (servicetype_id ASC),
   INDEX fk_permite_route_idx (route_id ASC),

   CONSTRAINT fk_permite_vehicle
       FOREIGN KEY (vehicle_id)
           REFERENCES vehicle (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_permite_branch
       FOREIGN KEY (branch_id)
           REFERENCES branch (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_permite_permitestatus
       FOREIGN KEY (permitestatus_id)
           REFERENCES permitestatus (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_permite_servicetype
       FOREIGN KEY (servicetype_id)
           REFERENCES servicetype (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_permite_route
       FOREIGN KEY (route_id)
           REFERENCES route (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION
);
