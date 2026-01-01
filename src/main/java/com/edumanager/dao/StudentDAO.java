package com.edumanager.dao;

import com.edumanager.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Student DAO with optimized search algorithms
 * Uses prepared statements for high-throughput operations
 */
@Repository
public class StudentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Student> studentRowMapper = (rs, rowNum) -> {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setEmail(rs.getString("email"));
        student.setPhoneNumber(rs.getString("phone_number"));
        student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        student.setAddress(rs.getString("address"));
        student.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
        student.setStatus(rs.getString("status"));
        return student;
    };

    public Student save(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, email, phone_number, " +
                    "date_of_birth, address, enrollment_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhoneNumber());
            ps.setDate(5, Date.valueOf(student.getDateOfBirth()));
            ps.setString(6, student.getAddress());
            ps.setDate(7, Date.valueOf(student.getEnrollmentDate()));
            ps.setString(8, student.getStatus());
            return ps;
        }, keyHolder);
        
        student.setId(keyHolder.getKey().longValue());
        return student;
    }

    public Student update(Student student) {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, " +
                    "phone_number = ?, date_of_birth = ?, address = ?, enrollment_date = ?, " +
                    "status = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(), 
                          student.getEmail(), student.getPhoneNumber(), 
                          Date.valueOf(student.getDateOfBirth()), student.getAddress(), 
                          Date.valueOf(student.getEnrollmentDate()), student.getStatus(), 
                          student.getId());
        return student;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM students WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Student> findById(Long id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        List<Student> students = jdbcTemplate.query(sql, studentRowMapper, id);
        return students.isEmpty() ? Optional.empty() : Optional.of(students.get(0));
    }

    public List<Student> findAll() {
        String sql = "SELECT * FROM students ORDER BY id";
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    /**
     * Optimized search using indexed columns
     * Reduces manual data entry by providing fast autocomplete results
     */
    public List<Student> searchByName(String searchTerm) {
        String sql = "SELECT * FROM students WHERE " +
                    "LOWER(first_name) LIKE LOWER(?) OR " +
                    "LOWER(last_name) LIKE LOWER(?) OR " +
                    "LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER(?) " +
                    "ORDER BY first_name, last_name LIMIT 20";
        String pattern = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, studentRowMapper, pattern, pattern, pattern);
    }

    /**
     * Optimized search by email with index
     */
    public Optional<Student> findByEmail(String email) {
        String sql = "SELECT * FROM students WHERE email = ?";
        List<Student> students = jdbcTemplate.query(sql, studentRowMapper, email);
        return students.isEmpty() ? Optional.empty() : Optional.of(students.get(0));
    }

    /**
     * Batch insert for high-throughput operations
     */
    public int[] batchInsert(List<Student> students) {
        String sql = "INSERT INTO students (first_name, last_name, email, phone_number, " +
                    "date_of_birth, address, enrollment_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        return jdbcTemplate.batchUpdate(sql, students, students.size(), 
            (PreparedStatement ps, Student student) -> {
                ps.setString(1, student.getFirstName());
                ps.setString(2, student.getLastName());
                ps.setString(3, student.getEmail());
                ps.setString(4, student.getPhoneNumber());
                ps.setDate(5, Date.valueOf(student.getDateOfBirth()));
                ps.setString(6, student.getAddress());
                ps.setDate(7, Date.valueOf(student.getEnrollmentDate()));
                ps.setString(8, student.getStatus());
            });
    }

    public List<Student> findByStatus(String status) {
        String sql = "SELECT * FROM students WHERE status = ? ORDER BY last_name, first_name";
        return jdbcTemplate.query(sql, studentRowMapper, status);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM students";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
