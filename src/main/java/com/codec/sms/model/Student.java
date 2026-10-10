package com.codec.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Roll number is required")
    @Column(unique = true, nullable = false)
    private String rollNo;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Course is required")
    private String course;

    private String phone;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mark> marks = new ArrayList<>();

    public int getTotalObtained() {
        return marks.stream().mapToInt(Mark::getObtained).sum();
    }

    public int getTotalMax() {
        return marks.stream().mapToInt(Mark::getMaxMarks).sum();
    }

    public double getPercentage() {
        int max = getTotalMax();
        return max == 0 ? 0.0 : (getTotalObtained() * 100.0) / max;
    }

    public String getGrade() {
        if (marks.isEmpty()) return "-";
        double p = getPercentage();
        if (p >= 90) return "A+";
        if (p >= 80) return "A";
        if (p >= 70) return "B";
        if (p >= 60) return "C";
        if (p >= 50) return "D";
        return "F";
    }

    public boolean isPassed() {
        return !marks.isEmpty() && getPercentage() >= 50;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<Mark> getMarks() { return marks; }
    public void setMarks(List<Mark> marks) { this.marks = marks; }
}
