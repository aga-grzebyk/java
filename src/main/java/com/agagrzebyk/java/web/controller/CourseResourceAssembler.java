package com.agagrzebyk.java.web.controller;

import com.agagrzebyk.java.model.Course;
import com.agagrzebyk.java.model.Status;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CourseResourceAssembler implements ResourceAssembler<Course, Resource<Course>> {
    @Override
    public Resource<Course> toResource(Course course) {

        Resource<Course> courseResource = new Resource<>(course,
                linkTo(methodOn(CourseController.class).getOneCourse(course.getId())).withSelfRel(),
                linkTo(methodOn(CourseController.class).all()).withRel("courses"));

        if(course.getStatus() == Status.OPEN){
            courseResource.add(linkTo(methodOn(CourseController.class).cancel(course.getId())).withRel("cancel"));
            try {
                courseResource.add(linkTo(methodOn(CourseController.class).complete(course.getId())).withRel("complete"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return courseResource;
    }
}
