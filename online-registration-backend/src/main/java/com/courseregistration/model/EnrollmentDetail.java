package com.courseregistration.model;

//import java.time.LocalDate;

public class EnrollmentDetail {
    private String courseId;
    private String courseName;
    private String partner;
    private String semester;
    private int year;
    private String grade;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public EnrollmentDetail() {
    }

    public EnrollmentDetail(String courseId, String courseName, String partner, String semester, int year,
            String grade) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.partner = partner;
        this.semester = semester;
        this.year = year;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return " - " + courseName + " (" + courseId + ") | Partner: " + partner + " | Term: " + semester + " " + year
                + " | Grade: " + grade;
    }
}
