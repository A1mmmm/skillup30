import axios from 'axios';

const api = axios.create({
    baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8081/api',
    withCredentials: true, // если используем HttpOnly cookie для refresh
});

api.interceptors.request.use((config) => {
    if (
        config.url.endsWith('/auth/login') ||
        config.url.endsWith('/auth/register')
    ) {
        delete config.headers.Authorization;
    } else {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
    }
    return config;
});

export default api; 