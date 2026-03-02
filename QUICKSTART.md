# Quick Start Guide

Get the Multi-Vendor Vehicle Rental Booking Platform up and running in 15 minutes!

## 🚀 Prerequisites Checklist

- [ ] Java 17+ installed
- [ ] Maven 3.6+ installed
- [ ] Oracle Database installed (or access to Oracle Cloud)
- [ ] Git installed
- [ ] Modern web browser

## ⚡ Quick Setup (5 Steps)

### Step 1: Database Setup (3 minutes)

#### Create Database User
```sql
-- Connect to Oracle as SYSDBA
sqlplus sys as sysdba

-- Create user
CREATE USER vehiclerental IDENTIFIED BY password123;
GRANT CONNECT, RESOURCE, DBA TO vehiclerental;
GRANT UNLIMITED TABLESPACE TO vehiclerental;
```

#### Run Schema
```sql
-- Connect as vehiclerental
sqlplus vehiclerental/password123@localhost:1521/xe

-- Run schema script
@backend/src/main/resources/schema.sql

-- Verify tables
SELECT table_name FROM user_tables;
-- Should show: USERS, SHOPS, VEHICLES, BOOKINGS, PAYMENTS, REVIEWS
```

### Step 2: Configure Application (1 minute)

Edit `backend/src/main/resources/application.properties`:

```properties
# Update these 3 lines only:
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=vehiclerental
spring.datasource.password=password123
```

That's it! Other settings use reasonable defaults.

### Step 3: Build & Run Backend (2 minutes)

```bash
cd backend

# Build (first time may take 2-3 minutes to download dependencies)
mvn clean install

# Run
mvn spring-boot:run
```

✅ **Backend is running when you see**: `Started VehicleRentalApplication in X seconds`

Test it: Open http://localhost:8080/swagger-ui.html

### Step 4: Run Frontend (1 minute)

#### Option A: Python (Easiest)
```bash
cd frontend
python -m http.server 8000
```

#### Option B: Node.js
```bash
cd frontend
npx serve .
```

✅ **Frontend is ready**: Open http://localhost:8000

### Step 5: Test the Application (2 minutes)

#### Default Admin Login
```
Email: admin@vehiclerental.com
Password: admin123
```

1. **Test Admin Panel**: Login → Go to Admin Dashboard
2. **Create Owner Account**: Register → Select Role: OWNER
3. **Verify Owner**: Admin Dashboard → Users → Click "Verify" on new owner
4. **Create Shop**: Logout → Login as Owner → Create Shop
5. **Add Vehicle**: Owner Dashboard → Add Vehicle
6. **Create Customer**: Register → Select Role: CUSTOMER
7. **Book Vehicle**: Login as Customer → Search Vehicles → Book

## 🎯 What You Should See

### After Backend Starts:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

Started VehicleRentalApplication in 8.524 seconds
```

### After Frontend Starts:
```
Serving HTTP on 0.0.0.0 port 8000 (http://0.0.0.0:8000/) ...
```

## 🔍 Testing Checklist

### Customer Flow
- [ ] Register as Customer
- [ ] Login successfully
- [ ] Search for vehicles
- [ ] View vehicle details
- [ ] Create a booking
- [ ] See booking in "My Bookings"
- [ ] Cancel a booking

### Owner Flow
- [ ] Register as Owner
- [ ] Admin verifies owner
- [ ] Create a shop
- [ ] Add a vehicle
- [ ] Receive booking request
- [ ] Accept booking (within 2 hours)
- [ ] Complete booking

### Admin Flow
- [ ] Login as admin
- [ ] View all users
- [ ] Verify owner accounts
- [ ] View all shops and vehicles
- [ ] Delete test data

## 📊 API Documentation

Once backend is running:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 🐛 Troubleshooting

### Backend won't start?

#### Error: "Unable to connect to database"
```bash
# Test Oracle connection
sqlplus vehiclerental/password123@localhost:1521/xe

# If fails, check:
1. Is Oracle running? (lsnrctl status)
2. Is connection string correct?
3. Are credentials correct?
```

#### Error: "Port 8080 already in use"
```properties
# Change port in application.properties
server.port=8081
```

#### Error: "Java version mismatch"
```bash
# Check Java version
java -version
# Should show: version "17.x.x"

# If wrong version, set JAVA_HOME:
export JAVA_HOME=/path/to/jdk-17
```

#### Error: "Maven not found"
```bash
# Install Maven:
# Windows: choco install maven
# Mac: brew install maven
# Linux: sudo apt install maven

# Verify:
mvn -version
```

### Frontend won't connect to backend?

#### Check API URL
Open `frontend/js/api.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```
Make sure this matches your backend URL.

#### CORS Error in Browser Console?
Backend CORS is configured to allow all origins by default. If you see CORS errors:
1. Restart backend
2. Clear browser cache
3. Try incognito mode

### Can't login?

#### "Invalid credentials" error
- Check email and password
- Ensure user exists in database:
```sql
SELECT email, role, is_active FROM users WHERE email = 'your@email.com';
```

#### "Account not verified" (for owners)
- Owner accounts need admin verification
- Login as admin → Verify owner

### Bookings auto-cancelling?

✅ **This is expected behavior!**

Bookings in `PENDING` status auto-cancel after **2 hours** if not accepted by owner.

Check confirmation deadline:
```sql
SELECT id, status, confirmation_deadline FROM bookings WHERE id = YOUR_BOOKING_ID;
```

Accept booking before deadline to prevent cancellation.

## 🚦 Port Reference

| Service | Port | URL |
|---------|------|-----|
| Backend | 8080 | http://localhost:8080 |
| Frontend | 8000 | http://localhost:8000 |
| Oracle DB | 1521 | localhost:1521/xe |
| Swagger UI | 8080 | http://localhost:8080/swagger-ui.html |

## 📝 Default Accounts

### Admin Account (Pre-created)
```
Email: admin@vehiclerental.com
Password: admin123
```

### Test Accounts (Create via Registration)
```
Owner:
- Email: owner@test.com
- Password: owner123
- Role: OWNER
- NOTE: Needs admin verification!

Customer:
- Email: customer@test.com  
- Password: customer123
- Role: CUSTOMER
```

## 🎨 Sample Data

### Create Sample Shop (as Owner)
```
Shop Name: Mumbai Car Rentals
City: Mumbai
State: Maharashtra
Address: 123 Main Street, Andheri
Phone: +919876543210
```

### Create Sample Vehicle (as Owner)
```
Vehicle Name: Toyota Innova
Brand: Toyota
Type: CAR
Price Per Hour: ₹150
Color: White
Registration: MH01AB1234
Description: Comfortable 7-seater SUV
```

### Create Sample Booking (as Customer)
```
Vehicle: Toyota Innova
Start Time: Tomorrow 10:00 AM
End Time: Tomorrow 6:00 PM
Total Hours: 8
Total Amount: ₹1,200
```

## ⏱️ Auto-Cancellation Test

Want to test the 2-hour auto-cancellation feature?

### Option 1: Wait 2 hours (Real-time test)
1. Customer creates booking → Status: PENDING
2. Owner doesn't accept
3. Wait 2 hours
4. Check booking status → Should be: CANCELLED
5. Check vehicle → Should be: Available

### Option 2: Change timeout (Quick test)
Edit `application.properties`:
```properties
# Change from 2 hours to 1 minute
booking.cancellation.delay=60000
```

Restart backend, create booking, wait 1 minute + 10 minutes (scheduler runs every 10 min).

**Don't forget to change back to 2 hours (7200000) after testing!**

## 📚 Next Steps

- [ ] Read full [README.md](README.md) for architecture details
- [ ] See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for all endpoints
- [ ] Check [DEPLOYMENT.md](DEPLOYMENT.md) for production deployment
- [ ] Explore Swagger UI for interactive API testing
- [ ] Customize branding (colors, logo, text)
- [ ] Add your own vehicles and test bookings
- [ ] Set up email configuration for notifications

## 💡 Pro Tips

### Development Workflow
```bash
# Terminal 1: Backend
cd backend
mvn spring-boot:run

# Terminal 2: Frontend
cd frontend
python -m http.server 8000

# Terminal 3: Database
sqlplus vehiclerental/password123@localhost:1521/xe
```

### Quick Database Queries
```sql
-- View all users
SELECT id, email, full_name, role, is_verified FROM users;

-- View all bookings
SELECT id, customer_id, vehicle_id, status, confirmation_deadline FROM bookings;

-- View pending bookings
SELECT * FROM bookings WHERE status = 'PENDING';

-- View expired bookings (past deadline)
SELECT * FROM bookings 
WHERE status = 'PENDING' 
AND confirmation_deadline < SYSTIMESTAMP;

-- Reset admin password
UPDATE users SET password = '$2a$10$...' WHERE email = 'admin@vehiclerental.com';
```

### Browser DevTools
- Press `F12` to open DevTools
- **Console Tab**: See JavaScript errors
- **Network Tab**: Monitor API calls
- **Application Tab**: View localStorage (JWT token)

### Testing API with curl
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"test123","fullName":"Test User","phone":"+1234567890","role":"CUSTOMER"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"test123"}'

# Copy token from response, then:
TOKEN="your-jwt-token-here"

# Get my bookings
curl -X GET http://localhost:8080/api/customer/bookings \
  -H "Authorization: Bearer $TOKEN"
```

## 🎉 Success!

You now have a fully functional Multi-Vendor Vehicle Rental Booking Platform running locally!

### What's Working:
✅ Three-role authentication system  
✅ JWT-based security  
✅ Vehicle search and booking  
✅ Shop and vehicle management  
✅ 2-hour auto-cancellation  
✅ Email notifications  
✅ Admin panel  
✅ Review system  
✅ Responsive UI  

### Need Help?
- Check [README.md](README.md) for detailed documentation
- See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for API reference
- Review [DEPLOYMENT.md](DEPLOYMENT.md) for production setup
- Open an issue on GitHub

---

**Happy Coding! 🚗💨**
