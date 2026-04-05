import { Outlet } from "react-router-dom";

const ReviewComponent = () => {
  return(
    <div className="myPageComponent">
        <Outlet />
    </div>
  )
}

export default ReviewComponent;