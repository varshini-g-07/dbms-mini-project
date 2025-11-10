package com.courseregistration.dao;

import com.courseregistration.model.Student;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class StudentDAO {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper for converting a ResultSet row into a Student object
    private final RowMapper<Student> studentRowMapper = (rs, rowNum) -> new Student(
            rs.getInt("Student_ID"),
            rs.getString("First_Name"),
            rs.getString("Last_Name"),
            rs.getString("Email"),
            rs.getString("Password"));

    // Spring automatically injects the JdbcTemplate configured in
    // application.properties
    public StudentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a new student and retrieves the auto-generated ID.
     * 
     * @param student The Student object to insert (ID will be null/0).
     * @return true if the insert and ID retrieval were successful.
     */
    public boolean insertStudent(Student student) {
        String sql = "INSERT INTO Students (First_Name, Last_Name, Email, Password) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, student.getFirstName());
                ps.setString(2, student.getLastName());
                ps.setString(3, student.getEmail());
                ps.setString(4, student.getPassword());
                return ps;
            }, keyHolder);
            if (rowsAffected > 0 && keyHolder.getKey() != null) {
                // Set the newly generated ID back on the Student object
                student.setStudentId(keyHolder.getKey().intValue());
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean authenticateStudent(String email, String password) {
        String sql = "SELECT * FROM Students WHERE Email = ? AND Password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, studentRowMapper, email, password) != null;
        } catch (EmptyResultDataAccessException e) {
            // Admin not found
            return false;
        }
    }

    /**
     * Retrieves student by ID, handling cases where the student does not exist.
     */
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM Students WHERE Student_ID = ?";
        try {
            // Use queryForObject for a single result
            return jdbcTemplate.queryForObject(sql, studentRowMapper, studentId);
        } catch (EmptyResultDataAccessException e) {
            // Student not found
            return null;
        }
    }

    /**
     * Retrieves student by email, handling cases where the student does not exist.
     */
    public Student getStudentByEmail(String email) {
        String sql = "SELECT * FROM Students WHERE Email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, studentRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            // Student not found
            return null;
        }
    }

    /**
     * Updates the details of an existing student.
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE Students SET First_Name = ?, Last_Name = ?, Email = ? WHERE Student_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getStudentId());

        return rowsAffected > 0;
    }

    /**
     * Deletes a student record by ID.
     */
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM Students WHERE Student_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql, studentId);
        return rowsAffected > 0;
    }
}