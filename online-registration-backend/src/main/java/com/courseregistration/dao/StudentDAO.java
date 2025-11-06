package com.courseregistration.dao;

import com.courseregistration.model.Students;
import com.courseregistration.util.DBConnection;
import java.sql.*;

public class StudentDAO {
    public Students getStudentById(int studentId) {
        String sql = "SELECT Student_ID, First_Name, Last_Name, Email FROM Students WHERE Student_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    Integer id = rs.getInt("Student_ID"); 
                    
                    String first = rs.getString("First_Name");
                    String last = rs.getString("Last_Name");
                    String email = rs.getString("Email");

                    return new Students(id, first, last, email);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving student: " + e.getMessage());
        }
        return null;
    }
    
    public boolean insertStudent(Students student) {
        String sql = "INSERT INTO Students (First_Name, Last_Name, Email) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1); 
                        student.setStudentId(generatedId); 
                        return true;
                    }
                }
            }
            
            return false;

        } catch (SQLException e) {
            System.err.println("SQL Error inserting student: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStudent(Students student) {
        String sql = "UPDATE Students SET First_Name = ?, Last_Name = ?, Email = ? WHERE Student_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setInt(4, student.getStudentId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM Students WHERE Student_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error deleting student: " + e.getMessage());
            return false;
        }
    }
}
