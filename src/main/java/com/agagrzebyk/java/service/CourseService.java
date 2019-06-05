package com.agagrzebyk.java.service;

import com.agagrzebyk.java.model.Course;
import com.agagrzebyk.java.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {

        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        Optional<Course> foundCourse = courseRepository.findById(id);
        Course course = foundCourse
                .orElseThrow(() -> new CourseNotFoundException(id));
        return course;
    }

    public Course addNewCourse(Course course) {
        //   course.setStatus(Status.OPEN);
        return courseRepository.save(course);
    }

    public Course replaceCourseById(Course course, Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id)
                .map(newCourse -> {
                    newCourse.setTitle(course.getTitle());
                    newCourse.setDescription(course.getDescription());
                    newCourse.setStartDate(course.getStartDate());
                    newCourse.setTeachers(course.getTeachers());
                    newCourse.setUsers(course.getUsers());
                    return courseRepository.save(newCourse);
                });
        Course updatedCourse = optionalCourse
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return updatedCourse;
    }

    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }
}
