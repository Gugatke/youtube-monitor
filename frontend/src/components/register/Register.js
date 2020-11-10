import React, {useRef} from 'react'
import {Link} from "react-router-dom";

const Register = (props) => {
    const usernameRef = useRef(null)
    const passwordRef = useRef(null)
    const repeatPasswordRef = useRef(null)
    const countryRef = useRef(null)
    const jobRunMinuteRef = useRef(null)
    
    const createUser = (e) => {
        e.preventDefault()
        const username = usernameRef.current.value
        const password = passwordRef.current.value
        if (password !== repeatPasswordRef.current.value) {
            console.log('passwords does not match')
            return 
        }
        const countryCode = countryRef.current.value
        const jobRunMinute = jobRunMinuteRef.current.value
        props.createUser({
            username,
            password,
            countryCode,
            jobRunMinute
        })
    }

    return (
        <div>
            <p>Please <Link to='/'>Log</Link> In or Register</p>
            <form onSubmit={(e) => createUser(e)}>
                <label htmlFor="username">Username</label>
                <input name="username" placeholder="Username" type="text" required ref={usernameRef}/> <br/>
                <label htmlFor="password">Password</label>
                <input name="password" placeholder="Password" type="password" required ref={passwordRef}/> <br/>
                <label htmlFor="repeatPassword">Repeat Password</label>
                <input name="repeatPassword" placeholder="Repeat Password" type="password" required ref={repeatPasswordRef}/> <br/>
                <label htmlFor="country">Country</label>
                <input type="text" name="country" pattern="[a-zA-Z]{2}" placeholder="Country code" required ref={countryRef}/><br/>
                <label htmlFor="jobMinute">Job Run Minute</label>
                <input name="jobMinute" placeholder="0-60" min="1" max="60" required type="number" ref={jobRunMinuteRef}/><br/>
                <input type="submit" value="Create"/>
            </form>
        </div>
    )
}

export default Register