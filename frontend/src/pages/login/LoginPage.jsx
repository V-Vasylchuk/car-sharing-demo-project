import React, { useEffect, useState } from 'react';
import './login.scss';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { setToken } from '../../config';

function LoginPage() {
  const [user, setUser] = useState({ email: '', password: '' });
  const [error, setError] = useState(null);
 
  const navigation = useNavigate();

  const onInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };
  useEffect(() =>{
    setToken(null)
  },[])

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/login', user);
      const token = response.data.token;
      setToken(token);
      navigation('/main');
    } catch (error) {
      setError('Invalid email or password!');
      console.error('Error logging in:', error);
    }
  };

  const { email, password } = user;

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
                value={email}
                name='email'
                onChange={onInputChange}
              />
              <label htmlFor='password'>Password</label>
              <input
                type='password'
                placeholder='password'
                name='password'
                value={password}
                onChange={onInputChange}
              />
              {error && <p className='error'>{error}</p>}
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