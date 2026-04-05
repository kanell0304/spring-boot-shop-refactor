import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { Outlet } from "react-router-dom";
import { getCookie } from "../../util/cookieUtil";
import BasicLayout from "../../layout/BasicLayout";
import "../../static/css/shop.scss";
import "../../static/css/siderbar.scss";


const ProductListPage = () => {
  const loginState = useSelector((state) => state.loginSlice);
  const isLoggedIn = loginState && loginState.email !== '';
  const [memberInfo, setMemberInfo] = useState(null);

  useEffect(() => {
    if (isLoggedIn) {
      const info = getCookie("member");
      setMemberInfo(info);
    } else {
      setMemberInfo(null);
    }
  }, [isLoggedIn]);

  return (
    <BasicLayout>
      <div className="rightNavLayoutWrap">
          <Outlet context={{ memberInfo }} />
      </div>
    </BasicLayout>
  );
};

export default ProductListPage;