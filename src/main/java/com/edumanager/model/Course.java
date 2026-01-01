package com.edumanager.model;

import java.util.Objects;

/**
 * Course domain model following OOP principles
 */
public class Course {
    private Long id;
    private String courseCode;
    private String courseName;
    private String description;
    private Integer credits;
    private String department;
    private String instructorName;
    private Integer capacity;
    private Integer enrolled;

    // Constructors
    public Course() {
    }

    public Course(Long id, String courseCode, String courseName, String description,
                  Integer credits, String department, String instructorName,
                  Integer capacity, Integer enrolled) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.department = department;
        this.instructorName = instructorName;
        this.capacity = capacity;
        this.enrolled = enrolled;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    public boolean hasAvailableSeats() {
        return enrolled < capacity;
    }

    public Integer getAvailableSeats() {
        return capacity - enrolled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", department='" + department + '\'' +
                ", enrolled=" + enrolled + "/" + capacity +
                '}';
    }
}
