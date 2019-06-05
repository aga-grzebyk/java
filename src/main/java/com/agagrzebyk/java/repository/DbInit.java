package com.agagrzebyk.java.repository;

import com.agagrzebyk.java.model.Course;
import com.agagrzebyk.java.model.Status;
import com.agagrzebyk.java.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {

    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DbInit(UserRepository userRepository,
                  CourseRepository courseRepository,
                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Delete all
        this.userRepository.deleteAll();
        this.courseRepository.deleteAll();

        //  Create courses
        Course course1 = new Course();
        course1.setTitle("Database");
        course1.setStartDate(Date.valueOf("2021-11-21"));
        course1.setStatus(Status.OPEN);

        Course course2 = new Course();
        course2.setTitle("Graphic Design");
        course2.setStartDate(Date.valueOf("1919-09-21"));
        course2.setStatus(Status.CANCELLED);

        Course course3 = new Course();
        course3.setTitle("Java 11");
        course3.setStartDate(Date.valueOf("2919-09-02"));
        course3.setStatus(Status.COMPLETED);

        Course course4 = new Course();
        course4.setTitle("ORM Hibernate");
        course4.setStartDate(Date.valueOf("2020-01-05"));
        course4.setStatus(Status.OPEN);

        this.courseRepository.saveAll(Arrays.asList(course1, course2, course3, course4));

        // Crete users
        User aga = new User("user", "user@java.dev", passwordEncoder.encode("user123"),"USER","");
        User admin = new User("admin", "admin@java.dev", passwordEncoder.encode("admin123"),"ADMIN","ACCESS_TEST1,ACCESS_TEST2");
        User student1 = new User("student1", "student1@java.dev", passwordEncoder.encode("student123"),"STUDENT","ACCESS_TEST1");
        User student2 = new User("student2", "student2@java.dev", passwordEncoder.encode("student123"),"STUDENT","ACCESS_TEST1");

        student1.getFieldOfStudy().add(course3);
        student1.getFieldOfStudy().add(course4);
        student2.getFieldOfStudy().add(course2);
        List<User> users = Arrays.asList(aga,admin,student1,student2);

        // Save to db
        this.userRepository.saveAll(users);
    }
}
