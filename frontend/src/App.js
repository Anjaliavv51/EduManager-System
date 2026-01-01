import React, { useState } from 'react';
import StudentList from './components/StudentList';
import CourseList from './components/CourseList';
import EnrollmentList from './components/EnrollmentList';
import './App.css';

/**
 * Main App component
 * EduManager System - ES6+ React Application
 */
function App() {
  const [activeTab, setActiveTab] = useState('students');

  const renderContent = () => {
    switch(activeTab) {
      case 'students':
        return <StudentList />;
      case 'courses':
        return <CourseList />;
      case 'enrollments':
        return <EnrollmentList />;
      default:
        return <StudentList />;
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <div className="header-content">
          <div className="title-section">
            <h1>🎓 EduManager System</h1>
            <p>Student Information Management Platform</p>
          </div>
          <nav className="nav-tabs">
            <button 
              className={activeTab === 'students' ? 'tab active' : 'tab'}
              onClick={() => setActiveTab('students')}
            >
              Students
            </button>
            <button 
              className={activeTab === 'courses' ? 'tab active' : 'tab'}
              onClick={() => setActiveTab('courses')}
            >
              Courses
            </button>
            <button 
              className={activeTab === 'enrollments' ? 'tab active' : 'tab'}
              onClick={() => setActiveTab('enrollments')}
            >
              Enrollments
            </button>
          </nav>
        </div>
      </header>
      <main>
        {renderContent()}
      </main>
      <footer className="App-footer">
        <p>Built with React, Spring Boot, and MySQL | © 2024 EduManager System</p>
      </footer>
    </div>
  );
}

export default App;
