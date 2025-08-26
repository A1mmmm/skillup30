import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../features/auth/AuthProvider';
import './Header.css';

function ThemeToggle() {
  const [dark, setDark] = useState(false); // По умолчанию светлая тема
  
  useEffect(() => {
    if (dark) {
      document.body.classList.add('dark-theme');
    } else {
      document.body.classList.remove('dark-theme');
    }
  }, [dark]);
  return (
    <button className="theme-toggle" onClick={() => setDark(v => !v)} aria-label="Переключить тему">
      <span className="theme-toggle-inner">
        <span className="theme-toggle-icon" style={{ opacity: dark ? 0 : 1, transform: dark ? 'scale(0.7)' : 'scale(1)', transition: 'opacity 0.2s, transform 0.2s' }}>
          {/* Sun */}
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none"><circle cx="12" cy="12" r="5" stroke="currentColor" strokeWidth="2" /><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42" stroke="currentColor" strokeWidth="2" strokeLinecap="round" /></svg>
        </span>
        <span className="theme-toggle-icon" style={{ opacity: dark ? 1 : 0, transform: dark ? 'scale(1)' : 'scale(0.7)', transition: 'opacity 0.2s, transform 0.2s' }}>
          {/* Moon */}
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none"><path d="M21 12.79A9 9 0 1111.21 3a7 7 0 109.79 9.79z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" /></svg>
        </span>
      </span>
    </button>
  );
}

const Header = () => {
  const { user, logout } = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  // Динамически формируем навигационные ссылки в зависимости от авторизации
  const navLinks = [
    { to: '/', label: 'Главная' },
    ...(user ? [{ to: '/today', label: 'Сегодня' }] : []), // Только для авторизованных
    { to: '/achievements', label: 'Достижения' },
    { to: '/leaderboard', label: 'Лидерборд' },
  ];

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="main-header">
      <div className="header-inner container">
        <Link to="/" className="logo">Skill<span>Up</span>30</Link>
        {menuOpen && <div className="menu-overlay" onClick={() => setMenuOpen(false)} />}
        <nav className={`nav ${menuOpen ? 'open' : ''}`}>
          {navLinks.map(link => (
            <Link key={link.to} to={link.to} className={`nav-link${location.pathname === link.to ? ' active' : ''}`} onClick={() => setMenuOpen(false)}>{link.label}</Link>
          ))}
          {user ? (
            <div className="user-block">
              <div className="user-avatar" onClick={() => { setMenuOpen(false); navigate('/profile'); }}>
                {user.avatarUrl ? (
                  <img src={user.avatarUrl} alt="avatar" style={{ width: '100%', height: '100%', borderRadius: '50%', objectFit: 'cover' }} />
                ) : (
                  user.username[0].toUpperCase()
                )}
              </div>
              <span className="user-name">{user.username}</span>
              <span className="user-level">lvl {user.level}</span>
              <button className="logout-btn" onClick={handleLogout}>Выйти</button>
            </div>
          ) : (
            <div className="auth-block">
              <Link to="/login" className="nav-link">Вход</Link>
              <Link to="/register" className="nav-link nav-link-accent">Регистрация</Link>
            </div>
          )}
        </nav>
        <ThemeToggle />
        <button className="burger" onClick={() => setMenuOpen(m => !m)} aria-label="Меню">
          <span />
          <span />
          <span />
        </button>
      </div>
    </header>
  );
};

export default Header; 