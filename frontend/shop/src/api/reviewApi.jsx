import axios from 'axios';
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/reviewList`;

/** 리뷰 등록 */
export const addReview = async (id,form) =>{
    try{
        const res = await axios.post(`${host}/add/${id}`,form, {
            headers: {'Content-Type': 'multipart/form-data'}
        });
    }catch(erorr){
        throw erorr;
    }
}

/** 리뷰 수정 */
export const modifyReview = async (id,form) =>{
    try{
        const res = await axios.post(`${host}/reviewList/edit/${id}`,form, {
            headers: {'Content-Type': 'multipart/form-data'}
        });
    }catch(erorr){
        throw erorr;
    }
}

/** 리뷰 삭제 */
export const deleteReview = async (data) =>{
    try{
        const res = await axios.get(`${host}/delete/`,data);
        return res.data;
    }catch(erorr){
        throw erorr;
    }
}

/** 상품 구매 여부 체크 */
export const purchaseCheck = async (memberId, itemId) => {
    try {
        const res = await axios.get(`${host}/checkPurchaseStatus`, {
            params: {
              memberId,
              itemId
            }
          });
        return res.data;
    } catch (error) {
      throw error;
    }
  };

/** 리뷰 조회 */
export const getOneReview = async (id) =>{
    try{
        const res = await axios.get(`${host}/list/${id}`);
        return res.data;
    }catch(erorr){
        throw erorr;
    }
}

/** 모든 리뷰 조회 */
export const reviewList = async () =>{
    try{
        const res = await axios.get(`${host}/listPage/${id}?page=0&size=10`);
        return res.data;
    }catch(erorr){
        throw erorr;
    }
}

/** 상품의 모든 리뷰 조회*/
export const getProductReviewList = async (id) =>{
    try{
        const res = await axios.get(`${host}/listPageByItemId/${id}?page=0&size=10`);
        console.log(res.data)
        return res.data;
    }catch(erorr){
        throw erorr;
    }
}

/** (관리자용) 모든 리뷰 조회 */
export const reviewListAll = async () =>{
    try{
        const res = await axios.get(`${host}/listPageWithDelFlag?page=0&size=10`);
        return res.data;
    }catch(erorr){
        throw erorr;
    }
}

/** (관리자용)상품의 모든 리뷰 조회*/
export const productReviewListAll = async (id) =>{
    try{
        const res = await axios.get(`${host}/listPageByItemId/${id}?page=0&size=10`);
        return res.data;
    }catch(erorr){
        throw erorr;
    }
}
