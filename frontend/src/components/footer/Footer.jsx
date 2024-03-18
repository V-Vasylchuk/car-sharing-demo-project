import React from 'react';
import './footer.scss';
import { FaGithub, FaPhone } from "react-icons/fa";
import { IoMail } from "react-icons/io5";
import { Link } from 'react-router-dom';
import { FaMapMarkerAlt } from "react-icons/fa";
import { SlSocialFacebook } from "react-icons/sl";
import { CiLinkedin, CiTwitter } from 'react-icons/ci';

function Footer() {
  return (
    <div className="footer-distributed">
    <div className="footer-left">

      <h3>CAR</h3>

      <p className="footer-links">
        <Link to="/main" className="link-1"></Link>
        
        <Link to="/car/add">ADD CAR</Link>
      
        <Link to="/cars">CARS</Link>
      </p>

      <p className="footer-company-name"> © 2024</p>
    </div>

    <div className="footer-center">

      <div>
        <FaMapMarkerAlt className="fa fa-map-marker"></FaMapMarkerAlt>
        <p><span></span> Lviv</p>
      </div>

      <div>
        <FaPhone className="fa fa-phone"></FaPhone>
        <p>+3806754343</p>
      </div>

      <div>
        <IoMail className="fa fa-envelope"></IoMail>
        <p><a href="mailto:support@company.com">support@company.com</a></p>
      </div>

    </div>

    <div className="footer-right">

      <p className="footer-company-about">
        <span>About the company</span>
        Lorem ipsum dolor sit amet, consectateur adispicing elit. Fusce euismod convallis velit, eu auctor lacus vehicula sit amet.
      </p>

      <div className="footer-icons">

        <a href="#"><SlSocialFacebook className="fa fa-facebook"></SlSocialFacebook></a>
        <a href="#"><CiTwitter className="fa fa-twitter"></CiTwitter></a>
        <a href="#"><CiLinkedin className="fa fa-linkedin"></CiLinkedin></a>
        <a href="#"><FaGithub className="fa fa-github"></FaGithub></a>

      </div>

    </div>
</div>
  )
}

export default Footer