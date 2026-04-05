import React from "react";
import { Outlet } from "react-router-dom";
import BasicLayout from "../../layout/BasicLayout";

import "../../static/css/siderbar.scss"
import "../../static/css/borad.scss";

const EventPage = () => {
  return (
    <BasicLayout>
      <Outlet/>
    </BasicLayout>
  );
};
export default EventPage;