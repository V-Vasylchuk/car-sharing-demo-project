import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './user.scss'
import { backendUrl, getBearerToken, setToken } from '../../config';
import axios from 'axios';

function User() {
  const [isTextVisible, setIsTextVisible] = useState(false);

  const [users, setUser] = useState([]);


  useEffect(() => {

    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      await axios.request({
        headers: {
          Authorization: getBearerToken()
        },
        method: "GET",
        url: `${backendUrl}/users/me`
      }).then(response => {
        setUser([response.data]);
      });

    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };


  const handleUserClick = () => {
    setIsTextVisible(!isTextVisible);
  };

  const handleLogout = () => {
    setToken(null);
    navigator("/")
  }

  return (
    <div className="user-container">
      <img className='img_user' src="https://freesvg.org/img/abstract-user-flat-4.png" alt="" onClick={handleUserClick} />
      {isTextVisible && users.map(user => (
        <div key={user.id} className="user-info-popup">
          <p className='user_info'>Name: <span className='user_info_text'>{user.firstName}</span> </p>
          <p className='user_info'>Email: <span className='user_info_text'>{user.email}</span> </p>
          <p className='user_info'>Role: <span className='user_info_text'>{user.role}</span> </p>
          <p><Link to="/user/me" className='user_info btn_info'>Edit</Link></p>
          <p><Link to='/' className='user_info btn_info'>Log out</Link></p>
        </div>
      ))}
    </div>


  );
}

export default User;