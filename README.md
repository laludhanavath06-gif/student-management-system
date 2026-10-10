# Student Management System

Spring Boot 3 · Spring Data JPA (Hibernate) · MySQL · Thymeleaf

## Screenshots

### Students List
Search, view, edit, delete students and add marks.

![Students List](screenshots/students-list.png)

### Add Student
Form to register a new student with roll number, name, email, course and phone.

![Add Student](screenshots/add-student.png)

### Class Performance Report
Rank, total, percentage, grade and result, with CSV export and print.

![Reports](screenshots/reports.png)

## Features
- CRUD for student details with validation and duplicate roll-number check
- Search by name / roll no / course
- Marks entry per subject (with max-marks validation)
- Report card per student (printable / save as PDF)
- Class performance report with ranking, average, pass count and CSV export

## Run
1. Install JDK 17+, Maven, MySQL.
2. Update DB credentials in `src/main/resources/application.properties` (database `student_db` is created automatically).
3. `mvn spring-boot:run`
4. Open http://localhost:8080
