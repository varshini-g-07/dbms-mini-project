package com.courseregistration.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.courseregistration.dao.CourseDAO;
import com.courseregistration.dao.EnrollmentDAO;
import com.courseregistration.dao.StudentDAO;
import com.courseregistration.model.EnrollmentDetail;
import com.courseregistration.model.Student;

@Service // Spring Service component
public class RetrievalService {

    // Injectable DAO dependencies
    private final StudentDAO studentDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final CourseDAO courseDAO;

    /**
     * Constructor used by Spring to inject the required DAO dependencies.
     * Spring automatically finds the beans annotated with @Repository (StudentDAO,
     * etc.) and passes them into this constructor.
     */
    public RetrievalService(StudentDAO studentDAO, EnrollmentDAO enrollmentDAO, CourseDAO courseDAO) {
        this.studentDAO = studentDAO;
        this.enrollmentDAO = enrollmentDAO;
        this.courseDAO = courseDAO;
    }

    public Student getStudentByEmail(String email) {
        Student retrievedStudent = studentDAO.getStudentByEmail(email);
        if (retrievedStudent != null) {
            retrievedStudent.clearPassword();
        }
        return retrievedStudent;
    }

    public List<EnrollmentDetail> getStudentEnrollmentDetails(int studentId) {
        return enrollmentDAO.getStudentEnrollments(studentId);
    }
}
