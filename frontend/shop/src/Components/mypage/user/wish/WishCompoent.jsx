import { Outlet } from "react-router-dom";

const WishCompoent = () => {
  return(
    <div className="myPageComponent">
        <Outlet />
    </div>
  )
}

export default WishCompoent;