package com.courseregistration.model;

public class Students {
    private Integer studentId;
    private String firstName;
    private String lastName;
    private String email;

    public Students(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Students(Integer studentId, String firstName, String lastName, String email) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }

    public void setStudentId(Integer studentId) {this.studentId = studentId;}
}
