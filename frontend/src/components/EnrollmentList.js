import React, { useState, useEffect } from 'react';
import { enrollmentAPI, studentAPI, courseAPI } from '../services/api';
import './EnrollmentList.css';

/**
 * EnrollmentList component for managing student-course enrollments
 */
const EnrollmentList = () => {
  const [enrollments, setEnrollments] = useState([]);
  const [students, setStudents] = useState([]);
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showEnrollForm, setShowEnrollForm] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState('');
  const [selectedCourse, setSelectedCourse] = useState('');

  useEffect(() => {
    loadEnrollments();
    loadStudents();
    loadCourses();
  }, []);

  const loadEnrollments = async () => {
    try {
      setLoading(true);
      const response = await enrollmentAPI.getAll();
      setEnrollments(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to load enrollments');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadStudents = async () => {
    try {
      const response = await studentAPI.getAll();
      setStudents(response.data);
    } catch (err) {
      console.error('Failed to load students', err);
    }
  };

  const loadCourses = async () => {
    try {
      const response = await courseAPI.getAvailable();
      setCourses(response.data);
    } catch (err) {
      console.error('Failed to load courses', err);
    }
  };

  const handleEnroll = async (e) => {
    e.preventDefault();
    try {
      await enrollmentAPI.enroll(selectedStudent, selectedCourse);
      setShowEnrollForm(false);
      setSelectedStudent('');
      setSelectedCourse('');
      loadEnrollments();
      loadCourses(); // Refresh to update available seats
      setError(null);
    } catch (err) {
      setError(err.response?.data || 'Failed to enroll student');
      console.error(err);
    }
  };

  const handleDrop = async (id) => {
    if (window.confirm('Are you sure you want to drop this enrollment?')) {
      try {
        await enrollmentAPI.drop(id);
        loadEnrollments();
        loadCourses(); // Refresh to update available seats
      } catch (err) {
        setError('Failed to drop enrollment');
        console.error(err);
      }
    }
  };

  const getStudentName = (studentId) => {
    const student = students.find(s => s.id === studentId);
    return student ? `${student.firstName} ${student.lastName}` : 'Unknown';
  };

  const getCourseName = (courseId) => {
    const course = courses.find(c => c.id === courseId);
    return course ? `${course.courseCode} - ${course.courseName}` : 'Unknown';
  };

  return (
    <div className="enrollment-list">
      <div className="header-section">
        <h2>Enrollments</h2>
        <button onClick={() => setShowEnrollForm(!showEnrollForm)} className="btn-enroll">
          {showEnrollForm ? 'Cancel' : '+ Enroll Student'}
        </button>
      </div>

      {showEnrollForm && (
        <div className="enroll-form">
          <h3>Enroll Student in Course</h3>
          <form onSubmit={handleEnroll}>
            <div className="form-group">
              <label>Select Student:</label>
              <select
                value={selectedStudent}
                onChange={(e) => setSelectedStudent(e.target.value)}
                required
              >
                <option value="">-- Choose Student --</option>
                {students.map(student => (
                  <option key={student.id} value={student.id}>
                    {student.firstName} {student.lastName} ({student.email})
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Select Course:</label>
              <select
                value={selectedCourse}
                onChange={(e) => setSelectedCourse(e.target.value)}
                required
              >
                <option value="">-- Choose Course --</option>
                {courses.map(course => (
                  <option key={course.id} value={course.id}>
                    {course.courseCode} - {course.courseName} ({course.capacity - course.enrolled} seats available)
                  </option>
                ))}
              </select>
            </div>
            <button type="submit" className="btn-submit">Enroll</button>
          </form>
        </div>
      )}

      {loading && <div className="loading">Loading...</div>}
      {error && <div className="error">{error}</div>}

      <table className="enrollment-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Student</th>
            <th>Course</th>
            <th>Enrollment Date</th>
            <th>Status</th>
            <th>Grade</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {enrollments.map((enrollment) => (
            <tr key={enrollment.id}>
              <td>{enrollment.id}</td>
              <td>{getStudentName(enrollment.studentId)}</td>
              <td>{getCourseName(enrollment.courseId)}</td>
              <td>{enrollment.enrollmentDate}</td>
              <td>
                <span className={`status ${enrollment.status.toLowerCase()}`}>
                  {enrollment.status}
                </span>
              </td>
              <td>{enrollment.grade || '-'}</td>
              <td>
                <button onClick={() => handleDrop(enrollment.id)} className="btn-drop">
                  Drop
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {enrollments.length === 0 && !loading && (
        <div className="no-data">No enrollments found</div>
      )}
    </div>
  );
};

export default EnrollmentList;
