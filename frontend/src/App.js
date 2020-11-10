import React from "react";
import {BrowserRouter} from 'react-router-dom'
import './App.css';
import YoutubeMonitor from "./containers/YoutubeMonitor";

function App() {
    return (
        <BrowserRouter>
            <YoutubeMonitor/>
        </BrowserRouter>
    );
}

export default App
