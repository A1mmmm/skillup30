import React, { useEffect, useState } from 'react';
import api from '../../../shared/api/api';
import { useAuth } from '../../auth/AuthProvider';
import './AchievementsPage.css';

const AchievementsPage = () => {
    const { user } = useAuth();
    const [userAchievements, setUserAchievements] = useState([]);
    const [allAchievements, setAllAchievements] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState('my'); // 'my' или 'all'

    useEffect(() => {
        fetchAchievements();
    }, [user]);

    const fetchAchievements = async () => {
        try {
            setError(null);
            setLoading(true);

            // Загружаем все достижения (доступно без аутентификации)
            const allRes = await api.get('/achievements/all');
            setAllAchievements(allRes.data);

            // Если пользователь авторизован, загружаем его достижения
            if (user) {
                try {
                    const userRes = await api.get('/achievements');
                    setUserAchievements(userRes.data);
                } catch (error) {
                    console.error('Error fetching user achievements:', error);
                    setUserAchievements([]);
                }
            }
        } catch (error) {
            console.error('Error fetching achievements:', error);
            let errorMessage = 'Ошибка загрузки достижений. ';

            if (error.response) {
                if (error.response.status === 500) {
                    errorMessage += 'Ошибка сервера. Попробуйте позже.';
                } else {
                    errorMessage += `Ошибка ${error.response.status}: ${error.response.data || 'Неизвестная ошибка'}`;
                }
            } else if (error.request) {
                errorMessage += 'Сервер не отвечает. Проверьте подключение.';
            } else {
                errorMessage += 'Ошибка настройки запроса.';
            }

            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    // Обработка ошибок
    if (error) {
        return (
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
    }

    const renderAchievementCard = (achievement, isUserAchievement = false) => (
        <div key={achievement.id} className={`card achievement-card ${isUserAchievement ? 'earned' : 'not-earned'}`}>
            {/* Индикатор полученного достижения */}
            {isUserAchievement && (
                <div className="badge-indicator">
                    ✓
                </div>
            )}

            <div className="icon">
                {achievement.badgeImage ? (
                    <img src={achievement.badgeImage} alt={achievement.name} style={{ width: 64, height: 64 }} />
                ) : (
                    isUserAchievement ? '🏆' : '🔒'
                )}
            </div>

            <h3 className="title">
                {achievement.name}
            </h3>

            <p className="description">
                {achievement.description}
            </p>

            {isUserAchievement && achievement.awardedAt && (
                <div className="status earned">
                    Получено {new Date(achievement.awardedAt).toLocaleDateString()}
                </div>
            )}

            {!isUserAchievement && (
                <div className="status not-earned">
                    Не получено
                </div>
            )}
        </div>
    );

    const getCurrentAchievements = () => {
        if (activeTab === 'my') {
            return userAchievements;
        } else {
            return allAchievements;
        }
    };

    const getCurrentLoading = () => {
        if (activeTab === 'my' && !user) {
            return false; // Не загружаем для неавторизованных пользователей
        }
        return loading;
    };

    return (
        <div className="container achievements-container">
            {/* Заголовок */}
            <div className="card achievements-header">
                <h1 style={{
                    color: 'var(--color-accent)',
                    marginBottom: '0.5em',
                    fontSize: '2.5em'
                }}>
                    🏆 Достижения
                </h1>
                <p style={{ fontSize: '1.1em', color: 'var(--color-text)' }}>
                    Отслеживайте свой прогресс и получайте награды за активность
                </p>
            </div>

            {/* Переключатель вкладок */}
            <div className="card" style={{ marginBottom: 24 }}>
                <div className="achievements-tabs">
                    <button
                        onClick={() => setActiveTab('my')}
                        className={`achievements-tab ${activeTab === 'my' ? 'active' : ''}`}
                    >
                        🎯 Мои достижения
                        {userAchievements.length > 0 && (
                            <span className="badge">
                                {userAchievements.length}
                            </span>
                        )}
                    </button>

                    <button
                        onClick={() => setActiveTab('all')}
                        className={`achievements-tab ${activeTab === 'all' ? 'active' : ''}`}
                    >
                        🌟 Все достижения
                        <span className="badge">
                            {allAchievements.length}
                        </span>
                    </button>
                </div>
            </div>

            {/* Информация о прогрессе */}
            {user && (
                <div className="card achievements-progress">
                    <h3 style={{ marginBottom: '0.5em' }}>Ваш прогресс</h3>
                    <div className="achievements-progress-stats">
                        <div className="achievements-progress-stat">
                            <div className="number">{userAchievements.length}</div>
                            <div className="label">получено</div>
                        </div>
                        <div className="achievements-progress-stat">
                            <div className="number">{allAchievements.length}</div>
                            <div className="label">всего</div>
                        </div>
                        <div className="achievements-progress-stat">
                            <div className="number">
                                {allAchievements.length > 0 ? Math.round((userAchievements.length / allAchievements.length) * 100) : 0}%
                            </div>
                            <div className="label">завершено</div>
                        </div>
                    </div>
                </div>
            )}

            {/* Список достижений */}
            <div style={{
                opacity: loading ? 0.5 : 1,
                transition: 'opacity 0.3s ease',
                minHeight: '400px'
            }}>
                {getCurrentLoading() ? (
                    <div className="card achievements-loading">
                        <div className="icon">⏳</div>
                        <div>Загрузка достижений...</div>
                    </div>
                ) : activeTab === 'my' && !user ? (
                    <div className="card achievements-auth-required">
                        <div className="icon">🔐</div>
                        <h3 className="title">Войдите в систему</h3>
                        <p className="description">
                            Чтобы увидеть свои достижения, необходимо авторизоваться
                        </p>
                        <button
                            onClick={() => window.location.href = '/login'}
                            className="login-button"
                        >
                            Войти в систему
                        </button>
                    </div>
                ) : (
                    <div className="achievements-grid">
                        {getCurrentAchievements().map(achievement =>
                            renderAchievementCard(
                                achievement,
                                activeTab === 'my' || userAchievements.some(ua => ua.id === achievement.id)
                            )
                        )}
                    </div>
                )}

                {/* Пустое состояние */}
                {!getCurrentLoading() && getCurrentAchievements().length === 0 && (
                    <div className="card achievements-empty">
                        <div className="icon">
                            {activeTab === 'my' ? '😔' : '📝'}
                        </div>
                        <h3 className="title">
                            {activeTab === 'my' ? 'Пока нет достижений' : 'Достижения не найдены'}
                        </h3>
                        <p className="description">
                            {activeTab === 'my'
                                ? 'Начните участвовать в челленджах, чтобы получить свои первые достижения!'
                                : 'Достижения будут добавлены позже.'
                            }
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AchievementsPage; 