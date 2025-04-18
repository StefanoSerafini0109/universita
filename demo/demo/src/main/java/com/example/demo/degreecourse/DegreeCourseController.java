package com.example.demo.degreecourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/degreecourses")
public class DegreeCourseController {

    private final DegreeCourseService degreeCourseService;

    @Autowired
    public DegreeCourseController(DegreeCourseService degreeCourseService) {
        this.degreeCourseService = degreeCourseService;
    }

    @GetMapping
    public List<DegreeCourse> getAllDegreeCourses() {
        return degreeCourseService.getAllDegreeCourses();
    }

    @GetMapping("/{id}")
    public DegreeCourse getDegreeCourseById(@PathVariable Long id) {
        return degreeCourseService.getDegreeCourseById(id);
    }

    @PostMapping
    public void addDegreeCourse(@RequestBody DegreeCourse degreeCourse) {
        degreeCourseService.addDegreeCourse(degreeCourse);
    }

    @PutMapping("/{id}")
    public void updateDegreeCourse(@PathVariable Long id, @RequestBody DegreeCourse degreeCourse) {
        degreeCourseService.updateDegreeCourse(id, degreeCourse);
    }

    @DeleteMapping("/{id}")
    public void deleteDegreeCourse(@PathVariable Long id) {
        degreeCourseService.deleteDegreeCourse(id);
    }

    @GetMapping("/stats")
    public Map<String, Object> getDegreeCourseStatistics() {
        return degreeCourseService.getDegreeCourseStatistics();
    }
}
