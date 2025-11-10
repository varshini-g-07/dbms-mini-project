package com.courseregistration.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.courseregistration.model.Admin;

@Repository
public class AdminDAO {
    private final JdbcTemplate jdbcTemplate;

    // RowMapper for converting a ResultSet row into a User object
    private final RowMapper<Admin> adminRowMapper = (rs, rowNum) -> new Admin(
            rs.getString("Email"),
            rs.getString("Password"));

    // Spring automatically injects the JdbcTemplate configured in
    // application.properties
    public AdminDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insertAdmin(Admin admin) {
        String sql = "INSERT INTO Admins (Email, Password) VALUES (?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, admin.getEmail());
                ps.setString(2, admin.getPassword());
                return ps;
            });
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean authenticateAdmin(String email, String password) {
        String sql = "SELECT * FROM Admins WHERE Email = ? AND Password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, adminRowMapper, email, password) != null; 
        } catch (EmptyResultDataAccessException e) {
            // Admin not found
            return false;
        }
    }
}
