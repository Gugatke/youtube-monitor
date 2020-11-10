import React from 'react'

const container = {
    margin: '0 auto',
    width: '600px',
    textAlign: 'center',
    height: 'auto'
}

const header = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center'
}

const title = {
    marginLeft: '20px',
    marginTop: '20px'
}

const button = {
    marginRight: '20px',
    cursor: 'pointer'
}

const Layout = (props) => {
    return (
        <div>
            <div style={header}>
                <h2 style={title}>YoutubeMonitor</h2>
                {props.logged && <h2 style={button} onClick={props.logout}>Logout</h2>}
            </div>
            <div style={container}>
                {props.children}
            </div>
        </div>)
}

export default Layout