package com.courseregistration.dao;

import com.courseregistration.model.EnrollmentDetail;
import com.courseregistration.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    public boolean registerStudentForCourse(int studentId, String courseId, String semester, int year) {
        LocalDate enrollmentDate = LocalDate.now();
        
        String sql = "INSERT INTO Enrollments (Student_ID, Course_ID, Semester, Year, Grade, Enrollment_Date) " + "VALUES (?, ?, ?, ?, ?, ?)";;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setString(2, courseId);
            stmt.setString(3, semester);
            stmt.setInt(4, year);
            stmt.setString(5, "IP");
            stmt.setDate(6, Date.valueOf(enrollmentDate));
            
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error during course registration: " + e.getMessage());
            return false;
        }
    }

    public boolean checkIfAlreadyEnrolled(int studentId, String courseId) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE Student_ID = ? AND Course_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setString(2, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during enrollment check: " + e.getMessage());
            return false; 
        }
        return false;
    }

    public List<EnrollmentDetail> getStudentEnrollments(int studentId) {
        List<EnrollmentDetail> enrollments = new ArrayList<>();
        
        String sql = "SELECT E.Course_ID, E.Semester, E.Year, E.Grade, C.Course_Name, C.Partner " + 
        "FROM Enrollments E " + "JOIN Courses C ON E.Course_ID = C.Course_ID " + "WHERE E.Student_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EnrollmentDetail detail = new EnrollmentDetail(
                        rs.getString("Course_ID"),
                        rs.getString("Course_Name"),
                        rs.getString("Partner"),
                        rs.getString("Semester"),
                        rs.getInt("Year"),
                        rs.getString("Grade")
                    );
                    enrollments.add(detail);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving enrollments: " + e.getMessage());
        }
        return enrollments;
    }

    public boolean unenrollStudent(int studentId, String courseId) {
        String sql = "DELETE FROM Enrollments WHERE Student_ID = ? AND Course_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setString(2, courseId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error during unenrollment: " + e.getMessage());
            return false;
        }
    }
    
    public boolean hasAnyEnrollments(int studentId) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE Student_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during enrollment check: " + e.getMessage());
        }
        return false;
    }

    public boolean hasStudentsEnrolled(String courseId) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE Course_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during course enrollment check: " + e.getMessage());
        }
        return false;
    }

    public int deleteEnrollmentsByCourseId(String courseId) {
        String sql = "DELETE FROM Enrollments WHERE Course_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courseId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Database error deleting enrollments for course " + courseId + ": " + e.getMessage());
            return -1;
        }
    }
}
