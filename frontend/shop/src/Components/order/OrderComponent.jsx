import React, { useState, useEffect } from "react";
import { orderAdd } from "../../api/orderApi.jsx";
import { getSelectList } from "../../api/cartApi.jsx";
import { addComma } from "../../util/priecUtil.jsx";
import { useNavigate } from "react-router-dom";
import AddressSearch from "../AddressSearch.jsx";
import "../../static/css/order.scss";
import "../../static/css/siderbar.scss";

const OrderComponent = ({ memberInfo }) => {
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);
  const [usePayerMemberInfo, setUsePayerMemberInfo] = useState(true);
  const [useRecipientMemberInfo, setUseRecipientMemberInfo] = useState(true);
  const [payer, setPayer] = useState({
    payerName: memberInfo.memberName,
    payerNumber: memberInfo.phoneNumber,
    orderRequest: ""
  });
  const [recipient, setRecipient] = useState({
    recipientName: memberInfo?.memberName || '',
    recipientNumber: memberInfo?.phoneNumber || '',
    zip_code: memberInfo?.address?.zip_code || '',
    default_address: memberInfo?.address?.default_address || '',
    detailed_address: memberInfo?.address?.detailed_address || ''
  });
  const [paymentMethod, setPaymentMethod] = useState("CARD");
  const [mileageStatus, setMileageStatus] = useState("NO_REDEEM");
  const [usingMileage, setUsingMileage] = useState(0);

  useEffect(() => {
    getSelectList(memberInfo.id).then(setCartItems);
  }, [memberInfo]);

  useEffect(() => {
    if (usePayerMemberInfo) {
      setPayer({
        payerName: memberInfo.memberName,
        payerNumber: memberInfo.phoneNumber,
        orderRequest: ""
      });
    } else {
      setPayer({ payerName: "", payerNumber: "", orderRequest: "" });
    }
  }, [usePayerMemberInfo, memberInfo]);

  useEffect(() => {
    if (useRecipientMemberInfo) {
      setRecipient({
        recipientName: memberInfo.memberName,
        recipientNumber: memberInfo.phoneNumber,
        zipCode: memberInfo.address.zip_code,
        defaultAddress: memberInfo.address.default_address,
        detailedAddress: memberInfo.address.detailed_address
      });
    } else {
      setRecipient({
        recipientName: "",
        recipientNumber: "",
        zipCode: "",
        defaultAddress: "",
        detailedAddress: ""
      });
    }
  }, [useRecipientMemberInfo, memberInfo]);

  useEffect(() => {
    if (mileageStatus === 'NO_REDEEM') {
      setUsingMileage('');  // 미사용 선택시 마일리지 입력값 비움
    }
  }, [mileageStatus]);

  const handleAddressChange = (zip, def, detail) => {
    setRecipient(prev => ({ ...prev, zipCode: zip, defaultAddress: def, detailedAddress: detail }));
  };

  const handleMileageFocus = () => {
    setUsingMileage('');
  };
  
  const handleMileageChange = (e) => {
    const input = e.target.value;
  
    // 숫자만 입력 허용
    if (!/^\d*$/.test(input)) {
      return; // 숫자가 아니면 무시
    }
  
    setUsingMileage(input);
  };
  
  const handleMileageValidate = () => {
    const inputNumber = Number(usingMileage);
  
    if (inputNumber < 0) {
      alert("마일리지는 0 이상이어야 합니다.");
      setUsingMileage('0');
      return;
    }
  
    if (inputNumber > memberInfo.stockMileage) {
      alert("보유한 마일리지보다 많이 사용할 수 없습니다.");
      setUsingMileage(memberInfo.stockMileage);
      return;
    }
  
    if (inputNumber % 100 !== 0) {
      alert("마일리지는 100P 단위로만 사용 가능합니다.");
      setUsingMileage('0');
      return;
    }
  
    // 정상적으로 숫자 입력된 경우 유지
  };

  const totalItemAmount = cartItems.reduce((sum, item) => {
    const dcPrice = Math.floor(item.itemPrice * (1 - item.discountRate / 100));
    return sum + (dcPrice + item.optionPrice) * item.qty;
  }, 0);

  const shippingFee = totalItemAmount >= 100000 ? 0 : 3000;

  const appliedMileage = mileageStatus === 'REDEEM' ? usingMileage : 0;

  const finalAmount = totalItemAmount + shippingFee - appliedMileage;

  const rateMap = { BRONZE: 0.01, SILVER: 0.02, GOLD: 0.03, PLATINUM: 0.05 };
  const rate = rateMap[memberInfo.memberShip] || 0;

  const earnedMileage = Math.floor(totalItemAmount * rate);

  console.log(cartItems)

  const handleOrderSubmit = () => {
    const orderData = {
      memberId: memberInfo.id,
      payerName: payer.payerName,
      payerNumber: payer.payerNumber,
      orderRequest: payer.orderRequest,
      paymentMethod,
      mileageStatus,
      usingMileage,
      recipientName: recipient.recipientName,
      recipientNumber: recipient.recipientNumber,
      recipient_zip_code: recipient.zipCode,
      recipient_default_address: recipient.defaultAddress,
      recipient_detailed_address: recipient.detailedAddress,
      selectId: cartItems
      .filter(item => item.optionId !== null && item.optionId !== undefined)
      .map(item => item.optionId)
    };
    console.log("전송할 주문 데이터:", orderData);
    orderAdd(orderData).then(data=>{
      try{
        navigate("/orderComplete", { state: { order: data } });
        alert("등록성공!");
      } catch(error) {
        throw error;
      }
    });
  };
  return (
    <>
    <div className="rightNavLayoutContainer">
      <div className="innerWrap orderContent">
        <h2>ORDER</h2>

        <section className="borderSection orderItemSection">
          <h3>주문상품목록</h3>
          <div className="tablePage">
            <div className="itemTableWrap">
              <table className="itemTable">
                <thead className="itemThead">
                  <tr className="itemTr">
                    <th className="itemInfo">상품</th>
                    <th className="itemPriceInfo">단품가격</th>
                    <th className="itemPriceInfo">주문금액</th>
                    <th className="itemPriceInfo">적립금</th>
                  </tr>
                </thead>
                <tbody className="itemTbody">
                  {cartItems.map((item, idx) =>{
                  const dcPrice = Math.floor(item.itemPrice * (1 - item.discountRate / 100));
                    return(
                    <tr className="itemTr" key={idx}>
                      <td className="itemInfo">
                        <div className="itemImg">
                          <img src={`http://localhost:8081/upload/${item.imageName}`} alt={item.itemName} />
                        </div>
                        <div className="itemInfo">
                          <p>개발 확인용 옵션 아이디 값: {item.optionId}</p>
                          <p>{item.itemName}</p>
                          <p>{item.optionName} : {item.optionValue} (수량 : {item.qty})</p>
                        </div>
                      </td>
                      <td className="itemPriceInfo">{addComma((dcPrice + item.optionPrice))}원</td>
                      <td className="itemPriceInfo">{addComma(((dcPrice + item.optionPrice) * item.qty))}원</td>
                      <td className="itemPriceInfo">{(((dcPrice + item.optionPrice) * item.qty) * rate).toLocaleString()}원</td>
                    </tr>
                  )})}
                </tbody>
              </table>
            </div>
          </div>
        </section>

        <section className="borderSection payerInfoSection">
          <h3>주문자 정보</h3>
          <div className="selectRadioWrap">
            <div className="selectInputWrap radio">
              <input id="MemberInfoTrue" name="info" type="radio" checked={usePayerMemberInfo} onChange={() => setUsePayerMemberInfo(true)} />
              <label htmlFor="MemberInfoTrue">회원 정보와 동일</label>
            </div>
            <div className="selectInputWrap radio">
              <input id="MemberInfoFalse" name="info" type="radio" checked={!usePayerMemberInfo} onChange={() => setUsePayerMemberInfo(false)} />
              <label htmlFor="MemberInfoFalse">주문자 정보 변경</label>
            </div>
          </div>
          <div className="formWrap">
            <div className='inputWrap'>
              <div className="inputTitle"><span className='point'>[필수]</span>주문자명</div>
              <div className="inputBox">
                <input
                  name="payerName"
                  value={usePayerMemberInfo ? memberInfo.memberName : payer.payerName}
                  onChange={e => setPayer({ ...payer, payerName: e.target.value })}
                  placeholder="주문자명을 입력해주세요."
                  type="text"
                />
              </div>
            </div>
            <div className='inputWrap'>
              <div className="inputTitle"><span className='point'>[필수]</span>연락처</div>
              <div className="inputBox">
                <input
                  name="payerNumber"
                  value={usePayerMemberInfo ? memberInfo.phoneNumber : payer.payerNumber}
                  onChange={e => setPayer({ ...payer, payerNumber: e.target.value })}
                  placeholder="'-'를 빼고 입력해주세요."
                  type="text"
                />
              </div>
            </div>
            <div className='inputWrap'>
              <div className="inputTitle">주문시 요청사항</div>
              <div className="inputBox">
                <textarea value={payer.orderRequest} onChange={e => setPayer({ ...payer, orderRequest: e.target.value })} placeholder="주문 요청사항을 적어주세요."></textarea>
              </div>
            </div>
          </div>
        </section>

        <section className="borderSection recipientInfoSection">
          <h3>배송지 정보</h3>
          <div className="selectRadioWrap">
            <div className="selectInputWrap radio">
              <input id="recipientTrue" type="radio" name="recipient" checked={useRecipientMemberInfo} onChange={() => setUseRecipientMemberInfo(true)} />
              <label htmlFor="recipientTrue">회원 정보와 동일</label>
            </div>
            <div className="selectInputWrap radio">
              <input id="recipientFalse" type="radio" name="recipient" checked={!useRecipientMemberInfo} onChange={() => setUseRecipientMemberInfo(false)} />
              <label htmlFor="recipientFalse">새로운 주소</label>
            </div>
          </div>
          <div className="formWrap">
            <div className='inputWrap'>
              <div className="inputTitle"><span className='point'>[필수]</span>수령인</div>
              <div className="inputBox">
                <input
                  value={useRecipientMemberInfo ? memberInfo.memberName : recipient.recipientName}
                  onChange={e => setRecipient({ ...recipient, recipientName: e.target.value })}
                  placeholder="수령인 성함을 입력해주세요."
                  type="text"
                />
              </div>
            </div>
            <div className='inputWrap'>
              <div className="inputTitle"><span className='point'>[필수]</span>연락처</div>
              <div className="inputBox">
                <input
                  value={useRecipientMemberInfo ? memberInfo.phoneNumber : recipient.recipientNumber}
                  onChange={e => setRecipient({ ...recipient, recipientNumber: e.target.value })}
                  placeholder="수령인 연락처를 입력해주세요."
                  type="text"
                />
              </div>
            </div>
            <div className='inputWrap'>
              <div className="inputTitle"><span className='point'>[필수]</span>수령인 주소</div>
                  <AddressSearch onComplete={handleAddressChange} setingAddress={
                    useRecipientMemberInfo ? memberInfo.address : 
                    {zip_code: '',default_address: '',detailed_address: ''}
                  } />
            </div>
          </div>
        </section>

        <section className="borderSection">
          <h3>결제방법</h3>
          <div className="selectRadioWrap">
            <div className="selectInputWrap radio">
              <input id="card" name="payment" type="radio" checked={paymentMethod === 'CARD'} onChange={() => setPaymentMethod("CARD")} />
              <label htmlFor="card">카드결제</label>
            </div>
            <div className="selectInputWrap radio">
              <input id="noBankbook" name="payment" type="radio"  checked={paymentMethod === 'NO_BANKBOOK'} onChange={() => setPaymentMethod("NO_BANKBOOK")} />
              <label htmlFor="noBankbook">무통장 입금</label>
            </div>
          </div>
          {paymentMethod === 'NO_BANKBOOK' && (
            <div className="bankInfo borderSection">
              <strong className="bankInfotitle">무통장 입금안내</strong>
              <p className="bankInfo"><strong>예금주 : 스카디 어패럴</strong> <strong>계좌번호 : 0000-000000-000000</strong></p>
              <ul>
                <li>무통장 입금으로 주문시 <strong>7일 이내에 입금하셔야 상품 준비 및 발송</strong>이 시작됩니다.</li>
                <li>무통장 입금으로 주문시 <strong>7일 이내에 미 입금시 주문이 자동 취소</strong> 됩니다.</li>
                <li>무통장 입금시 <strong>입금 대상과 계좌번호를 꼭 확인</strong>해주세요. 고객님의 <strong>실수로 인한 송금 사고는 책임 지지 않습니다.</strong></li>
                <li>무통장 입금시 송금인의 성함을 꼭 확인해주세요. 주문 취소 및 배송지연이 될 수 있습니다.</li>
              </ul>
            </div>
          )}
        </section>

        <section className="borderSection">
          <h3>마일리지 <span>(보유 마일리지 : {memberInfo.stockMileage})</span></h3>
          <div className="selectRadioWrap">
            <div className="selectInputWrap radio">
              <input id="noRedeem" name="mileage" type="radio" checked={mileageStatus === 'NO_REDEEM'} onChange={() => setMileageStatus("NO_REDEEM")} />
              <label htmlFor="noRedeem">미사용</label>
            </div>
            <div className="selectInputWrap radio">
              <input id="redeem" name="mileage" type="radio" checked={mileageStatus === 'REDEEM'} onChange={() => setMileageStatus("REDEEM")} />
              <label htmlFor="redeem">사용</label>
            </div>
          </div>
          {mileageStatus === 'REDEEM' && (
            <div className='inputWrap'>
              <div className="inputTitle">마일리지 사용 <span className='point'>(100P 단위로 사용 가능)</span></div>
              <div className="inputBox">
              <input
                type="text"
                value={usingMileage}
                onFocus={handleMileageFocus}
                onChange={handleMileageChange}
                onBlur={handleMileageValidate}
                placeholder="사용할 마일리지 포인트 입력"
              />
              </div>
            </div>
          )}
        </section>

        <section className="borderSection">
          <h3>배송안내</h3>
          <div>
            <p>10만원 이상 구매시 배송비는 무료입니다.</p>
            <p>배송 기본 가격은 3,000원입니다.</p>
          </div>
        </section>
      </div>

      
    </div>
    <aside className="itemSidebar order">
        <div className="innerSiedbarWrap">
          <div className="totalPriecInfo">
            <div><strong>주문금액</strong><strong>{addComma(totalItemAmount)}원</strong></div>
            <div><strong>적립 마일리지</strong><strong>{addComma(earnedMileage)}P</strong></div>
            <div><strong>배송비</strong><strong>+{addComma(shippingFee)}원</strong></div>
            {mileageStatus === 'REDEEM' ? (
              <div><strong>사용 마일리지</strong><strong>-{addComma(usingMileage)}P</strong></div>
            ) : (
              <div><strong>사용 마일리지</strong><strong>0P</strong></div>
            )}
          </div>
          <div className="totalPriec">
            <span>총 결제 금액</span><strong>{addComma(finalAmount)}원</strong>
          </div>
          <button className="btn black bigBtn paymentBtn" type="button" onClick={handleOrderSubmit}>
            총 결제 ({addComma(finalAmount)}원)
          </button>
        </div>
      </aside>

  </>
  );
};

export default OrderComponent;
