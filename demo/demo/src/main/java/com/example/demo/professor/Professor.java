package com.example.demo.professor;

import com.example.demo.exam.Exam;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Professor {

    @Id
    @SequenceGenerator(
            name = "professor_sequence",
            sequenceName = "professor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "professor_sequence"
    )
    private Long id;

    private String name;
    private String email;
    private LocalDate dob;
    private String department;

    @ElementCollection
    private List<String> subjectsTaught;

    @OneToMany(mappedBy = "professor")
    private List<Exam> taughtExams = new ArrayList<>();

    @Transient
    private Integer age;

    public Professor() {
    }

    public Professor(String name, String email, LocalDate dob, String department, List<String> subjectsTaught) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.department = department;
        this.subjectsTaught = subjectsTaught;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getSubjectsTaught() {
        return subjectsTaught;
    }

    public void setSubjectsTaught(List<String> subjectsTaught) {
        this.subjectsTaught = subjectsTaught;
    }

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", department='" + department + '\'' +
                ", subjectsTaught=" + subjectsTaught +
                ", age=" + age +
                '}';
    }

    public List<Exam> getTaughtExams() {
        return taughtExams;
    }

    public void setTaughtExams(List<Exam> taughtExams) {
        this.taughtExams = taughtExams;
    }
}


