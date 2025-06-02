# 🏥 Hospital Management System

A Java Servlet and MySQL-based web application to manage hospital patient and doctor data.

## 💡 Features

- Add / Delete Patients
- Add / Delete Doctors
- View Patients & Doctors
- Lightweight JSON API

## ⚙️ Technologies

- Java Servlets (JSP)
- MySQL Database
- HTML, CSS
- Apache Tomcat

## 🛠 Setup Instructions

1. Clone the repo:  
   `git clone https://github.com/Raajkr07/Hospital_Management_System.git`

2. Create MySQL DB `hospital_db`.

3. Add tables:
   ```sql
   CREATE TABLE patients (
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(100),
     age INT,
     gender VARCHAR(10),
     disease VARCHAR(100)
   );

   CREATE TABLE doctors (
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(100),
     age INT,
     gender VARCHAR(10),
     specialty VARCHAR(100)
   );

