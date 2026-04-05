import axios from "axios";
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const CART_HOST = `${API_SERVER_HOST}/api/cart`;
const WISHLIST_HOST = `${API_SERVER_HOST}/api/wish/add`;

export const fetchCartItems = async (memberId) => {
  try {
    const response = await axios.get(`${CART_HOST}/${memberId}`);
    return response.data;
  } catch (error) {
    console.error("장바구니 조회 실패:", error);
    throw error;
  }
};

export const addCartItem = async (cartData) => {
  try {
    const response = await axios.post(`${CART_HOST}/add`, cartData);
    return response.data;
  } catch (error) {
    console.error("장바구니 등록 실패:", error);
    throw error;
  }
};

export const clearCartByMemberId = async (memberId) => {
  try {
    await axios.delete(`${CART_HOST}/${memberId}`);
  } catch (error) {
    console.error("회원 ID 기준 장바구니 삭제 실패:", error);
    throw error;
  }
};

export const deleteSelectedItems = async (cartIds) => {
  try {
    await axios.delete(`${CART_HOST}/multipleDelete`, { data: cartIds });
  } catch (error) {
    console.error("선택 삭제 실패:", error);
    throw error;
  }
};

export const deleteCartItem = async (cartId) => {
  try {
    await axios.delete(`${CART_HOST}/deleteItem`, { data: { cartId } });
  } catch (error) {
    console.error("상품 삭제 실패:", error);
    throw error;
  }
};

export const addWishlistItem = async (memberId, itemId) => {
  try {
    await axios.post(WISHLIST_HOST, { memberId, itemId });
  } catch (error) {
    console.error("관심상품 등록 실패:", error);
    throw error;
  }
};

// 장바구니 카트
export const updateQty = async (optionId, changeQty) => {
  const response = await axios.post(`${CART_HOST}/updateQty`, null, {
    params: {
      optionId: optionId,
      changeQty: changeQty
    }
  });
  return response.data;
};

// 장바구니 수량 수정
export const updateCartQty = async (cartId, newQty) => {
  const response = await axios.post(`${CART_HOST}/updateCartQty`, null, {
    params: {
      cartId: cartId,
      newQty: newQty
    }
  });
  return response.data;
};

// 주문으로 넘길 상품들 체크해서 응답
export const selectItems = async (data) => {
  try {
    const res = await axios.post(`${CART_HOST}/editSelectList`, data);
    return res.data;
  } catch (error) {
    throw error;
  }
};

// 회원의 주문 아이템 리스트 불러오기
export const getSelectList = async (memberId) => {
  try {
    const res = await axios.get(`${CART_HOST}/checkItem/${memberId}`);
    return res.data;
  } catch (error) {
    throw error;
  }
};
