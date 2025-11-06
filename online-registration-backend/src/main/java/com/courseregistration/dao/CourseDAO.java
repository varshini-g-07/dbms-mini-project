package com.courseregistration.dao;

import com.courseregistration.model.Course;
import com.courseregistration.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseDAO {
    public boolean insertCourse(Course course) {
        String sql = "INSERT INTO Courses (Course_ID, Partner, Course_Name, Rating, Certificate_Type, Duration) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getCourseId());
            stmt.setString(2, course.getPartner());
            stmt.setString(3, course.getCourseName());
            stmt.setString(4, course.getRating()); // New
            stmt.setString(5, course.getCertificateType()); // New
            stmt.setString(6, course.getDuration()); // New
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("SQL Error inserting new course: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(String courseId) {
        String sql = "DELETE FROM Courses WHERE Course_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courseId);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error deleting course " + courseId + ": " + e.getMessage());
            return false;
        }
    }
}
