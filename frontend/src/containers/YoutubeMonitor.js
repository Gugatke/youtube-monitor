import React, {Component} from 'react'
import Layout from "../components/layout/Layout";
import Main from "../components/main/Main";
import Login from "../components/login/Login";

import axios from 'axios'
import {Route} from "react-router";
import Register from "../components/register/Register";


class YoutubeMonitor extends Component {

    state = {
        auth: null,
        userData: null,
        socket: null
    }

    componentDidMount() {
        let auth = localStorage.getItem('ym-auth')
        auth = JSON.parse(auth)
        if (auth === null) {
            return;
        }
        this.setAuthState(auth);
    }

    login = (username, password) => {
        const data = {username, password}
        axios.post('/not-secured/authenticate', data)
            .then(response => {
                localStorage.setItem('ym-auth', JSON.stringify(response.data))
                this.setAuthState(response.data)
            })
            .catch(reason => {
                console.log(reason)
            })
    }

    setAuthState(auth) {
        this.setState({
            auth,
            socket: new WebSocket(`ws://localhost:8080/not-secured/update-events?id=${auth.user.id}`)
        })
    }

    logout = () => {
        localStorage.removeItem('ym-auth')
        let socket = this.state.socket;
        if (socket !== null) {
            socket.close()
        }
        this.setState({auth: null, socket: null})
    }

    createUser = (user) => {
        axios.post('/not-secured/users', user)
            .then(response => {
                this.login(user.username, user.password)
            }).catch(reason => console.log(reason))
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.state.auth !== null && JSON.stringify(prevState.auth) !== JSON.stringify(this.state.auth)) {
            this.updateData();
        }
        if (this.state.socket !== null && prevState.socket !== this.state.socket) {
            this.state.socket.addEventListener('message', e => {
                console.log("Data update comming", e)
                this.updateData()
            })
            console.log("Set socket listener")
        }
    }

    updateData() {
        const id = this.state.auth.user.id;
        axios.get(`/users/${id}/countryData`)
            .then(response => {
                console.log(response)
                this.setState({userData: response.data})
            })
    }

    updateUser = (countryCode, jobRunMinute) => {
        axios.put(`/users/${this.state.auth.user.id}`, null, {
            params: {
                countryCode,
                jobRunMinute
            }
        })
            .then(response => {
                this.setState((prevState) => {
                    let user = {...prevState.auth.user, countryCode, jobRunMinute};
                    let auth = {...prevState.auth, user}
                    return { auth }
                })
            })
    }

    render() {
        return (
            <Layout logged={this.state.auth !== null} logout={this.logout}>
                {this.state.auth !== null ?
                    <Main userData={this.state.userData}
                          countryCode={this.state.auth.user.countryCode}
                          jobRunMinute={this.state.auth.user.jobRunMinute}
                          updateUser={this.updateUser}/>
                    :
                    <div>
                        <Route path="/" exact
                               render={() => <Login login={this.login}/>}/>
                        <Route path="/register" exact render={() => <Register createUser={this.createUser}/>}/>
                    </div>
                }
            </Layout>
        );
    }
}


export default YoutubeMonitor