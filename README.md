# Multi-Vendor Vehicle Rental Booking Platform

A comprehensive full-stack vehicle rental system built with Spring Boot, Oracle Database, and modern web technologies. The platform supports multi-vendor operations with three distinct user roles and includes advanced features like time-limited booking confirmations, automatic cancellation, and smart availability management.

## 🚀 Features

### Core Features
- **Multi-Vendor Support**: Multiple vehicle owners can register and manage their rental businesses
- **Role-Based Access Control**: Three user roles (Customer, Owner, Admin) with JWT authentication
- **Time-Limited Booking Confirmation**: Bookings automatically cancel if not confirmed within 2 hours
- **Smart Booking Management**: 
  - Automatic double-booking prevention
  - Overlapping time slot validation
  - Vehicle availability management
  - Complete booking lifecycle (PENDING → ACCEPTED → COMPLETED/CANCELLED)

### User Roles & Capabilities

#### 👤 Customer
- Search and filter vehicles by location, type, brand, and price
- View detailed vehicle information and reviews
- Book vehicles with start/end time selection
- Track booking status and history
- Add reviews and ratings
- Cancel pending bookings

#### 🏪 Vehicle Owner
- Create and manage multiple shops
- Add, update, and delete vehicles
- Set pricing and manage availability
- Upload vehicle images
- Enable/disable listings
- Accept or reject booking requests
- View booking statistics
- Complete bookings

#### 🔐 Admin
- Manage all users
- Verify vehicle owners
- Remove fake listings
- Monitor platform activity
- Deactivate suspicious accounts
- System security maintenance

### Technical Features
- **Secure Authentication**: JWT tokens + BCrypt password encryption
- **Background Scheduler**: Spring Scheduler for automatic booking cancellation
- **RESTful APIs**: Well-documented with Swagger/OpenAPI
- **Database**: Oracle Database with proper relational design
- **Real-time Updates**: Automatic vehicle status updates
- **Email Notifications**: Booking confirmations and alerts
- **Responsive Design**: Mobile-friendly interface

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Oracle Database 11g or higher
- Node.js (for frontend development server - optional)

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
cd "Multi-Vendor Vehicle Rental Booking Platform"
```

### 2. Database Setup

#### Create Oracle Database User
```sql
CREATE USER vehiclerental IDENTIFIED BY password123;
GRANT CONNECT, RESOURCE, DBA TO vehiclerental;
GRANT UNLIMITED TABLESPACE TO vehiclerental;
```

#### Run Schema Script
```bash
# Connect to Oracle using SQL*Plus or SQL Developer
sqlplus vehiclerental/password123@localhost:1521/xe
@backend/src/main/resources/schema.sql
```

### 3. Configure Application

Edit `backend/src/main/resources/application.properties`:

```properties
# Update these with your Oracle DB credentials
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=vehiclerental
spring.datasource.password=password123

# Update email configuration (optional)
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### 4. Build and Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

**Swagger Documentation**: `http://localhost:8080/swagger-ui.html`

### 5. Run Frontend

#### Option 1: Simple HTTP Server (Python)
```bash
cd frontend
python -m http.server 8000
```
Access at: `http://localhost:8000`

#### Option 2: Live Server (VS Code Extension)
- Install "Live Server" extension in VS Code
- Right-click on `index.html` → "Open with Live Server"

#### Option 3: Node.js HTTP Server
```bash
cd frontend
npx serve .
```

### 6. Test the Application

**Default Admin Account:**
- Email: `admin@vehiclerental.com`
- Password: `admin123`

## 📁 Project Structure

```
Multi-Vendor Vehicle Rental Booking Platform/
├── backend/
│   ├── src/main/java/com/vehiclerental/
│   │   ├── config/              # Security, Swagger, Scheduler configs
│   │   ├── controller/          # REST API controllers
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Global exception handlers
│   │   ├── model/               # JPA entities
│   │   ├── repository/          # Data access layer
│   │   ├── security/            # JWT & authentication
│   │   └── service/             # Business logic
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── schema.sql           # Database schema
│   └── pom.xml
├── frontend/
│   ├── css/                     # Stylesheets
│   ├── js/                      # JavaScript modules
│   ├── index.html              # Landing page
│   ├── login.html              # Login page
│   ├── register.html           # Registration page
│   ├── customer-dashboard.html
│   ├── owner-dashboard.html
│   ├── admin-dashboard.html
│   ├── vehicle-search.html
│   └── vehicle-details.html
└── README.md
```

## 🔧 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### Customer APIs
- `GET /api/customer/vehicles/search` - Search vehicles
- `GET /api/customer/vehicles/{id}` - Get vehicle details
- `POST /api/customer/bookings` - Create booking
- `GET /api/customer/bookings` - Get my bookings
- `PUT /api/customer/bookings/{id}/cancel` - Cancel booking
- `POST /api/customer/reviews` - Add review

### Owner APIs
- `POST /api/owner/shops` - Create shop
- `GET /api/owner/shops` - Get my shops
- `POST /api/owner/vehicles` - Add vehicle
- `GET /api/owner/shops/{shopId}/vehicles` - Get shop vehicles
- `PUT /api/owner/vehicles/{id}/availability` - Update availability
- `GET /api/owner/shops/{shopId}/bookings` - Get bookings
- `PUT /api/owner/bookings/{id}/accept` - Accept booking
- `PUT /api/owner/bookings/{id}/reject` - Reject booking
- `PUT /api/owner/bookings/{id}/complete` - Complete booking

### Admin APIs
- `GET /api/admin/users` - Get all users
- `PUT /api/admin/users/{id}/verify` - Verify owner
- `DELETE /api/admin/users/{id}` - Delete user
- `GET /api/admin/shops` - Get all shops
- `DELETE /api/admin/shops/{id}` - Delete shop
- `GET /api/admin/vehicles` - Get all vehicles

### Public APIs
- `GET /api/public/vehicles` - Browse vehicles (no auth required)
- `GET /api/public/vehicles/{id}` - View vehicle details

## ⚙️ Configuration

### JWT Configuration
```properties
jwt.secret=mySecretKeyForJWTTokenGenerationAndValidation2024
jwt.expiration=86400000  # 24 hours
```

### Booking Cancellation Scheduler
```properties
booking.cancellation.delay=7200000  # 2 hours in milliseconds
```

The scheduler runs every 10 minutes to check for expired bookings.

### Email Configuration
Configure SMTP settings in `application.properties` for notifications.

## 🚀 Deployment

### Deploy to Cloud

#### Backend (Spring Boot)
**AWS EC2 / Railway / Render:**
```bash
mvn clean package
java -jar target/vehicle-rental-platform-1.0.0.jar
```

#### Frontend
**Netlify / Vercel:**
- Update API base URL in `frontend/js/api.js`
- Deploy frontend folder

#### Database
**Oracle Cloud:**
- Create Oracle Autonomous Database instance
- Update connection string in application.properties
- Run schema.sql script

### Environment Variables
```bash
export DB_URL=jdbc:oracle:thin:@your-db-host:1521:xe
export DB_USERNAME=vehiclerental
export DB_PASSWORD=your-password
export JWT_SECRET=your-secret-key
export MAIL_USERNAME=your-email
export MAIL_PASSWORD=your-password
```

## 📊 Database Schema

### Main Tables
- **users** - User accounts (Customer, Owner, Admin)
- **shops** - Vehicle rental shops
- **vehicles** - Vehicle listings
- **bookings** - Rental bookings
- **payments** - Payment records
- **reviews** - Customer reviews

### Key Relationships
- User (Owner) → Shops (1:Many)
- Shop → Vehicles (1:Many)
- Vehicle → Bookings (1:Many)
- Booking → Payment (1:1)
- Booking → Review (1:1)

## 🔒 Security Features

- JWT token-based authentication
- BCrypt password encryption
- Role-based access control (RBAC)
- CORS configuration
- SQL injection prevention (JPA/Hibernate)
- XSS protection
- Secure password policies

## 🧪 Testing

```bash
# Run backend tests
cd backend
mvn test

# Run with coverage
mvn test jacoco:report
```

## 📱 Mobile Support

The application is fully responsive and works on:
- Desktop browsers
- Tablets
- Mobile devices (iOS & Android)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the Apache 2.0 License.

## 👥 Support

For support and questions:
- Email: support@vehiclerental.com
- Documentation: `http://localhost:8080/swagger-ui.html`

## 🎯 Future Enhancements

- [ ] Online payment integration (Stripe/PayPal)
- [ ] Advanced analytics dashboard
- [ ] Mobile application (React Native)
- [ ] Real-time chat support
- [ ] Push notifications
- [ ] Vehicle insurance integration
- [ ] GPS tracking
- [ ] Multi-language support

## 📸 Screenshots

Visit the application to see:
- Clean, modern UI
- Intuitive navigation
- Role-specific dashboards
- Real-time booking updates

---

**Built with ❤️ using Spring Boot, Oracle Database, and Modern Web Technologies**
