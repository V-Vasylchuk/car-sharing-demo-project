import React, { useEffect, useState } from 'react'
import {  useLocation, useNavigate, useParams } from 'react-router-dom';


import List from './List';

import './list.scss';
import HeaderPages from '../../components/header/HeaderPages'
import { backendUrl, getBearerToken, getToken } from '../../config';
import axios from 'axios';


function MainCarsList() {
  const [cars,setCars] = useState([]);
  const navigation = useNavigate();

  useEffect(() => {
    if (getToken() === null) {
        navigation("/")
    }
  },[getToken()]);
  
  useEffect(() => {
    const fetchData = async () => {
      try {
        await axios.request({
          headers: {
            Authorization: getBearerToken()
          },
          method: "GET",
          url: `${backendUrl}/cars`
        }).then(response => {
          setCars(response.data);
        });
      
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
  }, []);



  
    return ( 
        <>
        <HeaderPages />
        <List cars={cars} />
        </>
     );
}

export default MainCarsList