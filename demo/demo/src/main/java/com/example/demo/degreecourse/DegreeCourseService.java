package com.example.demo.degreecourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DegreeCourseService {

    private final DegreeCourseRepository degreeCourseRepository;

    @Autowired
    public DegreeCourseService(DegreeCourseRepository degreeCourseRepository) {
        this.degreeCourseRepository = degreeCourseRepository;
    }

    public List<DegreeCourse> getAllDegreeCourses() {
        return degreeCourseRepository.findAll();
    }

    public DegreeCourse getDegreeCourseById(Long id) {
        return degreeCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Degree course with id " + id + " does not exist"));
    }

    public void addDegreeCourse(DegreeCourse degreeCourse) {
        Optional<DegreeCourse> existingCourse = degreeCourseRepository.findByCode(degreeCourse.getCode());
        if (existingCourse.isPresent()) {
            throw new IllegalStateException("Degree course with code " + degreeCourse.getCode() + " already exists");
        }
        degreeCourseRepository.save(degreeCourse);
    }

    public void updateDegreeCourse(Long id, DegreeCourse updatedCourse) {
        DegreeCourse course = degreeCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Degree course with id " + id + " does not exist"));

        if (updatedCourse.getName() != null) {
            course.setName(updatedCourse.getName());
        }

        if (updatedCourse.getCode() != null) {
            Optional<DegreeCourse> existingCourse = degreeCourseRepository.findByCode(updatedCourse.getCode());
            if (existingCourse.isPresent() && !existingCourse.get().getId().equals(id)) {
                throw new IllegalStateException("Degree course with code " + updatedCourse.getCode() + " already exists");
            }
            course.setCode(updatedCourse.getCode());
        }

        if (updatedCourse.getDepartment() != null) {
            course.setDepartment(updatedCourse.getDepartment());
        }

        if (updatedCourse.getDurationYears() != null) {
            course.setDurationYears(updatedCourse.getDurationYears());
        }

        if (updatedCourse.getLevel() != null) {
            course.setLevel(updatedCourse.getLevel());
        }

        degreeCourseRepository.save(course);
    }

    public void deleteDegreeCourse(Long id) {
        boolean exists = degreeCourseRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Degree course with id " + id + " does not exist");
        }
        degreeCourseRepository.deleteById(id);
    }

    public Map<String, Object> getDegreeCourseStatistics() {
        List<DegreeCourse> courses = degreeCourseRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalDegreeCourses", courses.size());

        Map<String, Long> coursesByDepartment = courses.stream()
                .collect(Collectors.groupingBy(DegreeCourse::getDepartment, Collectors.counting()));
        stats.put("coursesByDepartment", coursesByDepartment);

        Map<String, Long> coursesByLevel = courses.stream()
                .collect(Collectors.groupingBy(DegreeCourse::getLevel, Collectors.counting()));
        stats.put("coursesByLevel", coursesByLevel);

        return stats;
    }
}