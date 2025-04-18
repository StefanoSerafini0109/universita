package com.example.demo.degreecourse;

import com.example.demo.exam.Exam;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class DegreeCourse {

    @Id
    @SequenceGenerator(
            name = "degree_course_sequence",
            sequenceName = "degree_course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "degree_course_sequence"
    )
    private Long id;

    private String name;
    private String code;
    private String department;
    private Integer durationYears;
    private String level; // Triennale, Magistrale, etc.

    @OneToMany(mappedBy = "degreeCourse", cascade = CascadeType.ALL)
    private List<Exam> exams = new ArrayList<>();

    public DegreeCourse() {
    }

    public DegreeCourse(String name, String code, String department, Integer durationYears, String level) {
        this.name = name;
        this.code = code;
        this.department = department;
        this.durationYears = durationYears;
        this.level = level;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(Integer durationYears) {
        this.durationYears = durationYears;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public String toString() {
        return "DegreeCourse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", department='" + department + '\'' +
                ", durationYears=" + durationYears +
                ", level='" + level + '\'' +
                '}';
    }
}