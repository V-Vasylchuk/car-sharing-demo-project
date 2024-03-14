import React, { useEffect } from 'react';
import './mainPage.scss';
import TitleBlock from '../../components/main/TitleBlock';
import Rules from '../../components/rules/Rules';
import Footer from '../../components/footer/Footer';
import HeaderPages from '../../components/header/HeaderPages';
import { getToken } from '../../config';
import { useNavigate } from 'react-router-dom';

function MainPage() {
  const navigation = useNavigate();
  useEffect(() => {
    if (getToken() === null) {
        navigation("/")
    }
  },[getToken()])
  return (
    <div className='header'>
      <HeaderPages /> 

    <TitleBlock />
    <Rules />
    <Footer />
    </div>
  )
}

export default MainPage;