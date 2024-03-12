import React, { useEffect, useState } from 'react';
import './payment.scss'
import Stripe from "react-stripe-checkout";
import { useNavigate, useParams } from 'react-router-dom';
import { getBearerToken, getToken } from '../../config';
import axios from 'axios';
import HeaderPages from '../../components/header/HeaderPages';
import { DateTimeFormatter, LocalDate } from 'js-joda';
import { useUser } from '../user/userContext';

function Payment() {
    const { id } = useParams();
    const { user, setUser } = useUser();
   
    const [errors, setErrors] = useState({
        rentalDate: '',
        brand: '',
        carId: ''
    });
    const [car, setCar] = useState({
        model: '',
        brand: '',
        inventory: '',
        dailyFee: '',
        type: 'SEDAN',
    });
    const [order, setOrder] = useState([]);

    const navigation = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                await axios.request({
                    headers: {
                        Authorization: getBearerToken()
                    },
                    method: "GET",
                    url: `http://localhost:8080/cars/${id}`,
                }).then(response => {
                    setCar(response.data);
                });

            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, []);

    useEffect(() => {
        const chargePayment = async () => {
            try {
                await axios.post("http://localhost:8080/api/payment/charge", "", {
                    headers: {
                        Authorization: getBearerToken(),
                        amount: 500,
                    }
                });
                alert("Payment Success");
            } catch (error) {
                setErrors(error);
            }
        };
        chargePayment();
    }, []);



    useEffect(() => {
        if (getToken() === null) {
            navigation("/login")
        }
    }, [getToken()]);


    const onInputChange = (e) => {
        const { name, value } = e.target;
        let parsedValue = value;
        if (name === 'rentalDate' || name === 'returnDate' || name === 'actualReturnDate') {
            parsedValue = LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
        }
        setOrder({ ...order, [name]: parsedValue });
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        let hasError = false;
        Object.entries(order).forEach(([key, value]) => {
            if (typeof value === 'string' && value.trim().length === 0) {
                hasError = true;
                setErrors({ ...errors, [key]: 'This field is required' });
            } else if (value === null || value === undefined) {
                hasError = true;
                setErrors({ ...errors, [key]: 'This field is required' });
            }
        });
    
        if (hasError) {
            return;
        }
    
        try {
            let body = {
                ...order,
                rentalDate : order.rentalDate + 'T11:31:22.321Z',
                returnDate : order.returnDate + 'T11:31:22.321Z',
                actualReturnDate : order.actualReturnDate + 'T11:31:22.321Z',
                userId : user.id,
                carId: car.id
            }

            console.log(body)
            await axios.request({
                headers: {
                    Authorization: getBearerToken()
                },
                method: "POST",
                url: 'http://localhost:8080/rentals',
                data: body
            }).then(response => {
                setOrder(response.data);
                alert(`You create a new order. Car: ${car.model}`);
            });
    
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    

  useEffect(() => {
    const fetchData = async () => {
      try {
        await axios.request({
          headers: {
            Authorization: getBearerToken()
          },
          method: "GET",
          url: `http://localhost:8080/users/me`
        }).then(response => {
          setUser(response.data);
        });

      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
  }, []);


    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            const form = event.target.form;
            const index = Array.prototype.indexOf.call(form, event.target);
            const nextField = form.elements[index + 1];
            if (nextField) {
                nextField.focus();
            } else {
                onSubmit(event);
            }
        }
    };


    return (
        <div className='addCar'>
            <HeaderPages />
            <div className='container_add'>
                <h1 className='container__title'>Payment Page</h1>
                <div className='wrapper_add'>
                    <div className='form-wrapper'>
                        <form onSubmit={onSubmit} noValidate>
                            <div className='rentalDate'>
                                <label htmlFor='rentalDate'>Rental Date</label>
                                <input type='date' name='rentalDate' value={order.rentalDate} onChange={onInputChange} onKeyPress={handleKeyPress} />
                                {errors.rentalDate && <span className='error'>{errors.rentalDate}</span>}
                            </div>
                            <div className='returnDate'>
                                <label htmlFor='returnDate'>Return Date</label>
                                <input type='date' name='returnDate' value={order.returnDate} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} />
                                {errors.returnDate && <span className='error'>{errors.returnDate}</span>}
                            </div>
                            <div className='actualReturnDate'>
                                <label htmlFor='inventory'>Actual Return Date</label>
                                <input type='date' name='actualReturnDate' value={order.actualReturnDate} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} />
                                {errors.inventory && <span className='error'>{errors.inventory}</span>}
                            </div>
                            <div className='brand'>
                                <label htmlFor='carId'>Car brand</label>
                                <input type='text' name='carId' value={car.brand} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />
                            </div>
                            {user.role === 'MANAGER' ? (
                                <div className='brand'>
                                <label htmlFor='userId'>user</label>
                                <input type='text' name='carId' value={order.userIdId} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />
                            </div>
                            ) : (
                                <div></div>
                            )}
                            
                            <div className='submit'>
                                <button type='submit'>Add Rental</button>
                            </div>
                        </form>
                    </div>
                    <div className="payment_wrapper">
                        <div><Stripe
                            stripeKey="pk_test_****************************"
                            token={getToken}
                        /></div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Payment