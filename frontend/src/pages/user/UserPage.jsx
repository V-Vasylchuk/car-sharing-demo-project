import React, { useEffect, useState } from 'react'
import HeaderPages from '../../components/header/HeaderPages'
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import { backendUrl, getBearerToken, getToken } from '../../config';
import { useUser } from './userContext';

function UserPage() {
  const navigation = useNavigate();

  useEffect(() => {
    if (getToken() === null) {
        navigation("/")
    }
  },[getToken()]);

  const { user, setUser } = useUser();
 
  useEffect(() => {
    const fetchData = async () => {
      try {
        await axios.request({
          headers: {
            Authorization: getBearerToken()
          },
          method: "GET",
          url: `${backendUrl}/users/me`
        }).then(response => {
          setUser(response.data);
        });
       
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
  }, []);



  const onInputChange = (e) => {
    setUser({...user, [e.target.name]: e.target.value})
  }

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.request({
        headers: {
          Authorization: getBearerToken()
        },
        method: "PUT",
        url: `${backendUrl}/users/me`,
        data: {
          ...user,
          password: 'test1234'
        }
      }).then(response => {
        setUser([response.data]);
        navigation("/main");
      });
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }


  const {firstName, email,lastName, password} = user;


  return (
    <div className='user_info'>
        <img className='bg_img' src="https://c.pxhere.com/photos/8e/2c/black_and_white_cars_city_lights_night_san_francisco_street_traffic-954079.jpg!s2" alt="" />

      <HeaderPages />
      <div className='user_info_container'>
        <h1 className='user_info__title'>Info about me</h1>
        <div className='user_info_wrapper'>
          <form className="user_info" onSubmit={e => onSubmit(e)}>
            <div className='text_info'>
              <label htmlFor="firstName">
                FIRST NAME
              </label>
              <input type="text" value={firstName || ''} name='firstName' onChange={e => onInputChange(e)}/>
            </div>
            <div className='text_info'>
              <label htmlFor="lastName">
                LAST NAME
              </label>
              <input type="text"  value={lastName || ''} name='lastName' onChange={e => onInputChange(e)}/>
            </div>
            <div className='text_info'>
              <label htmlFor="email">
              EMAIL
              </label>
              <input type="text" value={email || ''} name='email' onChange={e => onInputChange(e)}/>
            </div>
            <div className='text_info'>
              <label htmlFor="password">
              PASSWORD
              </label>
              <input type="text" value={password || ''} name='password'placeholder='********' onChange={e => onInputChange(e)}/>
            </div>
            <button type='Submit' className='btn button-52'>
              Edit
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

export default UserPage