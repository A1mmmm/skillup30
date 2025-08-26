import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../../shared/api/api';

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [accessToken, setAccessToken] = useState(null);
    const [loading, setLoading] = useState(true); // Начинаем с true для проверки токена
    const [error, setError] = useState(null);

    // Функция для проверки и восстановления аутентификации
    const checkAuth = async () => {
        try {
            const token = localStorage.getItem('accessToken');
            if (token) {
                setAccessToken(token);
                api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                await fetchProfile(token);
            }
        } catch (error) {
            console.error('Auth check failed:', error);
            // Если токен недействителен, удаляем его
            localStorage.removeItem('accessToken');
            setAccessToken(null);
            setUser(null);
        } finally {
            setLoading(false);
        }
    };

    // При первом запуске проверяем аутентификацию
    useEffect(() => {
        checkAuth();
    }, []);

    const login = async (usernameOrEmail, password) => {
        setLoading(true); 
        setError(null);
        try {
            const res = await api.post('/auth/login', { usernameOrEmail, password });
            const token = res.data.accessToken;
            setAccessToken(token);
            localStorage.setItem('accessToken', token);
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            await fetchProfile(token);
            setLoading(false);
            return true;
        } catch (e) {
            setError(e.response?.data?.message || 'Ошибка входа');
            setLoading(false);
            return false;
        }
    };

    const register = async (username, email, password) => {
        setLoading(true); 
        setError(null);
        try {
            await api.post('/auth/register', { username, email, password });
            return await login(username, password);
        } catch (e) {
            setError(e.response?.data?.message || 'Ошибка регистрации');
            setLoading(false);
            return false;
        }
    };

    const logout = () => {
        setUser(null);
        setAccessToken(null);
        localStorage.removeItem('accessToken');
        delete api.defaults.headers.common['Authorization'];
    };

    const fetchProfile = async (token) => {
        try {
            const res = await api.get('/users/me', {
                headers: { Authorization: `Bearer ${token}` }
            });
            setUser(res.data);
        } catch (error) {
            console.error('Failed to fetch profile:', error);
            setUser(null);
            // Если профиль не загружается, возможно токен истек
            if (error.response?.status === 401) {
                localStorage.removeItem('accessToken');
                setAccessToken(null);
                delete api.defaults.headers.common['Authorization'];
            }
        }
    };

    return (
        <AuthContext.Provider value={{ user, accessToken, login, register, logout, loading, error }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
} 