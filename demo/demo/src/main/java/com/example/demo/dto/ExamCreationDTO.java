package com.example.demo.dto;

import java.util.List;

public class ExamCreationDTO {
    private String name;
    private String code;
    private Integer credits;
    private String semester;
    private Integer year;
    private Long degreeCourseId;
    private Long professorId;

    public ExamCreationDTO() {
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

    public Long getDegreeCourseId() {
        return degreeCourseId;
    }

    public void setDegreeCourseId(Long degreeCourseId) {
        this.degreeCourseId = degreeCourseId;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }
}