package com.example.demo.professor;

import com.example.demo.exam.Exam;
import com.example.demo.exam.ExamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final ExamRepository examRepository;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository, ExamRepository examRepository) {
        this.professorRepository = professorRepository;
        this.examRepository = examRepository;
    }

    public List<Professor> getProfessor() {
        return professorRepository.findAll();
    }

    public List<Exam> getProfessorExams(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalStateException("Professor with id " + professorId + " does not exist"));

        return professor.getTaughtExams();
    }

    public void addNewProfessor(Professor professor) {
        String baseEmail = generateBaseEmail(professor.getName());
        String uniqueEmail = generateUniqueEmail(baseEmail);
        professor.setEmail(uniqueEmail);
        professorRepository.save(professor);
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
        while (professorRepository.findProfessorByEmail(email).isPresent()) {
            String withoutDomain = baseEmail.substring(0, baseEmail.indexOf("@"));
            String domain = baseEmail.substring(baseEmail.indexOf("@"));
            email = withoutDomain + count + domain;
            count++;
        }
        return email;
    }

    public void deleteProfessor(Long professorId) {
        boolean exists = professorRepository.existsById(professorId);
        if (!exists) {
            throw new IllegalStateException("Professor with id " + professorId + " does not exist");
        }
        professorRepository.deleteById(professorId);
    }



    @Transactional
    public void updateProfessor(Long professorId, String name, String email) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalStateException(
                        "Professor with id " + professorId + " does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(professor.getName(), name)) {
            professor.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(professor.getEmail(), email)) {
            Optional<Professor> professorByEmail = professorRepository.findProfessorByEmail(email);
            if (professorByEmail.isPresent()) {
                throw new IllegalStateException("Email already taken");
            }
            professor.setEmail(email);
        }
    }

    public void addMultipleProfessor(List<Professor> professors) {
        // Modifica per generare automaticamente le email
        for (Professor professor : professors) {
            // Applica la stessa logica di addNewStudent
            String baseEmail = generateBaseEmail(professor.getName());
            String uniqueEmail = generateUniqueEmail(baseEmail);
            professor.setEmail(uniqueEmail);
        }
        // Salva tutti gli studenti dopo aver generato le email
        professorRepository.saveAll(professors);
    }

    public Map<String, Object> getProfessorStatistics() {
        List<Professor> professors = professorRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalProfessors", professors.size());

        // Raggruppamento professori per dipartimento
        Map<String, Long> professorsByDepartment = professors.stream()
                .collect(Collectors.groupingBy(Professor::getDepartment, Collectors.counting()));
        stats.put("professorsByDepartment", professorsByDepartment);

        // Numero medio di materie insegnate per professore
        double avgTaughtExams = professors.stream()
                .mapToInt(professor -> professor.getTaughtExams().size())
                .average()
                .orElse(0.0);
        stats.put("averageTaughtExams", avgTaughtExams);

        // Professori con il maggior numero di esami insegnati
        professors.sort(Comparator.comparing(p -> p.getTaughtExams().size(), Comparator.reverseOrder()));

        List<Map<String, Object>> topProfessors = professors.stream()
                .limit(5)  // Prendiamo i primi 5 professori
                .map(p -> {
                    Map<String, Object> profStats = new HashMap<>();
                    profStats.put("name", p.getName());
                    profStats.put("department", p.getDepartment());
                    profStats.put("examCount", p.getTaughtExams().size());
                    return profStats;
                })
                .collect(Collectors.toList());

        stats.put("topProfessorsByExamCount", topProfessors);

        return stats;
    }
}
