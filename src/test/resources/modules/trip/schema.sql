-- 1. Reference Tables
CREATE TABLE IF NOT EXISTS triptype (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS originterminal (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tripstatus (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS overridestatus (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

-- 3. Operational Tables
CREATE TABLE IF NOT EXISTS trip (
    id INT NOT NULL AUTO_INCREMENT,
    branch_id INT NOT NULL,
    triptype_id INT NOT NULL,
    permite_id INT NOT NULL,
    doservice DATE NULL,
    todepature TIME NULL,
    toarrival TIME NULL,
    remarks VARCHAR(45) NULL,
    notrip INT NULL,
    tripstatus_id INT NOT NULL,
    originterminal_id INT NOT NULL,

    PRIMARY KEY (id),

    INDEX fk_trip_permite_idx (permite_id),
    INDEX fk_trip_triptype_idx (triptype_id),
    INDEX fk_trip_branch_idx (branch_id),
    INDEX fk_trip_tripstatus_idx (tripstatus_id),
    INDEX fk_trip_originterminal_idx (originterminal_id),

    CONSTRAINT fk_trip_permite
        FOREIGN KEY (permite_id)
            REFERENCES permite (id),

    CONSTRAINT fk_trip_triptype
        FOREIGN KEY (triptype_id)
            REFERENCES triptype (id),

    CONSTRAINT fk_trip_branch
        FOREIGN KEY (branch_id)
            REFERENCES branch (id),

    CONSTRAINT fk_trip_tripstatus
        FOREIGN KEY (tripstatus_id)
            REFERENCES tripstatus (id),

    CONSTRAINT fk_trip_originterminal
        FOREIGN KEY (originterminal_id)
            REFERENCES originterminal (id)
);

CREATE TABLE IF NOT EXISTS tripvehicleoverride (
   id INT NOT NULL AUTO_INCREMENT,
   dooverride DATE NULL,
   trip_id INT NOT NULL,
   reason VARCHAR(45) NULL,
   vehicle_id INT NOT NULL,
   overridestatus_id INT NOT NULL,

   PRIMARY KEY (id),

   INDEX fk_tripvehicleoverride_vehicle_idx (vehicle_id),
   INDEX fk_tripvehicleoverride_trip_idx (trip_id),
   INDEX fk_tripvehicleoverride_status_idx (overridestatus_id),

   CONSTRAINT fk_tripvehicleoverride_trip
       FOREIGN KEY (trip_id)
           REFERENCES trip (id),

   CONSTRAINT fk_tripvehicleoverride_vehicle
       FOREIGN KEY (vehicle_id)
           REFERENCES vehicle (id),

   CONSTRAINT fk_tripvehicleoverride_status
       FOREIGN KEY (overridestatus_id)
           REFERENCES overridestatus (id)
);
