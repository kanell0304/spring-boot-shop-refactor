import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { Outlet } from "react-router-dom";
import { memberIdSearch } from '../../api/memberApi';
import BasicLayout from "../../layout/BasicLayout";
import "../../static/css/shop.scss";
import "../../static/css/siderbar.scss";

const ProductPage = () => {
  const loginState = useSelector((state) => state.loginSlice);
  const isLoggedIn = loginState && loginState.email !== '';
  const [memberInfo, setMemberInfo] = useState(null);


  useEffect(() => {
    if (isLoggedIn) {
      memberIdSearch(loginState.memberId).then(res => {
        setMemberInfo(res);
      });
    } else {
      setMemberInfo(null);
    }
  }, [isLoggedIn, loginState.memberId]); // memberId도 의존성에 추가

  return (
    <BasicLayout>
      <div className="rightNavLayoutWrap">
          <Outlet context={ { memberInfo }  } />
      </div>
    </BasicLayout>
  );
};

export default ProductPage;