# Vehicle Rental Platform - Backend

Spring Boot backend for the Multi-Vendor Vehicle Rental Booking Platform.

## 🏗️ Architecture

### Technology Stack
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: Oracle Database
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security + JWT
- **Scheduler**: Spring Scheduler
- **API Documentation**: Swagger/OpenAPI (SpringDoc)
- **Build Tool**: Maven

### Key Components

#### 1. Models (Entities)
- `User` - User accounts with role-based access
- `Shop` - Vehicle rental shops
- `Vehicle` - Vehicle listings
- `Booking` - Rental bookings with lifecycle management
- `Payment` - Payment transactions
- `Review` - Customer reviews and ratings

#### 2. Repositories
JPA repositories for database operations with custom queries for:
- Vehicle search and filtering
- Booking overlap detection
- Expired booking identification
- Statistics and analytics

#### 3. Services
Business logic layer implementing:
- Authentication and authorization
- Shop and vehicle management
- Booking lifecycle management
- Automatic booking cancellation
- Email notifications
- Review management

#### 4. Controllers
RESTful API endpoints for:
- Authentication (`/api/auth`)
- Customer operations (`/api/customer`)
- Owner operations (`/api/owner`)
- Admin operations (`/api/admin`)
- Public access (`/api/public`)

#### 5. Security
- JWT token generation and validation
- BCrypt password encryption
- Role-based access control
- CORS configuration
- Authentication filter

#### 6. Scheduler
Background job that runs every 10 minutes to:
- Find bookings with expired confirmation deadlines
- Automatically cancel pending bookings
- Release vehicle availability
- Send cancellation notifications

## 🚀 Getting Started

### Prerequisites
```bash
Java 17+
Maven 3.6+
Oracle Database 11g+
```

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

### Run Tests
```bash
mvn test
```

### Package
```bash
mvn package
java -jar target/vehicle-rental-platform-1.0.0.jar
```

## 📡 API Documentation

Once the application is running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON specification:
```
http://localhost:8080/api-docs
```

## ⚙️ Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=vehiclerental
spring.datasource.password=password123
spring.jpa.hibernate.ddl-auto=update
```

### JWT Configuration
```properties
jwt.secret=your-secret-key
jwt.expiration=86400000  # 24 hours
```

### Scheduler Configuration
```properties
booking.cancellation.delay=7200000  # 2 hours
```

### Email Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## 🔐 Security

### Authentication Flow
1. User registers → Password encrypted with BCrypt
2. User logs in → Credentials validated
3. JWT token generated → Returned to client
4. Client includes token in Authorization header
5. JWT filter validates token on each request
6. User details loaded and authorities granted

### Authorization
- **ROLE_CUSTOMER**: Access customer endpoints
- **ROLE_OWNER**: Access owner endpoints
- **ROLE_ADMIN**: Access admin endpoints

## 📊 Database Schema

### Key Tables
```sql
users (id, email, password, full_name, phone, role, is_verified, is_active)
shops (id, owner_id, shop_name, description, city, state, address)
vehicles (id, shop_id, vehicle_name, brand, type, price_per_hour, is_available)
bookings (id, customer_id, vehicle_id, start_time, end_time, status, confirmation_deadline)
payments (id, booking_id, amount, status, transaction_id)
reviews (id, booking_id, vehicle_id, rating, comment)
```

### Relationships
- User → Shop (1:N)
- Shop → Vehicle (1:N)
- Booking (M:1) → Vehicle
- Booking (M:1) → Customer (User)
- Booking (1:1) → Payment
- Booking (1:1) → Review

## 🔄 Booking Lifecycle

```
PENDING → (Owner accepts) → ACCEPTED → (Rental complete) → COMPLETED
       ↓ (2 hours timeout)          ↓ (Owner rejects)
    CANCELLED                      REJECTED
```

### Auto-Cancellation Logic
```java
@Scheduled(fixedDelay = 600000) // Every 10 minutes
public void cancelExpiredBookings() {
    List<Booking> expired = findExpiredPendingBookings(LocalDateTime.now());
    for (Booking booking : expired) {
        booking.setStatus(CANCELLED);
        vehicle.setAvailable(true);
        sendCancellationEmail();
    }
}
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Test Coverage
```bash
mvn test jacoco:report
# Report at: target/site/jacoco/index.html
```

## 📦 Dependencies

### Core
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- spring-boot-starter-mail
- spring-boot-starter-quartz

### Database
- ojdbc8 (Oracle JDBC Driver)

### Security
- jjwt (JWT implementation)

### Documentation
- springdoc-openapi-starter-webmvc-ui

### Utilities
- lombok

## 🚀 Production Deployment

### Build Production JAR
```bash
mvn clean package -DskipTests
```

### Run with Production Profile
```bash
java -jar -Dspring.profiles.active=prod target/vehicle-rental-platform-1.0.0.jar
```

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:oracle:thin:@prod-db:1521:xe
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=prod_password
export JWT_SECRET=production-secret-key
export SPRING_MAIL_USERNAME=notifications@example.com
export SPRING_MAIL_PASSWORD=email-password
```

### Docker Deployment
```dockerfile
FROM openjdk:17-slim
COPY target/vehicle-rental-platform-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 📝 API Examples

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "role": "CUSTOMER"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Create Booking (with token)
```bash
curl -X POST http://localhost:8080/api/customer/bookings \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "startTime": "2026-02-10T10:00:00",
    "endTime": "2026-02-10T18:00:00"
  }'
```

## 🐛 Troubleshooting

### Common Issues

**Oracle Driver Not Found**
```bash
# Download ojdbc8.jar from Oracle website
mvn install:install-file \
  -Dfile=ojdbc8.jar \
  -DgroupId=com.oracle.database.jdbc \
  -DartifactId=ojdbc8 \
  -Dversion=21.1.0.0 \
  -Dpackaging=jar
```

**Port Already in Use**
```properties
# Change server port in application.properties
server.port=8081
```

**Database Connection Failed**
- Verify Oracle DB is running
- Check connection URL, username, password
- Ensure user has necessary privileges

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Oracle JDBC](https://www.oracle.com/database/technologies/appdev/jdbc.html)
- [JWT.io](https://jwt.io/)

---

**For frontend documentation, see: `../frontend/README.md`**
