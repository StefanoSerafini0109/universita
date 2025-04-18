package com.example.demo.professor;

import com.example.demo.exam.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public List<Professor> getProfessors() {
        return professorService.getProfessor();
    }

    @PostMapping
    public void registerNewProfessor(@RequestBody Professor professor) {
        professorService.addNewProfessor(professor);
    }

    @PostMapping("/bulk")
    public void registerMultipleProfessors(@RequestBody List<Professor> professorList) {
        professorService.addMultipleProfessor(professorList);
    }

    @DeleteMapping(path = "{professorId}")
    public void deleteProfessor(@PathVariable("professorId") Long professorId) {
        professorService.deleteProfessor(professorId);
    }

    @PutMapping(path = "{professorId}")
    public void updateProfessor(@PathVariable("professorId") Long professorId,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String email) {
        professorService.updateProfessor(professorId, name, email);
    }

    @GetMapping("/stats")
    public Map<String, Object> getStatistics() {
        return professorService.getProfessorStatistics();
    }
    @GetMapping("/{professorId}/exams")
    public List<Exam> getProfessorExams(@PathVariable Long professorId) {
        return professorService.getProfessorExams(professorId);
    }
}
