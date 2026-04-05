import React from "react";
import { Outlet } from "react-router-dom";
import BasicLayout from "../../layout/BasicLayout";

import "../../static/css/siderbar.scss"
import "../../static/css/borad.scss";

const EventDetailPage = () => {
  return (
    <BasicLayout>
      <Outlet></Outlet>
    </BasicLayout>
  );
};

export default EventDetailPage;
