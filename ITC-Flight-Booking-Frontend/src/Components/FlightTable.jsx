import React, { useState, useEffect } from 'react';
import axios from 'axios';

const FlightTable = () => {
    const [flights, setFlights] = useState([]);
    const [show, setShow] = useState(false);
    const [del,setDel] =useState(false);
    const [editMode, setEditMode] = useState(false);
    const [selectedFlight, setSelectedFlight] = useState({});
    const [newFlight, setNewFlight] = useState({
        flightName: '',
        airlineName: '',
        numberOfSeats: '',
        fare: ''
    });


    const handleChange = (e) => {
        setNewFlight({
            ...newFlight, [e.target.name] : e.target.value
        })
    }

    const handleEditChange = (e) => {
        setSelectedFlight({
            ...selectedFlight,
            [e.target.name]: e.target.value
        });
    };

  
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/flights/all');
                console.log(response.data);
                setFlights(response.data);
            } catch (error) {
                console.error('Error fetching flights:', error);
            }
        };
    
        fetchData();
        setDel(false);
    }, [show,del]);
    

const handleShowForm=()=>{
    setShow(true)
}

const handleNewFlight = async() =>{
    try {
        const resp = await axios.post('http://localhost:8080/api/flights/addFlight',newFlight)
        console.log(resp.data);
        setNewFlight({
            flightName: '',
            airlineName: '',
            numberOfSeats: '',
            fare: ''
        })
        setShow(false)
    } catch (error) {
        console.log(error)
    }
}

const handleDelete = async (flightName) => {
    try {
        const response = await axios.delete(`http://localhost:8080/api/flights/deleteFlight/${flightName}`);
        console.log(response.data);
        setDel(true)

        
    } catch (error) {
        console.log(error.response.data); 
    }
}


const handleEdit = async()=>{
    try {
        const response = await axios.put('http://localhost:8080/api/flights/updateFlight',selectedFlight)
        console.log(response.data);
        setShow(false)
    } catch (error) {
        console.log(error)
    }
}

const handleEditClick = (flight) => {
    setEditMode(true);
    setSelectedFlight(flight);
    setShow(true);
};



const handleClose=()=>{
    setEditMode(false)
    setShow(false)
}
  

return (
    <>
        {/* <button onClick={handleShowForm}>Add Flight</button> */}
        <div style={{display:"flex"}}>
        {/* {show && ( */}
            <div>
                <form className='form' style={{margin:"20px"}}>
                    <>
                        <h2>{editMode ? 'Edit Flight' : 'Add New Flight'}</h2>
                        <input
                            type="text"
                            name="airlineName"
                            id="airlineName"
                            placeholder="Airline Name"
                            value={editMode ? selectedFlight.airlineName : newFlight.airlineName}
                            onChange={editMode ? handleEditChange : handleChange}
                        />
                        <input
                            type="text"
                            name="flightName"
                            id="flightName"
                            placeholder="Flight Name"
                            value={editMode ? selectedFlight.flightName : newFlight.flightName}
                            onChange={editMode ? handleEditChange : handleChange}
                        />
                        <input
                            type="text"
                            name="numberOfSeats"
                            id="numberOfSeats"
                            placeholder="Seats"
                            value={editMode ? selectedFlight.numberOfSeats : newFlight.numberOfSeats}
                            onChange={editMode ? handleEditChange : handleChange}
                        />
                        <input
                            type="text"
                            name="fare"
                            id="fare"
                            placeholder="Fare"
                            value={editMode ? selectedFlight.fare : newFlight.fare}
                            onChange={editMode ? handleEditChange : handleChange}
                        />
                        <button type="button" onClick={editMode ? handleEdit : handleNewFlight}>
                            {editMode ? 'Update' : 'Add'}
                        </button>
                        <button type='button' onClick={handleClose}>Close</button>
                    </>
                </form>
            </div>
        {/* )} */}
        <div style={{width:"65%"}}>
        <table className="flight-table">
            <thead>
                <tr>
                    <th>Flight Name</th>
                    <th>Airline Name</th>
                    <th>Number of Seats</th>
                    <th>Fare</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {flights.map((flight) => (
                    <tr key={flight.flightName}>
                        <td>{flight.flightName}</td>
                        <td>{flight.airlineName}</td>
                        <td>{flight.numberOfSeats}</td>
                        <td>{"Rs."}{flight.fare}</td>
                        <td>
                            <button onClick={() => handleEditClick(flight)}>Edit</button>
                            <button onClick={() => handleDelete(flight.flightName)}>Delete</button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
        </div>
        </div>
    </>
);
};

export default FlightTable;
