import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import { getBearerToken, getToken } from '../../config';


function EditCarForm() {
  const {id} = useParams();
  const [errors, setErrors] = useState({
    model: '',
    brand: '',
    inventory: '',
    dailyFee: '',
  });
  const navigation = useNavigate();

  useEffect(() => {
    if (getToken() === null) {
        navigation("/login")
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
  

  const [car, setCar] = useState({
    model: '',
    brand: '',
    inventory: '',
    dailyFee:'',
    type: 'SEDAN',
  });

  const onInputChange = (e) => {
    const { name, value } = e.target;
    setCar({ ...car, [e.target.name]: e.target.value });
    if (value.length < 2 || value.length > 20) {
      setErrors({ ...errors, [name]: `${name.charAt(0).toUpperCase() + name.slice(1)} must be between 2 and 20 characters` });
    } else {
      setErrors({ ...errors, [name]: '' });
    };
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    let hasError = false;
    Object.values(car).forEach(value => {
      if (value.trim().length < 1) {
        hasError = true;
      }
    });

    if (hasError) {
      setErrors({
        model: car.model.trim().length < 1 ? 'Please fill in the Model field' : '',
        brand: car.brand.trim().length < 1 ? 'Please fill in the Brand field' : '',
        inventory: car.inventory.trim().length < 1 ? 'Please fill in the Inventory field' : '',
        dailyFee: car.dailyFee.trim().length < 1 ? 'Please fill in the Daily Fee field' : ''
      });
      return;
    }
    try {
      await axios.request({
        headers: {
          Authorization: getBearerToken()
        },
        method: "PUT",
        url: `http://localhost:8080/cars/${id}`,
        data: car 
      }).then(response => {
        setCar([response.data]);
        navigation(`/cars/${id}`);
      });
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }


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
       <button className="btn"><Link to="/cars">Go Back </Link> </button>
      <div className='container_add'>
        <h1 className='container__title'>Edit Car</h1>
        <div className='wrapper_add'>
          <img src='' alt='' className='img_bg' />
          <div className='form-wrapper'>
            <form onSubmit={e => onSubmit(e)} noValidate>
              <div className='model'>
                <label htmlFor='model'>Model</label>
                <input type='text' name='model' value={car.model} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />
                {errors.model && <span className='error'>{car.model}</span>}
              </div>
              <div className='brand'>
                <label htmlFor='brand'>Brand</label>
                <input type='text' name='brand' value={car.brand} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />
                {errors.brand && <span className='error'>{car.brand}</span>}
              </div>
              {/* <div className='photo'>
                <label htmlFor='photo'>Add Photo</label>
                <input type='file' name='photo' className='photo_input' onChange={e => onInputChange(e)} />
              </div> */}
              <div className='brand'>
                <label htmlFor='inventory'>Inventory</label>
                <input type='text' name='inventory' value={car.inventory} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />

              </div>
              <div className='brand'>
                <label htmlFor='dailyFee'>DailyFee</label>
                <input type='text' name='dailyFee' value={car.dailyFee} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />

              </div>
              {/* <div className='price'>
                <label htmlFor='price'>Price</label>
                <input type='text' name='price' value={price} onChange={e => onInputChange(e)} onKeyPress={handleKeyPress} noValidate required />
                {errors.price && <span className='error'>{errors.price}</span>}
              </div> */}
              <div className='carType'>
                <label htmlFor='carType'>Car Type</label>
                <select name='carType' value={car.type} onChange={(e) => setCar({ ...car, type: e.target.value })} onKeyPress={handleKeyPress}>
                  <option value='SEDAN'>Sedan</option>
                  <option value='SUV'>SUV</option>
                  <option value='HATCHBACK'>Hatchback</option>
                  <option value='UNIVERSAL'>Universal</option>
                </select>
              </div>
              <div className='submit'>
                <button type='submit'>Edit Car</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EditCarForm;
