package com.courseregistration.model;

/**
 * Data Transfer Object (DTO) for handling incoming JSON data
 * when registering a new student via the REST API (POST /students).
 */
public class CourseRegistrationRequest {

    private String CourseId;
    private String Partner;
    private String CourseName;
    private String Rating;
    private String CertificateType;
    private String Duration;

    public void setCourseId(String courseId) {
        this.CourseId = courseId;
    }

    public String getCourseId() {
        return CourseId;
    }

    public String getPartner() {
        return Partner;
    }

    public void setPartner(String Partner) {
        this.Partner = Partner;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String CourseName) {
        this.CourseName = CourseName;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getCertificateType() {
        return CertificateType;
    }

    public void setCertificateType(String CertificateType) {
        this.CertificateType = CertificateType;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String Duration) {
        this.Duration = Duration;
    }
}
