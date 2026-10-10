package com.codec.sms.controller;

import com.codec.sms.model.Mark;
import com.codec.sms.model.Student;
import com.codec.sms.repository.MarkRepository;
import com.codec.sms.repository.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    private final StudentRepository students;
    private final MarkRepository marks;

    public StudentController(StudentRepository students, MarkRepository marks) {
        this.students = students;
        this.marks = marks;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    // ---------- CRUD ----------

    @GetMapping("/students")
    @Transactional(readOnly = true)
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<Student> list = (keyword == null || keyword.isBlank())
                ? students.findAll()
                : students.findByNameContainingIgnoreCaseOrRollNoContainingIgnoreCaseOrCourseContainingIgnoreCase(
                        keyword, keyword, keyword);
        model.addAttribute("students", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("total", students.count());
        return "students/list";
    }

    @GetMapping("/students/new")
    public String newForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("title", "Add Student");
        return "students/form";
    }

    @GetMapping("/students/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Student> s = students.findById(id);
        if (s.isEmpty()) {
            ra.addFlashAttribute("error", "Student not found");
            return "redirect:/students";
        }
        model.addAttribute("student", s.get());
        model.addAttribute("title", "Edit Student");
        return "students/form";
    }

    @PostMapping("/students/save")
    public String save(@Valid @ModelAttribute("student") Student student, BindingResult result,
                       Model model, RedirectAttributes ra) {
        Optional<Student> duplicate = students.findByRollNo(student.getRollNo() == null ? "" : student.getRollNo().trim());
        if (duplicate.isPresent() && !duplicate.get().getId().equals(student.getId())) {
            result.rejectValue("rollNo", "duplicate", "Roll number already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("title", student.getId() == null ? "Add Student" : "Edit Student");
            return "students/form";
        }
        Student toSave = student;
        if (student.getId() != null) {
            toSave = students.findById(student.getId()).orElse(student);
            toSave.setRollNo(student.getRollNo().trim());
            toSave.setName(student.getName());
            toSave.setEmail(student.getEmail());
            toSave.setCourse(student.getCourse());
            toSave.setPhone(student.getPhone());
        } else {
            toSave.setRollNo(toSave.getRollNo().trim());
        }
        students.save(toSave);
        ra.addFlashAttribute("success", "Student saved successfully");
        return "redirect:/students";
    }

    @PostMapping("/students/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        students.deleteById(id);
        ra.addFlashAttribute("success", "Student deleted");
        return "redirect:/students";
    }

    // ---------- Marks ----------

    @GetMapping("/students/{id}")
    @Transactional(readOnly = true)
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Student> s = students.findWithMarksById(id);
        if (s.isEmpty()) {
            ra.addFlashAttribute("error", "Student not found");
            return "redirect:/students";
        }
        model.addAttribute("student", s.get());
        model.addAttribute("mark", new Mark());
        return "students/detail";
    }

    @PostMapping("/students/{id}/marks")
    public String addMark(@PathVariable Long id, @Valid @ModelAttribute("mark") Mark mark,
                          BindingResult result, Model model, RedirectAttributes ra) {
        Student s = students.findWithMarksById(id).orElse(null);
        if (s == null) {
            ra.addFlashAttribute("error", "Student not found");
            return "redirect:/students";
        }
        if (mark.getObtained() != null && mark.getMaxMarks() != null && mark.getObtained() > mark.getMaxMarks()) {
            result.rejectValue("obtained", "range", "Obtained marks cannot exceed maximum marks");
        }
        if (result.hasErrors()) {
            model.addAttribute("student", s);
            return "students/detail";
        }
        mark.setId(null);
        mark.setStudent(s);
        marks.save(mark);
        ra.addFlashAttribute("success", "Marks added for " + mark.getSubject());
        return "redirect:/students/" + id;
    }

    @PostMapping("/students/{studentId}/marks/{markId}/delete")
    public String deleteMark(@PathVariable Long studentId, @PathVariable Long markId, RedirectAttributes ra) {
        marks.deleteById(markId);
        ra.addFlashAttribute("success", "Marks entry removed");
        return "redirect:/students/" + studentId;
    }

    // ---------- Reports ----------

    @GetMapping("/students/{id}/report")
    @Transactional(readOnly = true)
    public String reportCard(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Student> s = students.findWithMarksById(id);
        if (s.isEmpty()) {
            ra.addFlashAttribute("error", "Student not found");
            return "redirect:/students";
        }
        model.addAttribute("student", s.get());
        return "students/report-card";
    }

    @GetMapping("/reports")
    @Transactional(readOnly = true)
    public String classReport(Model model) {
        List<Student> ranked = students.findAll().stream()
                .sorted(Comparator.comparingDouble(Student::getPercentage).reversed())
                .toList();
        double avg = ranked.stream().filter(s -> !s.getMarks().isEmpty())
                .mapToDouble(Student::getPercentage).average().orElse(0);
        long passed = ranked.stream().filter(Student::isPassed).count();
        model.addAttribute("ranked", ranked);
        model.addAttribute("average", avg);
        model.addAttribute("passed", passed);
        model.addAttribute("total", ranked.size());
        return "reports/class-report";
    }

    @GetMapping("/reports/export")
    @Transactional(readOnly = true)
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=student-report.csv");
        PrintWriter w = response.getWriter();
        w.println("Rank,Roll No,Name,Course,Total Obtained,Total Max,Percentage,Grade,Result");
        List<Student> ranked = students.findAll().stream()
                .sorted(Comparator.comparingDouble(Student::getPercentage).reversed()).toList();
        int rank = 1;
        for (Student s : ranked) {
            w.printf("%d,\"%s\",\"%s\",\"%s\",%d,%d,%.2f,%s,%s%n", rank++,
                    s.getRollNo(), s.getName().replace("\"", "\"\""), s.getCourse(),
                    s.getTotalObtained(), s.getTotalMax(), s.getPercentage(), s.getGrade(),
                    s.getMarks().isEmpty() ? "N/A" : (s.isPassed() ? "PASS" : "FAIL"));
        }
        w.flush();
    }
}
