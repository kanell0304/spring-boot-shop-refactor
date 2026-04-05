import axios from "axios";
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/category`;

/** 카테고리 생성 */
export const createCategory= async (date)=>{
    try {
      const res = await axios.post(`${host}/add`, date, {
        headers: {'Content-Type': 'application/json'}
      });
      console.log('분류 등록 성공:', res.data);
      return res.data;
    } catch (error) {
      console.error('분류 등록 실패:', error.response?.data || error.message);
      throw error;
    }
}

/** 모든 카테고리 조회 */
export const categoryList= async ()=>{
    try {
      const res = await axios.get(`${host}/list`);
      return res.data;
    } catch (error) {
      console.error('카테고리 조회 실패:', error.response?.data || error.message);
      throw error;
    }
}

/** 카테고리 수정 */
export const categoryModify = async (data) =>{
    try {
        const res = await axios.put(`${host}/edit`, data, {
            headers: {'Content-Type': 'application/json'}
          });
        console.log('분류정보 변경성공:', res.data);
        return res.data;
    } catch (error) {
        console.error('분류정보 변경실패:', error.response?.data || error.message);
        throw error;
    }
}

/** 카테고리 삭제 */
export const categoryDelete = async(id)=>{
    try {
        const res = await axios.delete(`${host}/${id}`);
        console.log('분류삭제 성공:', res.data);
        return res.data;
    } catch (error) {
        console.error('분류삭제 실패:', error.response?.data || error.message);
        throw error;
    }
}

/** 카테고리 조회 */
export const getCategories = async (id)=>{
  try {
    const res = await axios.get(`${host}/${id}`);
    console.log('카테고리 조회 성공:', res.data);
    return res.data;
  } catch (error) {
    console.error('카테고리 조회 실패:', error.response?.data || error.message);
    throw error;
  }
}

