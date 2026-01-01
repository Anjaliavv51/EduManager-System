import React from 'react';
import StudentList from './components/StudentList';
import './App.css';

/**
 * Main App component
 * EduManager System - ES6+ React Application
 */
function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>EduManager System</h1>
        <p>Student Information Management Platform</p>
      </header>
      <main>
        <StudentList />
      </main>
    </div>
  );
}

export default App;
