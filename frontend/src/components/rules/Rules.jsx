import React from 'react';
import './rules.scss'

function Rules() {
  return (
    <div className='container_rules'>

      <div className="content">
        <div className="bg_img_rules"></div>
        <h2>Rules rent auto</h2>
        <ul>
          <li className='li_rules'> Minimum Age: Most rental companies require drivers to be at least 21 years old, and some may require drivers to be 25 or older.</li>
          <li className='li_rules'>Credit Card: A valid credit card is often required for payment and as a security deposit. Debit cards may be accepted, but policies vary by rental company.</li>
          <li className='li_rules'>Driver's License: You must have a valid driver's license. International renters may need an International Driving Permit (IDP) in addition to their home country driver's license.</li>
          <li className='li_rules'>Insurance: Basic insurance coverage is usually included in the rental price, but additional coverage may be available for purchase.</li>
          <li className='li_rules'>Fuel Policy: Most rental companies provide the car with a full tank and require you to return it with a full tank. Some companies offer pre-purchase options for fuel.</li>
          <li className='li_rules'>Mileage Limit: Some rentals have a limit on the number of miles you can drive per day or per rental period. Additional fees may apply for exceeding the limit.</li>
          <li className='li_rules'>Vehicle Inspection: Before accepting the car, inspect it for any existing damage and ensure that any scratches or dents are noted on the rental agreement to avoid being charged for them later.</li>
        </ul>
      </div>
    </div>
  )
}

export default Rules