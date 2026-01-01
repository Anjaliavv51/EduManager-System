package com.edumanager.dao;

import com.edumanager.model.Enrollment;
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
 * Enrollment DAO for managing student-course relationships
 */
@Repository
public class EnrollmentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Enrollment> enrollmentRowMapper = (rs, rowNum) -> {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(rs.getLong("id"));
        enrollment.setStudentId(rs.getLong("student_id"));
        enrollment.setCourseId(rs.getLong("course_id"));
        enrollment.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
        enrollment.setGrade(rs.getString("grade"));
        enrollment.setStatus(rs.getString("status"));
        return enrollment;
    };

    public Enrollment save(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, enrollment_date, grade, status) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, enrollment.getStudentId());
            ps.setLong(2, enrollment.getCourseId());
            ps.setDate(3, Date.valueOf(enrollment.getEnrollmentDate()));
            ps.setString(4, enrollment.getGrade());
            ps.setString(5, enrollment.getStatus());
            return ps;
        }, keyHolder);
        
        enrollment.setId(keyHolder.getKey().longValue());
        return enrollment;
    }

    public Enrollment update(Enrollment enrollment) {
        String sql = "UPDATE enrollments SET student_id = ?, course_id = ?, enrollment_date = ?, " +
                    "grade = ?, status = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, enrollment.getStudentId(), enrollment.getCourseId(), 
                          Date.valueOf(enrollment.getEnrollmentDate()), enrollment.getGrade(), 
                          enrollment.getStatus(), enrollment.getId());
        return enrollment;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM enrollments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Enrollment> findById(Long id) {
        String sql = "SELECT * FROM enrollments WHERE id = ?";
        List<Enrollment> enrollments = jdbcTemplate.query(sql, enrollmentRowMapper, id);
        return enrollments.isEmpty() ? Optional.empty() : Optional.of(enrollments.get(0));
    }

    public List<Enrollment> findByStudentId(Long studentId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? ORDER BY enrollment_date DESC";
        return jdbcTemplate.query(sql, enrollmentRowMapper, studentId);
    }

    public List<Enrollment> findByCourseId(Long courseId) {
        String sql = "SELECT * FROM enrollments WHERE course_id = ? ORDER BY enrollment_date DESC";
        return jdbcTemplate.query(sql, enrollmentRowMapper, courseId);
    }

    public Optional<Enrollment> findByStudentAndCourse(Long studentId, Long courseId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?";
        List<Enrollment> enrollments = jdbcTemplate.query(sql, enrollmentRowMapper, studentId, courseId);
        return enrollments.isEmpty() ? Optional.empty() : Optional.of(enrollments.get(0));
    }

    public List<Enrollment> findAll() {
        String sql = "SELECT * FROM enrollments ORDER BY enrollment_date DESC";
        return jdbcTemplate.query(sql, enrollmentRowMapper);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM enrollments";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
