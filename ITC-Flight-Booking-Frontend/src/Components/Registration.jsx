import axios from 'axios';
import React, {useState} from 'react'
import { Link, useNavigate } from 'react-router-dom'

export default function Registration() {
  const nav = useNavigate();
  const [user, setUser] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    password: '',
})


const handleRegistrationChange = (e) => {
    setUser({...user,[e.target.name] : e.target.value});
}

const handleRegistrationSubmit = async (e) =>{
    try {
        const response = await axios.post("http://localhost:8080/api/user",user)
        console.log("Registration sucessful!", response.data);
        nav("/login")
        nav("/")
        console.log("Login sucessful!");
    } catch (error) {
        console.log("registration failed", error.message);
    }
}
  return (
    <div>
       <fieldset>
        <legend>Registration</legend>
      <form className='form'>
        <input type="text" placeholder='First Name' name="firstName" value={user.firstName} onChange={handleRegistrationChange} /><br />
        <input type="text" placeholder='Last Name' name="lastName" value={user.lastName} onChange={handleRegistrationChange} /><br />
        <input type="text" placeholder='Phone Number' name="phoneNumber" value={user.phoneNumber} onChange={handleRegistrationChange} /><br />
        <input type="text" placeholder='Email' name="email" value={user.email} onChange={handleRegistrationChange} /><br />
        <input type="password" placeholder='Password' name="password" value={user.password} onChange={handleRegistrationChange} /><br />
        <button type='button' onClick={handleRegistrationSubmit}>Register</button><br />
        <Link className='link' to={"/login"}>Register new user.</Link>
      </form>
       </fieldset>

    </div>
  )
}
