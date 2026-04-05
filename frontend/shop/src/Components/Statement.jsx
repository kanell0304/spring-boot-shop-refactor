
import { addComma, getFormattedPrice } from "../util/priecUtil";
import { formatDateToDot } from "../util/dateUtil";
import { getOrderStatusText} from "../util/orderStatus";
import defaultImg from "../static/images/default.png";
import '../static/css/basic.scss'

const Statement =({memberInfo, order})=>{
    if(!order || !memberInfo) return;


    const deliveryCharge = 3000;
    console.log(order)
    
    return(
        <div className="statementWrap innerWrap">
            <div className="statementInnerWrap">
                <div className="statementBox top">
                    <section>
                        <h3>주문자 정보</h3>
                        <ul>
                            <li>
                                <strong>주문일</strong>
                                <span>{formatDateToDot(order.orderDate)}</span>
                            </li>
                            <li>
                                <strong>주문번호</strong>
                                <span>{order.id}</span>
                            </li>
                            <li>
                                <strong>주문자</strong>
                                <span>{order.payerName}</span>
                            </li>
                            <li>
                                <strong>연락처</strong>
                                <span>{order.payerNumber}</span>
                            </li>
                            <li>
                                <strong>요청사항</strong>
                                <span>{order.orderRequest}</span>
                            </li>
                        </ul>
                    </section>
                    <section>
                        <h3>수령인 정보</h3>
                        <ul>
                            <li>
                                <strong>수령인</strong>
                                <span>{order.recipientName}</span>
                            </li>
                            <li>
                                <strong>연락처</strong>
                                <span>{order.recipientNumber}</span>
                            </li>
                            <li>
                                <strong>주소</strong>
                                <div className="address">
                                    <span>우편번호 : {order.recipient_zip_code}</span>
                                    <span>{order.recipient_default_address}</span>
                                    <span>{order.recipient_detailed_address}</span>
                                </div>
                            </li>
                        </ul>
                    </section>
                    <section>
                        <h3>결제방법</h3>
                        <ul>
                            <li>
                                <strong>결제수단</strong>
                                <span>{order.paymentMethod}</span>
                            </li>
                            <li>
                                <strong>구매자</strong>
                                <span>{order.payerName}</span>
                            </li>
                            <li>
                                <strong>결제기한</strong>
                                <span>{getOrderStatusText(order.orderStatus)}</span>
                            </li>
                        </ul>
                    </section>
                    <section>
                        <h3>상품금액</h3>
                        <ul>
                            <li>
                                <strong>주문금액</strong>
                                <span>{addComma(order.totalAmount-deliveryCharge)}원</span>
                            </li>
                            <li>
                                <strong>배송비</strong>
                                {order.totalAmount >= 100000 ?
                                (
                                    <span>무료</span>
                                ):(
                                    <span>+{addComma(deliveryCharge)}원</span>
                                )}
                            </li>
                        </ul>
                    </section>
                    <section>
                        <h3>마일리지</h3>
                        <ul>
                            <li>
                                <strong>적립 마일리지</strong>
                                <span>
                                {order.addMileageAmount > 0 ? (`+${order.addMileageAmount}P`) : (`0P`)}
                                </span>
                            </li>
                            <li>
                                <strong>사용 마일리지</strong>
                                <span>
                                {order.usingMileag > 0 ? (`0${order.usingMileag}P`) : (`0P`)}
                                </span>
                            </li>
                        </ul>
                    </section>
                    <div className="totalPrice">
                        <strong>총 주문금액</strong>
                        <h3>{addComma(order.totalAmount)}원</h3>
                    </div>
                </div>
                <div className="statementBox">
                    <section>
                        <h3>주문상품</h3>
                        <div className="tablePage">
                            <div className="itemTableWrap">
                                <table className="itemTable">
                                    <thead className="itemThead">
                                        <tr className="itemTr">
                                            <th className="itemInfo">상품정보</th>
                                            <th className="itemPriceInfo">가격</th>
                                        </tr>
                                    </thead>
                                    <tbody className="itemTbody">
                                    {order.orderItemList.map((item)=>{
                                        console.log(item)
                                        return(
                                        <tr key={item.itemId} className="itemTr">
                                            <td className="itemInfo">
                                                <div className="itemImg">
                                                    <img
                                                        src={
                                                            item.imageName
                                                            ? `http://localhost:8081/upload/${item.imageName}`
                                                            : defaultImg
                                                    }
                                                    alt={item.name}
                                                    />
                                                </div>
                                                <div className="itemDetailInfo">
                                                    <p>상품이름</p>
                                                    <p>{item.optionName} : {item.optionValue}</p>
                                                    <p>수량 : {item.qty}</p>
                                                </div>
                                            </td>
                                            <td className="itemPriceInfo">
                                                {item.discountRate > 0 ? (
                                                    <>
                                                    <span className="itemOriginalPrice">
                                                        {getFormattedPrice(item.orderPrice, 0).original}원
                                                    </span>
                                                    <span className="itemPrice">
                                                        {getFormattedPrice(item.orderPrice, item.discountRate).discounted}원
                                                    </span>
                                                    </>
                                                ) : (
                                                    <span className="itemPrice">
                                                    {getFormattedPrice(item.orderPrice, 0).original}원
                                                    </span>
                                                )}
                                            </td>
                                        </tr>
                                        )
                                    })}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    )
}
export default Statement;