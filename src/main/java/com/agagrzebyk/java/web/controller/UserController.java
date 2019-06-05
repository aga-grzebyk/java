package com.agagrzebyk.java.web.controller;


import com.agagrzebyk.java.model.User;
import com.agagrzebyk.java.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/student")
public class UserController {

//    @GetMapping("mycourses")
//    public String mycourses(){
//        return "my all courses";
//    }

    private final UserService userService;
    private final UserResourceAssembler userAssembler;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService,
                          UserResourceAssembler userAssembler,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    Resources<Resource<User>> all() {
        List<Resource<User>> users = userService.getAllStudents()
                .stream()
                .map(userAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(users,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping
    ResponseEntity<?> addStudent(@RequestBody User user) throws URISyntaxException {
        Resource<User> newStudent = userAssembler.toResource(userService.addNewUser(user));
        return ResponseEntity.created(new URI(newStudent.getId().expand().getHref())).body(newStudent);
    }

    @GetMapping("/{id}")
    Resource<User> getOneStudent(@PathVariable("id") long id) {

        User user = userService.getStudentById(id);
        return userAssembler.toResource(user);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceStudent(@RequestBody User user, @PathVariable("id") Long id) throws URISyntaxException {
        User updatedUser = userService.replaceStudentById(user, id);
        Resource<User> newStudent = userAssembler.toResource(updatedUser);
        return ResponseEntity.created(new URI(newStudent.getId().expand().getHref())).body(newStudent);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteStudent(@PathVariable("id") Long id) {
        userService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }
}
