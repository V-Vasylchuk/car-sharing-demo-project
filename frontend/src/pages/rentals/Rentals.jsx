import React, { useEffect, useState } from 'react'
import HeaderPages from '../../components/header/HeaderPages';
import './rentals.scss';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { getBearerToken, getToken } from '../../config';
import axios from 'axios';
import { FcApproval } from "react-icons/fc";
import { FcHighPriority } from "react-icons/fc";

function Rentals() {
  const { id } = useParams();
  const [rentals, setRentals] = useState([]);


  const navigation = useNavigate();

  useEffect(() => {
    if (getToken() === null) {
      navigation("/login")
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
        url: ` http://localhost:8080/rentals/`
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
              {rentals.map(rental => (
                <tr key={rental.id}>
                  <th scope="row">{rental.id}</th>
                  <td><Link to="/edit_user_role">{rental.userFirstName}</Link> </td>
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

export default Rentals