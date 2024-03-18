import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import RegisterPage from "./pages/register/RegisterPage";
import LoginPage from "./pages/login/LoginPage";
import MainPage from "./pages/main/mainPage";
import { useState } from "react";
import AddCar from "./pages/add/AddCar";
import MainCarsList from "./pages/list/MainCarsList";
import Car from "./pages/car/Car";
import Rentals from "./pages/rentals/Rentals";
import UserPage from "./pages/user/UserPage";
import EditCarForm from "./pages/editCar/EditCar";
import Payment from "./pages/payment/Payment";
import { UserProvider } from "./pages/user/userContext";
import UserEditRole from "./pages/user/UserEditRole";
import MyRentals from "./pages/MyRentals/Rentals";
import Success from "./pages/success/Success";
import MyPayment from "./pages/MyRentals/MyPayment";
import ErrorPage from "./pages/error/Error";




const App = () => {
  const [setIsLoggedIn] = useState(false);


  return (
    <UserProvider>
        <Routes>

          <Route path="/register" element={<RegisterPage />} />
          <Route path="/" element={<LoginPage setLoggedIn={setIsLoggedIn} />} />
          <Route path="/main" element={<MainPage />} />
          <Route path="/rentals" element={<Rentals />} />
          <Route path="/myrentals" element={<MyRentals />} />
          <Route path="/user/me" element={<UserPage />} />
          <Route path="car/add" element={<AddCar />} />
          <Route path="cars/edit/:id" element={<EditCarForm />} />
          <Route path="/cars" element={<MainCarsList />} />
          <Route path="/cars/:id" element={<Car />} />
          <Route path="/payment/:id" element={<Payment />} />
          <Route path="/mypayment/:id" element={<MyPayment />} />
          <Route path="/edit_user_role/:id" element={<UserEditRole />}/>
          <Route path='/success' element={<Success/>} />
          <Route path="*" element={<ErrorPage />} />
        </Routes>

    </UserProvider>


  )
};

export default App;