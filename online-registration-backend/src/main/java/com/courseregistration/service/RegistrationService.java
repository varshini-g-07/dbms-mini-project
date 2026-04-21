package com.courseregistration.service;

import com.courseregistration.dao.StudentDAO;
import com.courseregistration.dao.EnrollmentDAO;
import com.courseregistration.dao.AdminDAO;
import com.courseregistration.dao.CourseDAO;
import com.courseregistration.model.Student;
import com.courseregistration.model.EnrollmentDetail;
import com.courseregistration.model.Admin;
import com.courseregistration.model.Course;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service layer containing the core business logic for the course registration
 * system.
 * This class orchestrates data access via DAOs and enforces business rules.
 */
@Service // Spring Service component
public class RegistrationService {

    // Injectable DAO dependencies
    private final AdminDAO adminDAO;
    private final StudentDAO studentDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final CourseDAO courseDAO;

    /**
     * Constructor used by Spring to inject the required DAO dependencies.
     * Spring automatically finds the beans annotated with @Repository and passes
     * them into this constructor.
     */
    public RegistrationService(AdminDAO adminDAO, StudentDAO studentDAO, EnrollmentDAO enrollmentDAO,
            CourseDAO courseDAO) {
        this.adminDAO = adminDAO;
        this.studentDAO = studentDAO;
        this.enrollmentDAO = enrollmentDAO;
        this.courseDAO = courseDAO;
    }

    // --- Admin Management Methods ---

    /**
     * Attempts to register a new Admin.
     */
    public Admin addNewAdmin(String email, String password) {
        validateEmail(email);
        Admin newAdmin = new Admin(email, password);
        System.out.println("Registering new admin: " + email + " ...");
        return adminDAO.insertAdmin(newAdmin) ? newAdmin : null;
    }

    // --- Student Management Methods ---

    /**
     * Attempts to register a new student.
     */
    public Student addNewStudent(String firstName, String lastName, String email, String password) {
        validateEmail(email);
        Student newStudent = new Student(firstName, lastName, email, password);
        System.out.println("Registering: " + firstName + " " + lastName + "...");
        return studentDAO.insertStudent(newStudent) ? newStudent : null;
    }

    /**
     * Retrieves student data and displays a summary.
     */
    public void displayStudentInfo(int studentId) {
        Student student = studentDAO.getStudentById(studentId);

        if (student != null) {
            System.out.printf("Student Found: ID %d, Name: %s %s, Email: %s\n",
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail());
        } else {
            System.out.printf("Error: Student with ID %d not found.\n", studentId);
        }
    }

    /**
     * Helper method to retrieve a Student object for updating/viewing purposes.
     */
    public Student getStudentDetails(int studentId) {
        return studentDAO.getStudentById(studentId);
    }

    /**
     * Handles the business logic for updating a student's information.
     */
    public boolean updateStudent(Student student) {
        if (studentDAO.updateStudent(student)) {
            System.out.printf("Successfully updated details for Student ID %d.\n", student.getStudentId());
            return true;
        } else {
            System.out.printf("Failed to update details for Student ID %d. (ID might not exist or database error).\n",
                    student.getStudentId());
            return false;
        }
    }

    /**
     * Deletes a student account, but only if they are not enrolled in any courses.
     * This enforces the prerequisite business rule.
     */
    public boolean deleteStudentAccount(int studentId) {
        // 1. Check if student exists
        if (studentDAO.getStudentById(studentId) == null) {
            System.out.printf("Deletion failed: Student with ID %d does not exist.\n", studentId);
            return false;
        }

        // 2. BUSINESS RULE CHECK: Must be unenrolled from all courses
        if (enrollmentDAO.hasAnyEnrollments(studentId)) {
            System.out.printf(
                    "Deletion blocked: Student %d is still enrolled in active courses. Please unenroll first.\n",
                    studentId);
            return false;
        }

        // 3. Perform Deletion
        if (studentDAO.deleteStudent(studentId)) {
            System.out.printf("Student account ID %d successfully deleted.\n", studentId);
            return true;
        } else {
            System.out.printf("Deletion failed due to a database error for ID %d.\n", studentId);
            return false;
        }
    }

    // --- Course Management Methods ---

    /**
     * Attempts to add a new course to the catalog.
     */
    public Course addNewCourse(String courseId, String name, String partner,
            String rating, String certificateType, String duration) {
        Course newCourse = new Course(courseId, name, partner, rating, certificateType, duration);
        System.out.println("Adding course: " + name + " (" + courseId + ")...");
        if (courseDAO.insertCourse(newCourse)) {
            return newCourse;
        }
        throw new InternalError("Failed to add new course '" + name + "' to catalog.");
    }

    /**
     * Deletes a course from the catalog. Implements the business rule:
     * If students are enrolled, their enrollments must be deleted first.
     * 
     * @param courseId The ID of the course to delete.
     * @return true if the course was successfully deleted, false otherwise.
     */
    public boolean deleteCourseFromCatalog(String courseId) {

        // 1. Check for enrollments (Business Logic)
        if (enrollmentDAO.hasStudentsEnrolled(courseId)) {
            System.out.printf(
                    "Warning: Course %s has active enrollments. Removing student enrollment records first...\n",
                    courseId);

            // 2. Delete enrollments
            boolean success = enrollmentDAO.deleteEnrollmentsByCourseId(courseId);

            if (success) {
                System.out.printf("Successfully cleared enrollments for %s.\n", courseId);
            } else {
                System.out.printf(
                        "Critical Error: Failed to clear enrollments for %s due to a database issue. Aborting course deletion.\n",
                        courseId);
                return false;
            }
        } else {
            System.out.printf("ℹNo active enrollments found for course %s. Proceeding directly to course deletion.\n",
                    courseId);
        }

        // 3. Delete the course itself
        if (courseDAO.deleteCourse(courseId)) {
            System.out.printf("Course %s successfully removed from the catalog.\n", courseId);
            return true;
        } else {
            System.out.printf("Failed to delete course %s. It may not exist in the catalog.\n", courseId);
            return false;
        }
    }

    // --- Enrollment Management Methods ---

    /**
     * Handles enrollment of a student in a course.
     */
    public boolean enrollStudentInCourse(Integer studentId, String courseId, Integer year, String semester) {
        if (enrollmentDAO.checkIfAlreadyEnrolled(studentId, courseId)) {
            System.out.printf("Student %d is already enrolled in course %s.\n", studentId, courseId);
            return false;
        }
        // Enrollement date.
        LocalDate enrollment_date = LocalDate.now();
        ;

        if (enrollmentDAO.registerStudentForCourse(studentId, courseId, semester, year, enrollment_date)) {
            System.out.printf("Student %d successfully registered for %s.\n", studentId, courseId);
            return true;
        }
        return false;
    }

    /**
     * Handles the process of unenrolling a student from a course.
     */
    public boolean handleUnenrollment(int studentId, String courseId) {
        // Check if the enrollment exists before trying to delete
        if (!enrollmentDAO.checkIfAlreadyEnrolled(studentId, courseId)) {
            System.out.printf("Error: Student %d is not currently enrolled in course %s.\n", studentId, courseId);
            return false;
        }

        if (enrollmentDAO.unenrollStudent(studentId, courseId)) {
            System.out.printf("Student %d successfully unenrolled from course %s.\n", studentId, courseId);
            return true;
        } else {
            System.out.println("Unenrollment failed due to a database issue.");
            return false;
        }
    }

    /**
     * Retrieves and displays a list of courses a student is enrolled in (using the
     * SQL JOIN).
     */
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

    /**
     * Validates the given email ID.
     */
    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format provided.");
        }
        return;
    }
}
