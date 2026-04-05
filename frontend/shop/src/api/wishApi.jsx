import axios from "axios";
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/wish`;

/** 관심 상품 추가&삭제 */
export const wishAdd = async (id,itemId)=>{
    const data = {
        memberId:id,
        itemId:itemId
    };
    try {
      const res = await axios.post(`${host}/add`, data, {
        headers: {'Content-Type': 'application/json'}
      });
      return res.data;
    } catch (error) {
      console.error('관심 등록 실패:', error.response?.data || error.message);
      throw error;
    }
}


export const getwishList = async (id, page = 0, size = 10) => {
    try {
        const res = await axios.get(`${host}/page/${id}`, { params: { page, size } });
        return res.data;
    } catch (error) {
        console.error('관심 상품 조회 실패:', error.response?.data || error.message);
        throw error;
    }
}

/** 위시리스트 단건 삭제 */
export const deleteWish = async (wishListId) => {
    try {
        const res = await axios.delete(`${host}/${wishListId}`);
        return res.data;
    } catch (error) {
        console.error('관심 상품 삭제 실패:', error.response?.data || error.message);
        throw error;
    }
}

/** 위시리스트 다중 삭제 */
export const deleteWishMultiple = async (wishListIds) => {
    try {
        const res = await axios.delete(`${host}/multipleDelete`, {
            data: { wishListIds }
        });
        return res.data;
    } catch (error) {
        console.error('관심 상품 다중 삭제 실패:', error.response?.data || error.message);
        throw error;
    }
}