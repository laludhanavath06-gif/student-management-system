package com.codec.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "marks")
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull @Min(0)
    private Integer obtained;

    @NotNull @Min(1)
    private Integer maxMarks = 100;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public Integer getObtained() { return obtained; }
    public void setObtained(Integer obtained) { this.obtained = obtained; }
    public Integer getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Integer maxMarks) { this.maxMarks = maxMarks; }
}
