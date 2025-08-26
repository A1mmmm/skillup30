import React, { useEffect, useState } from 'react';
import api from '../../../shared/api/api';

const LeaderboardPage = () => {
    const [leaderboard, setLeaderboard] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchLeaderboard = async () => {
            try {
                const res = await api.get('/leaderboard');
                setLeaderboard(res.data);
            } catch (error) {
                console.error('Error fetching leaderboard:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchLeaderboard();
    }, []);

    return (
        <div className="container">
            <div className="card">
                <h1>Лидерборд</h1>
                <p>Топ игроков по уровню и активности</p>
            </div>

            {loading ? (
                <div className="card">
                    <div>Загрузка...</div>
                </div>
            ) : (
                <div className="card">
                    <div style={{ display: 'grid', gap: 16 }}>
                        {leaderboard.map((user, index) => (
                            <div key={user.id} style={{ 
                                display: 'flex', 
                                alignItems: 'center', 
                                gap: 16, 
                                padding: '1em',
                                backgroundColor: index < 3 ? 'rgba(99, 102, 241, 0.1)' : 'transparent',
                                borderRadius: '0.5em',
                                border: index < 3 ? '1px solid var(--color-accent)' : 'none'
                            }}>
                                <div style={{ 
                                    width: 40, 
                                    height: 40, 
                                    borderRadius: '50%', 
                                    display: 'flex', 
                                    alignItems: 'center', 
                                    justifyContent: 'center',
                                    backgroundColor: index < 3 ? 'var(--color-accent)' : '#e5e7eb',
                                    color: index < 3 ? 'white' : 'var(--color-text)',
                                    fontWeight: 'bold'
                                }}>
                                    {index + 1}
                                </div>
                                <div className="user-avatar" style={{ width: 48, height: 48, fontSize: '1.2em' }}>
                                    {user.avatarUrl ? (
                                        <img src={user.avatarUrl} alt="avatar" style={{ width: '100%', height: '100%', borderRadius: '50%', objectFit: 'cover' }} />
                                    ) : (
                                        user.username[0].toUpperCase()
                                    )}
                                </div>
                                <div style={{ flex: 1 }}>
                                    <h3 style={{ margin: 0, marginBottom: 4 }}>{user.username}</h3>
                                    <p style={{ margin: 0, fontSize: '0.9em', color: 'var(--color-text)' }}>
                                        Уровень {user.level} • {user.xp} XP
                                    </p>
                                </div>
                                <div style={{ textAlign: 'right' }}>
                                    <div style={{ fontSize: '1.2em', fontWeight: 'bold', color: 'var(--color-accent)' }}>
                                        {user.totalPoints}
                                    </div>
                                    <div style={{ fontSize: '0.8em', color: 'var(--color-text)' }}>
                                        очков
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default LeaderboardPage; 