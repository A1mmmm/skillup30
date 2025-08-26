import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './features/challenges/components/HomePage';
import ChallengePage from './features/challenges/components/ChallengePage';
import ProfilePage from './features/user/components/ProfilePage';
import TodayPage from './features/user/components/TodayPage';
import LeaderboardPage from './features/leaderboard/components/LeaderboardPage';
import LoginPage from './features/auth/components/LoginPage';
import RegisterPage from './features/auth/components/RegisterPage';
import AchievementsPage from './features/achievements/components/AchievementsPage';
import Header from './shared/components/Header';
import ProtectedRoute from './shared/components/ProtectedRoute';
import { AuthProvider } from './features/auth/AuthProvider';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/challenge/:id" element={<ChallengePage />} />
          <Route path="/profile" element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} />
          <Route path="/today" element={<ProtectedRoute><TodayPage /></ProtectedRoute>} />
          <Route path="/leaderboard" element={<LeaderboardPage />} />
          <Route path="/achievements" element={<AchievementsPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
