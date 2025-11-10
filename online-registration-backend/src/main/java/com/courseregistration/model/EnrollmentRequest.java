package com.courseregistration.model;

/**
 * Data Transfer Object (DTO) for handling incoming JSON data
 * when enrolling a student to a course via the REST API (POST /enroll).
 */
public class EnrollmentRequest {

    private Integer studentId;
    private String courseId;
    private Integer year;
    private String semester;

    public Integer getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Integer getYear() {
        return year;
    }

    public String getSemester() {
        return semester;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
