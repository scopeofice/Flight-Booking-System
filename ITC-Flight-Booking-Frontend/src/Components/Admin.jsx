import React, { useState, useEffect } from 'react';
import axios from 'axios';
import FlightTable from './FlightTable';
import FlighScheduletTable from './FlightScheduleTable';

export default function Admin() {
    
    const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("user")));
    const [userDetails, setUserDetails] = useState(user?.accountInfo);




  return (
    <>
    <div className='user-info'>
          <h1>Welcome</h1>
          <p>
            <strong>Name:</strong> {userDetails.firstName} {userDetails.lastName}
          </p>
          <p>
            <strong>Email:</strong> {userDetails.email}
          </p>
          <p>
            <strong>Phone Number:</strong> {userDetails.phoneNumber}
          </p>
      </div>
      <div>
      <FlightTable/>
      <FlighScheduletTable/>
    </div>
      
    </>
  )
}
