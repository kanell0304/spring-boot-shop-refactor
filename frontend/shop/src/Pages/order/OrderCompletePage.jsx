import React,{ useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useSelector } from "react-redux";
import { memberIdSearch } from '../../api/memberApi';
import BasicLayout from "../../layout/BasicLayout";
import OrderCompleteComponent from '../../Components/order/OrderCompleteComponent';

const OrderCompletePage = () =>{
    const loginState = useSelector(state => state.loginSlice);
    const isLoggedIn = loginState && loginState.email !== '';
    const [memberInfo, setMemberInfo] = useState(null);
    const location = useLocation();
    const { order } = location.state || {};

    useEffect(() => {
        if (isLoggedIn) {
            memberIdSearch(loginState.memberId).then(res => {
                setMemberInfo(res);
            });
        } else {
            alert("로그인이 필요합니다.");
            setMemberInfo(null);
        }
        }, [isLoggedIn, order]);


    if (!order) {
        return <div>주문 정보가 없습니다.</div>;
    }

    if (!isLoggedIn || !memberInfo) return null;

    return(
        <BasicLayout>
            <OrderCompleteComponent memberInfo={memberInfo} order={order}/>
        </BasicLayout>
    )
}
export default OrderCompletePage;