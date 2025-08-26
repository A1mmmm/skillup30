import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../../../shared/api/api';
import { useAuth } from '../../auth/AuthProvider';

const ChallengePage = () => {
    const { id } = useParams();
    const [challenge, setChallenge] = useState(null);
    const [userChallenge, setUserChallenge] = useState(null);
    const [challengeDays, setChallengeDays] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { user } = useAuth();

    useEffect(() => {
        const fetchChallenge = async () => {
            try {
                setError(null);

                // Загружаем основную информацию о челлендже и план
                const [challengeRes, challengeDaysRes] = await Promise.all([
                    api.get(`/challenges/${id}`),
                    api.get(`/challenges/${id}/plan`)
                ]);

                setChallenge(challengeRes.data);
                setChallengeDays(challengeDaysRes.data);

                // Если пользователь авторизован, пытаемся загрузить его прогресс
                if (user) {
                    try {
                        const userChallengeRes = await api.get(`/challenges/${id}/user-progress`);
                        setUserChallenge(userChallengeRes.data);
                        console.log('✅ User progress loaded successfully');
                    } catch (userProgressError) {
                        // Если пользователь не участвует в челлендже, это нормально
                        if (userProgressError.response && userProgressError.response.status === 400) {
                            console.log('ℹ️ User not participating in this challenge - this is normal');
                            setUserChallenge(null);
                        } else if (userProgressError.response && userProgressError.response.status === 401) {
                            console.log('ℹ️ User not authenticated for progress request');
                            setUserChallenge(null);
                        } else {
                            console.error('❌ Error fetching user progress:', userProgressError);
                            // Не показываем ошибку пользователю, просто не загружаем прогресс
                            setUserChallenge(null);
                        }
                    }
                } else {
                    setUserChallenge(null);
                }

            } catch (error) {
                console.error('Error fetching challenge:', error);
                let errorMessage = 'Ошибка загрузки челленджа. ';

                if (error.response) {
                    // Сервер вернул ошибку
                    if (error.response.status === 404) {
                        errorMessage += 'Челлендж не найден.';
                    } else if (error.response.status === 500) {
                        errorMessage += 'Ошибка сервера. Попробуйте позже.';
                    } else {
                        errorMessage += `Ошибка ${error.response.status}: ${error.response.data?.message || 'Неизвестная ошибка'}`;
                    }
                } else if (error.request) {
                    // Запрос был отправлен, но ответ не получен
                    errorMessage += 'Сервер не отвечает. Проверьте подключение.';
                } else {
                    // Ошибка при настройке запроса
                    errorMessage += 'Ошибка настройки запроса.';
                }

                setError(errorMessage);
            } finally {
                setLoading(false);
            }
        };
        fetchChallenge();
    }, [id, user]);

    const joinChallenge = async () => {
        try {
            setError(null);
            console.log('🔍 Присоединяюсь к челленджу ID:', id);
            await api.post(`/challenges/${id}/join`);
            console.log('✅ Успешно присоединился к челленджу');
            window.location.reload();
        } catch (error) {
            console.error('❌ Ошибка присоединения к челленджу:', error);
            let errorMessage = 'Ошибка присоединения к челленджу. ';

            if (error.response) {
                if (error.response.status === 409) {
                    errorMessage += 'Вы уже участвуете в этом челлендже.';
                } else if (error.response.status === 401) {
                    errorMessage += 'Необходимо войти в систему.';
                } else if (error.response.status === 404) {
                    errorMessage += 'Челлендж не найден.';
                } else {
                    errorMessage += `Ошибка ${error.response.status}: ${error.response.data?.message || 'Неизвестная ошибка'}`;
                }
            } else if (error.request) {
                errorMessage += 'Сервер не отвечает. Проверьте подключение.';
            } else {
                errorMessage += 'Ошибка настройки запроса.';
            }

            setError(errorMessage);
        }
    };

    const checkIn = async () => {
        try {
            setError(null);
            console.log('🔍 Отмечаю день для челленджа ID:', id);
            await api.post(`/challenges/${id}/checkin`);
            console.log('✅ День успешно отмечен');
            window.location.reload();
        } catch (error) {
            console.error('❌ Ошибка отметки дня:', error);
            let errorMessage = 'Ошибка отметки дня. ';

            if (error.response) {
                if (error.response.status === 409) {
                    errorMessage += 'Сегодня вы уже отметили день.';
                } else if (error.response.status === 401) {
                    errorMessage += 'Необходимо войти в систему.';
                } else if (error.response.status === 404) {
                    errorMessage += 'Челлендж не найден.';
                } else {
                    errorMessage += `Ошибка ${error.response.status}: ${error.response.data?.message || 'Неизвестная ошибка'}`;
                }
            } else if (error.request) {
                errorMessage += 'Сервер не отвечает. Проверьте подключение.';
            } else {
                errorMessage += 'Ошибка настройки запроса.';
            }

            setError(errorMessage);
        }
    };

    if (loading) return <div style={{ textAlign: 'center', padding: '2em' }}>Загрузка...</div>;
    if (error) return (
        <div className="container">
            <div className="error-message" style={{ margin: '2em auto', maxWidth: '600px' }}>
                <h3 style={{ margin: '0 0 1em 0', color: 'inherit' }}>⚠️ Ошибка</h3>
                {error}
                <button
                    onClick={() => window.location.reload()}
                    style={{
                        marginTop: '1em',
                        padding: '0.5em 1em',
                        backgroundColor: 'var(--color-accent)',
                        color: 'white',
                        border: 'none',
                        borderRadius: '0.5em',
                        cursor: 'pointer'
                    }}
                >
                    Попробовать снова
                </button>
            </div>
        </div>
    );
    if (!challenge) return <div style={{ textAlign: 'center', padding: '2em' }}>Челлендж не найден</div>;

    return (
        <div className="container">
            <div className="card">
                <h1 style={{
                    color: 'var(--color-accent)',
                    marginBottom: '0.5em',
                    fontSize: '2.2em'
                }}>{challenge.title}</h1>
                <p style={{
                    fontSize: '1.1em',
                    lineHeight: 1.6,
                    marginBottom: '1.5em'
                }}>{challenge.description}</p>
                <div style={{
                    display: 'flex',
                    gap: 16,
                    marginBottom: '2em',
                    flexWrap: 'wrap'
                }}>
                    <span style={{
                        padding: '0.5em 1em',
                        backgroundColor: 'var(--color-accent)',
                        color: 'white',
                        borderRadius: '0.5em',
                        fontSize: '0.9em'
                    }}>
                        🎯 Сложность: {challenge.difficultyLevel}
                    </span>
                    <span style={{
                        padding: '0.5em 1em',
                        backgroundColor: '#10b981',
                        color: 'white',
                        borderRadius: '0.5em',
                        fontSize: '0.9em'
                    }}>
                        🏆 Награда: {challenge.rewardPoints} XP
                    </span>
                </div>

                {/* Детальный план челленджа */}
                <div style={{ marginTop: 32 }}>
                    <h3 style={{
                        color: 'var(--color-accent)',
                        marginBottom: '1em',
                        fontSize: '1.5em',
                        textAlign: 'center'
                    }}>📅 30-дневный план</h3>
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 16, marginTop: 16 }}>
                        {challengeDays.map((day) => (
                            <div key={day.dayNumber} style={{
                                border: '1px solid var(--color-border)',
                                borderRadius: 8,
                                padding: 16,
                                backgroundColor: 'var(--color-card-bg)',
                                backdropFilter: 'var(--color-card-blur)',
                                transition: 'transform 0.2s, box-shadow 0.2s'
                            }}>
                                <h4 style={{ margin: '0 0 8px 0', color: 'var(--color-accent)' }}>День {day.dayNumber}</h4>
                                <p style={{ margin: 0, fontSize: '14px', color: 'var(--color-text)' }}>{day.task}</p>
                            </div>
                        ))}
                    </div>
                </div>

                {userChallenge ? (
                    <div style={{ marginTop: 32, textAlign: 'center' }}>
                        <h3 style={{
                            color: 'var(--color-accent)',
                            marginBottom: '1em',
                            fontSize: '1.5em'
                        }}>📊 Ваш прогресс</h3>
                        <div style={{
                            display: 'flex',
                            justifyContent: 'center',
                            gap: '2em',
                            marginBottom: '1.5em',
                            flexWrap: 'wrap'
                        }}>
                            <div style={{
                                padding: '1em',
                                backgroundColor: 'var(--color-accent)',
                                color: 'white',
                                borderRadius: '0.5em',
                                minWidth: '120px'
                            }}>
                                <div style={{ fontSize: '1.5em', fontWeight: 'bold' }}>{userChallenge.completedDays}/30</div>
                                <div style={{ fontSize: '0.9em' }}>дней</div>
                            </div>
                            <div style={{
                                padding: '1em',
                                backgroundColor: userChallenge.isCompleted ? '#10b981' : '#f59e0b',
                                color: 'white',
                                borderRadius: '0.5em',
                                minWidth: '120px'
                            }}>
                                <div style={{ fontSize: '1.5em', fontWeight: 'bold' }}>
                                    {userChallenge.isCompleted ? '🏆' : '🚀'}
                                </div>
                                <div style={{ fontSize: '0.9em' }}>
                                    {userChallenge.isCompleted ? 'Завершен' : 'В процессе'}
                                </div>
                            </div>
                        </div>
                        {!userChallenge.isCompleted && (
                            <button
                                onClick={checkIn}
                                className="btn"
                                style={{
                                    fontSize: '1.1em',
                                    padding: '0.8em 2em',
                                    backgroundColor: '#10b981',
                                    color: 'white',
                                    border: 'none',
                                    borderRadius: '0.5em',
                                    cursor: 'pointer'
                                }}
                            >
                                ✅ Отметить сегодня
                            </button>
                        )}
                    </div>
                ) : user ? (
                    <div style={{ marginTop: 24, textAlign: 'center' }}>
                        <button
                            onClick={joinChallenge}
                            className="btn"
                            style={{
                                fontSize: '1.1em',
                                padding: '0.8em 2em',
                                backgroundColor: 'var(--color-accent)',
                                color: 'white',
                                border: 'none',
                                borderRadius: '0.5em',
                                cursor: 'pointer',
                                transition: 'background-color 0.2s'
                            }}
                        >
                            🚀 Присоединиться к челленджу
                        </button>
                        <p style={{ marginTop: '0.5em', fontSize: '0.9em', color: 'var(--color-text)' }}>
                            Начните свой 30-дневный путь к успеху!
                        </p>
                    </div>
                ) : (
                    <div style={{ marginTop: 24, textAlign: 'center' }}>
                        <p style={{ marginBottom: '1em', fontSize: '1.1em' }}>Войдите, чтобы присоединиться к челленджу</p>
                        <Link to="/login" className="btn" style={{
                            textDecoration: 'none',
                            display: 'inline-block',
                            padding: '0.8em 2em',
                            backgroundColor: 'var(--color-accent)',
                            color: 'white',
                            borderRadius: '0.5em',
                            transition: 'background-color 0.2s'
                        }}>
                            Войти в систему
                        </Link>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ChallengePage; 