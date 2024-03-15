import ItemList from "./ItemList";
 
const List = ({cars =[]}) => {
    return ( 
      <div className="list">
        {cars.map (car =>
          <ItemList key={car.id} {...car}/>)}
      </div> 
    );
}
 
export default List;