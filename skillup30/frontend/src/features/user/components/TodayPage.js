import React, { useEffect, useState } from 'react';
import api from '../../../shared/api/api';
import { useAuth } from '../../auth/AuthProvider';

const TodayPage = () => {
    const { user } = useAuth();
    const [todayChallenges, setTodayChallenges] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchTodayChallenges = async () => {
            try {
                const res = await api.get('/users/me/today-challenges');
                setTodayChallenges(res.data);
            } catch (error) {
                console.error('Error fetching today challenges:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchTodayChallenges();
    }, []);

    const checkIn = async (challengeId) => {
        try {
            await api.post(`/challenges/${challengeId}/checkin`);
            // Обновляем список после чек-ина
            const res = await api.get('/users/me/today-challenges');
            setTodayChallenges(res.data);
        } catch (error) {
            console.error('Error checking in:', error);
        }
    };

    if (!user) return <div>Загрузка...</div>;

    return (
        <div className="container">
            <div className="card">
                <h1>Сегодня</h1>
                <p>Ваши активные челленджи на сегодня</p>
            </div>

            {loading ? (
                <div className="card">
                    <div>Загрузка...</div>
                </div>
            ) : todayChallenges.length === 0 ? (
                <div className="card">
                    <h2>Нет активных челленджей</h2>
                    <p>Присоединитесь к челленджу, чтобы начать отслеживать свой прогресс</p>
                </div>
            ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 24 }}>
                    {todayChallenges.map(userChallenge => (
                        <div key={userChallenge.id} className="card">
                            <h3>{userChallenge.challenge.title}</h3>
                            <p style={{ color: 'var(--color-text)', marginBottom: 16 }}>
                                {userChallenge.challenge.dailyTask}
                            </p>
                            <div style={{ marginBottom: 16 }}>
                                <p>Прогресс: {userChallenge.completedDays}/30 дней</p>
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
                            {!userChallenge.isCompleted && (
                                <button
                                    onClick={() => checkIn(userChallenge.challenge.id)}
                                    className="btn"
                                    style={{ width: '100%' }}
                                >
                                    Отметить сегодня
                                </button>
                            )}
                            {userChallenge.isCompleted && (
                                <div style={{
                                    textAlign: 'center',
                                    padding: '1em',
                                    backgroundColor: '#10b981',
                                    color: 'white',
                                    borderRadius: '0.5em',
                                    fontWeight: 600
                                }}>
                                    🎉 Челлендж завершен!
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default TodayPage; 