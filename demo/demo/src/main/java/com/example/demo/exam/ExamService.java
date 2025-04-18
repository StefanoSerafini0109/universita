package com.example.demo.exam;

import com.example.demo.degreecourse.DegreeCourse;
import com.example.demo.degreecourse.DegreeCourseRepository;
import com.example.demo.professor.Professor;
import com.example.demo.professor.ProfessorRepository;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final DegreeCourseRepository degreeCourseRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ExamService(
            ExamRepository examRepository,
            DegreeCourseRepository degreeCourseRepository,
            ProfessorRepository professorRepository,
            StudentRepository studentRepository) {
        this.examRepository = examRepository;
        this.degreeCourseRepository = degreeCourseRepository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Exam with id " + id + " does not exist"));
    }

    public void addExam(Exam exam, Long degreeCourseId, Long professorId) {
        Optional<Exam> existingExam = examRepository.findByCode(exam.getCode());
        if (existingExam.isPresent()) {
            throw new IllegalStateException("Exam with code " + exam.getCode() + " already exists");
        }

        DegreeCourse degreeCourse = degreeCourseRepository.findById(degreeCourseId)
                .orElseThrow(() -> new IllegalStateException("Degree course with id " + degreeCourseId + " does not exist"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalStateException("Professor with id " + professorId + " does not exist"));

        exam.setDegreeCourse(degreeCourse);
        exam.setProfessor(professor);

        examRepository.save(exam);
    }

    @Transactional
    public void updateExam(Long id, Exam updatedExam, Long degreeCourseId, Long professorId) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Exam with id " + id + " does not exist"));

        if (updatedExam.getName() != null) {
            exam.setName(updatedExam.getName());
        }

        if (updatedExam.getCode() != null) {
            Optional<Exam> existingExam = examRepository.findByCode(updatedExam.getCode());
            if (existingExam.isPresent() && !existingExam.get().getId().equals(id)) {
                throw new IllegalStateException("Exam with code " + updatedExam.getCode() + " already exists");
            }
            exam.setCode(updatedExam.getCode());
        }

        if (updatedExam.getCredits() != null) {
            exam.setCredits(updatedExam.getCredits());
        }

        if (updatedExam.getSemester() != null) {
            exam.setSemester(updatedExam.getSemester());
        }

        if (updatedExam.getYear() != null) {
            exam.setYear(updatedExam.getYear());
        }

        if (degreeCourseId != null) {
            DegreeCourse degreeCourse = degreeCourseRepository.findById(degreeCourseId)
                    .orElseThrow(() -> new IllegalStateException("Degree course with id " + degreeCourseId + " does not exist"));
            exam.setDegreeCourse(degreeCourse);
        }

        if (professorId != null) {
            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new IllegalStateException("Professor with id " + professorId + " does not exist"));
            exam.setProfessor(professor);
        }
    }

    public void deleteExam(Long id) {
        boolean exists = examRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Exam with id " + id + " does not exist");
        }
        examRepository.deleteById(id);
    }

    @Transactional
    public void enrollStudentInExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalStateException("Exam with id " + examId + " does not exist"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist"));

        exam.enrollStudent(student);
    }

    @Transactional
    public void removeStudentFromExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalStateException("Exam with id " + examId + " does not exist"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist"));

        exam.removeStudent(student);
    }

    public List<Exam> getExamsByDegreeCourse(Long degreeCourseId) {
        boolean exists = degreeCourseRepository.existsById(degreeCourseId);
        if (!exists) {
            throw new IllegalStateException("Degree course with id " + degreeCourseId + " does not exist");
        }
        return examRepository.findByDegreeCourseId(degreeCourseId);
    }

    public List<Exam> getExamsByProfessor(Long professorId) {
        boolean exists = professorRepository.existsById(professorId);
        if (!exists) {
            throw new IllegalStateException("Professor with id " + professorId + " does not exist");
        }
        return examRepository.findByProfessorId(professorId);
    }

    public List<Student> getEnrolledStudents(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalStateException("Exam with id " + examId + " does not exist"));

        return exam.getEnrolledStudents();
    }

    public Map<String, Object> getExamStatistics() {
        List<Exam> exams = examRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalExams", exams.size());

        // Media dei crediti per esame
        double avgCredits = exams.stream()
                .mapToInt(Exam::getCredits)
                .average()
                .orElse(0.0);
        stats.put("averageCredits", avgCredits);

        // Numero di esami per corso di laurea
        Map<String, Long> examsByDegreeCourse = exams.stream()
                .collect(Collectors.groupingBy(
                        exam -> exam.getDegreeCourse().getName(),
                        Collectors.counting()));
        stats.put("examsByDegreeCourse", examsByDegreeCourse);

        // Numero di esami per professore
        Map<String, Long> examsByProfessor = exams.stream()
                .collect(Collectors.groupingBy(
                        exam -> exam.getProfessor().getName(),
                        Collectors.counting()));
        stats.put("examsByProfessor", examsByProfessor);

        // Numero medio di studenti iscritti per esame
        double avgEnrolledStudents = exams.stream()
                .mapToInt(exam -> exam.getEnrolledStudents().size())
                .average()
                .orElse(0.0);
        stats.put("averageEnrolledStudents", avgEnrolledStudents);

        return stats;
    }
}
