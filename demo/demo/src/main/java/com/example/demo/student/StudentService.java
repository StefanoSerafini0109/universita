package com.example.demo.student;

import com.example.demo.exam.Exam;
import com.example.demo.exam.ExamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, ExamRepository examRepository) {
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
    }


    public List<Student> getStudents(){
        return studentRepository.findAll();
    }


    public void addNewStudent(Student student) {
        String baseEmail = generateBaseEmail(student.getName());
        String uniqueEmail = generateUniqueEmail(baseEmail);
        student.setEmail(uniqueEmail);
        studentRepository.save(student);
    }

    public List<Exam> getStudentExams(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist"));

        return student.getExams();
    }



    private String generateBaseEmail(String fullName) {
        String[] parts = fullName.trim().toLowerCase().split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Il nome deve includere almeno nome e cognome");
        }
        return parts[0] + "." + parts[parts.length - 1] + "@polito.it";
    }

    private String generateUniqueEmail(String baseEmail) {
        String email = baseEmail;
        int count = 1;
        while (studentRepository.findStudentByEmail(email).isPresent()) {
            String withoutDomain = baseEmail.substring(0, baseEmail.indexOf("@"));
            String domain = baseEmail.substring(baseEmail.indexOf("@"));
            email = withoutDomain + count + domain;
            count++;
        }
        return email;
    }

    public void deleteStudent(Long studentId) {

        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " does not exist");

        }

        studentRepository.deleteById(studentId);

    }


    public Map<String, Object> getStudentStatistics() {
        List<Student> students = studentRepository.findAll();

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalStudents", students.size());

        Set<String> departments = students.stream()
                .map(Student::getDepartment)
                .filter(department -> department != null && !department.isEmpty())
                .collect(Collectors.toSet());

        stats.put("totalDepartments", departments.size());
        stats.put("departments", departments);

        Map<String, Set<String>> departmentDegreeCourses = new HashMap<>();

        // Studenti per dipartimento e corso
        Map<String, Map<String, Object>> studentsPerDepartmentAndCourse = new HashMap<>();

        Map<String, List<Student>> studentsByDepartment = students.stream()
                .filter(s -> s.getDepartment() != null && s.getDegreeCourse() != null)
                .collect(Collectors.groupingBy(Student::getDepartment));

        for (Map.Entry<String, List<Student>> entry : studentsByDepartment.entrySet()) {
            String department = entry.getKey();
            List<Student> deptStudents = entry.getValue();

            Map<String, Long> courseCounts = deptStudents.stream()
                    .collect(Collectors.groupingBy(Student::getDegreeCourse, Collectors.counting()));

            Map<String, Object> deptStats = new HashMap<>();
            deptStats.put("totalStudents", deptStudents.size());
            deptStats.put("degreeCourses", courseCounts);

            studentsPerDepartmentAndCourse.put(department, deptStats);
        }

        stats.put("studentsPerDepartmentAndCourse", studentsPerDepartmentAndCourse);

        // Nuova statistica: numero medio di esami per studente
        double avgExamsPerStudent = students.stream()
                .mapToInt(student -> student.getExams().size())
                .average()
                .orElse(0.0);
        stats.put("averageExamsPerStudent", avgExamsPerStudent);

        return stats;
    }





    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student with id " + studentId + " does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentByEmail = studentRepository.findStudentByEmail(email);
            if (studentByEmail.isPresent()) {
                throw new IllegalStateException("Email already taken");
            }
            student.setEmail(email);
        }


    }

    public void addMultipleStudents(List<Student> students) {
        // Modifica per generare automaticamente le email
        for (Student student : students) {
            // Applica la stessa logica di addNewStudent
            String baseEmail = generateBaseEmail(student.getName());
            String uniqueEmail = generateUniqueEmail(baseEmail);
            student.setEmail(uniqueEmail);
        }
        // Salva tutti gli studenti dopo aver generato le email
        studentRepository.saveAll(students);
    }

}
