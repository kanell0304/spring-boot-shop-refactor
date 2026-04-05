import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Statement from "../Statement.jsx"
import "../../static/css/order.scss";

const OrderComponent = ({ memberInfo, order }) => {
  if(!order || !memberInfo) return;

  return (
    <div className="odrCompleteWrap">
      <div className="completeTitle">
          <h2>YOUR ORDER IS COMPLETE</h2>
          <p>주문이 완료되었습니다.</p>
      </div>
      <Statement memberInfo={memberInfo} order={order}></Statement>
      <Link className="btn black big" to={"/"}>홈으로 이동</Link>
    </div>
  );
};

export default OrderComponent;
