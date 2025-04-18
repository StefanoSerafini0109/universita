package com.example.demo.exam;

import com.example.demo.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/exams")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }

    @GetMapping("/{id}")
    public Exam getExamById(@PathVariable Long id) {
        return examService.getExamById(id);
    }

    @PostMapping
    public void addExam(
            @RequestBody Exam exam,
            @RequestParam Long degreeCourseId,
            @RequestParam Long professorId) {
        examService.addExam(exam, degreeCourseId, professorId);
    }

    @PutMapping("/{id}")
    public void updateExam(
            @PathVariable Long id,
            @RequestBody Exam exam,
            @RequestParam(required = false) Long degreeCourseId,
            @RequestParam(required = false) Long professorId) {
        examService.updateExam(id, exam, degreeCourseId, professorId);
    }

    @DeleteMapping("/{id}")
    public void deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
    }

    @PostMapping("/{examId}/enroll/{studentId}")
    public void enrollStudentInExam(
            @PathVariable Long examId,
            @PathVariable Long studentId) {
        examService.enrollStudentInExam(examId, studentId);
    }

    @DeleteMapping("/{examId}/enroll/{studentId}")
    public void removeStudentFromExam(
            @PathVariable Long examId,
            @PathVariable Long studentId) {
        examService.removeStudentFromExam(examId, studentId);
    }

    @GetMapping("/degreecourse/{degreeCourseId}")
    public List<Exam> getExamsByDegreeCourse(@PathVariable Long degreeCourseId) {
        return examService.getExamsByDegreeCourse(degreeCourseId);
    }

    @GetMapping("/professor/{professorId}")
    public List<Exam> getExamsByProfessor(@PathVariable Long professorId) {
        return examService.getExamsByProfessor(professorId);
    }

    @GetMapping("/{examId}/students")
    public List<Student> getEnrolledStudents(@PathVariable Long examId) {
        return examService.getEnrolledStudents(examId);
    }

    @GetMapping("/stats")
    public Map<String, Object> getExamStatistics() {
        return examService.getExamStatistics();
    }
}
