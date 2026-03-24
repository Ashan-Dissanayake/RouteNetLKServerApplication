CREATE TABLE IF NOT EXISTS employeestatus (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS employeetype (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS department (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS designation (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS gender (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS employee (
    id INT AUTO_INCREMENT PRIMARY KEY,

    number VARCHAR(50) NOT NULL,
    fullname VARCHAR(100) NOT NULL UNIQUE,
    callingname VARCHAR(50),
    nic VARCHAR(20),
    gender_id INT NOT NULL,
    mobile VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(255),
    emergencycontact VARCHAR(20),
    image LONGBLOB,

    branch_id INT NOT NULL,
    department_id INT NOT NULL,
    designation_id INT NOT NULL,
    employeetype_id INT NOT NULL,
    doj DATE,
    employeestatus_id INT NOT NULL,

    deleted TINYINT(1) NOT NULL DEFAULT 0,

    CONSTRAINT fk_employee_gender
        FOREIGN KEY (gender_id)
            REFERENCES gender(id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,

    CONSTRAINT fk_employee_branch
        FOREIGN KEY (branch_id)
            REFERENCES branch(id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,

    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id)
            REFERENCES department(id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,

    CONSTRAINT fk_employee_designation
        FOREIGN KEY (designation_id)
            REFERENCES designation(id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,

    CONSTRAINT fk_employee_employeetype
        FOREIGN KEY (employeetype_id)
            REFERENCES employeetype(id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,

    CONSTRAINT fk_employee_employeestatus
        FOREIGN KEY (employeestatus_id)
            REFERENCES employeestatus(id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

