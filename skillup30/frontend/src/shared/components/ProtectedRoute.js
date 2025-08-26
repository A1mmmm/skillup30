import React from 'react';
import { useAuth } from '../../features/auth/AuthProvider';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
    const { user, loading } = useAuth();
    
    // Показываем загрузку, пока проверяем аутентификацию
    if (loading) {
        return (
            <div style={{ 
                display: 'flex', 
                justifyContent: 'center', 
                alignItems: 'center', 
                height: '50vh',
                fontSize: '1.2em'
            }}>
                Проверка аутентификации...
            </div>
        );
    }
    
    // Если пользователь не авторизован, перенаправляем на страницу входа
    if (!user) {
        return <Navigate to="/login" />;
    }
    
    // Если пользователь авторизован, показываем защищенный контент
    return children;
};

export default ProtectedRoute; 