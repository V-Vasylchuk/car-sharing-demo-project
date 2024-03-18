import React, { useEffect, useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { CiMenuBurger } from "react-icons/ci";
import './header.scss';
import User from '../../pages/user/User';
import { useUser } from '../../pages/user/userContext';
import axios from 'axios';
import { backendUrl, getBearerToken} from '../../config';



function HeaderPages() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const { user, setUser } = useUser();

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

  const handleMenuToggle = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <div className="header__wrapper">
      <div className='header__logo'>
        <button className='btn__main'>
          <Link to='/main' className='text'>
            <p className='logo text'>CAR</p>
          </Link>
        </button>
      </div>

      <button className={`menu-icon ${isMenuOpen ? 'active' : ''}`} onClick={handleMenuToggle}>
        <CiMenuBurger />
      </button>
      {user.role === 'MANAGER' ? (
        <div className={`nav__wrapper ${isMenuOpen ? 'active' : ''}`}>
          <button className="close-menu" onClick={handleMenuToggle}>X</button>
          <ul className="nav-options">
            <li className="option">
              <NavLink to="/car/add" className="text" activeclassname="active" >ADD CAR</NavLink>
            </li>
            <li className="option">
              <NavLink  to="/cars" className="text" activeclassname="active">CARS</NavLink>
            </li>
            <li className="option">
              <NavLink to="/rentals" className="text" activeclassname="active" >RENTALS</NavLink>
            </li>
            <li className="option">
              <User />
            </li>
          </ul>
        </div>
      ) : (
        <div className={`nav__wrapper ${isMenuOpen ? 'active' : ''}`}>
          <button className="close-menu" onClick={handleMenuToggle}>X</button>
          <ul className="nav-options">
            <li className="option">
              <NavLink to="/cars" className="text" activeclassname="active">CARS</NavLink>
            </li>
            <li className="option">
              <NavLink to="/myrentals" className="text" activeclassname="active" >MY RENTAL</NavLink>
            </li>
            <li className="option">
              <User />
            </li>
          </ul>
        </div>
      )}

    </div>
  );
}

export default HeaderPages;
