package com.courseregistration.service;

import org.springframework.stereotype.Service;

import com.courseregistration.dao.AdminDAO;
import com.courseregistration.dao.StudentDAO;

@Service // Spring Service component
public class AuthService {
    // Injectable DAO dependencies
    private final AdminDAO adminDAO;
    private final StudentDAO studentDAO;

    /**
     * Constructor used by Spring to inject the required DAO dependencies.
     */
    public AuthService(AdminDAO adminDAO, StudentDAO studentDAO) {
        this.adminDAO = adminDAO;
        this.studentDAO = studentDAO;
    }

    /**
     * Attempts to login an existing Admin.
     */
    public boolean loginAdmin(String email, String password) {
        validateEmail(email);
        return adminDAO.authenticateAdmin(email, password);
    }

    /**
     * Attempts to login an existing Student.
     */
    public boolean loginStudent(String email, String password) {
        validateEmail(email);
        return studentDAO.authenticateStudent(email, password);
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
