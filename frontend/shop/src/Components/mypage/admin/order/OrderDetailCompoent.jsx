import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate, useSearchParams, useParams } from "react-router-dom";
import { getOneOrder } from "../../../../api/orderApi";
import { formatDateToDot } from "../../../../util/dateUtil";
import { addComma, getFormattedPrice } from "../../../../util/priecUtil";
import { getOrderStatusText } from "../../../../util/orderStatus"
import Statement from "../../../statement";

const OrderDetailCompoent = () => {
  const navigate = useNavigate();
  const loginState = useSelector(state => state.loginSlice);
  const isLoggedIn = loginState && loginState.email !== '';
  const { orderId  } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const [ orderData,setData ] = useState({})

  useEffect(() => {
    if (!isLoggedIn) {
      alert("로그인이 필요합니다.");
      navigate("/member/login");
      return;
    }
    getOneOrder(orderId).then(setData);

  }, [isLoggedIn, navigate, orderId]);

  if (!orderData || !orderData.id) return null;

  return(
    <>
    <h2 className="pageTitle">주문 - 상세보기</h2>
    <div className="pageContainer">
      <div>
        <h3>[{getOrderStatusText(orderData.deliveryStatus)}] 입니다.</h3>
        {/* <p>배송이 시작되면  배송지 변경 및 주문취소가 불가능 합니다.</p> */}
      </div>
      <div className="wide">
      {orderData && <Statement memberInfo={isLoggedIn} order={orderData}></Statement>}
      </div>
    </div>
    </>
  )
}

export default OrderDetailCompoent;