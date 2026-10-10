package com.codec.sms.repository;

import com.codec.sms.model.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNo(String rollNo);

    List<Student> findByNameContainingIgnoreCaseOrRollNoContainingIgnoreCaseOrCourseContainingIgnoreCase(
            String name, String rollNo, String course);

    @Override
    @EntityGraph(attributePaths = "marks")
    List<Student> findAll();

    @EntityGraph(attributePaths = "marks")
    Optional<Student> findWithMarksById(Long id);
}
