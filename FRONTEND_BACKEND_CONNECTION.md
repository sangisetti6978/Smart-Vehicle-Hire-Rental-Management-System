# Frontend-Backend Connection Guide

## ✅ Configuration Status

Your frontend and backend are now **properly configured** to connect with each other!

## 🔧 Current Configuration

### Backend Configuration
- **Server Port**: 8080
- **Base URL**: `http://localhost:8080`
- **CORS**: Enabled for localhost and file:// origins
- **Authentication**: JWT-based with Bearer tokens
- **Public Endpoints**: `/api/public/**` and `/api/auth/**`

### Frontend Configuration
- **API Base URL**: `http://localhost:8080` (configured in `js/api.js`)
- **Authentication**: Token-based (stored in localStorage)
- **API Module**: Centralized API calls in `js/api.js`

## 🚀 How to Start the Application

### 1. Start the Backend

**Option A: Using Maven Command**
```powershell
cd "d:\Multi-Vendor Vehicle Rental Booking Platform\backend"
mvn spring-boot:run
```

**Option B: Using Java**
```powershell
cd "d:\Multi-Vendor Vehicle Rental Booking Platform\backend"
mvn clean package
java -jar target\vehicle-rental-platform-0.0.1-SNAPSHOT.jar
```

**Verify Backend is Running:**
- Open browser and navigate to: `http://localhost:8080/swagger-ui.html`
- You should see the Swagger API documentation

### 2. Start the Frontend

**Option A: Using Live Server (Recommended)**
- Install "Live Server" extension in VS Code
- Right-click on `frontend/index.html`
- Click "Open with Live Server"
- Frontend will open at `http://127.0.0.1:5500` or similar

**Option B: Using Python HTTP Server**
```powershell
cd "d:\Multi-Vendor Vehicle Rental Booking Platform\frontend"
python -m http.server 8000
```
Then open: `http://localhost:8000`

**Option C: Using Node.js HTTP Server**
```powershell
cd "d:\Multi-Vendor Vehicle Rental Booking Platform\frontend"
npx http-server -p 8000
```
Then open: `http://localhost:8000`

**Option D: Direct File Access**
- Simply open `frontend/index.html` in your browser
- The CORS configuration supports file:// protocol

## 🧪 Testing the Connection

### Quick Test
1. Open `frontend/connection-test.html` in your browser
2. Click the test buttons to verify:
   - Backend health
   - API endpoints
   - Authentication flow
   - Protected endpoints

### Manual Testing Steps

1. **Test Backend Health**
   ```powershell
   curl http://localhost:8080/api/public/vehicles
   ```

2. **Test Registration**
   - Navigate to `frontend/register.html`
   - Create a new account (CUSTOMER, OWNER, or ADMIN)
   - Check if you're redirected to the appropriate dashboard

3. **Test Login**
   - Navigate to `frontend/login.html`
   - Login with your credentials
   - Verify JWT token is stored in localStorage

4. **Test Vehicle Search**
   - Navigate to `frontend/vehicle-search.html`
   - Vehicles should load automatically
   - Try the search filters

## 📋 Prerequisites Checklist

Before running the application, ensure:

- ✅ Java 17 or higher installed
- ✅ Maven installed
- ✅ Oracle Database running on localhost:1521
- ✅ Database schema created (user: `vehiclerental`, password: `password123`)
- ✅ Dependencies installed (`mvn clean install`)
- ✅ Backend compiled successfully
- ✅ Port 8080 is available (not used by another application)

## 🔐 Authentication Flow

### How It Works:

1. **User Registration/Login**
   - Frontend sends credentials to `/api/auth/register` or `/api/auth/login`
   - Backend validates and returns JWT token + user details
   - Frontend stores token in localStorage

2. **Authenticated Requests**
   - Frontend includes token in Authorization header: `Bearer <token>`
   - Backend validates token using JwtAuthenticationFilter
   - Request is processed if token is valid

3. **Role-Based Access**
   - CUSTOMER: Can search vehicles, make bookings, add reviews
   - OWNER: Can manage shops, vehicles, and view bookings
   - ADMIN: Full system access

## 🛠️ API Endpoints Overview

### Public Endpoints (No Authentication Required)
- `GET /api/public/vehicles` - Browse all vehicles
- `GET /api/public/vehicles/{id}` - View vehicle details
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Customer Endpoints (CUSTOMER role required)
- `GET /api/customer/bookings` - View my bookings
- `POST /api/customer/bookings` - Create new booking
- `DELETE /api/customer/bookings/{id}` - Cancel booking
- `POST /api/customer/reviews` - Add review

### Owner Endpoints (OWNER role required)
- `GET /api/owner/shops` - View my shops
- `POST /api/owner/shops` - Create new shop
- `GET /api/owner/vehicles` - View my vehicles
- `POST /api/owner/vehicles` - Add new vehicle
- `PUT /api/owner/vehicles/{id}` - Update vehicle
- `DELETE /api/owner/vehicles/{id}` - Delete vehicle
- `GET /api/owner/bookings` - View bookings for my vehicles
- `PUT /api/owner/bookings/{id}/confirm` - Confirm booking
- `PUT /api/owner/bookings/{id}/complete` - Complete booking

### Admin Endpoints (ADMIN role required)
- `GET /api/admin/users` - View all users
- `GET /api/admin/bookings` - View all bookings
- `GET /api/admin/vehicles` - View all vehicles
- `GET /api/admin/stats` - View platform statistics

## 🔍 Troubleshooting

### Issue: "Cannot connect to backend"
**Solutions:**
1. Verify backend is running: `curl http://localhost:8080/api/public/vehicles`
2. Check backend logs for errors
3. Ensure Oracle database is running
4. Verify port 8080 is not blocked by firewall

### Issue: "CORS Error"
**Solutions:**
1. Backend CORS is already configured for localhost
2. If using a different port, update SecurityConfig.java
3. Clear browser cache and try again

### Issue: "Login Failed - 401 Unauthorized"
**Solutions:**
1. Verify user exists in database
2. Check password is correct
3. Ensure JWT secret is configured in application.properties
4. Check backend logs for authentication errors

### Issue: "403 Forbidden on protected endpoints"
**Solutions:**
1. Verify JWT token is being sent in Authorization header
2. Check user has the correct role (CUSTOMER, OWNER, or ADMIN)
3. Token might be expired (default expiration: 24 hours)
4. Try logging out and logging back in

### Issue: "No vehicles showing"
**Solutions:**
1. Check if vehicles exist in database
2. Run the sample data insertion scripts if needed
3. Verify `/api/public/vehicles` returns data (check in browser or Postman)
4. Check browser console for JavaScript errors

## 📱 Frontend File Structure

```
frontend/
├── index.html              # Landing page
├── login.html              # Login page
├── register.html           # Registration page
├── vehicle-search.html     # Vehicle search and browse
├── vehicle-details.html    # Individual vehicle details
├── customer-dashboard.html # Customer dashboard
├── owner-dashboard.html    # Owner dashboard
├── admin-dashboard.html    # Admin dashboard
├── connection-test.html    # Connection testing utility
├── css/
│   ├── style.css          # Main styles
│   ├── dashboard.css      # Dashboard-specific styles
│   └── responsive.css     # Responsive design
└── js/
    ├── api.js             # API configuration and calls
    ├── auth.js            # Authentication helpers
    ├── customer.js        # Customer dashboard logic
    ├── owner.js           # Owner dashboard logic
    ├── admin.js           # Admin dashboard logic
    └── utils.js           # Utility functions
```

## 🎯 Next Steps

1. **Start the Backend**: Follow the instructions above
2. **Verify Database**: Ensure Oracle DB is set up with the schema
3. **Test Connection**: Open `connection-test.html` in your browser
4. **Create Test Users**: Register at least one user of each role
5. **Add Sample Data**: Owners should add some vehicles for testing
6. **Test Full Flow**: Search → View Details → Book → Manage Bookings

## 📚 Additional Resources

- **API Documentation**: http://localhost:8080/swagger-ui.html (when backend is running)
- **Project Structure**: See `PROJECT_STRUCTURE.md`
- **API Details**: See `API_DOCUMENTATION.md`
- **Deployment Guide**: See `DEPLOYMENT.md`

## ✨ Features Implemented

✅ User registration and authentication
✅ JWT-based security
✅ Role-based access control (RBAC)
✅ Vehicle search and filtering
✅ Booking management
✅ Multi-vendor shop management
✅ Review system
✅ Admin dashboard
✅ Responsive design
✅ CORS support for development
✅ Comprehensive error handling

---

**Need Help?** Check the browser console (F12) and backend logs for detailed error messages.
