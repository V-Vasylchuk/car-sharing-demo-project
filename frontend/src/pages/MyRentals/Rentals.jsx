import React, { useEffect, useState } from 'react'
import HeaderPages from '../../components/header/HeaderPages';
import './rentals.scss';
import { useNavigate } from 'react-router-dom';
import { backendUrl, getBearerToken, getToken } from '../../config';
import axios from 'axios';
import { FcApproval } from "react-icons/fc";
import { FcHighPriority } from "react-icons/fc";
import { useUser } from '../user/userContext';

function MyRentals() {
  const [rentals, setRentals] = useState([]);
  const [userRentals, setUserRentals] = useState([]);
  const {user} = useUser();
  const navigation = useNavigate();

  useEffect(() => {
    if (getToken() === null) {
      navigation("/")
    }
  }, [getToken()]);

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    if (user.id) {
      const filteredRentals = rentals.filter(rental => rental.userId === user.id);
      setUserRentals(filteredRentals);
    }
  }, [user, rentals]);

  const fetchData = async () => {
    try {
      await axios.request({
        headers: {
          Authorization: getBearerToken()
        },
        method: "GET",
        url: `${backendUrl}/rentals/`
      }).then(response => {
        setRentals(response.data);
      });

    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };


  const isActive = (rental) => {
    const returnDate = new Date(rental.returnDate);
    const currentDate = new Date();

    return returnDate < currentDate;
  }


  return (
    <div className='rental'>
      <HeaderPages />
      <div className='rental_container'>
        <h1 className='container__title'>History Rentals</h1>
        <div className='rental_wrapper'>
          <table className="rental_table table-bordered border-primary table">
            <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">User</th>
                <th scope="col">Cars</th>
                <th scope='col'>Return Date</th>
                <th scope="col">Active</th>
              </tr>
            </thead>
            <tbody>
              {userRentals.map((rental, index) => (
                <tr key={rental.id}>
                  <th scope="row">{index + 1}</th>
                  <td>{user.firstName}</td>
                  <td>{rental.carBrand}</td>
                  <td>{rental.actualReturnDate.split('T')[0]}</td>
                  <td>{isActive(rental) ? <FcHighPriority /> : <FcApproval />}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default MyRentals;
