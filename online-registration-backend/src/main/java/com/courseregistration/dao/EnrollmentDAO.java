package com.courseregistration.dao;

import com.courseregistration.model.EnrollmentDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class EnrollmentDAO {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper for combining Course and Enrollment data into EnrollmentDetail
    private final RowMapper<EnrollmentDetail> enrollmentDetailRowMapper = (rs, rowNum) -> new EnrollmentDetail(
            rs.getString("Course_ID"),
            rs.getString("Course_Name"),
            rs.getString("Partner"),
            rs.getString("Semester"),
            rs.getInt("Year"),
            rs.getString("Grade"));

    public EnrollmentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a new enrollment record.
     */
    public boolean registerStudentForCourse(int studentId, String courseId, String semester, int year,
            LocalDate enrollment_date) {
        String sql = "INSERT INTO Enrollments (Student_ID, Course_ID, Semester, Year, Grade, Enrollment_Date) VALUES (?, ?, ?, ?, ?, ?)";
        // Grade is initialized to 'IP' (In Progress)
        int rowsAffected = jdbcTemplate.update(sql, studentId, courseId, semester, year, "IP", enrollment_date);
        return rowsAffected > 0;
    }

    /**
     * Deletes a specific enrollment record.
     */
    public boolean unenrollStudent(int studentId, String courseId) {
        String sql = "DELETE FROM Enrollments WHERE Student_ID = ? AND Course_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql, studentId, courseId);
        return rowsAffected > 0;
    }

    /**
     * Checks if a student is already registered for a specific course.
     */
    public boolean checkIfAlreadyEnrolled(int studentId, String courseId) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE Student_ID = ? AND Course_ID = ?";
        // Use queryForObject(String, Class<T>, Object...) to get a single value
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, studentId, courseId);
        return count != null && count > 0;
    }

    /**
     * Checks if a student has ANY active enrollments (used before deleting
     * student).
     */
    public boolean hasAnyEnrollments(int studentId) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE Student_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, studentId);
        return count != null && count > 0;
    }

    /**
     * Retrieves all enrollment details and course info for a given student ID using
     * a JOIN.
     */
    public List<EnrollmentDetail> getStudentEnrollments(int studentId) {
        String sql = "SELECT E.Course_ID, E.Semester, E.Year, E.Grade, C.Course_Name, C.Partner " +
                "FROM Enrollments E " +
                "JOIN Courses C ON E.Course_ID = C.Course_ID " +
                "WHERE E.Student_ID = ?";

        return jdbcTemplate.query(sql, enrollmentDetailRowMapper, studentId);
    }

    /**
     * Checks if a course has any active student enrollments (used before deleting
     * course).
     */
    public boolean hasStudentsEnrolled(String courseId) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE Course_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, courseId);
        return count != null && count > 0;
    }

    /**
     * Deletes ALL enrollment records associated with a specific course.
     */
    public boolean deleteEnrollmentsByCourseId(String courseId) {
        String sql = "DELETE FROM Enrollments WHERE Course_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql, courseId);
        return rowsAffected >= 0; // returns true even if 0 rows are affected (meaning no enrollments existed)
    }
}
