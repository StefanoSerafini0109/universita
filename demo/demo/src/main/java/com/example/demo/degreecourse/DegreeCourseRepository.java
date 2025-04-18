package com.example.demo.degreecourse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DegreeCourseRepository extends JpaRepository<DegreeCourse, Long> {

    @Query("SELECT d FROM DegreeCourse d WHERE d.code = ?1")
    Optional<DegreeCourse> findByCode(String code);

    @Query("SELECT d FROM DegreeCourse d WHERE d.department = ?1")
    List<DegreeCourse> findByDepartment(String department);
}