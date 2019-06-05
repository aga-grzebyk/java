package com.agagrzebyk.java.web.controller;

import com.agagrzebyk.java.model.Course;
import com.agagrzebyk.java.model.Status;
import com.agagrzebyk.java.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final CourseResourceAssembler courseAssembler;

    @Autowired
    public CourseController(CourseService courseService, CourseResourceAssembler courseAssembler) {
        this.courseService = courseService;
        this.courseAssembler = courseAssembler;
    }


    @GetMapping("/all")
    Resources<Resource<Course>> all() {
        List<Resource<Course>> courses = courseService.getAllCourses()
                .stream()
                .map(courseAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(courses,
                linkTo(methodOn(CourseController.class).all()).withSelfRel());
    }

    @PostMapping
    ResponseEntity<?> newCourse(@RequestBody Course course) throws URISyntaxException {
        course.setStatus(Status.OPEN);
        Resource<Course> newCourse = courseAssembler.toResource(courseService.addNewCourse(course));
        return ResponseEntity.created(new URI(newCourse.getId().expand().getHref())).body(newCourse);
    }

    @GetMapping("/{id}")
    Resource<Course> getOneCourse(@PathVariable("id") long id) {

        Course course = courseService.getCourseById(id);
        return courseAssembler.toResource(course);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceCourse(@RequestBody Course course, @PathVariable("id") Long id) throws URISyntaxException {
        Course updatedCourse = courseService.replaceCourseById(course, id);
        Resource<Course> newCourse = courseAssembler.toResource(updatedCourse);
        return ResponseEntity.created(new URI(newCourse.getId().expand().getHref())).body(newCourse);
    }

    @PutMapping("/{id}/complete")
    ResponseEntity<ResourceSupport> complete(@PathVariable("id") Long id) throws URISyntaxException {
        Course course = courseService.getCourseById(id);

        if(course.getStatus() == Status.OPEN){
            course.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(courseAssembler.toResource(courseService.replaceCourseById(course, id)));
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed",
                        "You can't cancel a course that is in the " + course.getStatus() + " status"));
    }

    @DeleteMapping("/{id}/cancel")
    ResponseEntity<ResourceSupport> cancel(@PathVariable("id") Long id) {
        Course course = courseService.getCourseById(id);

        if(course.getStatus() == Status.OPEN){
            course.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(courseAssembler.toResource(courseService.replaceCourseById(course, id)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed",
                        "You can't cancel a course that is in the " + course.getStatus() + " status"));
    }
}
