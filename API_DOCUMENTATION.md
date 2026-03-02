# API Documentation

Complete API reference for the Multi-Vendor Vehicle Rental Booking Platform.

## 🔑 Authentication

All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

### Base URL
```
http://localhost:8080/api
```

## 📝 Response Format

### Success Response
```json
{
    "data": { },
    "message": "Success",
    "timestamp": "2024-02-10T10:30:00"
}
```

### Error Response
```json
{
    "error": "Error message",
    "status": 400,
    "timestamp": "2024-02-10T10:30:00",
    "path": "/api/endpoint"
}
```

## Authentication Endpoints

### 1. Register User
Create a new user account.

**Endpoint**: `POST /api/auth/register`  
**Access**: Public  

**Request Body**:
```json
{
    "email": "john.doe@example.com",
    "password": "SecurePass123!",
    "fullName": "John Doe",
    "phone": "+1234567890",
    "role": "CUSTOMER"
}
```

**Parameters**:
- `email` (string, required): Valid email address
- `password` (string, required): Minimum 8 characters
- `fullName` (string, required): User's full name
- `phone` (string, required): Contact number
- `role` (enum, required): `CUSTOMER` or `OWNER`

**Response**: `201 Created`
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": 1,
        "email": "john.doe@example.com",
        "fullName": "John Doe",
        "role": "CUSTOMER",
        "isVerified": false,
        "isActive": true
    }
}
```

**curl Example**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123!",
    "fullName": "John Doe",
    "phone": "+1234567890",
    "role": "CUSTOMER"
  }'
```

### 2. Login
Authenticate and receive JWT token.

**Endpoint**: `POST /api/auth/login`  
**Access**: Public  

**Request Body**:
```json
{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
}
```

**Response**: `200 OK`
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": 1,
        "email": "john.doe@example.com",
        "fullName": "John Doe",
        "role": "CUSTOMER"
    }
}
```

**Error Responses**:
- `401 Unauthorized`: Invalid credentials
- `403 Forbidden`: Account deactivated

**curl Example**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }'
```

---

## Customer Endpoints

### 1. Search Vehicles
Search and filter available vehicles.

**Endpoint**: `POST /api/customer/vehicles/search`  
**Access**: Customer  
**Auth**: Required  

**Request Body**:
```json
{
    "city": "Mumbai",
    "state": "Maharashtra",
    "vehicleType": "CAR",
    "brand": "Toyota",
    "minPrice": 50,
    "maxPrice": 200
}
```

**Parameters** (all optional):
- `city` (string): Filter by city
- `state` (string): Filter by state
- `vehicleType` (enum): `CAR`, `BIKE`, `SCOOTER`, `VAN`
- `brand` (string): Filter by brand
- `minPrice` (number): Minimum price per hour
- `maxPrice` (number): Maximum price per hour

**Response**: `200 OK`
```json
[
    {
        "id": 1,
        "vehicleName": "Toyota Innova",
        "brand": "Toyota",
        "type": "CAR",
        "pricePerHour": 150.00,
        "description": "Comfortable 7-seater SUV",
        "color": "White",
        "registrationNumber": "MH01AB1234",
        "isAvailable": true,
        "imageUrl": "/uploads/innova.jpg",
        "shop": {
            "id": 1,
            "shopName": "Mumbai Car Rentals",
            "city": "Mumbai",
            "state": "Maharashtra"
        }
    }
]
```

**curl Example**:
```bash
curl -X POST http://localhost:8080/api/customer/vehicles/search \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "city": "Mumbai",
    "vehicleType": "CAR"
  }'
```

### 2. Get Vehicle Details
Get detailed information about a specific vehicle.

**Endpoint**: `GET /api/customer/vehicles/{id}`  
**Access**: Customer  
**Auth**: Required  

**Path Parameters**:
- `id` (number): Vehicle ID

**Response**: `200 OK`
```json
{
    "id": 1,
    "vehicleName": "Toyota Innova",
    "brand": "Toyota",
    "type": "CAR",
    "pricePerHour": 150.00,
    "description": "Comfortable 7-seater SUV",
    "color": "White",
    "registrationNumber": "MH01AB1234",
    "isAvailable": true,
    "imageUrl": "/uploads/innova.jpg",
    "shopId": 1,
    "averageRating": 4.5,
    "totalReviews": 25
}
```

### 3. Create Booking
Book a vehicle for specific time period.

**Endpoint**: `POST /api/customer/bookings`  
**Access**: Customer  
**Auth**: Required  

**Request Body**:
```json
{
    "vehicleId": 1,
    "startTime": "2024-02-15T10:00:00",
    "endTime": "2024-02-15T18:00:00"
}
```

**Response**: `201 Created`
```json
{
    "id": 10,
    "vehicleId": 1,
    "vehicleName": "Toyota Innova",
    "customerId": 5,
    "startTime": "2024-02-15T10:00:00",
    "endTime": "2024-02-15T18:00:00",
    "totalAmount": 1200.00,
    "status": "PENDING",
    "confirmationDeadline": "2024-02-15T12:00:00",
    "createdAt": "2024-02-15T10:00:00"
}
```

**Business Rules**:
- Vehicle must be available
- No overlapping bookings allowed
- Booking status starts as `PENDING`
- Confirmation deadline set to 2 hours from creation
- Auto-cancels if not accepted within 2 hours

**Error Responses**:
- `400 Bad Request`: Vehicle not available or overlapping booking
- `404 Not Found`: Vehicle not found

### 4. Get My Bookings
Retrieve all bookings for the logged-in customer.

**Endpoint**: `GET /api/customer/bookings`  
**Access**: Customer  
**Auth**: Required  

**Response**: `200 OK`
```json
[
    {
        "id": 10,
        "vehicleName": "Toyota Innova",
        "shopName": "Mumbai Car Rentals",
        "startTime": "2024-02-15T10:00:00",
        "endTime": "2024-02-15T18:00:00",
        "totalAmount": 1200.00,
        "status": "ACCEPTED",
        "confirmationDeadline": "2024-02-15T12:00:00"
    }
]
```

### 5. Cancel Booking
Cancel a pending or accepted booking.

**Endpoint**: `PUT /api/customer/bookings/{id}/cancel`  
**Access**: Customer  
**Auth**: Required  

**Path Parameters**:
- `id` (number): Booking ID

**Response**: `200 OK`
```json
{
    "message": "Booking cancelled successfully"
}
```

**Business Rules**:
- Only `PENDING` or `ACCEPTED` bookings can be cancelled
- Vehicle availability is updated
- Cancellation email sent

### 6. Submit Review
Add a review for a completed booking.

**Endpoint**: `POST /api/customer/reviews`  
**Access**: Customer  
**Auth**: Required  

**Request Body**:
```json
{
    "bookingId": 10,
    "vehicleId": 1,
    "rating": 5,
    "comment": "Excellent service and well-maintained vehicle!"
}
```

**Parameters**:
- `bookingId` (number, required): Booking ID
- `vehicleId` (number, required): Vehicle ID
- `rating` (number, required): 1-5 stars
- `comment` (string, optional): Review text

**Response**: `201 Created`
```json
{
    "id": 15,
    "bookingId": 10,
    "vehicleId": 1,
    "rating": 5,
    "comment": "Excellent service!",
    "createdAt": "2024-02-20T15:00:00"
}
```

---

## Owner Endpoints

### 1. Create Shop
Register a new rental shop.

**Endpoint**: `POST /api/owner/shops`  
**Access**: Owner (Verified)  
**Auth**: Required  

**Request Body**:
```json
{
    "shopName": "Mumbai Car Rentals",
    "description": "Premium car rental service in Mumbai",
    "city": "Mumbai",
    "state": "Maharashtra",
    "address": "123 Main Street, Andheri",
    "phone": "+919876543210",
    "email": "contact@mumbaicarrentals.com"
}
```

**Response**: `201 Created`
```json
{
    "id": 1,
    "ownerId": 3,
    "shopName": "Mumbai Car Rentals",
    "description": "Premium car rental service",
    "city": "Mumbai",
    "state": "Maharashtra",
    "address": "123 Main Street, Andheri",
    "phone": "+919876543210",
    "email": "contact@mumbaicarrentals.com",
    "isActive": true,
    "createdAt": "2024-02-10T10:00:00"
}
```

### 2. Get My Shops
Retrieve all shops owned by the logged-in owner.

**Endpoint**: `GET /api/owner/shops`  
**Access**: Owner  
**Auth**: Required  

**Response**: `200 OK`
```json
[
    {
        "id": 1,
        "shopName": "Mumbai Car Rentals",
        "city": "Mumbai",
        "state": "Maharashtra",
        "isActive": true,
        "totalVehicles": 15,
        "totalBookings": 120
    }
]
```

### 3. Add Vehicle
Add a new vehicle to a shop.

**Endpoint**: `POST /api/owner/vehicles`  
**Access**: Owner  
**Auth**: Required  
**Content-Type**: `multipart/form-data`  

**Request Body** (Form Data):
```
shopId: 1
vehicleName: Toyota Innova
brand: Toyota
type: CAR
pricePerHour: 150.00
description: Comfortable 7-seater SUV
color: White
registrationNumber: MH01AB1234
image: [file]
```

**Response**: `201 Created`
```json
{
    "id": 1,
    "shopId": 1,
    "vehicleName": "Toyota Innova",
    "brand": "Toyota",
    "type": "CAR",
    "pricePerHour": 150.00,
    "description": "Comfortable 7-seater SUV",
    "color": "White",
    "registrationNumber": "MH01AB1234",
    "isAvailable": true,
    "imageUrl": "/uploads/vehicles/1_innova.jpg"
}
```

### 4. Get Shop Vehicles
Get all vehicles for a specific shop.

**Endpoint**: `GET /api/owner/shops/{shopId}/vehicles`  
**Access**: Owner  
**Auth**: Required  

**Path Parameters**:
- `shopId` (number): Shop ID

**Response**: `200 OK`
```json
[
    {
        "id": 1,
        "vehicleName": "Toyota Innova",
        "type": "CAR",
        "pricePerHour": 150.00,
        "isAvailable": true,
        "totalBookings": 45
    }
]
```

### 5. Update Vehicle Availability
Enable or disable a vehicle.

**Endpoint**: `PUT /api/owner/vehicles/{id}/availability`  
**Access**: Owner  
**Auth**: Required  

**Path Parameters**:
- `id` (number): Vehicle ID

**Request Body**:
```json
{
    "isAvailable": false
}
```

**Response**: `200 OK`
```json
{
    "message": "Vehicle availability updated"
}
```

### 6. Get Shop Bookings
Get all bookings for a shop's vehicles.

**Endpoint**: `GET /api/owner/shops/{shopId}/bookings`  
**Access**: Owner  
**Auth**: Required  

**Path Parameters**:
- `shopId` (number): Shop ID

**Query Parameters**:
- `status` (optional): Filter by status (`PENDING`, `ACCEPTED`, etc.)

**Response**: `200 OK`
```json
[
    {
        "id": 10,
        "vehicleName": "Toyota Innova",
        "customerName": "John Doe",
        "customerPhone": "+1234567890",
        "startTime": "2024-02-15T10:00:00",
        "endTime": "2024-02-15T18:00:00",
        "totalAmount": 1200.00,
        "status": "PENDING",
        "confirmationDeadline": "2024-02-15T12:00:00"
    }
]
```

### 7. Accept Booking
Accept a pending booking request.

**Endpoint**: `PUT /api/owner/bookings/{id}/accept`  
**Access**: Owner  
**Auth**: Required  

**Path Parameters**:
- `id` (number): Booking ID

**Response**: `200 OK`
```json
{
    "message": "Booking accepted successfully"
}
```

**Business Rules**:
- Only `PENDING` bookings can be accepted
- Must be accepted before confirmation deadline
- Confirmation email sent to customer

### 8. Reject Booking
Reject a pending booking request.

**Endpoint**: `PUT /api/owner/bookings/{id}/reject`  
**Access**: Owner  
**Auth**: Required  

**Path Parameters**:
- `id` (number): Booking ID

**Response**: `200 OK`
```json
{
    "message": "Booking rejected"
}
```

### 9. Complete Booking
Mark a booking as completed.

**Endpoint**: `PUT /api/owner/bookings/{id}/complete`  
**Access**: Owner  
**Auth**: Required  

**Path Parameters**:
- `id` (number): Booking ID

**Response**: `200 OK`
```json
{
    "message": "Booking completed"
}
```

**Business Rules**:
- Only `ACCEPTED` bookings can be completed
- Vehicle availability updated

---

## Admin Endpoints

### 1. Get All Users
Retrieve all registered users.

**Endpoint**: `GET /api/admin/users`  
**Access**: Admin  
**Auth**: Required  

**Query Parameters**:
- `role` (optional): Filter by role (`CUSTOMER`, `OWNER`, `ADMIN`)
- `isVerified` (optional): Filter by verification status

**Response**: `200 OK`
```json
[
    {
        "id": 1,
        "email": "john.doe@example.com",
        "fullName": "John Doe",
        "phone": "+1234567890",
        "role": "CUSTOMER",
        "isVerified": true,
        "isActive": true,
        "createdAt": "2024-01-15T08:30:00"
    }
]
```

### 2. Verify Owner
Verify an owner's account.

**Endpoint**: `PUT /api/admin/users/{id}/verify`  
**Access**: Admin  
**Auth**: Required  

**Path Parameters**:
- `id` (number): User ID

**Response**: `200 OK`
```json
{
    "message": "Owner verified successfully"
}
```

### 3. Deactivate/Delete User
Remove or deactivate a user account.

**Endpoint**: `DELETE /api/admin/users/{id}`  
**Access**: Admin  
**Auth**: Required  

**Path Parameters**:
- `id` (number): User ID

**Response**: `200 OK`
```json
{
    "message": "User deleted successfully"
}
```

### 4. Get All Shops
Retrieve all rental shops.

**Endpoint**: `GET /api/admin/shops`  
**Access**: Admin  
**Auth**: Required  

**Response**: `200 OK`
```json
[
    {
        "id": 1,
        "shopName": "Mumbai Car Rentals",
        "ownerName": "Jane Smith",
        "city": "Mumbai",
        "totalVehicles": 15,
        "totalBookings": 120,
        "isActive": true
    }
]
```

### 5. Delete Shop
Remove a rental shop.

**Endpoint**: `DELETE /api/admin/shops/{id}`  
**Access**: Admin  
**Auth**: Required  

**Path Parameters**:
- `id` (number): Shop ID

**Response**: `200 OK`
```json
{
    "message": "Shop deleted successfully"
}
```

### 6. Get All Vehicles
Retrieve all vehicles across all shops.

**Endpoint**: `GET /api/admin/vehicles`  
**Access**: Admin  
**Auth**: Required  

**Response**: `200 OK`
```json
[
    {
        "id": 1,
        "vehicleName": "Toyota Innova",
        "shopName": "Mumbai Car Rentals",
        "ownerName": "Jane Smith",
        "type": "CAR",
        "isAvailable": true
    }
]
```

---

## Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Invalid or missing token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 409 | Conflict - Duplicate resource |
| 500 | Internal Server Error |

## Enums

### User Role
```
CUSTOMER, OWNER, ADMIN
```

### Vehicle Type
```
CAR, BIKE, SCOOTER, VAN
```

### Booking Status
```
PENDING, ACCEPTED, COMPLETED, CANCELLED, REJECTED
```

### Payment Status
```
PENDING, COMPLETED, FAILED, REFUNDED
```

---

**Swagger UI**: `http://localhost:8080/swagger-ui.html`

For more details, see the [main README](README.md).
