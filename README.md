# Student Management System

Spring Boot 3 · Spring Data JPA (Hibernate) · MySQL · Thymeleaf

 ## Screenshots

### Students List
![Students List](Image/Screenshot%202026-10-11%20000709.png)

### Add Student
![Add Student](Image/Screenshot%202026-10-11%20000723.png)

### Class Performance Report
![Reports](Image/Screenshot%202026-10-11%20000732.png)
   
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
