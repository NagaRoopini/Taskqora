# Taskqora – Team Task Management System

## Live Demo

**Deployed Application:** [https://taskqora.onrender.com](https://taskqora.onrender.com)

---

## Project Overview

Taskqora is a full-stack Team Task Management System designed to streamline project collaboration, task assignment, and team productivity within organizations.

Built with **Spring Boot + Thymeleaf + MySQL + Bootstrap + Chart.js**, Taskqora provides role-based dashboards for Admins and Team Members, enabling efficient project tracking, task delegation, and performance monitoring.

---

## Key Features

### Admin Module

* Secure Admin Login/Register
* Interactive Admin Dashboard
* Create, Edit, Delete Projects
* Create, Assign, Track Tasks
* Team Member Management
* Project Progress Monitoring
* Task Status Analytics
* Profile Management
* Settings Configuration
* System Overview

### Team Member Module

* Secure Member Registration/Login
* Personalized Dashboard
* View Assigned Tasks
* View Assigned Projects
* Track Task Deadlines
* Profile Management
* Settings Management
* Task Progress Visibility

---

## Technology Stack

### Frontend

* HTML5
* CSS3
* Bootstrap 5
* JavaScript
* Thymeleaf
* Chart.js

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

### Database

* MySQL (Aiven Cloud MySQL for deployment)

### Deployment

* Render (Docker-based deployment)
* GitHub (Version Control)

---

## Security Features

* JWT Authentication
* Role-Based Access Control
* Password Encryption
* Secure Session Handling
* Environment Variable Protection

---

## Project Structure

```bash
Taskqora/
│── src/main/java/com/taskqora/
│   ├── controller/
│   ├── model/
│   ├── repository/
│   ├── service/
│   ├── security/
│
│── src/main/resources/
│   ├── templates/
│   ├── static/
│   ├── application.properties
│
│── Dockerfile
│── pom.xml
│── README.md
```

---

## Deployment Details

### Live URL:

[https://taskqora.onrender.com](https://taskqora.onrender.com)

### Hosting Platform:

* Render Web Service
* Docker Containerized Deployment
* Cloud MySQL Database

---

## Installation Guide (Local Setup)

### Prerequisites

* Java 17+
* Maven
* MySQL
* Git

### Steps

```bash
git clone https://github.com/yourusername/Taskqora.git
cd Taskqora
```

### Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskqora
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Run Application

```bash
mvn spring-boot:run
```

### Access

```bash
http://localhost:8080
```

---

## Future Enhancements

* Email Notifications
* Google OAuth Integration
* Advanced Analytics Dashboard
* File Attachments for Tasks
* Team Chat System
* Mobile Responsive Enhancements
* AI-based Productivity Insights

---

## Screens Included

* Login Page
* Registration Page
* Admin Dashboard
* Member Dashboard
* Projects Page
* Tasks Page
* Team Members Page
* Profile Page
* Settings Page

---

## Author

**Naga Roopini Battu**

* BTech IT Student
* Full Stack Developer
* Java | Spring Boot | MySQL | Web Development

---

## Resume Highlights

* Dynamic Portfolio Development
* Hospital Management System
* EduBridge AI Platform
* Taskqora Team Management System

---

## License

This project is developed for educational, internship, and professional portfolio purposes.

---

## Final Note

Taskqora demonstrates practical full-stack development skills including:

* Backend Architecture
* Frontend UI/UX
* Secure Authentication
* Cloud Deployment
* Database Management
* Real-world Team Collaboration Features

---

## Thank You

If you found this project valuable, feel free to explore, fork, and enhance it for future productivity solutions.
