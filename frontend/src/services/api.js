import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Student API
export const studentAPI = {
  getAll: () => api.get('/students'),
  getById: (id) => api.get(`/students/${id}`),
  create: (student) => api.post('/students', student),
  update: (id, student) => api.put(`/students/${id}`, student),
  delete: (id) => api.delete(`/students/${id}`),
  search: (query) => api.get(`/students/search?q=${query}`),
  getByEmail: (email) => api.get(`/students/email/${email}`),
  getByStatus: (status) => api.get(`/students/status/${status}`),
  getCount: () => api.get('/students/count'),
};

// Course API
export const courseAPI = {
  getAll: () => api.get('/courses'),
  getById: (id) => api.get(`/courses/${id}`),
  create: (course) => api.post('/courses', course),
  update: (id, course) => api.put(`/courses/${id}`, course),
  delete: (id) => api.delete(`/courses/${id}`),
  search: (query) => api.get(`/courses/search?q=${query}`),
  getByCourseCode: (code) => api.get(`/courses/code/${code}`),
  getByDepartment: (dept) => api.get(`/courses/department/${dept}`),
  getAvailable: () => api.get('/courses/available'),
  getCount: () => api.get('/courses/count'),
};

// Enrollment API
export const enrollmentAPI = {
  getAll: () => api.get('/enrollments'),
  getById: (id) => api.get(`/enrollments/${id}`),
  enroll: (studentId, courseId) => api.post(`/enrollments?studentId=${studentId}&courseId=${courseId}`),
  update: (id, enrollment) => api.put(`/enrollments/${id}`, enrollment),
  drop: (id) => api.delete(`/enrollments/${id}`),
  getByStudent: (studentId) => api.get(`/enrollments/student/${studentId}`),
  getByCourse: (courseId) => api.get(`/enrollments/course/${courseId}`),
  getCount: () => api.get('/enrollments/count'),
};

export default api;
