package com.example.demo.dto;

public class EnrollmentDTO {
    private Long studentId;
    private Long examId;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Long studentId, Long examId) {
        this.studentId = studentId;
        this.examId = examId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }
}