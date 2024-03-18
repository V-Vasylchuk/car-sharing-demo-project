import React, { useEffect, useState } from 'react'
import HeaderPages from '../../components/header/HeaderPages';
import './rentals.scss';
import { Link, useNavigate } from 'react-router-dom';
import { backendUrl, getBearerToken, getToken } from '../../config';
import axios from 'axios';
import { FcApproval } from "react-icons/fc";
import { FcHighPriority } from "react-icons/fc";
import { useUser } from '../user/userContext';

function Rentals() {
  const [error, setError] = useState(null);
  const [rentals, setRentals] = useState([]);
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
              {rentals.map((rental,index) => (
                <tr key={rental.id}>
                  <th scope="row">{index + 1}</th>
                  <td><Link to={`/edit_user_role/${rental.user.id}`}>{rental.user.firstName}</Link> </td>
                  <td>{rental.car.brand}</td>
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

export default Rentals