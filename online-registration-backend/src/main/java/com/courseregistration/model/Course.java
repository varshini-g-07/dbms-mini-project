package com.courseregistration.model;

public class Course {
    private String courseId;
    private String courseName;
    private String partner; 
    private String rating; 
    private String certificateType; 
    private String duration; 
    
    public Course(String courseId, String courseName, String partner, String rating, String certificateType, 
    String duration) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.partner = partner;
        this.rating = rating;
        this.certificateType = certificateType;
        this.duration = duration;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getPartner() {
        return partner;
    }
    
    public String getRating() {
        return rating;
    }
    
    public String getCertificateType() {
        return certificateType;
    }
    
    public String getDuration() {
        return duration;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
    
    public void setRating(String rating) {
        this.rating = rating;
    }
    
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    @Override
    public String toString() {
        return String.format("Course ID: %s | Name: %s | Partner: %s | Rating: %s | Duration: %s", courseId, courseName, 
        partner, rating, duration);
    }
}
