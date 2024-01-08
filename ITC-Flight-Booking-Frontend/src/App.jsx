import React from 'react';
import { BrowserRouter as Router,Routes, Route } from 'react-router-dom';
import Home from "./Components/Home"
import Login from "./Components/Login"
import Registration from "./Components/Registration"
import Booking from './Components/Booking';
import User from './Components/User'; 
import Admin from './Components/Admin';
import './App.css'


function App() {

  return (
    <Router>
     <Routes>
          <Route path="/"  element={<Home/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/registration" element={<Registration/>} />
          <Route path="/booking" element={<Booking/>} />
          <Route path="/user" element={<User/>} />
          <Route path="/admin" element={<Admin/>} />

          </Routes>
    </Router>
  )
}

export default App
