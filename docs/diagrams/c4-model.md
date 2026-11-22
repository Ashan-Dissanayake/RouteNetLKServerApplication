# C4 Model - RouteNetLK System

This document provides a C4 model representation of the RouteNetLK system, covering System Context, Container, Component, and Code levels where applicable.

## 1. Level 1: System Context Diagram

The System Context diagram provides a high-level view of how the RouteNetLK system interacts with its users and external systems.

```mermaid
C4Context
    title System Context Diagram for RouteNetLK

    Person(admin, "System Administrator", "Manages branches, employees, vehicles, and system configurations.")
    Person(operator, "Branch Operator", "Manages rosters, trips, and day-to-day branch operations.")
    
    System(routenet, "RouteNetLK System", "Automated bus rostering and transport management system.")
    
    System_Ext(email, "Email System", "External SMTP server for sending notifications.")
    
    Rel(admin, routenet, "Uses", "HTTPS")
    Rel(operator, routenet, "Uses", "HTTPS")
    Rel(routenet, email, "Sends emails using", "SMTP")
```

## 2. Level 2: Container Diagram

The Container diagram shows the high-level technical architecture of the RouteNetLK system.

```mermaid
C4Container
    title Container Diagram for RouteNetLK

    Person(user, "User", "Administrator or Operator")
    
    System_Boundary(c1, "RouteNetLK System") {
        Container(spa, "Single Page Application", "Angular", "Provides the user interface for management.")
        Container(api, "API Application", "Java, Spring Boot", "Handles business logic, security, and scheduling.")
        ContainerDb(db, "Database", "MySQL", "Stores user, branch, vehicle, and roster data.")
    }
    
    System_Ext(email, "Email System", "External SMTP server")
    
    Rel(user, spa, "Uses", "HTTPS")
    Rel(spa, api, "Makes API calls to", "JSON/HTTPS")
    Rel(api, db, "Reads/Writes to", "JDBC/JPA")
    Rel(api, email, "Sends emails using", "SMTP")
```

## 3. Level 3: Component Diagram (API Application)

This diagram focuses on the internal components of the Spring Boot API Application.

```mermaid
C4Component
    title Component Diagram for API Application

    Container(spa, "Single Page Application", "Angular", "User Interface")
    
    Container_Boundary(api, "API Application") {
        Component(security, "Security Component", "Spring Security, JWT", "Handles authentication and authorization.")
        Component(roster, "Roster Module", "Spring Boot, Timefold", "Optimizes and manages bus rosters.")
        Component(trip, "Trip Module", "Spring Boot", "Manages trip schedules and execution.")
        Component(shared, "Shared Infrastructure", "Spring Boot", "Common utilities, exceptions, and auditing.")
        Component(persistence, "Data Access", "Spring Data JPA", "Abstracts database operations.")
    }
    
    ContainerDb(db, "Database", "MySQL")
    
    Rel(spa, security, "Authenticates via", "HTTPS")
    Rel(security, roster, "Allows access to")
    Rel(security, trip, "Allows access to")
    Rel(roster, persistence, "Uses")
    Rel(trip, persistence, "Uses")
    Rel(shared, roster, "Provides utilities to")
    Rel(shared, trip, "Provides utilities to")
    Rel(persistence, db, "Reads/Writes", "JDBC")
```

## 4. Level 4: Code (Architecture Overview)

The code is organized using a **Module-based Layered Architecture**:

- **Controller Layer**: Handles HTTP requests and maps them to DTOs.
- **Service Layer**: Contains business logic and interacts with specialized engines like Timefold.
- **Repository Layer**: Manages data persistence using Spring Data JPA.
- **Model Layer**: Defines Entities and DTOs.
- **Shared Layer**: Contains cross-cutting concerns like global exception handling, soft delete aspects, and auditing.

### Key Technical Decisions
- **Roster Optimization**: Uses **Timefold Solver** with Interval Trees for efficient scheduling.
- **Data Isolation**: Implements **Hibernate Filters** and AOP to ensure users only see data related to their branch.
- **Security**: Stateless **JWT** authentication with brute-force protection.
