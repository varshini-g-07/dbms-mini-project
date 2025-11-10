package com.courseregistration.model;

public class Student {
    private Integer studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Student() {
    }

    public Student(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Student(Integer studentId, String firstName, String lastName, String email, String password) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public void clearPassword() {
        this.password = "";
    }
}
