import React, { useEffect, useState } from 'react';
import './login.scss';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { backendUrl, setToken } from '../../config';

const validEmailRegex = RegExp(
  /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
);

function LoginPage() {
  const [loginError, setLoginError] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState({
    email: '',
    password: '',
  });

 
  const validateForm = (error) => {
    let valid = true;
    Object.values(error).forEach((val) => val.length > 0 && (valid = false));
    return valid;
  };
 
  const navigation = useNavigate();

  const onInputChange = (e) => {
    const { name, value } = e.target;
    let updatedErrors = { ...error };

    switch (name) {
      case 'email':
        updatedErrors.email = value && validEmailRegex.test(value) ? '' : 'Email is not valid!';
        break;
      case 'password':
        updatedErrors.password = value && value.length < 8 ? 'Password must be at least 8 characters long!' : '';
        break;
      default:
        break;
    }

    setError(updatedErrors);
    if (name === 'email') {
      setEmail(value ? value : '');
    } else if (name === 'password') {
      setPassword(value ? value : '');
    }
  };

  useEffect(() =>{
    setToken(null)
  },[])

  const onSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm(error)) {
      alert('Invalid form');
      return;
    }

    try {
      let body = {
        email,
        password
      }
      const response = await axios.post(`${backendUrl}/login`, body);
      const token = response.data.token;
      setToken(token);
      navigation('/main');
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setLoginError('Invalid email or password!');
      } else {
        setLoginError('An error occurred. Please try again later.');
      }
      console.error('Error logging in:', error);
    }
  };

  return (
    <div className='login'>
      <div className='container'>
        <div className='wrapper'>
          <div className='form-wrapper'>
            <form action='post' onSubmit={onSubmit} className='form'>
              <label htmlFor='email'>Email</label>
              <input
                type='text'
                placeholder='email'
                name='email'
                onChange={onInputChange}
              />
              {loginError && <span className='error'>{loginError}</span>}
              {error.email && error.email.length > 0 && <span className='error'>{error.email}</span>}
              <label htmlFor='password'>Password</label>
              <input
                type='password'
                placeholder='password'
                name='password'
                onChange={onInputChange}
              />
              {loginError && <span className='error'>{loginError}</span>}
              {error.password && error.password.length > 0 && <span className='error'>{error.password}</span>}
              <button type='submit'>Login</button>
            </form>
            <p>
              Do you have an Account?{' '}
              <Link to='/register' className='link'>
                Register Now
              </Link>{' '}
            </p>
          </div>
          <img
            src='https://pngimg.com/d/audi_PNG1768.png'
            alt=''
            className='car_img'
          />
        </div>
      </div>
    </div>
  );
}

export default LoginPage;