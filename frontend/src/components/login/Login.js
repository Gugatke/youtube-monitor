import React, {useRef} from 'react'
import {Link} from 'react-router-dom'

const Login = (props) => {
    let usernameRef = useRef(null)
    let passwordRef = useRef(null)
    const login = (e) => {
        e.preventDefault()
        let username = usernameRef.current.value
        let password = passwordRef.current.value
        props.login(username, password)
    }

    return (
        <div>
            <p>Please Log In or <Link to='/register'>Register</Link></p>
            <form onSubmit={(e) => login(e)}>
                <label htmlFor="username">Username</label>
                <input name="username" placeholder="username" type="text" required ref={usernameRef}/> <br/>
                <label htmlFor="password">Password</label>
                <input name="password" placeholder="password" type="password" required ref={passwordRef}/> <br/>
                <input name="Submit" type="submit"/>
            </form>
        </div>
    )
}

export default Login