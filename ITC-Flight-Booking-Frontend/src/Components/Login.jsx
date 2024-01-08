import axios from 'axios';
import React, {useState} from 'react'
import { Link, useNavigate } from 'react-router-dom'

export default function Login() {

    const nav = useNavigate();
    const[selectedFlight, setSelectedFlight] = useState(sessionStorage.getItem('selectedScheduleCode'));
    
    const [credientials, setCredientials] = useState({
        email: "",
        password: "",
    });
    const handleLoginChange = (e) =>{
        setCredientials({
            ...credientials,
            [e.target.name]: e.target.value, 
        });
    };



    const handleLoginSubmit = async (e) => {
        try {
            const resp = await axios.post("http://localhost:8080/api/user/login",credientials);
            
            sessionStorage.setItem("user",JSON.stringify(resp.data));
            if(selectedFlight === null){
              nav("/user");
            }
            else{
              nav("/booking")
            }
            // window.location.reload();
            console.log("Login sucessful!");
        } catch (error) {
            // alert("f")
            console.log("Login failed", error.message);
        }
    }
  return (
    <div>
       <fieldset>
        <legend>Login</legend>
      <form className='form'>
        <input type="text" placeholder='Email' name="email" value={credientials.email} onChange={handleLoginChange} /><br />
        <input type="password" placeholder='Password' name="password" value={credientials.password} onChange={handleLoginChange} /><br />
        <button type='button' onClick={handleLoginSubmit}>Login</button><br />
        <Link className='link' to={"/registration"}>Register new user.</Link>
      </form>
       </fieldset>
      
    </div>
  )
}