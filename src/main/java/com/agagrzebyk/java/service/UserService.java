package com.agagrzebyk.java.service;

import com.agagrzebyk.java.model.User;
import com.agagrzebyk.java.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllStudents() {

        return userRepository.findAll();
    }

    public User getStudentById(Long id) {
        Optional<User> foundStudent = userRepository.findById(id);

        User user = foundStudent
                .orElseThrow(() -> new EntityNotFoundException("Could not find user with " + id + " id."));
            return user;

    }

    public User addNewUser(User user) {
        return userRepository.save(user);
    }

    public User replaceStudentById(User user, Long id) {
        Optional<User> optionalstudent = userRepository.findById(id)
                .map(newStudent -> {
                    newStudent.setFirstName(user.getFirstName());
                    newStudent.setLastName(user.getLastName());
                    newStudent.setEmail(user.getEmail());
                    newStudent.setPassword(user.getPassword());
                    newStudent.setFieldOfStudy(user.getFieldOfStudy());
                    newStudent.setIndexNumber(user.getIndexNumber());
                    return userRepository.save(newStudent);
                });
        User updatedUser = optionalstudent
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return updatedUser;
    }

    public void deleteStudentById(Long id) {
        userRepository.deleteById(id);
    }
}
