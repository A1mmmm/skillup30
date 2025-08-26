import React, { useEffect, useState } from 'react';
import api from '../../../shared/api/api';
import { useAuth } from '../../auth/AuthProvider';

const ProfilePage = () => {
    const { user } = useAuth();
    const [userChallenges, setUserChallenges] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUserChallenges = async () => {
            try {
                const res = await api.get('/users/me/challenges');
                setUserChallenges(res.data);
            } catch (error) {
                console.error('Error fetching user challenges:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchUserChallenges();
    }, []);

    if (!user) return <div>Загрузка...</div>;

    return (
        <div className="container">
            <div className="card">
                <h1>Профиль</h1>
                <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 24 }}>
                    <div className="user-avatar" style={{ width: 80, height: 80, fontSize: '2em' }}>
                        {user.avatarUrl ? (
                            <img src={user.avatarUrl} alt="avatar" style={{ width: '100%', height: '100%', borderRadius: '50%', objectFit: 'cover' }} />
                        ) : (
                            user.username[0].toUpperCase()
                        )}
                    </div>
                    <div>
                        <h2>{user.username}</h2>
                        <p>Уровень: {user.level}</p>
                        <p>XP: {user.xp}</p>
                        <p>Всего очков: {user.totalPoints}</p>
                    </div>
                </div>
            </div>

            <div className="card">
                <h2>Мои челленджи</h2>
                {loading ? (
                    <div>Загрузка...</div>
                ) : userChallenges.length === 0 ? (
                    <p>Вы пока не участвуете ни в одном челлендже</p>
                ) : (
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 16 }}>
                        {userChallenges.map(userChallenge => (
                            <div key={userChallenge.id} className="card" style={{ padding: '1em' }}>
                                <h3>{userChallenge.challenge.title}</h3>
                                <p>Прогресс: {userChallenge.completedDays}/30 дней</p>
                                <p>Статус: {userChallenge.isCompleted ? 'Завершен' : 'В процессе'}</p>
                                <div style={{ 
                                    width: '100%', 
                                    height: 8, 
                                    backgroundColor: '#e5e7eb', 
                                    borderRadius: 4, 
                                    overflow: 'hidden',
                                    marginTop: 8
                                }}>
                                    <div style={{ 
                                        width: `${(userChallenge.completedDays / 30) * 100}%`, 
                                        height: '100%', 
                                        backgroundColor: 'var(--color-accent)',
                                        transition: 'width 0.3s'
                                    }} />
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProfilePage; 