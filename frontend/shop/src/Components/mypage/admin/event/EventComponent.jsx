import { Outlet } from "react-router-dom";

const EventComponent = () =>{
    return(
        <div className="myPageComponent">
            <Outlet />
        </div>
    )
}

export default EventComponent;