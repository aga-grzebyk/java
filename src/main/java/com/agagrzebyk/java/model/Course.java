package com.agagrzebyk.java.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tytuł jest wymagany")
    private String title;

    private String description;
    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    //private Set<User> attendance = new HashSet<>();

    @ManyToMany(mappedBy = "classes")
    // @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();


    @ManyToMany(mappedBy = "fieldOfStudy")
    @JsonIgnore
    private Set<User> users = new HashSet<>();



    public Course(){}

    public Course(@NotBlank(message = "Title is mandatory") String title,
                  Status status,
                  @NotBlank(message = "The Date of course start is mandatory") Date startDate) {
        this.title = title;
        this.status = status;
        this.startDate = startDate;
    }

    public Course(@NotBlank(message = "Title is mandatory") String title,
                  String description,
                  @NotBlank(message = "The Date of course start is mandatory") Date startDate,
                  Set<Teacher> teachers,
                  Set<User> users) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.teachers = teachers;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

//    public Set<User> getAttendance() {
//        return attendance;
//    }
//
//    public void setAttendance(Set<User> attendance) {
//        this.attendance = attendance;
//    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startDate='" + startDate + '\'' +
                ", teachers='" + teachers + '\'' +
                ", users=" + users +
                '}';
    }
}
