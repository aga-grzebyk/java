package com.agagrzebyk.java.web.controller;


import com.agagrzebyk.java.model.User;
import com.agagrzebyk.java.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SimpleController {

    private UserRepository userRepository;

    public SimpleController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("test1")
    public String test1(){
        return "API test 1";
    }

    @GetMapping("test2")
    public String test2(){
        return "API test 2";
    }

    @GetMapping("users")
    public List<User> users(){
        return this.userRepository.findAll();
    }
}
