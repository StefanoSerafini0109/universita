package com.example.demo.professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Long> {

    @Query("SELECT p FROM Professor p WHERE p.email=?1")
    Optional<Professor> findProfessorByEmail(String email);
}