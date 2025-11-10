package com.courseregistration.dao;

import com.courseregistration.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CourseDAO {

    private final JdbcTemplate jdbcTemplate;

    public CourseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a new course record into the Courses table.
     */
    public boolean insertCourse(Course course) {
        String sql = "INSERT INTO Courses (Course_ID, Partner, Course_Name, Rating, Certificate_Type, Duration) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        int rowsAffected = jdbcTemplate.update(sql, 
            course.getCourseId(),
            course.getPartner(),
            course.getCourseName(),
            course.getRating(),
            course.getCertificateType(),
            course.getDuration()
        );
        
        return rowsAffected > 0;
    }

    /**
     * Deletes a course record by Course ID.
     */
    public boolean deleteCourse(String courseId) {
        String sql = "DELETE FROM Courses WHERE Course_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql, courseId);
        return rowsAffected > 0;
    }
}
