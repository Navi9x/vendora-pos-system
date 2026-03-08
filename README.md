# Vendora POS System

Multi-tenant Point of Sale system with comprehensive inventory management, sales tracking, and JWT authentication built using Spring Boot and Angular.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17.x-red.svg)](https://angular.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-blue.svg)](https://www.typescriptlang.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue.svg)](https://www.mysql.com/)

---

## 🚀 Overview

Vendora is a modern, cloud-ready POS system designed for multi-tenant retail businesses. Built with enterprise-grade architecture, it offers real-time inventory tracking, comprehensive sales analytics, and secure authentication.

### Key Features

- 🏢 **Multi-Tenancy** - Isolated data for multiple businesses on a single platform
- 📊 **Real-time Dashboard** - Live sales analytics and business metrics
- 📦 **Inventory Management** - Track stock levels, low stock alerts, product variations
- 💰 **Sales Processing** - Fast POS interface with multiple payment methods
- 📈 **Sales History** - Comprehensive transaction history with filters
- 👥 **Customer Management** - Track customer purchases and preferences
- 🔐 **JWT Authentication** - Secure, token-based authentication
- 📱 **Responsive Design** - Works seamlessly on desktop, tablet, and mobile
- 📚 **API Documentation** - Interactive Swagger UI

---

## 📸 Screenshots

### Dashboard
Real-time business metrics, sales overview, and quick actions.

![Dashboard Overview](./screenshots/Dashboard1.png)
![Dashboard Analytics](./screenshots/Dashboard2.png)
![Dashboard Activities](./screenshots/Dashboard3.png)

### Inventory Management
Comprehensive product catalog with stock tracking and low-stock alerts.

![Inventory](./screenshots/Inventory.png)

### Sales History
Track all transactions with advanced filtering and export capabilities.

![Sales History](./screenshots/sales-history.png)

### Point of Sale
Fast, intuitive interface for processing customer orders.

![Create Sale](./screenshots/create-sale.png)

### Product Management
Add and manage products with detailed information including pricing, inventory, and pharmacy data.

![Add Product](./screenshots/add-new-product.png)

---

## 🛠️ Tech Stack

### Backend
- **Spring Boot 4.0.1** - Core framework
- **Spring Security** - JWT authentication & authorization
- **Spring Data JPA** - Database abstraction
- **MySQL 8.x** - Primary database
- **Hibernate** - ORM
- **Lombok** - Reduce boilerplate code
- **ModelMapper** - Object mapping
- **JWT (JJWT 0.11.5)** - Token generation & validation
- **SpringDoc OpenAPI** - API documentation (Swagger UI)
- **Maven** - Dependency management

### Frontend
- **Angular 19.2.19** - Frontend framework
- **TypeScript** - Programming language
- **Angular Material** - UI components
- **SCSS** - Styling

### Architecture
- **Multi-tenant** - Separate data per business
- **RESTful API** - Standard HTTP methods
- **JWT** - Stateless authentication
- **MVC Pattern** - Clean separation of concerns

### Key Dependencies

**Backend:**
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Web MVC
- Spring Boot Starter Validation
- MySQL Connector/J
- Lombok
- ModelMapper 3.2.4
- JJWT 0.11.5 (JWT implementation)
- SpringDoc OpenAPI 2.7.0

**Testing:**
- Spring Boot Starter Test
- Spring Security Test
- Spring Data JPA Test

---

## 📋 Prerequisites

Before running this project, ensure you have:

- **Java 17** installed
- **Node.js 20** and npm installed
- **MySQL 8+** database server
- **Maven 3.8+** for backend
- **Angular CLI** for frontend

---

## ⚙️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Navi9x/vendora-pos-system.git
cd vendora-pos-system
```

### 2. Backend Setup

#### Configure Database

Create a MySQL database:

```sql
CREATE DATABASE vendora_pos;
CREATE USER 'vendora_user'@'localhost' IDENTIFIED BY 'db_password';
GRANT ALL PRIVILEGES ON vendora_pos.* TO 'vendora_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Update Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/vendora_pos?useSSL=false&serverTimezone=UTC
spring.datasource.username=vendora_user
spring.datasource.password=db_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=secret-key-here-min-256-bits-long
jwt.expiration=86400000

# Server Port
server.port=8080

# Swagger UI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

#### Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will start at `http://localhost:8080`

### 3. Frontend Setup

#### Install Dependencies

```bash
cd frontend
npm install
```

#### Update API Endpoint

Edit `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

#### Run Frontend

```bash
ng serve
```

Frontend will start at `http://localhost:4200`

---

## 🗂️ Project Structure

```
vendora-pos-system/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/vendora/pos/
│   │   │   │   ├── config/          # Security, JWT configs
│   │   │   │   ├── controller/      # REST endpoints
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── entity/          # JPA entities
│   │   │   │   ├── repository/      # Database repositories
│   │   │   │   ├── service/         # Business logic
│   │   │   │   └── security/        # Auth components
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── dashboard/
│   │   │   │   ├── inventory/
│   │   │   │   ├── sales/
│   │   │   │   ├── sales-history/
│   │   │   │   └── products/
│   │   │   ├── services/
│   │   │   ├── models/
│   │   │   └── guards/
│   │   ├── assets/
│   │   └── environments/
│   ├── angular.json
│   └── package.json
│
└── README.md
```

---

## 🔑 API Endpoints

### Authentication
```
POST   /api/auth/login          - User login
POST   /api/auth/register       - User registration
POST   /api/auth/refresh        - Refresh JWT token
```

### Sales
```
GET    /api/sales               - Get all sales (paginated)
POST   /api/sales               - Create new sale
GET    /api/sales/{id}          - Get sale by ID
GET    /api/sales/invoice/{invoiceNumber} - Get sale by invoice
```

### Inventory
```
GET    /api/inventory           - Get all inventory (paginated)
POST   /api/inventory           - Add new product
PUT    /api/inventory/{id}      - Update product
DELETE /api/inventory/{id}      - Delete product
POST   /api/inventory/restock   - Restock product
```

### Products
```
GET    /api/products            - Get all products
GET    /api/products/{id}       - Get product by ID
GET    /api/products/sku/{sku}  - Get product by SKU
```

### Dashboard
```
GET    /api/dashboard/stats     - Get dashboard statistics
GET    /api/dashboard/recent-transactions - Recent sales
GET    /api/dashboard/top-products - Top selling products
```

---

## 📚 API Documentation

This project uses **SpringDoc OpenAPI** for interactive API documentation.

After starting the backend, access:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

The Swagger UI provides:
- Interactive API testing
- Request/Response examples
- Schema definitions
- Authentication testing

---

## 🔐 Authentication Flow

1. User logs in with credentials
2. Backend validates and returns JWT token
3. Frontend stores token in localStorage
4. Token included in Authorization header for all requests
5. Backend validates token on each request
6. Token expires after 24 hours (configurable)

**JWT Configuration:**
- Algorithm: HS256
- Library: JJWT 0.11.5
- Token format: `Bearer <token>`

---

## 🌟 Core Features Explained

### Multi-Tenancy
Each business has isolated data using a `business_id` field in all tables. Row-level security ensures data separation.

### Inventory Management
- Real-time stock tracking
- Low stock alerts (configurable thresholds)
- Product variants (size, color, etc.)
- Barcode/SKU support
- Image upload support
- Pharmacy-specific fields (generic name, expiry date, batch number)

### Sales Processing
- Fast product search
- Multiple payment methods (Cash, Card, Digital Wallet)
- Customer assignment (optional)
- Order types (Dine-in, Takeaway, Delivery)
- Automatic invoice generation

### Dashboard Analytics
- Total sales & revenue
- Products sold count
- Average transaction value
- Sales trends (daily, weekly, monthly)
- Top selling products
- Category-wise breakdown
- Stock alerts

---

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

---

## 💡 Project Highlights

### Technical Skills Demonstrated

**Backend Development:**
- RESTful API design and implementation
- JWT authentication & authorization
- Multi-tenant architecture
- Database modeling with JPA/Hibernate
- Data validation and error handling
- Dependency injection with Spring

**Frontend Development:**
- Component-based architecture with Angular
- Reactive programming with RxJS
- State management
- Form validation
- Responsive design
- TypeScript & SCSS

**Database Design:**
- Normalized database schema
- Entity relationships (One-to-Many, Many-to-One)
- Query optimization
- Data integrity constraints

**Security:**
- JWT token-based authentication
- Password encryption
- Role-based access control
- Multi-tenant data isolation

**Code Quality:**
- Clean code principles
- MVC/MVVM patterns
- DTO pattern for data transfer
- Service layer architecture
- Lombok for boilerplate reduction
- ModelMapper for object mapping

---

## 👨‍💻 Developer

**Profile**
- 📧 Email: navidurashoda@icloud.com 
- 💼 LinkedIn: [navidu-rathnayaka](https://linkedin.com/in/navidu-rathnayaka)
- 🐙 GitHub: [@Navi9x](https://github.com/Navi9x)

---

## 📄 About This Project

This is a **personal project** developed to demonstrate full-stack development skills using modern technologies. It showcases:

- **Real-world application** - Solving actual business problems
- **Production-ready code** - Following best practices and design patterns
- **Scalable architecture** - Multi-tenant design for growth
- **Modern tech stack** - Using industry-standard frameworks
- **Attention to detail** - Professional UI/UX design

**Development Timeline:** [35-40 Days]

**Purpose:** Portfolio project showcasing full-stack development capabilities for job opportunities

---

## 🎯 Future Enhancements

**Phase 1 - Core Features:**
- [x] Multi-tenant POS system
- [x] Inventory management
- [x] Sales tracking & history
- [x] Dashboard analytics
- [x] JWT authentication

**Phase 2 - Mobile & Expansion:**
- [ ] React Native mobile app for customers
- [ ] Customer-facing e-commerce platform
- [ ] Self-pickup order system

**Phase 3 - Local Market Integration:**
- [ ] Sri Lankan payment gateways (PayHere, Koko)
- [ ] Multi-language support (Sinhala, Tamil, English)
- [ ] WhatsApp notifications
- [ ] Integration with local delivery services

**Phase 4 - Advanced Features:**
- [ ] Advanced analytics & reporting
- [ ] Barcode scanner integration
- [ ] Email receipt system
- [ ] Loyalty program management
- [ ] Employee management module

---

## 🇱🇰 Vision

Building a comprehensive POS ecosystem for Sri Lankan small businesses:
1. **Affordable POS system** - Help businesses digitize operations
2. **Centralized marketplace** - Aggregate products from multiple businesses
3. **Customer mobile app** - Free e-commerce for POS users
4. **Data-driven insights** - Help businesses make better decisions

This creates a **network effect** where more businesses = more products = more customers = more value for everyone.

---

**Made with ❤️ for small businesses in Sri Lanka**
