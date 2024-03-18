import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import { backendUrl, getBearerToken, getToken } from '../../config';


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
  

  const [car, setCar] = useState({
    model: '',
    brand: '',
    inventory: '',
    dailyFee:'',
    type: 'SEDAN',
    presignedUrl: ''
  });

  const onInputChange = (e) => {
    const {name, value} = e.target;
    setCar({...car, [name]: value});

    if (name === 'inventory' || name === 'dailyFee') {
      if (!/^\d+(\.\d{1,2})?$/.test(value)) {
        setErrors({...errors, [name]: `${name.charAt(0).toUpperCase() + name.slice(1)} must be a number`});
      } else {
        setErrors({...errors, [name]: ''});
      }
    } else {
      setErrors({
        ...errors,
        [name]: value.length < 2 || value.length > 20 ? `${name.charAt(0).toUpperCase() + name.slice(1)} must be between 2 and 20 characters` : ''
      });
    }

    setCar({...car, [name]: value});
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    let hasError = false;
    Object.values(car).forEach(value => {
      if (typeof value === 'string' && value.trim().length < 1) {
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

    console.log(car.dailyFee);
    try {
      console.log(car.dailyFee);
      await axios.request({
        headers: {
          Authorization: getBearerToken()
        },
        method: "PUT",
        url: `${backendUrl}/cars/${id}`,
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
       <button className="btn car_btn button-52"><Link to={`/cars/${id}`}>Go Back </Link> </button>
      <div className='container_add'>
        <h1 className='container__title'>Edit Car</h1>
        <div className='wrapper_add'>
          <div className='form-wrapper'>
            <form onSubmit={e => onSubmit(e)} noValidate>
              <div>
                <img src={car.presignedUrl} alt='Uploaded' style={{maxWidth: '100%'}}/>
              </div>
              <div className='model'>
                <label htmlFor='model'>Model</label>
                <input type='text' name='model' value={car.model || ''} onChange={e => onInputChange(e)}
                       onKeyPress={handleKeyPress} noValidate required/>
                {errors.model && <span className='error'>{errors.model}</span>}
              </div>
              <div className='brand'>
                <label htmlFor='brand'>Brand</label>
                <input type='text' name='brand' value={car.brand || ''} onChange={e => onInputChange(e)}
                       onKeyPress={handleKeyPress} noValidate required/>
                {errors.brand && <span className='error'>{errors.brand}</span>}
              </div>

              <div className='brand'>
                <label htmlFor='inventory'>Inventory</label>
                <input type='text' name='inventory' value={car.inventory || ''} onChange={e => onInputChange(e)}
                       onKeyPress={handleKeyPress} noValidate required/>
                {errors.inventory && <span className='error'>{errors.inventory}</span>}
              </div>
              <div className='brand'>
                <label htmlFor='dailyFee'>DailyFee</label>
                <input type='text' name='dailyFee' value={car.dailyFee || ''} onChange={e => onInputChange(e)}
                       onKeyPress={handleKeyPress} noValidate required/>
                {errors.dailyFee && <span className='error'>{errors.dailyFee}</span>}
              </div>
              <div className='carType'>
                <label htmlFor='carType'>Car Type</label>
                <select name='carType' value={car.type || ''} onChange={(e) => setCar({...car, type: e.target.value})}
                        onKeyPress={handleKeyPress}>
                  <option value='SEDAN'>SEDAN</option>
                  <option value='SUV'>SUV</option>
                  <option value='HATCHBACK'>HATCHBACK</option>
                  <option value='UNIVERSAL'>UNIVERSAL</option>
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
