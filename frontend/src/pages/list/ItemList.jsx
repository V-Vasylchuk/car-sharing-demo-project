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
        <p className="card-title">{brand}</p>
        <p className="card-desc">{model}</p>
        <p className="card-desc">{type}</p>
      </div>

        <Link to={`/cars/${id}`} className="link_btn" ><button className="btn car_btn button-52">Show more </button>
        </Link>

</div>)
    ;
}

export default ItemList;