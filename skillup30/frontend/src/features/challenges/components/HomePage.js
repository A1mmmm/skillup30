import React, { useEffect, useState } from 'react';
import api from '../../../shared/api/api';
import { Link } from 'react-router-dom';
import { useAuth } from '../../auth/AuthProvider';

const features = [
    {
        title: 'Геймификация',
        desc: 'Зарабатывай XP, повышай уровень и открывай новые возможности.'
    },
    {
        title: 'Достижения',
        desc: 'Получай награды за streak, прогресс и активность.'
    },
    {
        title: 'Лидерборд',
        desc: 'Соревнуйся с другими участниками и попади в топ-10.'
    },
    {
        title: 'Сообщество',
        desc: 'Вдохновляйся успехами других и делись своими результатами.'
    }
];

const tips = [
    'Начни с малого — главное регулярность!',
    'Не бойся ошибаться, бойся сдаваться.',
    '30 дней — это всего 4 недели перемен.',
    'Делай чек-ин каждый день и увидишь прогресс!',
    'Вдохновляйся успехами других, но сравнивай себя только с собой.'
];
const randomTip = tips[Math.floor(Math.random() * tips.length)];

const HomePage = () => {
    const [challenges, setChallenges] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { user } = useAuth();

    useEffect(() => {
        const fetchChallenges = async () => {
            try {
                console.log('🔍 Загружаю список челленджей...');
                const res = await api.get('/challenges');
                console.log('✅ Получены челленджи:', res.data);
                setChallenges(res.data);
            } catch (error) {
                console.error('❌ Ошибка загрузки челленджей:', error);
                setError('Ошибка загрузки челленджей. Попробуйте обновить страницу.');
                setChallenges([]);
            } finally {
                setLoading(false);
            }
        };
        fetchChallenges();
    }, []);

    return (
        <div className="container">
            {/* Совет дня и статистика */}
            <section style={{ display: 'flex', gap: 32, flexWrap: 'wrap', justifyContent: 'center', marginBottom: 32 }}>
                <div className="card" style={{ minWidth: 220, flex: '1 1 220px', textAlign: 'center', padding: '1.5em 1em', animation: 'fadeInUp 0.7s' }}>
                    <div style={{ fontSize: 32, marginBottom: 8 }}>💡</div>
                    <div style={{ fontWeight: 600, marginBottom: 6 }}>Совет дня</div>
                    <div style={{ color: 'var(--color-text)', fontSize: '1.05em' }}>{randomTip}</div>
                </div>
                <div className="card" style={{ minWidth: 220, flex: '1 1 220px', textAlign: 'center', padding: '1.5em 1em', animation: 'fadeInUp 0.7s 0.1s' }}>
                    <div style={{ fontSize: 32, marginBottom: 8 }}>📊</div>
                    <div style={{ fontWeight: 600, marginBottom: 6 }}>Статистика</div>
                    <div style={{ color: 'var(--color-text)', fontSize: '1.05em' }}>
                        <b>1 250</b> пользователей<br />
                        <b>87</b> челленджей<br />
                        <b>14 000</b> чек-инов<br />
                        <b>320</b> завершённых челленджей
                    </div>
                </div>
            </section>
            {/* Приветственный блок */}
            <section className="card" style={{ textAlign: 'center', marginBottom: 40, padding: '3em 2em' }}>
                <h2 style={{ fontSize: '2.2em', marginBottom: 8 }}>Добро пожаловать в SkillUp30!</h2>
                <div style={{ fontSize: '1.2em', color: 'var(--color-accent)', marginBottom: 18 }}>
                    Присоединяйся к челленджу и начни свой путь к новым привычкам и развитию.
                </div>
                {!user && (
                    <Link to="/register" className="hero-btn" style={{ textDecoration: 'none' }}>Зарегистрироваться</Link>
                )}
            </section>

            {/* Секция преимуществ */}
            <section style={{ display: 'flex', flexWrap: 'wrap', gap: 24, justifyContent: 'center', marginBottom: 48 }}>
                {features.map((f, idx) => (
                    <div key={f.title} className="card" style={{ minWidth: 220, flex: '1 1 220px', textAlign: 'center', padding: '2em 1em', animation: `fadeInUp 0.7s ${0.1 + idx * 0.07}s both` }}>
                        <h3 style={{ color: 'var(--color-accent)', marginBottom: 8 }}>{f.title}</h3>
                        <div style={{ color: 'var(--color-text)', fontSize: '1.05em' }}>{f.desc}</div>
                    </div>
                ))}
            </section>

            {/* Список челленджей */}
            <h2 style={{ textAlign: 'center', marginBottom: 24 }}>Список челленджей</h2>
            {loading ? (
                <div style={{ textAlign: 'center' }}>Загрузка...</div>
            ) : challenges.length === 0 ? (
                <div style={{ textAlign: 'center' }}>Нет доступных челленджей</div>
            ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 24 }}>
                    {challenges.map(challenge => (
                        <Link key={challenge.id} to={`/challenge/${challenge.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                            <div className="card" style={{ cursor: 'pointer', transition: 'transform 0.2s', animation: 'fadeInUp 0.7s' }}>
                                <h3 style={{ color: 'var(--color-accent)', marginBottom: 8 }}>{challenge.title}</h3>
                                <p style={{ color: 'var(--color-text)', marginBottom: 16, lineHeight: 1.5 }}>{challenge.description}</p>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                    <span style={{ fontSize: '0.9em', color: 'var(--color-text)' }}>Сложность: {challenge.difficultyLevel}</span>
                                    <span style={{ fontSize: '0.9em', color: 'var(--color-accent)', fontWeight: 600 }}>{challenge.rewardPoints} XP</span>
                                </div>
                            </div>
                        </Link>
                    ))}
                </div>
            )}
        </div>
    );
};

export default HomePage; 