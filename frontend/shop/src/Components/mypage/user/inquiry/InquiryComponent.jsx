import { Outlet } from "react-router-dom";

const InquiryComponent = () => {
  return(
    <div className="myPageComponent">
        <Outlet />
    </div>
  )
}

export default InquiryComponent;