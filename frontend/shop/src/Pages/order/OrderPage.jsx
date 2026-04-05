import React,{ useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { memberIdSearch } from '../../api/memberApi';
import BasicLayout from "../../layout/BasicLayout";
import OrderComponent from '../../Components/order/OrderComponent';

const OrderPage =()=>{
  const loginState = useSelector(state => state.loginSlice);
  const isLoggedIn = loginState && loginState.email !== '';
  const [memberInfo, setMemberInfo] = useState(null);

  useEffect(() => {
    if (isLoggedIn) {
        memberIdSearch(loginState.memberId).then(res => {
            setMemberInfo(res);
        });
    } else {
        alert("로그인이 필요합니다.");
        setMemberInfo(null);
    }
  }, [isLoggedIn]);

  if (!isLoggedIn || !memberInfo) return null;

    return(
      <BasicLayout>
        <div className="rightNavLayoutWrap">
        {memberInfo &&
          <OrderComponent memberInfo={memberInfo}/>
        }
        </div>
      </BasicLayout>
    )
}
export default OrderPage;