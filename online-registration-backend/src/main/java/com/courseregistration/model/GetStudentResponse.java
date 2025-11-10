package com.courseregistration.model;

import java.util.List;

public class GetStudentResponse {
    Student student;
    List<EnrollmentDetail> enrollmentDetails;

    public GetStudentResponse() {
    }

    public GetStudentResponse(Student student, List<EnrollmentDetail> enrollmentDetails) {
        this.student = student;
        this.enrollmentDetails = enrollmentDetails;
    }

    public List<EnrollmentDetail> getEnrollmentDetails() {
        return enrollmentDetails;
    }

    public void setEnrollmentDetails(List<EnrollmentDetail> enrollmentDetails) {
        this.enrollmentDetails = enrollmentDetails;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
