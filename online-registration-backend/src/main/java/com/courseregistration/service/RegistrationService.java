package com.courseregistration.service;

import com.courseregistration.dao.StudentDAO;
import com.courseregistration.dao.EnrollmentDAO;
import com.courseregistration.dao.CourseDAO;
import com.courseregistration.model.Students;
import com.courseregistration.model.Course;
import com.courseregistration.model.EnrollmentDetail;
import java.util.List;

public class RegistrationService {

    private final StudentDAO studentDAO = new StudentDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    public Students addNewStudent(String firstName, String lastName, String email) {
        Students newStudent = new Students(firstName, lastName, email);
        
        System.out.println("Registering: " + firstName + " " + lastName + "...");

        if (studentDAO.insertStudent(newStudent)) {
            System.out.println("Registration Successful! Student ID: " + newStudent.getStudentId());
            return newStudent;
        }

        return null;
    }

    public void displayStudentInfo(int studentId) {
        Students student = studentDAO.getStudentById(studentId);
        
        if (student != null) {
            System.out.printf("Student Found: ID %d, Name: %s %s, Email: %s\n", student.getStudentId(), 
            student.getFirstName(), student.getLastName(), student.getEmail());
        } else {
            System.out.printf("Error: Student with ID %d not found.\n", studentId);
        }
    }

    public Students getStudentDetails(int studentId) {
        return studentDAO.getStudentById(studentId);
    }

    public boolean updateStudent(Students student) {
        if (studentDAO.updateStudent(student)) {
            System.out.printf("Successfully updated details for Student ID %d.\n", student.getStudentId());
            return true;
        } else {
            System.out.printf("Failed to update details for Student ID %d.\n", student.getStudentId());
            return false;
        }
    }

    public boolean deleteStudentAccount(int studentId) {
        if (studentDAO.getStudentById(studentId) == null) {
            System.out.printf("Deletion failed.\n");
            return false;
        }

        if (enrollmentDAO.hasAnyEnrollments(studentId)) {
            System.out.printf("Deletion blocked: Student %d is still enrolled in active courses. Please unenroll first.\n", studentId);
            return false;
        }

        if (studentDAO.deleteStudent(studentId)) {
            System.out.printf("Student account ID %d successfully deleted.\n", studentId);
            return true;
        } else {
            System.out.printf("Deletion failed.\n");
            return false;
        }
    }

        public boolean addNewCourse(String courseId, String name, String partner, String rating, 
        String certificateType, String duration) {
        
        Course newCourse = new Course(courseId, name, partner, rating, certificateType, duration);
        
        System.out.println("Adding course: " + name + " (" + courseId + ")...");
        
        if (courseDAO.insertCourse(newCourse)) {
            System.out.printf("Course %s added successfully to the catalog.\n", courseId);
            return true;
        } else {
            System.out.println("Failed to add course.");
            return false;
        }
    }

    public boolean deleteCourseFromCatalog(String courseId) {
        
        if (enrollmentDAO.hasStudentsEnrolled(courseId)) {
            System.out.printf("Course %s has active enrollments. Removing student enrollment records...\n", courseId);
            int deletedEnrollments = enrollmentDAO.deleteEnrollmentsByCourseId(courseId);
            if (deletedEnrollments >= 0) {
                 System.out.printf("Successfully deleted %d enrollment records for %s.\n", deletedEnrollments, courseId);
            } else {
                System.out.printf("Failed to clear enrollments for %s. Aborting course deletion.\n", courseId);
                return false;
            }
        }
        if (courseDAO.deleteCourse(courseId)) {
            System.out.printf("Course %s successfully removed from the catalog.\n", courseId);
            return true;
        } else {
             System.out.printf("Failed to delete course %s.\n", courseId);
             return false;
        }
    }

    public boolean handleRegistration(int studentId, String courseId, String semester, int year) {
        
        if (enrollmentDAO.checkIfAlreadyEnrolled(studentId, courseId)) {
            System.out.printf("ERROR: Student %d is already enrolled in course %s.\n", studentId, courseId);
            return false;
        }
        
        if (enrollmentDAO.registerStudentForCourse(studentId, courseId, semester, year)) {
            System.out.printf("Student %d successfully registered for %s.\n", studentId, courseId);
            return true;
        } else {
            System.out.println("Registration failed.");
            return false;
        }
    }

    public boolean handleUnenrollment(int studentId, String courseId) {
        if (!enrollmentDAO.checkIfAlreadyEnrolled(studentId, courseId)) {
            System.out.printf("Error: Student %d is not currently enrolled in course %s.\n", studentId, courseId);
            return false;
        }

        if (enrollmentDAO.unenrollStudent(studentId, courseId)) {
            System.out.printf("Student %d successfully unenrolled from course %s.\n", studentId, courseId);
            return true;
        } else {
            System.out.println("Unenrollment failed.");
            return false;
        }
    }

    public void displayEnrolledCourses(int studentId) {
        
        List<EnrollmentDetail> courses = enrollmentDAO.getStudentEnrollments(studentId);
        
        System.out.printf("--- Courses Enrolled by Student %d ---\n", studentId);
        if (courses.isEmpty()) {
            System.out.println("No courses found for this student.");
            return;
        }
        
        for (EnrollmentDetail course : courses) {
            System.out.println(course.toString());
        }
        System.out.println("----------------------------------------");
    }


}