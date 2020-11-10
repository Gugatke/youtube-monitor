import React, {useRef} from 'react'

const Main = (props) => {

    const countryRef = useRef(null)
    const jobRunMinuteRef = useRef(null)

    const updateUser = (e) => {
        e.preventDefault()
        const countryCode = countryRef.current.value
        const jobRunMinute = jobRunMinuteRef.current.value
        props.updateUser(countryCode, jobRunMinute)
    }

    let render = <h1 style={{color: 'red'}}>User video data for {props.countryCode} is not loaded</h1>
    if (props.userData !== null) {
        render = (
            <div>
                {props.userData.thumbnailUrl &&
                <img src={props.userData.thumbnailUrl} alt="The most popular video" width="400px"/>}
                {props.userData.thumbnailUrl && <br/>}
                {props.userData.videoUrl &&
                <a href={props.userData.videoUrl}>The most popular video in {props.userData.countryCode}</a>}
                {props.userData.videoUrl && <br/>}
                {props.userData.commentUrl &&
                <a href={props.userData.commentUrl}>Video's most relevant comment</a>}
            </div>)
    }
    return <div>
        {render}
        <hr/>
        <h3>Edit user preferences</h3>
        <form onSubmit={(e) => updateUser(e)}>
            <label htmlFor="country">Country</label>
            <input type="text" name="country" pattern="[a-zA-Z]{2}" placeholder={props.countryCode} required
                   ref={countryRef}/><br/>
            <label htmlFor="jobMinute">Job Run Minute</label>
            <input name="jobMinute" placeholder={props.jobRunMinute} min="1" max="60" required type="number"
                   ref={jobRunMinuteRef}/><br/>
            <input type="submit" value="Update"/>
        </form>
    </div>
}

export default Main