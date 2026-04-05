import { NavLink } from "react-router-dom";

const OrderSidebarComponent = () => {
    return(
        <aside className="itemSidebar">
            <div className="innerSiedbarWrap">
                <div>
                    <strong>주문금액</strong>
                    <strong>원</strong>
                </div>
                <div>
                    <strong>마일리지</strong>
                    <strong>P</strong>
                </div>
                <div>
                    <strong>배송비</strong>
                    <strong>+3000원</strong>
                </div>
                <div>
                    <span>총 결제 금액</span>
                    <strong>원</strong>
                </div>
                <button type="button">총 결제(원)</button>
            </div>
        </aside>
    )
}
export default OrderSidebarComponent;