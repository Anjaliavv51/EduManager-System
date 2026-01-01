import React, { useState, useEffect } from 'react';
import { courseAPI } from '../services/api';
import './CourseList.css';

/**
 * CourseList component for managing courses
 * Displays courses with search and CRUD operations
 */
const CourseList = () => {
  const [courses, setCourses] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showAddForm, setShowAddForm] = useState(false);
  const [newCourse, setNewCourse] = useState({
    courseCode: '',
    courseName: '',
    description: '',
    credits: '',
    department: '',
    instructorName: '',
    capacity: '',
    enrolled: 0
  });

  useEffect(() => {
    loadCourses();
  }, []);

  const loadCourses = async () => {
    try {
      setLoading(true);
      const response = await courseAPI.getAll();
      setCourses(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to load courses');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (term) => {
    setSearchTerm(term);
    if (term.trim() === '') {
      loadCourses();
      return;
    }

    try {
      setLoading(true);
      const response = await courseAPI.search(term);
      setCourses(response.data);
      setError(null);
    } catch (err) {
      setError('Search failed');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this course?')) {
      try {
        await courseAPI.delete(id);
        loadCourses();
      } catch (err) {
        setError('Failed to delete course');
        console.error(err);
      }
    }
  };

  const handleAddCourse = async (e) => {
    e.preventDefault();
    try {
      await courseAPI.create({
        ...newCourse,
        credits: parseInt(newCourse.credits),
        capacity: parseInt(newCourse.capacity),
        enrolled: 0
      });
      setShowAddForm(false);
      setNewCourse({
        courseCode: '',
        courseName: '',
        description: '',
        credits: '',
        department: '',
        instructorName: '',
        capacity: '',
        enrolled: 0
      });
      loadCourses();
    } catch (err) {
      setError('Failed to add course');
      console.error(err);
    }
  };

  const handleInputChange = (e) => {
    setNewCourse({
      ...newCourse,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div className="course-list">
      <div className="header-section">
        <h2>Courses</h2>
        <button onClick={() => setShowAddForm(!showAddForm)} className="btn-add">
          {showAddForm ? 'Cancel' : '+ Add Course'}
        </button>
      </div>

      {showAddForm && (
        <div className="add-form">
          <h3>Add New Course</h3>
          <form onSubmit={handleAddCourse}>
            <div className="form-row">
              <input
                type="text"
                name="courseCode"
                placeholder="Course Code (e.g., CS101)"
                value={newCourse.courseCode}
                onChange={handleInputChange}
                required
              />
              <input
                type="text"
                name="courseName"
                placeholder="Course Name"
                value={newCourse.courseName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="form-row">
              <input
                type="text"
                name="department"
                placeholder="Department"
                value={newCourse.department}
                onChange={handleInputChange}
                required
              />
              <input
                type="text"
                name="instructorName"
                placeholder="Instructor Name"
                value={newCourse.instructorName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="form-row">
              <input
                type="number"
                name="credits"
                placeholder="Credits"
                value={newCourse.credits}
                onChange={handleInputChange}
                required
              />
              <input
                type="number"
                name="capacity"
                placeholder="Capacity"
                value={newCourse.capacity}
                onChange={handleInputChange}
                required
              />
            </div>
            <textarea
              name="description"
              placeholder="Course Description"
              value={newCourse.description}
              onChange={handleInputChange}
              rows="3"
            />
            <button type="submit" className="btn-submit">Add Course</button>
          </form>
        </div>
      )}

      <div className="search-bar">
        <input
          type="text"
          placeholder="Search courses by code, name, or department..."
          value={searchTerm}
          onChange={(e) => handleSearch(e.target.value)}
          className="search-input"
        />
      </div>

      {loading && <div className="loading">Loading...</div>}
      {error && <div className="error">{error}</div>}

      <table className="course-table">
        <thead>
          <tr>
            <th>Code</th>
            <th>Course Name</th>
            <th>Department</th>
            <th>Instructor</th>
            <th>Credits</th>
            <th>Enrollment</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {courses.map((course) => (
            <tr key={course.id}>
              <td><strong>{course.courseCode}</strong></td>
              <td>{course.courseName}</td>
              <td>{course.department}</td>
              <td>{course.instructorName}</td>
              <td>{course.credits}</td>
              <td>
                <span className={course.enrolled >= course.capacity ? 'full' : 'available'}>
                  {course.enrolled}/{course.capacity}
                </span>
              </td>
              <td>
                <button onClick={() => handleDelete(course.id)} className="btn-delete">
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {courses.length === 0 && !loading && (
        <div className="no-data">No courses found</div>
      )}
    </div>
  );
};

export default CourseList;
