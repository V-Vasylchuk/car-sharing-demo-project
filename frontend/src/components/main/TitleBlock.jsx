import React from 'react';
import './titleBlock.scss';

function TitleBlock() {
  return (
    <div className='title_block__wrapper'>
      <div className="title_block_title">
        <div className="line">
          <p className='title_text'> Share the Ride,  </p>
          <p className='title_text'> Share the Journey: </p>
          <p className='title_text'>Your Ultimate </p>
          <p className='title_text'>Car Sharing</p>
          <p className='title_text'>Destination</p>
        </div>
      </div>
      <div className='title_block_img'>
        <img src="http://udc1018.shiningcorp.com/sh_img/index/main_banner/bg.png" alt="" className='img_bg'/>
        <img className='title_img' src="https://www.freepnglogos.com/uploads/bmw-png/bmw-reviews-and-rating-motor-trend-1.png" alt=""/>
      </div>
    </div>
  )
}

export default TitleBlock;