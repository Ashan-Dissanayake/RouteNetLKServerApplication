CREATE TABLE IF NOT EXISTS bustype (
   id INT NOT NULL AUTO_INCREMENT,
   name VARCHAR(255) NULL DEFAULT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS vehiclestatus (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS fueltype (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS conditionrate (
     id INT NOT NULL AUTO_INCREMENT,
     name VARCHAR(255) NULL DEFAULT NULL,
     PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS make (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS model (
     id INT NOT NULL AUTO_INCREMENT,
     name VARCHAR(255) NULL DEFAULT NULL,
     make_id INT NOT NULL,

     PRIMARY KEY (id),

     INDEX fk_model_make_idx (make_id ASC),

     CONSTRAINT fk_model_make
         FOREIGN KEY (make_id)
             REFERENCES make (id)
             ON DELETE NO ACTION
             ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS vehicle (
   id INT NOT NULL AUTO_INCREMENT,
   branch_id INT NOT NULL,
   number CHAR(7) NOT NULL,
   model_id INT NOT NULL,
   bustype_id INT NOT NULL,
   mileage INT NOT NULL,
   fueltype_id INT NOT NULL,
   conditionrate_id INT NOT NULL,
   vehiclestatus_id INT NOT NULL,
   remarks VARCHAR(255) NULL DEFAULT NULL,
   deleted TINYINT(1) NOT NULL DEFAULT 0,

   PRIMARY KEY (id),

   UNIQUE INDEX number_UNIQUE (number ASC),

   INDEX fk_vehicle_branch_idx (branch_id ASC),
   INDEX fk_vehicle_model_idx (model_id ASC),
   INDEX fk_vehicle_bustype_idx (bustype_id ASC),
   INDEX fk_vehicle_fueltype_idx (fueltype_id ASC),
   INDEX fk_vehicle_conditionrate_idx (conditionrate_id ASC),
   INDEX fk_vehicle_vehiclestatus_idx (vehiclestatus_id ASC),

   CONSTRAINT fk_vehicle_branch1
       FOREIGN KEY (branch_id)
           REFERENCES branch (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_vehicle_model
       FOREIGN KEY (model_id)
           REFERENCES model (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_vehicle_bustype
       FOREIGN KEY (bustype_id)
           REFERENCES bustype (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_vehicle_fueltype
       FOREIGN KEY (fueltype_id)
           REFERENCES fueltype (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_vehicle_conditionrate
       FOREIGN KEY (conditionrate_id)
           REFERENCES conditionrate (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION,

   CONSTRAINT fk_vehicle_vehiclestatus
       FOREIGN KEY (vehiclestatus_id)
           REFERENCES vehiclestatus (id)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION
);
