# Student Management System

Spring Boot 3 · Spring Data JPA (Hibernate) · MySQL · Thymeleaf

   ## Screenshots

   ![Students List](screenshots/students-list.png)
   ![Add Student](screenshots/add-student.png)
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
