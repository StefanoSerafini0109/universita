package com.example.demo.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT e FROM Exam e WHERE e.code = ?1")
    Optional<Exam> findByCode(String code);

    @Query("SELECT e FROM Exam e WHERE e.degreeCourse.id = ?1")
    List<Exam> findByDegreeCourseId(Long degreeCourseId);

    @Query("SELECT e FROM Exam e WHERE e.professor.id = ?1")
    List<Exam> findByProfessorId(Long professorId);
}