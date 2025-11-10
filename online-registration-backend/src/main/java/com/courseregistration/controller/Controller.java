package com.courseregistration.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.courseregistration.service.AuthService;
import com.courseregistration.service.RegistrationService;
import com.courseregistration.service.RetrievalService;
import com.courseregistration.model.Student;
import com.courseregistration.model.StudentRegistrationRequest;
import com.courseregistration.model.UnenrollmentRequest;
import com.courseregistration.model.Admin;
import com.courseregistration.model.AdminRegistrationRequest;
import com.courseregistration.model.Course;
import com.courseregistration.model.CourseRegistrationRequest;
import com.courseregistration.model.EnrollmentDetail;
import com.courseregistration.model.EnrollmentRequest;
import com.courseregistration.model.GetStudentResponse;
import com.courseregistration.model.LoginRequest;

@RestController
@RequestMapping("/api/v1")
// --- CORS is CRUCIAL for HTML to talk to this API ---
@CrossOrigin(origins = "http://localhost:8080")
public class Controller {

    private final AuthService authService;
    private final RegistrationService registrationService;
    private final RetrievalService retrievalService;

    public Controller(RegistrationService registrationService, RetrievalService retrievalService,
            AuthService authService) {
        this.registrationService = registrationService;
        this.retrievalService = retrievalService;
        this.authService = authService;
    }

    /**
     * Endpoint for student & admin login.
     * Request: POST /api/v1/login.
     * Payload: JSON body mapped to LoginRequest
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();

            boolean success = request.getAdmin() ? authService.loginAdmin(email, password)
                    : authService.loginStudent(email, password);
            return new ResponseEntity<>(success ? (request.getAdmin() ? "admin" : "student") : "fail", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error during login.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for Admin registration.
     * Request: POST /api/v1/registration/admin
     * Payload: JSON body mapped to AdminRegistrationRequest
     */
    @PostMapping("/registration/admin")
    public ResponseEntity<?> registerNewAdmin(@RequestBody AdminRegistrationRequest request) {
        try {
            Admin admin = registrationService.addNewAdmin(request.getEmail(), request.getPassword());
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for Student Registration.
     * Request: POST /api/v1/registration/student
     * Payload: JSON body mapped to StudentRegistrationRequest
     */
    @PostMapping("/registration/student")
    public ResponseEntity<?> registerNewStudent(@RequestBody StudentRegistrationRequest request) {
        try {
            Student newStudent = registrationService.addNewStudent(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword());
            return new ResponseEntity<>(newStudent, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for Course Registration.
     * Request: POST /api/v1/registration/course
     * Payload: JSON body mapped to CourseRegistrationRequest
     */
    @PostMapping("/registration/course")
    public ResponseEntity<?> addNewCourse(@RequestBody CourseRegistrationRequest request) {
        try {
            Course newCourse = registrationService.addNewCourse(
                    request.getCourseId(),
                    request.getPartner(),
                    request.getCourseName(),
                    request.getRating(),
                    request.getCertificateType(),
                    request.getDuration());
            return new ResponseEntity<>(newCourse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for Course Enrollment.
     * Request: POST /api/v1/registration/enroll
     * Payload: JSON body mapped to EnrollmentRequest
     */
    @PostMapping("/registration/enroll")
    public ResponseEntity<?> enrollStudentInCourse(@RequestBody EnrollmentRequest request) {
        try {
            boolean status = registrationService.enrollStudentInCourse(request.getStudentId(), request.getCourseId(),
                    request.getYear(), request.getSemester());
            return new ResponseEntity<>(status, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error during enrollment.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/registration/unenroll")
    public ResponseEntity<?> unenrollStudentFromCourse(@RequestBody UnenrollmentRequest request) {
        try {
            boolean status = registrationService.handleUnenrollment(request.getStudentId(), request.getCourseId());
            return new ResponseEntity<>(status, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error during unenrollment.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student")
    public ResponseEntity<?> getStudentByEmail(String email) {
        System.out.println("Getting student with ID: " + email);
        try {
            Student student = retrievalService.getStudentByEmail(email);
            List<EnrollmentDetail> enrollmentDetails = null;
            if (student != null) {
                enrollmentDetails = retrievalService.getStudentEnrollmentDetails(student.getStudentId());
            }
            return student != null
                    ? new ResponseEntity<>(new GetStudentResponse(student, enrollmentDetails), HttpStatus.OK)
                    : new ResponseEntity<>("Requested student not found.", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
