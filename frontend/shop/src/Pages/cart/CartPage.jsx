import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from 'react-router-dom';
import {
  fetchCartItems,
  clearCartByMemberId,
  deleteSelectedItems,
  addWishlistItem,
  updateCartQty,
  selectItems,
  deleteCartItem
} from "../../api/cartApi";
import { addComma } from '../../util/priecUtil';
import { getMemberById } from '../../api/memberApi';
import { getCookie } from "../../util/cookieUtil";
import BasicLayout from "../../layout/BasicLayout";
import "../../static/css/cart.scss";


const CartPage = () => {
  const loginState = useSelector((state) => state.loginSlice);
  const isLoggedIn = loginState && loginState.email !== '';
  const navigate = useNavigate();
  const [memberInfo, setMemberInfo] = useState(null);
  const [cartItems, setCartItems] = useState([]);
  const [checkedItems, setCheckedItems] = useState([]);
  const [isAllChecked, setIsAllChecked] = useState(false);
  const [selectedSizes, setSelectedSizes] = useState({});

  useEffect(() => {
    if (!isLoggedIn) {
      setMemberInfo(null);
      return;
    }
    const init = async () => {
      try {
        const info = getCookie("member");
        await getMemberById(info.memberId).then(setMemberInfo);
        await loadCartItems();
      } catch (e) {
        setMemberInfo(null);
      }
    };
    init();
  }, [isLoggedIn]);

  const loadCartItems = async () => {
    try {
      const data = await fetchCartItems(loginState.memberId);
      const initialSizes = {};
      data.forEach(item => {
        initialSizes[item.cartId] = item.selectedOptionId || (item.options && item.options[0]?.optionId);
      });
      setSelectedSizes(initialSizes);
      setCartItems(data);
    } catch (error) {
      console.error("장바구니 불러오기 실패:", error);
      setCartItems([]);
      setSelectedSizes({});
    }
  };

  const handleAllCheck = () => {
    setIsAllChecked(!isAllChecked);
    setCheckedItems(isAllChecked ? [] : cartItems.map((item) => item.cartId));
  };

  const handleSingleCheck = (cartId) => {
    setCheckedItems(
      checkedItems.includes(cartId)
        ? checkedItems.filter((id) => id !== cartId)
        : [...checkedItems, cartId]
    );
  };

  const handleSelectDelete = async () => {
    if (checkedItems.length === 0) {
      alert("선택된 상품이 없습니다.");
      return;
    }
    try {
      await deleteSelectedItems(checkedItems);
      loadCartItems();
      setCheckedItems([]);
      setIsAllChecked(false);
      alert("선택 항목 삭제 완료");
    } catch (error) {
      console.error("선택 삭제 실패:", error);
    }
  };

  // 버그 수정: memberId → loginState.memberId
  const handleClearCart = async () => {
    try {
      await clearCartByMemberId(loginState.memberId);
      loadCartItems();
      setCheckedItems([]);
      setIsAllChecked(false);
      alert("장바구니 비움 완료");
    } catch (error) {
      console.error("장바구니 비우기 실패:", error);
    }
  };

  const handleSizeChange = (cartId, event) => {
    setSelectedSizes({ ...selectedSizes, [cartId]: parseInt(event.target.value) });
  };

  // 수량 증가
  const increaseQuantity = async (item) => {
    const newQty = item.qty + 1;

    if (newQty > item.stockQty) {
      alert(`재고 초과! 현재 남은 수량은 ${item.stockQty}개입니다.`);
      return;
    }

    try {
      await updateCartQty(item.cartId, newQty);
      setCartItems(prevItems =>
        prevItems.map(prevItem =>
          prevItem.cartId === item.cartId
            ? { ...prevItem, qty: newQty }
            : prevItem
        )
      );
    } catch (error) {
      console.error("수량 증가 오류:", error);
      alert(error.response?.data?.message || "수량 증가 중 오류가 발생했습니다.");
    }
  };

  // 수량 감소
  const decreaseQuantity = async (item) => {
    if (item.qty > 1) {
      const newQty = item.qty - 1;
      try {
        await updateCartQty(item.cartId, newQty);
        setCartItems(prevItems =>
          prevItems.map(prevItem =>
            prevItem.cartId === item.cartId
              ? { ...prevItem, qty: newQty }
              : prevItem
          )
        );
      } catch (error) {
        console.error("수량 감소 오류:", error);
        alert("수량 감소 중 오류가 발생했습니다.");
      }
    } else {
      alert("수량은 1개보다 작을 수 없습니다.");
    }
  };

  // 버그 수정: memberId 미정의 → loginState.memberId 사용
  const handleAddToWishlist = async (itemId) => {
    try {
      await addWishlistItem(loginState.memberId, itemId);
      alert("관심상품에 등록되었습니다.");
    } catch (error) {
      console.error("관심상품 등록 실패:", error);
      alert("관심상품 등록에 실패했습니다.");
    }
  };

  // 버그 수정: 미정의 함수 구현
  const handleRemoveItem = async (cartId) => {
    try {
      await deleteCartItem(cartId);
      setCartItems(prev => prev.filter(item => item.cartId !== cartId));
      setCheckedItems(prev => prev.filter(id => id !== cartId));
      alert("상품이 삭제되었습니다.");
    } catch (error) {
      console.error("카트 삭제 실패:", error);
      alert("상품 삭제에 실패했습니다.");
    }
  };

  const handleSelectedOrder = () => {
    if (checkedItems.length > 0) {
      const selectedItems = cartItems.filter(item => checkedItems.includes(item.cartId));
      const selectIds = selectedItems.map(item => item.optionId);

      const requestData = {
        memberId: loginState.memberId,
        selectId: selectIds
      };

      selectItems(requestData).then(data => console.log(data));
      navigate('/order', { state: requestData });
    } else {
      alert("선택된 상품이 없습니다.");
    }
  };

  const handleAllOrder = () => {
    if (cartItems.length > 0) {
      const selectIds = cartItems.map(item => item.optionId);

      const requestData = {
        memberId: loginState.memberId,
        selectId: selectIds
      };

      selectItems(requestData).then(data => {
        try {
          alert("주문 아이템 추가 완료");
          navigate('/order');
        } catch (error) {
          throw error;
        }
      });
    } else {
      alert("장바구니에 상품이 없습니다.");
    }
  };

  if (memberInfo === null) return;

  const rateMap = { BRONZE: 0.01, SILVER: 0.02, GOLD: 0.03, PLATINUM: 0.05 };
  const rate = rateMap[memberInfo.memberShip] || 0;

  const totalPrice = cartItems.reduce((sum, item) => {
    const itemTotalPrice = (item.itemPrice + item.optionPrice) * item.qty;
    return sum + itemTotalPrice;
  }, 0);

  return (
    <BasicLayout>
      <div className="cartBox">
        <h2>CART</h2>
        <div className="membership">
          <div className="membershipLevel">{memberInfo.memberShip}</div>
          <p>
            {memberInfo.memberName}은 구매금액의 {rate * 100}% 마일리지로 적립됩니다.
          </p>
        </div>
        <div className="tablePage">
          <div className="itemTableWrap">
            <table className="itemTable">
              <thead className="itemThead">
                <tr className="itemTr">
                  <th className="itemNumber">
                    <div className="selectInputWrap checkbox">
                      <input id="all" type="checkbox" checked={isAllChecked} onChange={handleAllCheck} />
                      <label htmlFor="all"><span className="blind">전체선택</span></label>
                    </div>
                  </th>
                  <th className="itemInfo">상품</th>
                  <th className="itemPriceInfo">단품가격</th>
                  <th className="itemPriceInfo">주문금액</th>
                  <th className="itemPriceInfo">적립금</th>
                  <th className="itemWriter">기타</th>
                </tr>
              </thead>
              <tbody className="itemTbody">
                {cartItems?.length > 0 ? (
                  cartItems.map((item) => {
                    const dcPrice = Math.floor(item.itemPrice * (1 - item.discountRate / 100));
                    return (
                      <tr className="itemTr" key={item.cartId}>
                        <td className="itemNumber">
                          <div className="selectInputWrap checkbox">
                            <input
                              id={`item_${item.cartId}`}
                              type="checkbox"
                              checked={checkedItems.includes(item.cartId)}
                              onChange={() => { handleSingleCheck(item.cartId); }}
                            />
                            <label htmlFor={`item_${item.cartId}`}><span className="blind">선택</span></label>
                          </div>
                        </td>
                        <td className="itemInfo">
                          {item.imageName && (
                            <div className="itemImg">
                              <img src={`http://localhost:8081/upload/${item.imageName}`} alt={item.itemName} className="itemImage" />
                            </div>
                          )}
                          <div className="itemDetailInfo">
                            <span className="itemName">{item.itemName}</span>
                            <span>{item.optionName} : {item.optionValue} {item.optionPrice > 0 && `(+${item.optionPrice}원)`}</span>
                            <div className="quantityControl">
                              <button type="button" onClick={() => decreaseQuantity(item)}>-</button>
                              <span>{item.qty}</span>
                              <button type="button" onClick={() => increaseQuantity(item)}>+</button>
                            </div>
                          </div>
                        </td>
                        <td className="itemPriceInfo">{addComma(dcPrice + item.optionPrice)}원</td>
                        <td className="itemPriceInfo">{addComma((dcPrice + item.optionPrice) * item.qty)}원</td>
                        <td className="itemPriceInfo">{(((dcPrice + item.optionPrice) * item.qty) * rate).toLocaleString()}원</td>
                        <td className="itemWriter">
                          <button onClick={() => handleAddToWishlist(item.itemId)}>관심상품 등록</button>
                          <button onClick={() => handleRemoveItem(item.cartId)}>카트에서 삭제</button>
                        </td>
                      </tr>
                    );
                  })
                ) : (
                  <tr className="itemTr">
                    <td colSpan={6} className="noDataView">장바구니에 담긴 상품이 없습니다.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
        <div className="cartButtons">
          <div className="leftButtons">
            <button onClick={handleAllCheck} className="btn black">전체선택</button>
            <button onClick={handleSelectDelete} className="btn gray">선택삭제</button>
            <button onClick={handleClearCart} className="btn gray">장바구니 비우기</button>
          </div>
          <div className="rightButtons">
            <button onClick={handleSelectedOrder} className="btn line bigBtn">선택주문</button>
            <button onClick={handleAllOrder} className="btn black bigBtn">
              전체주문 ({totalPrice.toLocaleString()}원)
            </button>
          </div>
        </div>
      </div>
    </BasicLayout>
  );
};

export default CartPage;
