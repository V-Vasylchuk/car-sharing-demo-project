import {Link, useNavigate, useParams} from "react-router-dom";

import {useState, useEffect} from "react";
import axios from 'axios'
import './car.scss'
import {backendUrl, getBearerToken, getToken} from "../../config";
import {useUser} from "../user/userContext";


const Car = () => {
    const {id} = useParams();
    const {user, setUser} = useUser();
    const [car, setCar] = useState({
        model: '',
        dailyFee: '',
        brand: '',
        presignedUrl: '',
        type: '',
        inventory: '',
    });
    const navigation = useNavigate();

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

    useEffect(() => {
        if (getToken() === null) {
            navigation("/")
        }
    }, [getToken()]);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            await axios.request({
                headers: {
                    Authorization: getBearerToken()
                },
                method: "GET",
                url: `${backendUrl}/cars/${id}`
            }).then(response => {
                setCar(response.data);
            });

        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };


    const onDelete = async (e) => {
        try {
            await axios.request({
                headers: {
                    Authorization: getBearerToken()
                },
                method: "DELETE",
                url: `${backendUrl}/cars/${id}`,
                data: car
            }).then(response => {
                setCar([response.data]);
                alert(`You really want delete ${car.model}?`)
                navigation("/cars")
            });
        } catch (error) {
            console.error('Error deleting car:', error);
            if (error.response && error.response.status === 500) {
                alert('Cannot delete the car because it is associated with rentals.');
            } else {
                alert('Cannot delete the car because it is associated with rentals.');
            }
        }
    }


    const {
        model,
        dailyFee,
        brand,
        presignedUrl,
        type,
        inventory
    } = car;


    return (
        <div className="car">
            <img className="bg_img"
                 src="https://i.pinimg.com/originals/c8/c4/9c/c8c49c36fe2cafc94f3fb55cfea141a1.jpg"
                 alt=""/>
            <Link to="/cars">
                <button className="btn button-52">Go Back</button>
            </Link>
            <div className="car_wrapper">
                <div className="car_img"><img src={presignedUrl} alt=''/></div>
                <div className="car_info">
                    <div className="car_info_text">
                        <h2 className="text_title"><span
                            className="text_title_s">Brand:</span> {brand}</h2>
                        <h3 className="text_title"><span
                            className="text_title_s">Model:</span> {model}</h3>
                        <p className="text_title"><span
                            className="text_title_s">DailyFee:</span> {dailyFee}</p>
                        <p className="text_title"><span className="text_title_s">Type:</span> {type}
                        </p>
                        <p className="text_title"><span
                            className="text_title_s">Inventory:</span> {inventory}</p>
                    </div>
                    {user.role === 'MANAGER' ? (
                        <div className="car_info_btn">
                            <Link to={`/cars/edit/${id}`}>
                                <button className="btn car_btn button-52">EDIT</button>
                            </Link>
                            <button className="btn car_btn button-52"
                                    onClick={() => onDelete(car.id)}>DELETE
                            </button>
                            <Link to={`/payment/${id}`}>
                                <button className="btn car_btn button-52">ORDER</button>
                            </Link>
                        </div>
                    ) : (
                        <div className="car_info_btn">
                            <Link to={`/mypayment/${id}`}>
                                <button className="btn car_btn button-52">ORDER</button>
                            </Link>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Car;