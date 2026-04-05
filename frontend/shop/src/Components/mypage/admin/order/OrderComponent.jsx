import { Outlet } from "react-router-dom";

const OrderComponent = () =>{
    return(
        <div className="myPageComponent">
            <Outlet />
        </div>
    )
}

export default OrderComponent;