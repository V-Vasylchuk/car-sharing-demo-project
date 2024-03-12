import React, { useEffect, useState } from 'react';
import './register.scss';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { setToken } from '../../config';

const validEmailRegex = RegExp(
  /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
);

function RegisterPage() {
  const [firstName, setFirstName] = useState(null);
  const [lastName, setLastName] = useState(null);
  const [email, setEmail] = useState(null);
  const [password, setPassword] = useState(null);
  const [errors, setErrors] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  });
  
  useEffect(() =>{
    setToken(null)
  },[])
 

  const handleChange = (event) => {
    event.preventDefault();
    const { name, value } = event.target;
    let updatedErrors = { ...errors };

    switch (name) {
      case 'firstName':
        updatedErrors.firstName = (value.length < 2 || value.length > 20) ? 'First Name must be at least 2 and more 20 characters long!' : '';
        break;
      case 'lastName':
        updatedErrors.lastName = (value.length < 2 || value.length > 20) ? 'Last Name must be at least 5 characters long!' : '';
        break;
      case 'email':
        updatedErrors.email = validEmailRegex.test(value) ? '' : 'Email is not valid!';
        break;
      case 'password':
        updatedErrors.password = value.length < 8 ? 'Password must be at least 8 characters long!' : '';
        break;
      default:
        break;
    }

    setErrors(updatedErrors);
    if (name === 'firstName') {
      setFirstName(value);
    } else if (name === 'lastName') {
      setLastName(value);
    } else if (name === 'email') {
      setEmail(value);
    } else if (name === 'password') {
      setPassword(value);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      let body = {
        firstName,
        lastName,
        email,
        password
      }
      const response = await axios.post('http://localhost:8080/register', body);
      const token = response.data.token;
      setToken(token);
      navigation('/main');
    } catch (error) {
      setErrors('Invalid email or password!');
      console.error('Error logging in:', error);
    }

    if (!firstName || !lastName || !email || !password) {
      console.error('Please fill in all required fields');
      return;
    }
    navigation('/main');
    if (validateForm(errors)) {
      console.info('Valid Form');
    } else {
      console.error('Invalid Form');
    }
  };

  const validateForm = (errors) => {
    let valid = true;
    Object.values(errors).forEach((val) => val.length > 0 && (valid = false));
    return valid;
  };

  let navigation = useNavigate();


  return (
    <div className='register'>
      <div className="container">
        <h1 className='container__title'>Sign in with your account</h1>
        <div className='wrapper'>
          <div className='form-wrapper'>
            <form onSubmit={handleSubmit} noValidate>
              <div className='firstName'>
                <label htmlFor='firstName'>First Name</label>
                <input type='text' name='firstName' onChange={handleChange} noValidate required />
                {errors.firstName.length > 0 && <span className='error'>{errors.firstName}</span>}
              </div>
              <div className='lastName'>
                <label htmlFor='lastName'>Last Name</label>
                <input type='text' name='lastName' onChange={handleChange} noValidate />
                {errors.lastName.length > 0 && <span className='error'>{errors.lastName}</span>}
              </div>
              <div className='email'>
                <label htmlFor='email'>Email</label>
                <input type='email' name='email' onChange={handleChange} noValidate />
                {errors.email.length > 0 && <span className='error'>{errors.email}</span>}
              </div>
              <div className='password'>
                <label htmlFor='password'>Password</label>
                <input type='password' name='password' onChange={handleChange} noValidate />
                {errors.password.length > 0 && <span className='error'>{errors.password}</span>}
              </div>
              <div className='submit'>
                <button>Create</button>
              </div>
            </form>

          </div>
          <img src="https://pngimg.com/d/audi_PNG1768.png" alt="" className='car_img' />
        </div>

      </div>
    </div>
  )
}

export default RegisterPage;