import axios from 'axios';
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../CSS/Login.css'

export default function Login() {
  const nav = useNavigate();
  const [selectedFlight, setSelectedFlight] = useState(sessionStorage.getItem('selectedScheduleCode'));

  const [credentials, setCredentials] = useState({
    email: "",
    password: "",
  });

  const handleLoginChange = (e) => {
    setCredentials({
      ...credentials,
      [e.target.name]: e.target.value,
    });
  };

  const handleLoginSubmit = async (e) => {
    try {
      const resp = await axios.post("http://localhost:8080/api/user/login", credentials);

      sessionStorage.setItem("user", JSON.stringify(resp.data));
      if (selectedFlight === '') {
        if(resp.data.accountInfo.role === 'ROLE_USER'){
          nav("/user");
        }else{
          nav("/admin")
        }
      } else {
        nav("/booking");
      }
      console.log("Login successful!");
    } catch (error) {
      console.log("Login failed", error.message);
    }
  };

  return (
    <div>
        <form className='form'>
        <h2>Login</h2>
          <input type="text" placeholder='Email' name="email" value={credentials.email} onChange={handleLoginChange} /><br />
          <input type="password" placeholder='Password' name="password" value={credentials.password} onChange={handleLoginChange} /><br />
          <button type='button' onClick={handleLoginSubmit}>Login</button><br />
          <Link className='link' to={"/registration"}>Register new user.</Link>
        </form>
    </div>
  );
}
