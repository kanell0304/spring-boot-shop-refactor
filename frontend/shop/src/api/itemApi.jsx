import axios from 'axios';
import jwtAxios from "../util/jwtUtil"; 
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const itemHost = `${API_SERVER_HOST}/api/items`; 

/** 상품 목록 조회 */
export const fetchItems = async (category, page = 1, sortBy = 'NEWEST', size = 10) => {
  console.log('상품 목록 조회 요청 (카테고리:', category, ', 페이지:', page, ', 정렬:', sortBy, ', 사이즈:', size, ')');

  try {
    const res = await axios.get(`${itemHost}/listPage`, {
      params: {
        category: category,
        page: page - 1,
        sortBy: sortBy,
        size: size,
      },
    });
    console.log('상품 목록 조회 성공:', res.data);
    return res.data;
  } catch (error) {
    console.error('상품 목록 조회 실패:', error.response?.data || error.message);
    throw error; 
  }
};

/** 특정 상품 상세 정보 조회 */
export const fetchItemDetail = async (itemId) => {
  console.log('상품 상세 정보 조회 요청 (ID:', itemId, ')');

  try {
    const res = await axios.get(`${itemHost}/${itemId}`);
    console.log(`상품 ID ${itemId} 상세 정보 조회 성공:`, res.data);
    return res.data;
  } catch (error) {
    console.error(`상품 ID ${itemId} 상세 정보 조회 실패:`, error.response?.data || error.message);
    throw error;
  }
};

/** 상품 추가 */
export const addItem = async (itemData) => {
  console.log('상품 추가 요청 데이터:', itemData);

  try {
    const res = await jwtAxios.post(`${itemHost}/add`, itemData); 
    return res.data;
  } catch (error) {
    console.error('상품 추가 실패:', error.response?.data || error.message);
    throw error;
  }
};

/** 상품 정보 수정 */
export const updateItem = async (itemId, itemData) => {
  console.log(`상품 ID ${itemId} 정보 수정 요청 데이터:`, itemData);

  try {
    const res = await jwtAxios.put(`${itemHost}/${itemId}`, itemData);
    console.log(`상품 ID ${itemId} 정보 수정 성공:`, res.data);
    return res.data;
  } catch (error) {
    console.error(`상품 ID ${itemId} 정보 수정 실패:`, error.response?.data || error.message);
    throw error;
  }
};

/** 상품 삭제 */
export const deleteItem = async (itemId) => {
  console.log('상품 삭제 요청 ID:', itemId);

  try {
    const res = await jwtAxios.delete(`${itemHost}/${itemId}`); 
    console.log(`상품 ID ${itemId} 삭제 성공:`, res.data);
    return res.data;
  } catch (error) {
    console.error(`상품 ID ${itemId} 삭제 실패:`, error.response?.data || error.message);
    throw error;
  }
};