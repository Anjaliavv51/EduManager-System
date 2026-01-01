package com.edumanager.service;

import com.edumanager.dao.StudentDAO;
import com.edumanager.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Student service with multi-threading support for high-throughput operations
 */
@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    public Student createStudent(Student student) {
        return studentDAO.save(student);
    }

    public Student updateStudent(Student student) {
        return studentDAO.update(student);
    }

    public void deleteStudent(Long id) {
        studentDAO.delete(id);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentDAO.findById(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    /**
     * Optimized search with custom algorithm
     * Provides fast autocomplete for data entry
     */
    public List<Student> searchStudents(String searchTerm) {
        return studentDAO.searchByName(searchTerm);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentDAO.findByEmail(email);
    }

    public List<Student> getStudentsByStatus(String status) {
        return studentDAO.findByStatus(status);
    }

    /**
     * Async method for batch processing
     * Enables multi-threaded bulk operations
     */
    @Async
    public CompletableFuture<Integer> batchImportStudents(List<Student> students) {
        int[] results = studentDAO.batchInsert(students);
        return CompletableFuture.completedFuture(results.length);
    }

    public long getStudentCount() {
        return studentDAO.count();
    }
}
