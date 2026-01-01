package com.edumanager.service;

import com.edumanager.dao.CourseDAO;
import com.edumanager.dao.EnrollmentDAO;
import com.edumanager.model.Course;
import com.edumanager.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Enrollment service managing student-course enrollments
 */
@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentDAO enrollmentDAO;

    @Autowired
    private CourseDAO courseDAO;

    /**
     * Enroll a student in a course with validation
     */
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        Optional<Course> courseOpt = courseDAO.findById(courseId);
        
        if (!courseOpt.isPresent()) {
            throw new RuntimeException("Course not found");
        }

        Course course = courseOpt.get();
        if (!course.hasAvailableSeats()) {
            throw new RuntimeException("Course is full");
        }

        Optional<Enrollment> existingEnrollment = enrollmentDAO.findByStudentAndCourse(studentId, courseId);
        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ENROLLED");
        
        Enrollment saved = enrollmentDAO.save(enrollment);
        courseDAO.incrementEnrolled(courseId);
        
        return saved;
    }

    public Enrollment updateEnrollment(Enrollment enrollment) {
        return enrollmentDAO.update(enrollment);
    }

    public void dropEnrollment(Long enrollmentId) {
        Optional<Enrollment> enrollmentOpt = enrollmentDAO.findById(enrollmentId);
        if (enrollmentOpt.isPresent()) {
            Enrollment enrollment = enrollmentOpt.get();
            courseDAO.decrementEnrolled(enrollment.getCourseId());
            enrollmentDAO.delete(enrollmentId);
        }
    }

    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentDAO.findById(id);
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentDAO.findByCourseId(courseId);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.findAll();
    }

    public long getEnrollmentCount() {
        return enrollmentDAO.count();
    }
}
