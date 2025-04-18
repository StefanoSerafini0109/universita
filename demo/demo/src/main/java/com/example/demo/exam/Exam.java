package com.example.demo.exam;

import com.example.demo.degreecourse.DegreeCourse;
import com.example.demo.professor.Professor;
import com.example.demo.student.Student;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Exam {

    @Id
    @SequenceGenerator(
            name = "exam_sequence",
            sequenceName = "exam_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exam_sequence"
    )
    private Long id;

    private String name;
    private String code;
    private Integer credits;
    private String semester; // 1, 2, Annuale
    private Integer year; // Anno di corso (1, 2, 3, 4, 5)

    @ManyToOne
    @JoinColumn(name = "degree_course_id")
    private DegreeCourse degreeCourse;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToMany
    @JoinTable(
            name = "student_exam",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> enrolledStudents = new ArrayList<>();

    public Exam() {
    }

    public Exam(String name, String code, Integer credits, String semester, Integer year, DegreeCourse degreeCourse, Professor professor) {
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.semester = semester;
        this.year = year;
        this.degreeCourse = degreeCourse;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public DegreeCourse getDegreeCourse() {
        return degreeCourse;
    }

    public void setDegreeCourse(DegreeCourse degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void enrollStudent(Student student) {
        this.enrolledStudents.add(student);
    }

    public void removeStudent(Student student) {
        this.enrolledStudents.remove(student);
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", credits=" + credits +
                ", semester='" + semester + '\'' +
                ", year=" + year +
                ", degreeCourse=" + (degreeCourse != null ? degreeCourse.getName() : null) +
                ", professor=" + (professor != null ? professor.getName() : null) +
                ", enrolledStudentsCount=" + enrolledStudents.size() +
                '}';
    }
}
