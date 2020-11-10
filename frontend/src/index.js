import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import axios from 'axios';


axios.defaults.baseURL = 'http://localhost:8080'

const getAccessToken = () => {
    let auth = localStorage.getItem('ym-auth')
    auth = JSON.parse(auth)
    if (auth != null && auth.access !== undefined) {
        return auth.access
    } else {
        localStorage.removeItem('ym-auth')
        return null
    }
}

// Request interceptor for API calls
axios.interceptors.request.use(
    async request => {
        console.log(request)
        const accessToken = getAccessToken();
        if (accessToken !== null) {
            request.headers = {
                'Authorization': `Bearer ${accessToken}`,
                'Accept': 'application/json'
            }
        }
        return request;
    },
    error => {
        Promise.reject(error)
    });

const refreshAccessToken = async () => {
    let auth = localStorage.getItem("ym-auth")
    auth = JSON.parse(auth)
    if (auth === null || auth.refresh === undefined) {
        return false
    }
    auth = await axios.post("/not-secured/refresh", {refresh: auth.refresh})
        .then(response => response.data)
        .catch(error => {
            console.log("Refresh failed", error)
            return false
        })

    if (auth == null) {
        return false
    }
    localStorage.setItem('ym-auth', JSON.stringify(auth))
    return true
}

// Response interceptor for API calls
axios.interceptors.response.use((response) => {
    return response
}, async function (error) {
    const originalRequest = error.config;
    if (error.response === undefined) {
        return Promise.reject(error)
    }
    if (error.response.status === 403 && !originalRequest._retry) {
        originalRequest._retry = await refreshAccessToken();
        return axios(originalRequest);
    }
    return Promise.reject(error);
});

ReactDOM.render(
    <React.StrictMode>
        <App/>
    </React.StrictMode>,
    document.getElementById('root')
);
