import { Link } from "react-router-dom";

const ItemList = (props) => {
  const {
    id,
    model,
    brand,
    type,
      presignedUrl
    } = props;
  return (
    <div className="card">
      <div className="card-image">
        <img src={presignedUrl} alt={model} />
      </div>
      <div className="card-content">
        <p className="card-title">{model}</p>
        <p className="card-desc">{brand}</p>
        <p className="card-desc">{type}</p>
      </div>
      <button className="btn car_btn button-52">
        <Link to={`/cars/${id}`} >Show more</Link>
      </button>
    </div>);
}

export default ItemList;