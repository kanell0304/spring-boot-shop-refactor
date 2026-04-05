import { Outlet } from "react-router-dom";

const MileageComponent = () => {
  return(
    <div className="myPageComponent">
        <Outlet />
    </div>
  )
}

export default MileageComponent;