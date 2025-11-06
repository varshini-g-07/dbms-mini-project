package com.courseregistration.model;

//import java.time.LocalDate;

public class EnrollmentDetail {
    private String courseId;
    private String courseName;
    private String partner;
    private String semester;
    private int year;
    private String grade;
    
    // Constructor (omitted getters/setters for brevity)
    public EnrollmentDetail(String courseId, String courseName, String partner, String semester, int year, String grade) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.partner = partner;
        this.semester = semester;
        this.year = year;
        this.grade = grade;
    }
    
    // Add a toString() method for easy printing in the Service Layer
    @Override
    public String toString() {
        return " - " + courseName + " (" + courseId + ") | Partner: " + partner + " | Term: " + semester + " " + year + " | Grade: " + grade;
    }
}
