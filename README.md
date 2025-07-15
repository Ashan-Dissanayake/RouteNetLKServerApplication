# Full-Stack Application

A modern web application built with Angular frontend and Spring Boot backend for Permit,Route and Funds Management System of NTC.

## 🚀 Tech Stack

**Frontend:**
- Angular 18 (LTS)
- TypeScript
- Angular Material
- RxJS

**Backend:**
- Spring Boot 3.x
- Java 17+
- Spring Security
- Spring Data JPA
- MySQL/PostgreSQL

**Tools:**
- Maven
- Node.js & npm
- Git
- IntelijIdea
- MySQL Workbentch
- Postman 

## 📋 Prerequisites

Before running this application, make sure you have:

- **Node.js** (v18.x or later)
- **Java** (17 or later)
- **Maven** (3.6 or later)
- **MySQL** (for database)
- **Git**

## 🏗️ Project Structure

```
project-root/
├── client/                 # Angular frontend application
│   ├── src/
│   ├── package.json
│   └── angular.json
├── server/                 # Spring Boot backend application
│   ├── src/
│   ├── pom.xml
│   └── application.properties
├── shared/                 # Shared utilities and types
├── docs/                   # Documentation
└── README.md
```

## 🔧 Installation & Setup

### 1. Clone Repository
```bash
git clone https://github.com/Ashan-Dissanayake/NTCApplication
cd your-project
```

### 2. Database Setup
```bash
Option A: Using MySQL Workbench (Recommended)

Open MySQL Workbench
Connect to your MySQL server
Import SQL backup file:

Go to Server → Data Import
Select Import from Self-Contained File
Browse and select database/backup.sql
Choose Default Target Schema or create new schema
Click Start Import

Option B: Command Line Import
bash# Navigate to project root
cd your-project

# Import SQL backup
mysql -u your_username -p your_database_name < database/backup.sql
```
### 3. Backend Setup
```bash
cd server
# Update application.properties with your database credentials
mvn clean install
mvn spring-boot:run
```
Backend will run on `http://localhost:8080`

### 4. Frontend Setup
```bash
cd client
npm install
ng serve
```
Frontend will run on `http://localhost:4200`

## 🚀 Quick Start

### Development Mode
```bash
# Terminal 1 - Start backend
cd server
mvn spring-boot:run

# Terminal 2 - Start frontend
cd client
ng serve
```

## 📦 Available Scripts

### Frontend (client/)
```bash
npm start          # Start development server
npm run build      # Build for production
npm test           # Run unit tests
npm run e2e        # Run end-to-end tests
npm run lint       # Run linting
```

### Backend (server/)
```bash
mvn spring-boot:run    # Start development server
mvn clean package     # Build for production
mvn test              # Run tests
mvn clean install     # Clean and install dependencies
```

## 🔍 API Documentation

The REST API is available at `http://localhost:8080/api`
*For detailed API documentation, visit: `http://localhost:8080/swagger-ui.html`*

## 🧪 Testing

### Frontend Tests
```bash
cd client
npm test                    # Unit tests
npm run test:coverage       # Coverage report
```

### Backend Tests
```bash
cd server
mvn test                    # Unit tests
```

## 🔒 Environment Variables

### Backend (.env or application.properties)
```properties
# Database
DB_URL=jdbc:mysql://localhost:3306/ntcsrilanka
DB_USERNAME=ntcuser
DB_PASSWORD=1234

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400
```

### Frontend (environment.ts)
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  appName: 'NTCApplication'
};
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style Guidelines
- Follow Angular style guide for frontend
- Follow Java conventions for backend
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed

## 👥 Authors

- **Your Name** - *Initial work* - https://github.com/Ashan-Dissanayake

## 🙏 Acknowledgments

- Angular Team for the amazing framework
- Spring Boot Team for the excellent backend framework
- All contributors who helped with this project
