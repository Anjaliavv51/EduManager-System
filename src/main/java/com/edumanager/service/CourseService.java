package com.edumanager.service;

import com.edumanager.dao.CourseDAO;
import com.edumanager.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Course service for managing course operations
 */
@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseDAO courseDAO;

    public Course createCourse(Course course) {
        return courseDAO.save(course);
    }

    public Course updateCourse(Course course) {
        return courseDAO.update(course);
    }

    public void deleteCourse(Long id) {
        courseDAO.delete(id);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseDAO.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    /**
     * Optimized course search
     */
    public List<Course> searchCourses(String searchTerm) {
        return courseDAO.searchCourses(searchTerm);
    }

    public Optional<Course> getCourseByCourseCode(String courseCode) {
        return courseDAO.findByCourseCode(courseCode);
    }

    public List<Course> getCoursesByDepartment(String department) {
        return courseDAO.findByDepartment(department);
    }

    public List<Course> getAvailableCourses() {
        return courseDAO.findAvailableCourses();
    }

    public long getCourseCount() {
        return courseDAO.count();
    }
}
