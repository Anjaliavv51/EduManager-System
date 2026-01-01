package com.edumanager.dao;

import com.edumanager.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Course DAO with optimized search functionality
 */
@Repository
public class CourseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Course> courseRowMapper = (rs, rowNum) -> {
        Course course = new Course();
        course.setId(rs.getLong("id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setDescription(rs.getString("description"));
        course.setCredits(rs.getInt("credits"));
        course.setDepartment(rs.getString("department"));
        course.setInstructorName(rs.getString("instructor_name"));
        course.setCapacity(rs.getInt("capacity"));
        course.setEnrolled(rs.getInt("enrolled"));
        return course;
    };

    public Course save(Course course) {
        String sql = "INSERT INTO courses (course_code, course_name, description, credits, " +
                    "department, instructor_name, capacity, enrolled) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, course.getCourseCode());
            ps.setString(2, course.getCourseName());
            ps.setString(3, course.getDescription());
            ps.setInt(4, course.getCredits());
            ps.setString(5, course.getDepartment());
            ps.setString(6, course.getInstructorName());
            ps.setInt(7, course.getCapacity());
            ps.setInt(8, course.getEnrolled());
            return ps;
        }, keyHolder);
        
        course.setId(keyHolder.getKey().longValue());
        return course;
    }

    public Course update(Course course) {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, description = ?, " +
                    "credits = ?, department = ?, instructor_name = ?, capacity = ?, enrolled = ? WHERE id = ?";
        
        jdbcTemplate.update(sql, course.getCourseCode(), course.getCourseName(), 
                          course.getDescription(), course.getCredits(), course.getDepartment(), 
                          course.getInstructorName(), course.getCapacity(), course.getEnrolled(), 
                          course.getId());
        return course;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Course> findById(Long id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        List<Course> courses = jdbcTemplate.query(sql, courseRowMapper, id);
        return courses.isEmpty() ? Optional.empty() : Optional.of(courses.get(0));
    }

    public List<Course> findAll() {
        String sql = "SELECT * FROM courses ORDER BY course_code";
        return jdbcTemplate.query(sql, courseRowMapper);
    }

    /**
     * Optimized search by course code or name
     */
    public List<Course> searchCourses(String searchTerm) {
        String sql = "SELECT * FROM courses WHERE " +
                    "LOWER(course_code) LIKE LOWER(?) OR " +
                    "LOWER(course_name) LIKE LOWER(?) OR " +
                    "LOWER(department) LIKE LOWER(?) " +
                    "ORDER BY course_code LIMIT 20";
        String pattern = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, courseRowMapper, pattern, pattern, pattern);
    }

    public Optional<Course> findByCourseCode(String courseCode) {
        String sql = "SELECT * FROM courses WHERE course_code = ?";
        List<Course> courses = jdbcTemplate.query(sql, courseRowMapper, courseCode);
        return courses.isEmpty() ? Optional.empty() : Optional.of(courses.get(0));
    }

    public List<Course> findByDepartment(String department) {
        String sql = "SELECT * FROM courses WHERE department = ? ORDER BY course_code";
        return jdbcTemplate.query(sql, courseRowMapper, department);
    }

    public List<Course> findAvailableCourses() {
        String sql = "SELECT * FROM courses WHERE enrolled < capacity ORDER BY course_code";
        return jdbcTemplate.query(sql, courseRowMapper);
    }

    public void incrementEnrolled(Long courseId) {
        String sql = "UPDATE courses SET enrolled = enrolled + 1 WHERE id = ?";
        jdbcTemplate.update(sql, courseId);
    }

    public void decrementEnrolled(Long courseId) {
        String sql = "UPDATE courses SET enrolled = enrolled - 1 WHERE id = ? AND enrolled > 0";
        jdbcTemplate.update(sql, courseId);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM courses";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
