import { Outlet } from "react-router-dom";

const MagazineComponent = () =>{
    return(
        <div className="myPageComponent">
            <Outlet />
        </div>
    )
}

export default MagazineComponent;