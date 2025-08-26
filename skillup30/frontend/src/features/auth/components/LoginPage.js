import React, { useState } from 'react';
import { useAuth } from '../AuthProvider';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const { login, loading, error } = useAuth();
    const [usernameOrEmail, setUsernameOrEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const success = await login(usernameOrEmail, password);
        if (success) navigate('/profile');
    };

    return (
        <div style={{ maxWidth: 400, margin: '40px auto' }}>
            <h2>Вход</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Имя пользователя или Email"
                    value={usernameOrEmail}
                    onChange={e => setUsernameOrEmail(e.target.value)}
                    required
                    style={{ width: '100%', marginBottom: 8 }}
                />
                <input
                    type="password"
                    placeholder="Пароль"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                    style={{ width: '100%', marginBottom: 8 }}
                />
                <button type="submit" disabled={loading} style={{ width: '100%' }}>
                    {loading ? 'Вход...' : 'Войти'}
                </button>
                {error && <div style={{ color: 'red', marginTop: 8 }}>{error}</div>}
            </form>
        </div>
    );
};

export default LoginPage; 