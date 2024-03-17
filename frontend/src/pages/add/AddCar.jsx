import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import HeaderPages from '../../components/header/HeaderPages';
import './add.scss';
import axios from 'axios';
import {backendUrl, getBearerToken, getToken} from '../../config';

function AddCar() {
    const [errors, setErrors] = useState({
        model: '',
        brand: '',
        inventory: '',
        dailyFee: '',
    });

    const [car, setCar] = useState({
        model: '',
        brand: '',
        inventory: '',
        dailyFee: '',
        type: 'SEDAN'
    });

    const [carFile, setCarFile] = useState(null);

    const navigation = useNavigate();

    useEffect(() => {
        if (getToken() === null) {
            navigation("/")
        }
    }, [getToken()]);

    const onInputChange = (e) => {
        const {name, value} = e.target;
        setCar({...car, [name]: value});

        if (name === 'inventory' || name === 'dailyFee') {
            if (!/^\d+(\.\d{1,2})?$/.test(value)) {
                setErrors({...errors, [name]: `${name.charAt(0).toUpperCase() + name.slice(1)} must be a number`});
            } else {
                setErrors({...errors, [name]: ''});
            }
        } else {
            setErrors({
                ...errors,
                [name]: value.length < 2 || value.length > 20 ? `${name.charAt(0).toUpperCase() + name.slice(1)} must be between 2 and 20 characters` : ''
            });
        }

        setCar({...car, [name]: value});
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        let hasError = false;
        Object.values(car).forEach(value => {
            if (value.trim().length < 1) {
                hasError = true;
            }
        });

        if (hasError) {
            setErrors({
                model: car.model.trim().length < 1 ? 'Please fill in the Model field' : '',
                brand: car.brand.trim().length < 1 ? 'Please fill in the Brand field' : '',
                inventory: car.inventory.trim().length < 1 ? 'Please fill in the Inventory field' : '',
                dailyFee: car.dailyFee.trim().length < 1 ? 'Please fill in the Daily Fee field' : ''
            });
            return;
        }

        try {
            const formData = new FormData();
            formData.append('car', JSON.stringify(car));
            formData.append('file', carFile);

            const config = {
                headers: {
                    'Authorization': getBearerToken(),
                    'Content-Type': 'multipart/form-data'
                }
            };

            const response = await axios.post(`${backendUrl}/cars`, formData, config);
            setCar(response.data);
            console.log(response.data);
            navigation("/cars");
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            const form = event.target.form;
            const index = Array.prototype.indexOf.call(form, event.target);
            const nextField = form.elements[index + 1];
            if (nextField) {
                nextField.focus();
            } else {
                onSubmit(event);
            }
        }
    };

    const handleFileChange = (e) => {
        setCarFile(e.target.files[0]);
    };


    return (
        <div className='addCar'>
            <HeaderPages/>
            <div className='container_add'>
                <h1 className='container__title'>Add a New Car</h1>
                <div className='wrapper_add'>
                    <div className='form-wrapper'>
                        <form onSubmit={onSubmit} noValidate>
                            <div className='brand'>
                                <label htmlFor='brand'>Brand</label>
                                <input type='text' name='brand' value={car.brand} onChange={e => onInputChange(e)}
                                       onKeyPress={handleKeyPress} noValidate required/>
                                {errors.brand && <span className='error'>{errors.brand}</span>}
                            </div>
                            <div className='model'>
                                <label htmlFor='model'>Model</label>
                                <input type='text' name='model' value={car.model} onChange={onInputChange}
                                       onKeyPress={handleKeyPress}
                                       noValidate required/>
                                {errors.model && <span className='error'>{errors.model}</span>}
                            </div>
                            <div className='photo'>
                                <label htmlFor='photo'>Add Photo</label>
                                <input type='file' name='photo' className='photo_input' accept="image/*"
                                       onChange={handleFileChange}/>
                            </div>
                            <div className='brand'>
                                <label htmlFor='inventory'>Inventory</label>
                                <input type='text' name='inventory' value={car.inventory}
                                       onChange={e => onInputChange(e)}
                                       onKeyPress={handleKeyPress} noValidate required/>
                                {errors.inventory && <span className='error'>{errors.inventory}</span>}
                            </div>
                            <div className='brand'>
                                <label htmlFor='dailyFee'>Daily Fee</label>
                                <input type='text' name='dailyFee' value={car.dailyFee} onChange={e => onInputChange(e)}
                                       onKeyPress={handleKeyPress} noValidate required/>
                                {errors.dailyFee && <span className='error'>{errors.dailyFee}</span>}
                            </div>
                            <div className='carType'>
                                <label htmlFor='carType'>Car Type</label>
                                <select name='carType' value={car.type}
                                        onChange={(e) => setCar({...car, type: e.target.value})}
                                        onKeyPress={handleKeyPress}>
                                    <option value='SEDAN'>SEDAN</option>
                                    <option value='SUV'>SUV</option>
                                    <option value='HATCHBACK'>HATCHBACK</option>
                                    <option value='UNIVERSAL'>UNIVERSAL</option>
                                </select>
                            </div>
                            <div className='submit'>
                                <button type='submit'>Add Car</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AddCar;
