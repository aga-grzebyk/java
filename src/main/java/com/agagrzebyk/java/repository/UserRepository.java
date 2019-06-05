package com.agagrzebyk.java.repository;

import com.agagrzebyk.java.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

 //   User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByRoles(String role);
}
