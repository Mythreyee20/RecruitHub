# RecruitHub 🚀

A full-stack placement management system that helps students discover suitable companies, upload resumes, calculate job-match percentages, apply for opportunities, and track their applications.

## ✨ Features

- 🔐 Student Registration & Login
- 📄 Resume Upload
- 🧠 Resume Skill Extraction
- 📊 Resume-to-Job Match Percentage
- 🏢 Company & Job Listings
- 📝 Apply for Jobs
- 🚫 Duplicate Application Prevention
- 📋 My Applications
- ↩️ Withdraw Application
- 💾 MySQL Database Integration
- 🔗 Zoho CRM Lead Integration

## 🛠️ Tech Stack

### Frontend
- HTML
- CSS
- JavaScript

### Backend
- Java
- Spring Boot
- Spring Data JPA
- REST APIs

### Database
- MySQL

### Integration
- Zoho CRM API

## 🔄 Application Workflow

Student
→ Register / Login
→ Upload Resume
→ Extract Skills
→ Calculate Match Percentage
→ View Companies
→ Apply
→ Application Stored in MySQL
→ Lead Created in Zoho CRM
→ Track Application

## 📁 Project Structure

```text
RecruitHub/
├── Backend/
│   └── backend/
│       ├── src/
│       ├── pom.xml
│       └── ...
│
├── Frontend/
│   ├── CSS/
│   │   └── style.css
│   ├── HTML/
│   │   ├── index.html
│   │   ├── register.html
│   │   └── dashboard.html
│   └── JS/
│       └── script.js
│
├── Database/
└── README.md
```


⚙️ How to Run

Backend

Open the Backend/backend folder.
Configure MySQL database in application.properties.
Start the Spring Boot application.

Backend runs on:

http://localhost:8080
Frontend

Open:

Frontend/HTML/index.html

using Live Server in VS Code.

🔗 Zoho CRM Integration

When a student applies for a company, RecruitHub sends the student's application details to Zoho CRM and creates a Lead.

🎯 Project Goal

RecruitHub aims to simplify the student placement process by combining resume analysis, job matching, application tracking, and CRM integration into a single platform.

📌 Project Status

✅ Core features completed and tested.

