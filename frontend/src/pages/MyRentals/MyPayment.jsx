import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { backendUrl, getBearerToken, getToken } from '../../config';
import axios from 'axios';
import HeaderPages from '../../components/header/HeaderPages';
import { DateTimeFormatter, LocalDate, LocalDateTime } from 'js-joda';
import { useUser } from '../user/userContext';

function MyPayment() {
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
    const [sessionId, setSessionId] = useState('');

    const createPayment = async () => {
        try {
            const response = await fetch(`${backendUrl}/api/v1/stripe/create-payment`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: getBearerToken()

                },
                body: JSON.stringify({
                    amount: car.dailyFee * 100,
                    quantity: 1,
                    currency: 'USD',
                    name: car.brand + ' ' + car.model,
                    successUrl: 'http://localhost:3000/success',
                    cancelUrl: 'http://localhost:3000/cancel',
                }),
                mode: 'cors',
            });
            const data = await response.json();
            setSessionId(data.data.sessionId);
            window.location.href = data.data.sessionUrl;
        } catch (error) {
            console.error('Error creating payment:', error);
        }
    };

    const capturePayment = async () => {
        try {
            const response = await fetch(`${backendUrl}/api/v1/stripe/capture-payment?sessionId=${sessionId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: getBearerToken()
                }
            });
            const data = await response.json();
            console.log('Capture payment response:', data);
        } catch (error) {
            console.error('Error capturing payment:', error);
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
                    url: `${backendUrl}/cars/${id}`,
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
        if (getToken() === null) {
            navigation("/")
        }
    }, [getToken()]);


    const onInputChange = (e) => {
        const { name, value } = e.target;
        let parsedValue = value;
        if (name === 'rentalDate' || name === 'returnDate' || name === 'actualReturnDate') {
            parsedValue = LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
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
                carId: car.id,
                userId: user.id
            }

            console.log(body)
            await axios.request({
                headers: {
                    Authorization: getBearerToken()
                },
                method: "POST",
                url: `${backendUrl}/rentals`,
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
                                <input type="datetime-local" name='rentalDate' value={order.rentalDate} onChange={onInputChange} onKeyPress={handleKeyPress} />
                                {errors.rentalDate && <span className='error'>{errors.rentalDate}</span>}
                            </div>
                            <div className='returnDate'>
                                <label htmlFor='returnDate'>Return Date</label>
                                <input type="datetime-local" name='returnDate' value={order.returnDate} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} />
                                {errors.returnDate && <span className='error'>{errors.returnDate}</span>}
                            </div>
                            <div className='actualReturnDate'>
                                <label htmlFor='inventory'>Actual Return Date</label>
                                <input type="datetime-local" name='actualReturnDate' value={order.actualReturnDate} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} />
                                {errors.inventory && <span className='error'>{errors.inventory}</span>}
                            </div>
                            <div className='brand'>
                                <label htmlFor='carId'>Car brand</label>
                                <input type='text' name='carId' value={car.brand} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />
                            </div>
                            {user.role === 'MANAGER' ? (
                                <div className='brand'>
                                    <label htmlFor='userId'>user</label>
                                    <input type='text' name='carId' value={order.userId} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />
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
                        <div>
                            <button onClick={createPayment}>Create Payment</button>
                            {sessionId && <button onClick={capturePayment}>Capture Payment</button>}
                        </div>

                    </div>
                </div>
            </div>
        </div>
    )
}

export default MyPayment