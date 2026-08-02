# RouteNetLK - Fleet, Operation and Service Delivery Management System for SLTB (Server Application)

## 🌴 Overview

**RouteNetLK Server Application** is the robust, high-performance backend system engineered for Sri Lanka Transport Board (SLTB) depot management. Built on **Spring Boot 3** and **Java 17**, it powers depot-level operations by automating complex workflows including metaheuristic crew rostering, automated trip planning, trip dispatch execution, breakdown & incident handling, vehicle maintenance, fare collection, and comprehensive operational reporting. It replaces legacy fragmented systems with a secure, RESTful, data-driven platform.

## 🎯 Project Objectives

- **Enhance Operational Efficiency:** Automate complex weekly roster generation and daily trip scheduling using constraint optimization algorithms.
- **Digital Transformation:** Eliminate error-prone manual logs with automated validation pipelines and automated lifecycle state transitions.
- **Centralized Data & Communication:** Provide centralized RESTful API endpoints for depot officers, maintenance engineers, and administration modules.
- **Data-Driven Analytics:** Aggregate daily revenue collection, vehicle breakdown metrics, and operational performance metrics for decision-making.

## 🚀 Key Features

* **Planning & Scheduling:**
  * **Employee & Crew Allocation:** Driver and conductor profiling, license validation, and fair-workload distribution logic.
  * **Permit Management:** Registration, route assignment, and validity constraint enforcement for SLTB route permits.
  * **Automated Rostering (Timefold Engine):** Metaheuristic constraint solver for automated crew assignment and shift generation.
* **Depot Operations:**
  * **Trip Execution & State Engine:** Real-time trip status tracking (*Scheduled* ➔ *Dispatched* ➔ *In-Transit* ➔ *Completed* / *Interrupted*).
  * **Incident & Breakdown Re-Allocation:** Real-time incident logging and automated alternative vehicle allocation during transit breakdowns.
  * **Fare Collection Aggregation:** Aggregation of manual paper ticketing and Electronic Ticketing Machine (ETM) collections with daily revenue insights.
* **Maintenance & Inventory:**
  * **Fleet & Vehicle Registry:** Database tracking vehicle availability, fitness certificates, and depot assignments.
  * **Vehicle Service Logging:** Scheduled preventive maintenance, service history, and repair management.
  * **Spare Parts & Inventory (GRN):** Spare parts catalog management, inventory adjustments, internal part requests, and Goods Received Notes (GRN).
* **Operational Reporting & Analytics:**
  * Aggregated endpoints for Dispatch Summaries, Revenue by Payment Method, Maintenance Trends, Fleet Utilization, and Incident Breakdown reports.
* **System Administration & Security:**
  * **Stateless JWT Security:** Token-based authentication and role-based authorization.
  * **User & Privilege Management:** Dynamic user management, privilege assignments, and depot/branch configuration.
  * **Notification Engine:** Automated email notifications via Spring Mail and Thymeleaf templates.

## 🏗️ System Architecture & Engineering Highlights

This backend is architected using a **Modular Layered Architecture** combined with enterprise design patterns to ensure high performance, maintainability, and clean separation of concerns.

### 🧠 Design Patterns & Clean Architecture
* **Metaheuristic Optimization (Timefold Solver):** Integrated **Timefold Solver 1.32** to automatically solve complex multi-constraint driver/conductor shift allocation problems, eliminating manual roster generation errors.
* **Design Patterns Implemented:**
  * **Strategy Pattern (Business Validations):** Decomposed business validation rules into single-responsibility strategy implementations (e.g., *Uniqueness Validation Strategies*, *Create-New-Entry Strategies*), executed dynamically based on operational context.
  * **State Pattern (Trip Lifecycle):** Enforces rigid state transitions for transit dispatches, preventing invalid state jumps and guaranteeing audit compliance.
  * **Compile-Time DTO Mapping (MapStruct):** Enforces strict separation between JPA entities and API DTO contracts using **MapStruct 1.5** and **Lombok**, eliminating reflection runtime overhead.
* **Stateless Authentication & Security:** Integrated Spring Security with **JJWT 0.11** for stateless token authentication and fine-grained method-level security.

### Technology Stack
* **Core Framework:** Java 17, Spring Boot 3.5.3
* **Database & Persistence:** MySQL 8.0.33, Spring Data JPA / Hibernate
* **Optimization Engine:** Timefold Solver Starter 1.32.0
* **Security & Auth:** Spring Security, JJWT (io.jsonwebtoken 0.11.5)
* **DTO Mapping & Tooling:** MapStruct 1.5.5, Project Lombok 1.18.30
* **Notifications:** Spring Boot Starter Mail, Thymeleaf
* **Testing & Quality Assurance:** JUnit 5, Spring Security Test, Testcontainers (MySQL), Postman, Maven

### System Requirements

#### Production / Development Server
- **CPU**: Intel Core i5 @ 3.0GHz or AMD Ryzen 5 equivalent (Recommended: Intel Core i7 / AMD Ryzen 7)
- **RAM**: Minimum 8 GB (Recommended: 16 GB for Timefold Solver optimizations)
- **Storage**: 100 GB NVMe SSD space
- **Database**: MySQL Server 8.0.31+
- **Java Runtime**: OpenJDK 17 or Oracle JDK 17

#### Client Web Browser Requirements
- **Supported Browsers**: Google Chrome, Microsoft Edge, Firefox, Safari (JavaScript enabled)

## 📋 Project Scope

### Included Features
✅ Fleet & Crew Management  
✅ Metaheuristic Weekly Roster & Daily Trip Planning  
✅ Breakdown Logging & Instant Vehicle Re-allocation  
✅ Fare Collection & Multi-Source Revenue Aggregation  
✅ Vehicle Service Records, Spare Part Inventory & GRN Workflow  
✅ Stateless JWT Authentication & Role-Based Authorization  
✅ Spring Mail Automated Email Notifications  

### Out of Scope
❌ Live GPS real-time map tracking on backend streams (restricted by hardware scope)  
❌ Major legal accident insurance claims processing  

## 🔧 Installation & Setup

### Prerequisites
Ensure Java JDK 17 and MySQL Server are installed:
```bash
# Verify Java JDK 17
java -version

# Verify Maven
mvn -v

# Verify MySQL Server
mysql --version
```

### Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/Ashan-Dissanayake/RouteNetLKServerApplication
   cd RouteNetLKServerApplication
   ```

2. **Database Setup**
   ```bash
   # Connect to MySQL and create the database
   mysql -u root -p
   CREATE DATABASE routenetlk;
   EXIT;

   # Import initial schema and data backup (if available)
   mysql -u root -p routenetlk < database/db_backup.sql
   ```

3. **Configure Database Connection**  
   Edit `src/main/resources/application.properties` (or `application.yml`):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/routenetlk?createDatabaseIfNotExist=true&useSSL=false
   spring.datasource.username=your_mysql_user
   spring.datasource.password=your_mysql_password
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Build and Run the Server Application**
   ```bash
   # Clean compile and install dependencies
   ./mvnw clean install

   # Run Spring Boot Application
   ./mvnw spring-boot:run
   ```
   The backend API will start on `http://localhost:8080`.

5. **Client Application Integration**  
   To run the frontend dashboard, refer to the [RouteNetLK Client Application Repository](https://github.com/Ashan-Dissanayake/RouteNetLKClientApplication).

## 🧪 Testing

The server application includes unit, integration, and API testing suites:

- **Unit & Constraint Solver Testing:** Tested with JUnit 5 and `timefold-solver-test`.
  ```bash
  ./mvnw test
  ```
- **Database Integration Testing:** Integration tests run against isolated MySQL containers via **Testcontainers**.
- **API Contract Testing:** Validated using Postman API test collections.

## 📊 Development Methodology

This project follows an **Iterative Incremental Development** approach:
1. **Requirements & Domain Modeling:** Stakeholder interviews and UCSC system analysis.
2. **Database Schema & Architecture Design:** Entity-relationship modeling, DTO abstraction, and validation strategy design.
3. **Core Development & Solver Tuning:** Spring Boot REST controller building and Timefold Solver constraint score balancing.
4. **Integration Testing & Security Hardening:** JWT token verification, Testcontainers integration testing, and Postman API verification.

## 🎓 Academic Context

This project was developed as part of the **IT5106 - Software Development Project** at University of Colombo School of Computing (UCSC).

## 📞 Contact

**Developer**: Ashan Dissanayake  
**Email**: ashanpathum899@gmail.com  
**Phone**: +94 71 60 42 647  

## 🤝 Contributing

This is an academic project, but contributions and suggestions are welcome for educational purposes.

## 📄 License

This project is developed for academic purposes as part of a university degree program.

---

*Empowering Sri Lanka's public transport through innovative digital solutions.*
