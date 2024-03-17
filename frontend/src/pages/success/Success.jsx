import React from 'react'
import { Link } from 'react-router-dom'

function Success() {
  return (
    <div>
      <button className='btn button-52'><Link to='/main'>GO BACK</Link></button>
      <img src="https://i.pinimg.com/564x/4c/e6/4b/4ce64b8335ed4432058c5a4ed284ace8.jpg" alt="" style={{"width" : "70%"}}/>
    </div>
  )
}

export default Success