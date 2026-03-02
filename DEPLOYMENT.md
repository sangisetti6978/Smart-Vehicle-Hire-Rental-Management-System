# Deployment Guide

Complete guide for deploying the Multi-Vendor Vehicle Rental Booking Platform to production.

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Database Setup (Oracle Cloud)](#database-setup-oracle-cloud)
3. [Backend Deployment](#backend-deployment)
4. [Frontend Deployment](#frontend-deployment)
5. [Environment Services](#environment-variables)
6. [Security Checklist](#security-checklist)
7. [Monitoring & Maintenance](#monitoring--maintenance)

## Prerequisites

### Required Accounts
- [ ] Oracle Cloud account (for database)
- [ ] AWS account (for EC2) OR Railway/Render account
- [ ] Netlify or Vercel account (for frontend)
- [ ] Gmail account (for email notifications)
- [ ] Domain name (optional but recommended)

### Tools Required
- Java 17+
- Maven 3.6+
- Git
- SSH client
- Oracle SQL Developer or SQL*Plus

## Database Setup (Oracle Cloud)

### 1. Create Oracle Autonomous Database

#### Via Oracle Cloud Console
1. Login to [Oracle Cloud Console](https://cloud.oracle.com/)
2. Navigate to **Database → Autonomous Database**
3. Click **Create Autonomous Database**
4. Configure:
   - **Workload Type**: Transaction Processing (OLTP)
   - **Deployment Type**: Shared Infrastructure
   - **Database Version**: 19c or higher
   - **OCPU Count**: 1 (can scale later)
   - **Storage**: 1 TB
   - **Admin Password**: Choose strong password
   - **Network Access**: Secure access from everywhere

5. Wait for provisioning (5-10 minutes)

### 2. Download Connection Wallet
1. Click on your database name
2. Go to **DB Connection**
3. Click **Download Wallet**
4. Save wallet ZIP file securely
5. Set wallet password

### 3. Configure TNS Connection
Extract wallet and note the connection strings from `tnsnames.ora`:
```
vehiclerental_high = (description= (retry_count=20)(retry_delay=3)...)
```

### 4. Create Database User
Connect as ADMIN:
```sql
-- Connect using SQL Developer or SQL*Plus
sqlplus admin/<password>@vehiclerental_high

-- Create application user
CREATE USER vehiclerental IDENTIFIED BY "YourSecurePassword123!";
GRANT CONNECT, RESOURCE TO vehiclerental;
GRANT UNLIMITED TABLESPACE TO vehiclerental;

-- Grant additional privileges
GRANT CREATE SESSION TO vehiclerental;
GRANT CREATE TABLE TO vehiclerental;
GRANT CREATE SEQUENCE TO vehiclerental;
GRANT CREATE VIEW TO vehiclerental;

-- Verify user created
SELECT username FROM all_users WHERE username = 'VEHICLERENTAL';
```

### 5. Import Database Schema
```sql
-- Connect as vehiclerental user
sqlplus vehiclerental/<password>@vehiclerental_high

-- Run schema script
@/path/to/schema.sql

-- Verify tables created
SELECT table_name FROM user_tables;
```

### 6. Get Connection Details
```properties
# Note these for backend configuration:
JDBC URL: jdbc:oracle:thin:@vehiclerental_high?TNS_ADMIN=/path/to/wallet
Username: vehiclerental
Password: YourSecurePassword123!
```

## Backend Deployment

### Option 1: AWS EC2

#### 1. Launch EC2 Instance
```bash
# EC2 Configuration:
- AMI: Amazon Linux 2 or Ubuntu 22.04
- Instance Type: t2.small (1 vCPU, 2 GB RAM minimum)
- Security Group: Allow ports 22 (SSH), 8080 (App), 443 (HTTPS)
- Storage: 20 GB
```

#### 2. Connect to Instance
```bash
ssh -i your-key.pem ec2-user@your-ec2-ip
```

#### 3. Install Java 17
```bash
# Amazon Linux
sudo yum install java-17-amazon-corretto-devel -y

# Ubuntu
sudo apt update
sudo apt install openjdk-17-jdk -y

# Verify installation
java -version
```

#### 4. Install Maven
```bash
# Amazon Linux
sudo yum install maven -y

# Ubuntu
sudo apt install maven -y

# Verify
mvn -version
```

#### 5. Clone and Build Application
```bash
cd /home/ec2-user
git clone https://github.com/yourusername/vehicle-rental-platform.git
cd vehicle-rental-platform/backend

# Build application
mvn clean package -DskipTests
```

#### 6. Configure Application
```bash
# Create production properties
nano src/main/resources/application-prod.properties
```

Add:
```properties
# Server
server.port=8080

# Oracle Database (Autonomous)
spring.datasource.url=jdbc:oracle:thin:@vehiclerental_high?TNS_ADMIN=/home/ec2-user/wallet
spring.datasource.username=vehiclerental
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle12cDialect

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

#### 7. Upload Wallet to EC2
```bash
# From local machine
scp -i your-key.pem wallet.zip ec2-user@your-ec2-ip:/home/ec2-user/

# On EC2
unzip wallet.zip -d /home/ec2-user/wallet
chmod 600 /home/ec2-user/wallet/*
```

#### 8. Set Environment Variables
```bash
nano ~/.bash_profile
```

Add:
```bash
export DB_PASSWORD="YourSecurePassword123!"
export JWT_SECRET="YourSecretKey256BitsLongForProduction2024!"
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="your-app-password"
export TNS_ADMIN="/home/ec2-user/wallet"
```

Apply:
```bash
source ~/.bash_profile
```

#### 9. Run Application
```bash
# Test run
java -jar -Dspring.profiles.active=prod target/vehicle-rental-platform-1.0.0.jar

# Run in background with nohup
nohup java -jar -Dspring.profiles.active=prod target/vehicle-rental-platform-1.0.0.jar > app.log 2>&1 &

# Check if running
ps aux | grep java
```

#### 10. Create Systemd Service (Recommended)
```bash
sudo nano /etc/systemd/system/vehicle-rental.service
```

Add:
```ini
[Unit]
Description=Vehicle Rental Platform
After=syslog.target network.target

[Service]
User=ec2-user
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod /home/ec2-user/vehicle-rental-platform/backend/target/vehicle-rental-platform-1.0.0.jar
SuccessExitStatus=143
Environment="DB_PASSWORD=YourSecurePassword123!"
Environment="JWT_SECRET=YourSecretKey"
Environment="MAIL_USERNAME=your-email@gmail.com"
Environment="MAIL_PASSWORD=your-app-password"
Environment="TNS_ADMIN=/home/ec2-user/wallet"

[Install]
WantedBy=multi-user.target
```

Enable and start:
```bash
sudo systemctl daemon-reload
sudo systemctl enable vehicle-rental
sudo systemctl start vehicle-rental
sudo systemctl status vehicle-rental

# View logs
sudo journalctl -u vehicle-rental -f
```

#### 11. Configure Nginx (Optional - for HTTPS)
```bash
sudo yum install nginx -y
sudo nano /etc/nginx/conf.d/vehicle-rental.conf
```

Add:
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Start Nginx:
```bash
sudo systemctl start nginx
sudo systemctl enable nginx
```

Setup SSL with Let's Encrypt:
```bash
sudo yum install certbot python3-certbot-nginx -y
sudo certbot --nginx -d your-domain.com
```

### Option 2: Railway Deployment

#### 1. Prepare Application
```bash
# Ensure pom.xml has:
<packaging>jar</packaging>
```

#### 2. Create `railway.json`:
```json
{
  "build": {
    "builder": "NIXPACKS"
  },
  "deploy": {
    "startCommand": "java -Dspring.profiles.active=prod -jar target/vehicle-rental-platform-1.0.0.jar",
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

#### 3. Create `nixpacks.toml`:
```toml
[phases.setup]
nixPkgs = ["...", "jdk17"]

[phases.build]
cmds = ["mvn clean package -DskipTests"]

[start]
cmd = "java -Dspring.profiles.active=prod -jar target/vehicle-rental-platform-1.0.0.jar"
```

#### 4. Deploy
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login
railway login

# Initialize project
railway init

# Upload wallet as volume
railway volume create wallet
railway volume mount wallet:/app/wallet

# Set environment variables
railway variables set DB_PASSWORD="password"
railway variables set JWT_SECRET="secret"
railway variables set MAIL_USERNAME="email"
railway variables set MAIL_PASSWORD="password"
railway variables set TNS_ADMIN="/app/wallet"

# Deploy
railway up
```

### Option 3: Render Deployment

#### 1. Create `render.yaml`:
```yaml
services:
  - type: web
    name: vehicle-rental-api
    env: java
    buildCommand: mvn clean package -DskipTests
    startCommand: java -Dspring.profiles.active=prod -jar target/vehicle-rental-platform-1.0.0.jar
    envVars:
      - key: JAVA_VERSION
        value: 17
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: DB_PASSWORD
        sync: false
      - key: JWT_SECRET
        sync: false
      - key: MAIL_USERNAME
        sync: false
      - key: MAIL_PASSWORD
        sync: false
```

#### 2. Deploy via Dashboard
1. Go to [render.com](https://render.com)
2. Click **New → Web Service**
3. Connect GitHub repository
4. Configure environment variables
5. Deploy

## Frontend Deployment

### Option 1: Netlify

#### 1. Prepare Frontend
Update `frontend/js/api.js`:
```javascript
const API_BASE_URL = 'https://your-backend-domain.com/api';
```

#### 2. Create `netlify.toml`:
```toml
[build]
  publish = "."

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200

[[headers]]
  for = "/*"
  [headers.values]
    X-Frame-Options = "DENY"
    X-Content-Type-Options = "nosniff"
    X-XSS-Protection = "1; mode=block"
```

#### 3. Deploy
```bash
# Install Netlify CLI
npm install -g netlify-cli

# Login
netlify login

# Initialize
cd frontend
netlify init

# Deploy
netlify deploy --prod
```

Or use **Drag & Drop** on [netlify.com](https://netlify.com).

### Option 2: Vercel

#### 1. Create `vercel.json`:
```json
{
  "rewrites": [
    { "source": "/(.*)", "destination": "/" }
  ],
  "headers": [
    {
      "source": "/(.*)",
      "headers": [
        { "key": "X-Content-Type-Options", "value": "nosniff" },
        { "key": "X-Frame-Options", "value": "DENY" },
        { "key": "X-XSS-Protection", "value": "1; mode=block" }
      ]
    }
  ]
}
```

#### 2. Deploy
```bash
# Install Vercel CLI
npm install -g vercel

# Login
vercel login

# Deploy
cd frontend
vercel --prod
```

## Environment Variables

### Backend Environment Variables
```bash
# Database
DB_URL=jdbc:oracle:thin:@vehiclerental_high?TNS_ADMIN=/path/to/wallet
DB_USERNAME=vehiclerental
DB_PASSWORD=YourSecurePassword123!

# JWT
JWT_SECRET=YourSecretKey256BitsLongForProduction2024!
JWT_EXPIRATION=86400000

# Email
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-gmail-app-password

# Oracle Wallet
TNS_ADMIN=/path/to/wallet

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

### Frontend Environment
Update `js/api.js` before deployment:
```javascript
const API_BASE_URL = process.env.API_URL || 'https://api.yourapp.com/api';
```

## Security Checklist

### Before Production
- [ ] Change default admin password
- [ ] Use strong JWT secret (256-bit minimum)
- [ ] Enable HTTPS (SSL/TLS certificates)
- [ ] Configure CORS to allow only your frontend domain
- [ ] Set secure database passwords
- [ ] Use environment variables for all secrets
- [ ] Enable firewall rules (only necessary ports)
- [ ] Implement rate limiting
- [ ] Enable SQL injection protection (JPA handles this)
- [ ] Sanitize user inputs
- [ ] Enable CSRF protection
- [ ] Set secure HTTP headers
- [ ] Regular security updates
- [ ] Backup database regularly
- [ ] Monitor logs for suspicious activity

### Database Security
```sql
-- Revoke unnecessary privileges
REVOKE ALL PRIVILEGES FROM vehiclerental;
GRANT CONNECT, RESOURCE TO vehiclerental;

-- Enable audit
AUDIT ALL ON vehiclerental.users;
```

### Application Security
```java
// Set in SecurityConfig
http.headers()
    .frameOptions().deny()
    .xssProtection()
    .contentSecurityPolicy("default-src 'self'");
```

## Monitoring & Maintenance

### Health Check Endpoint
```java
@RestController
public class HealthController {
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "timestamp", LocalDateTime.now().toString());
    }
}
```

### Logging
```properties
# application-prod.properties
logging.level.root=WARN
logging.level.com.vehiclerental=INFO
logging.file.name=/var/log/vehicle-rental/app.log
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

### Monitoring Tools
- **Application**: Spring Boot Actuator + Prometheus
- **Database**: Oracle Cloud monitoring
- **Server**: CloudWatch (AWS), Railway/Render dashboards
- **Uptime**: UptimeRobot, Pingdom

### Backup Strategy
```bash
# Database backup (daily cron job)
0 2 * * * sqlplus vehiclerental/<password>@vehiclerental_high @/path/to/backup.sql
```

### Performance Optimization
- Enable connection pooling (HikariCP)
- Add database indexes on frequently queried columns
- Implement caching (Redis/Caffeine)
- Use CDN for static assets
- Minify frontend assets
- Enable GZIP compression

## Troubleshooting

### Backend Issues
```bash
# Check application logs
sudo journalctl -u vehicle-rental -n 100

# Check if running
ps aux | grep java

# Test database connection
sqlplus vehiclerental/<password>@connection_string
```

### Frontend Issues
- Check browser console for errors
- Verify API URL is correct
- Test CORS configuration
- Check network tab in DevTools

### Database Connection Issues
```bash
# Test TNS connection
tnsping vehiclerental_high

# Verify wallet path
echo $TNS_ADMIN
ls -la $TNS_ADMIN
```

## Rollback Plan

### Backend Rollback
```bash
# Revert to previous version
sudo systemctl stop vehicle-rental
cp /backup/previous-version.jar /home/ec2-user/vehicle-rental-platform/backend/target/
sudo systemctl start vehicle-rental
```

### Database Rollback
```sql
-- Restore from backup
IMPDP vehiclerental/<password> DIRECTORY=backup_dir DUMPFILE=backup.dmp
```

## Cost Optimization

### Free Tier Options
- **Database**: Oracle Cloud Free Tier (2 ATP databases)
- **Backend**: Railway (free tier), Render (free tier)
- **Frontend**: Netlify/Vercel (free tier)
- **SSL**: Let's Encrypt (free)

### Estimated Monthly Costs (Production)
- Oracle DB: $0-50 (depending on usage)
- AWS EC2 t2.small: $17
- Netlify/Vercel: $0-20
- Total: ~$17-87/month

---

**Deployment complete! 🚀 Your application is now live!**

For support: Create an issue on GitHub or contact support@vehiclerental.com
