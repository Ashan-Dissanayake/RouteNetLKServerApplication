# RouteNetLK - Fleet, Operation and Service Delivery Management System for SLTB

## 🌴 Overview

RouteNetLK streamlines depot-level operations by automating complex workflows including crew rostering, automated trip planning, trip execution, incident management, and vehicle maintenance. It replaces legacy fragmented systems with a centralized, data-driven platform.

## 🎯 Project Objectives

- **Enhance Service Efficiency:** Simplify weekly roster creation and daily trip scheduling.
- **Digital Transformation:** Eliminate error-prone manual logs with automated, constraint-based scheduling.
- **Centralized Communication:** Improve coordination among depot administrators, crews, and operational managers.
- **Data-Driven Decisions:** Provide insightful analytics on trip execution vs. breakdowns and daily revenue collection.

## 🚀 Key Features

* **Fleet & Permit Management:** Comprehensive vehicle database and validity tracking.
* **Crew Management:** Driver and conductor profiling and fair-way allocation.
* **Automated Trip Planning:** Intelligent roster generation and daily trip scheduling.
* **Incident & Breakdown Handling:** Real-time logging and vehicle re-allocation during transit issues.
* **Fare Collection & Reporting:** Digital and mechanical ticketing aggregation with comprehensive revenue insights.
* **Maintenance & Inventory:** Scheduled bus servicing and spare parts tracking.

## 🏗️ System Architecture & Engineering Highlights

This backend is architected using a **Modular Layered Architecture** combined with robust object-oriented design patterns to ensure scalability, clean separation of concerns, and high maintainability.

### 🧠 Design Patterns & Clean Architecture
* **Metaheuristic Optimization (Timefold):** Integrated the **Timefold** engine to solve complex crew rostering constraints automatically, moving away from tedious manual allocations.
* **Design Patterns Implemented:**
  * **Strategy Pattern (Business-Level Validations):** 
  * Decomposed complex validations out of the service layer into dedicated, single-responsibility strategy classes.
  * Examples include **Uniqueness Validation Strategies**, **Create-New-Entry Validation Strategies**, and modular validation pipelines that execute rules dynamically based on context.
  * **State Pattern:** Cleanly manages complex lifecycle transitions for trips (e.g., *Scheduled* ➔ *Dispatched* ➔ *In-Transit* ➔ *Completed* or *Interrupted*) preventing invalid state jumps.
  * **Clean DTO Mapping (MapStruct):** 
  * Enforced strict separation of concerns between database entities and API contracts. 
  * Utilized **MapStruct** for compile-time, type-safe object mapping, eliminating runtime reflection overhead and boilerplate mapping code.
* **Security** Stateless authentication via Spring Security/JWT

### Technology Stack
* **Core Framework:** Java 17, Spring Boot 3
* **Database:** MySQL Server 8.0.31, Spring Data JPA / Hibernate
* **Optimization Engine:** Timefold Solver
* **Testing & Tooling:** Postman, Maven, JUnit

### System Requirements

#### Development Environment
- **CPU**: Intel Core i5-11300H @ 3.10GHz or equivalent
- **RAM**: 8 GB (recommended)
- **Storage**: 1.5 TB NVMe SSD
- **OS**: Windows 10

#### Production Server
- **CPU**: Intel Core i5 or higher
- **RAM**: Minimum 16 GB
- **Storage**: Minimum 100 GB
- **Database**: MySQL 8.0.31

#### Client Requirements
- **CPU**: Intel Core i3 or equivalent
- **RAM**: Minimum 8 GB
- **Storage**: Minimum 256 GB
- **Browser**: JavaScript-enabled (Chrome, Edge, Firefox)

## 📋 Project Scope

### Included Features
✅ Fleet & Crew Management  
✅ Automated Weekly/Daily Trip Planning  
✅ Incident Reporting & Vehicle Re-allocation  
✅ Fare Collection Tracking  
✅ Vehicle Service & Inventory Management  

### Out of Scope
❌ Live GPS streaming / real-time map tracking (restricted by scope constraints)  
❌ Major accident legal/insurance workflows

## 🔧 Installation & Setup

### Prerequisites
```bash
# Java JDK 17
java -version

# Node.js v20.17.0
node --version

# MySQL Server 8.0.31
mysql --version
```

### Getting Started
1. **Clone the repository**
   ```bash
   git clone https://github.com/Ashan-Dissanayake/RouteNetLKServerApplication
   cd RouteNetLK
   ```

2. **Database Setup**
   ```bash
   # Create database and import schema
   mysql -u root -p < database/db_backup.sql
   ```

3. **Backend Setup**
   ```bash
   # Navigate to backend directory
   cd backend
   #Configure Application Properties
   spring.datasource.url=jdbc:mysql://localhost:3306/routenetlk
   spring.datasource.username=root
   spring.datasource.password=your_password
   # Install dependencies and run
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

4. **Frontend Setup**
   ```bash
   # Navigate to frontend directory
   cd frontend
   npm install
   ng serve
   ```

## 🧪 Testing

The project follows comprehensive testing protocols:
- **Unit Testing**: Individual component testing
- **Integration Testing**: API testing using Postman
- **User Acceptance Testing**: Client validation testing
- **Regression Testing**: Continuous testing during development

## 📊 Development Methodology

This project follows an **Iterative Incremental Development** approach:
1. **Requirements Gathering**: Stakeholder interviews and system analysis
2. **System Design**: Incremental architecture and UI/UX design
3. **Development**: Modular feature development
4. **Testing**: Comprehensive testing at each iteration

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
