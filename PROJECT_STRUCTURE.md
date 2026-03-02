# Project Structure

Complete file structure of the Multi-Vendor Vehicle Rental Booking Platform.

```
Multi-Vendor Vehicle Rental Booking Platform/
│
├── README.md                           # Main project documentation
├── QUICKSTART.md                       # Quick setup guide
├── DEPLOYMENT.md                       # Production deployment guide
├── API_DOCUMENTATION.md                # Complete API reference
├── .gitignore                          # Git ignore rules
│
├── backend/                            # Spring Boot Backend
│   ├── README.md                       # Backend-specific documentation
│   ├── pom.xml                         # Maven dependencies
│   │
│   └── src/
│       └── main/
│           ├── java/com/vehiclerental/
│           │   │
│           │   ├── VehicleRentalApplication.java    # Main Spring Boot application
│           │   │
│           │   ├── config/                           # Configuration classes
│           │   │   ├── SecurityConfig.java          # Security & JWT configuration
│           │   │   ├── SwaggerConfig.java           # API documentation config
│           │   │   └── SchedulerConfig.java         # Background task configuration
│           │   │
│           │   ├── controller/                       # REST API Controllers
│           │   │   ├── AuthController.java          # Authentication endpoints
│           │   │   ├── CustomerController.java      # Customer operations
│           │   │   ├── OwnerController.java         # Owner operations
│           │   │   ├── AdminController.java         # Admin operations
│           │   │   ├── VehicleController.java       # Vehicle operations
│           │   │   └── BookingController.java       # Booking operations
│           │   │
│           │   ├── dto/                              # Data Transfer Objects
│           │   │   ├── LoginRequest.java            # Login request payload
│           │   │   ├── RegisterRequest.java         # Registration payload
│           │   │   ├── AuthResponse.java            # Authentication response
│           │   │   ├── VehicleDTO.java              # Vehicle data transfer
│           │   │   ├── BookingDTO.java              # Booking data transfer
│           │   │   └── SearchRequest.java           # Search filter payload
│           │   │
│           │   ├── exception/                        # Exception handling
│           │   │   ├── ResourceNotFoundException.java    # 404 exceptions
│           │   │   └── GlobalExceptionHandler.java      # Global error handler
│           │   │
│           │   ├── model/                            # JPA Entities
│           │   │   ├── User.java                    # User entity (Customer/Owner/Admin)
│           │   │   ├── Shop.java                    # Shop entity
│           │   │   ├── Vehicle.java                 # Vehicle entity
│           │   │   ├── Booking.java                 # Booking entity
│           │   │   ├── Payment.java                 # Payment entity
│           │   │   └── Review.java                  # Review entity
│           │   │
│           │   ├── repository/                       # Data Access Layer
│           │   │   ├── UserRepository.java          # User database operations
│           │   │   ├── ShopRepository.java          # Shop database operations
│           │   │   ├── VehicleRepository.java       # Vehicle database operations
│           │   │   ├── BookingRepository.java       # Booking database operations
│           │   │   ├── PaymentRepository.java       # Payment database operations
│           │   │   └── ReviewRepository.java        # Review database operations
│           │   │
│           │   ├── security/                         # Security components
│           │   │   ├── JwtTokenProvider.java        # JWT token generation/validation
│           │   │   ├── JwtAuthenticationFilter.java # JWT authentication filter
│           │   │   └── UserDetailsServiceImpl.java  # User details for authentication
│           │   │
│           │   └── service/                          # Business Logic Layer
│           │       ├── AuthService.java             # Authentication service
│           │       ├── UserService.java             # User management service
│           │       ├── ShopService.java             # Shop management service
│           │       ├── VehicleService.java          # Vehicle management service
│           │       ├── BookingService.java          # Booking lifecycle service
│           │       ├── SchedulerService.java        # Background task scheduler
│           │       ├── EmailService.java            # Email notification service
│           │       ├── ReviewService.java           # Review management service
│           │       └── PaymentService.java          # Payment processing service
│           │
│           └── resources/
│               ├── application.properties            # Application configuration
│               └── schema.sql                        # Oracle database schema
│
└── frontend/                           # Frontend (HTML/CSS/JS)
    ├── README.md                       # Frontend-specific documentation
    │
    ├── index.html                      # Landing page
    ├── login.html                      # Login page
    ├── register.html                   # Registration page
    ├── customer-dashboard.html         # Customer dashboard
    ├── owner-dashboard.html            # Owner dashboard
    ├── admin-dashboard.html            # Admin dashboard
    ├── vehicle-search.html             # Vehicle search page
    ├── vehicle-details.html            # Vehicle details page
    │
    ├── css/                            # Stylesheets
    │   ├── style.css                   # Main styles (600+ lines)
    │   ├── dashboard.css               # Dashboard-specific styles
    │   └── responsive.css              # Mobile/tablet responsiveness
    │
    └── js/                             # JavaScript modules
        ├── api.js                      # API wrapper with JWT authentication
        ├── auth.js                     # Authentication guards & utilities
        ├── customer.js                 # Customer dashboard logic
        ├── owner.js                    # Owner dashboard logic
        ├── admin.js                    # Admin dashboard logic
        └── utils.js                    # Utility functions
```

## File Count Summary

### Backend
- **Configuration**: 3 files (Security, Swagger, Scheduler)
- **Controllers**: 6 files (Auth, Customer, Owner, Admin, Vehicle, Booking)
- **DTOs**: 6 files (Request/Response objects)
- **Exceptions**: 2 files (Custom exceptions + handler)
- **Models**: 6 files (Entities)
- **Repositories**: 6 files (Data access)
- **Security**: 3 files (JWT provider, filter, user details)
- **Services**: 9 files (Business logic)
- **Resources**: 2 files (application.properties, schema.sql)
- **Main Application**: 1 file (VehicleRentalApplication.java)

**Total Backend**: 44 Java files + 2 config files = **46 files**

### Frontend
- **HTML Pages**: 8 files
- **CSS Files**: 3 files
- **JavaScript Modules**: 6 files

**Total Frontend**: **17 files**

### Documentation
- README.md (main)
- backend/README.md
- frontend/README.md
- QUICKSTART.md
- DEPLOYMENT.md
- API_DOCUMENTATION.md
- .gitignore

**Total Documentation**: **7 files**

### Grand Total: **70 files**

## Key Directories Explained

### `/backend/src/main/java/com/vehiclerental/config/`
Contains Spring configuration classes:
- **SecurityConfig**: Configures Spring Security, JWT filters, CORS, and role-based access
- **SwaggerConfig**: Sets up OpenAPI/Swagger documentation
- **SchedulerConfig**: Enables scheduled tasks for auto-cancellation

### `/backend/src/main/java/com/vehiclerental/controller/`
REST API endpoints organized by role:
- **AuthController**: Public authentication endpoints (register, login)
- **CustomerController**: Customer-specific operations (`@PreAuthorize('ROLE_CUSTOMER')`)
- **OwnerController**: Owner-specific operations (`@PreAuthorize('ROLE_OWNER')`)
- **AdminController**: Admin-only operations (`@PreAuthorize('ROLE_ADMIN')`)
- **VehicleController**: Public vehicle browsing
- **BookingController**: Shared booking operations

### `/backend/src/main/java/com/vehiclerental/service/`
Business logic layer:
- **BookingService**: Core booking lifecycle, overlap detection, cancellation
- **SchedulerService**: Background job running every 10 minutes
- **EmailService**: SMTP email notifications
- **AuthService**: User registration and JWT token generation

### `/backend/src/main/java/com/vehiclerental/security/`
JWT authentication system:
- **JwtTokenProvider**: Generates and validates JWT tokens (HMAC-SHA256)
- **JwtAuthenticationFilter**: Intercepts requests, validates tokens
- **UserDetailsServiceImpl**: Loads user details for Spring Security

### `/frontend/js/`
Modular JavaScript:
- **api.js**: Centralized API client with JWT header injection
- **auth.js**: Authentication utilities, role guards, logout
- **customer.js**: Customer booking management and reviews
- **owner.js**: Shop/vehicle/booking management with modal forms
- **admin.js**: User/shop/vehicle administration
- **utils.js**: Date formatting, currency, validation helpers

## Database Tables (Oracle)

1. **users** - User accounts (Customer, Owner, Admin)
2. **shops** - Rental shops owned by Owners
3. **vehicles** - Vehicle listings belonging to shops
4. **bookings** - Rental bookings with lifecycle tracking
5. **payments** - Payment transactions
6. **reviews** - Customer reviews and ratings

**Relationships**:
- User (Owner) → Shop (1:N)
- Shop → Vehicle (1:N)
- Booking → Vehicle (N:1)
- Booking → Customer (N:1)
- Booking → Payment (1:1)
- Booking → Review (1:1)

## Application Layers

### Presentation Layer (Frontend)
```
HTML Pages → JavaScript Modules → API Client → Backend
```

### API Layer (Backend)
```
Controllers → DTOs → Services → Repositories → Database
           ↑
    JWT Filter (Security)
```

### Data Flow Example: Create Booking

1. **Customer fills form** → `vehicle-details.html`
2. **JavaScript validates** → `customer.js`
3. **API call with JWT** → `api.js` → `POST /api/customer/bookings`
4. **JWT filter validates** → `JwtAuthenticationFilter`
5. **Controller receives** → `CustomerController.createBooking()`
6. **Service processes** → `BookingService.createBooking()`
   - Check vehicle availability
   - Check for overlapping bookings
   - Calculate total amount
   - Set confirmation deadline (now + 2 hours)
   - Save booking with status PENDING
   - Send email notification
7. **Repository saves** → `BookingRepository.save()`
8. **Database INSERT** → Oracle DB `bookings` table
9. **Response returned** → BookingDTO → JSON → Frontend
10. **UI updates** → Display booking confirmation

### Background Job: Auto-Cancellation

```
Every 10 minutes:
  SchedulerService.cancelExpiredBookings()
    → BookingService.autoCancelExpiredBookings()
      → Find all PENDING bookings past confirmation_deadline
      → For each expired booking:
          - Set status = CANCELLED
          - Set vehicle.isAvailable = true
          - Send cancellation email
          - Save changes
```

## Technology Stack Summary

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Security**: Spring Security + JWT (jjwt 0.12.3)
- **Database**: Oracle Database with JDBC (ojdbc8)
- **ORM**: Spring Data JPA (Hibernate)
- **Scheduler**: Spring Quartz
- **Documentation**: SpringDoc OpenAPI 2.2.0
- **Email**: Spring Mail
- **Build**: Maven

### Frontend
- **Markup**: HTML5
- **Styling**: CSS3 (Flexbox, Grid)
- **Scripting**: JavaScript ES6+ (Vanilla JS, no frameworks)
- **Storage**: localStorage (JWT tokens)

### Database
- **RDBMS**: Oracle Database 11g+
- **Dialect**: Oracle12cDialect
- **Connection**: JDBC Thin Driver
- **Schema**: 6 tables with constraints and indexes

## Design Patterns Used

1. **MVC (Model-View-Controller)**
   - Models: JPA entities
   - Views: HTML pages
   - Controllers: REST controllers

2. **DTO (Data Transfer Object)**
   - Separate DTOs for request/response
   - Decouples API from domain model

3. **Repository Pattern**
   - Data access abstraction
   - JpaRepository interfaces

4. **Service Layer Pattern**
   - Business logic separation
   - Transaction management

5. **Dependency Injection**
   - Spring's @Autowired
   - Constructor injection

6. **Builder Pattern**
   - JWT token building
   - Entity construction

7. **Strategy Pattern**
   - Custom queries in repositories
   - Role-based access control

8. **Observer Pattern**
   - Spring Events (implicit)
   - Email notifications

## Key Features Implementation

### 1. JWT Authentication
**Files**: `JwtTokenProvider.java`, `JwtAuthenticationFilter.java`, `SecurityConfig.java`
- Token generation with user details
- Token validation on each request
- Role-based access control

### 2. Two-Hour Auto-Cancellation
**Files**: `SchedulerService.java`, `BookingService.java`, `BookingRepository.java`
- Scheduled task every 10 minutes
- Query for expired PENDING bookings
- Automatic status update and email notification

### 3. Double-Booking Prevention
**Files**: `BookingService.java`, `BookingRepository.java`
- Custom query to find overlapping bookings
- Validation before booking creation
- Transaction management

### 4. Role-Based Dashboards
**Files**: `customer-dashboard.html`, `owner-dashboard.html`, `admin-dashboard.html`
- Separate HTML pages per role
- Role guards in JavaScript (`requireAuth()`)
- Dynamic content loading via API

### 5. Vehicle Search & Filter
**Files**: `VehicleRepository.java`, `vehicle-search.html`, `customer.js`
- Custom JPA query with multiple filters
- Dynamic WHERE clause building
- Client-side filter UI

## Development Workflow

### Local Development
```bash
# Terminal 1: Backend
cd backend
mvn spring-boot:run

# Terminal 2: Frontend
cd frontend
python -m http.server 8000

# Terminal 3: Database monitoring
sqlplus vehiclerental/password123@localhost:1521/xe
```

### Testing Flow
1. Register test accounts (Customer, Owner)
2. Admin verifies Owner
3. Owner creates Shop and Vehicles
4. Customer searches and books
5. Owner accepts booking
6. Test auto-cancellation (wait 2 hours or modify config)

### Deployment Flow
1. Build backend: `mvn clean package`
2. Deploy JAR to server (AWS EC2, Railway, Render)
3. Deploy frontend to CDN (Netlify, Vercel)
4. Configure production database (Oracle Cloud)
5. Set environment variables
6. Enable HTTPS

## Security Considerations

### Implemented
✅ JWT authentication  
✅ BCrypt password hashing  
✅ Role-based access control  
✅ SQL injection prevention (JPA)  
✅ CORS configuration  
✅ Secure headers  

### Recommended for Production
- Rate limiting
- Input sanitization
- CSRF tokens (if using session-based auth)
- HTTPS enforcement
- Security headers (CSP, HSTS)
- Regular dependency updates
- Database connection pooling limits
- Log monitoring and alerts

## Performance Optimizations

### Database
- Indexes on foreign keys
- Query optimization
- Connection pooling (HikariCP)

### Backend
- DTO projection (avoid loading full entities)
- Lazy loading relationships
- Caching (consider Redis)

### Frontend
- Minified CSS/JS
- Image optimization
- Lazy loading
- Client-side caching

---

**For detailed information about any component, see the respective README files in backend/ and frontend/ directories.**
