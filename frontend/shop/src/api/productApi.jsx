import axios from 'axios';
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/items`;

/** 상품 리스트 불러오기 */
export const productList = async(page,size)=>{
    const p = page ? page : 0;
    const s = size ? size : 5;
    try{
        const res = await axios.get(`${host}/listPage?page=${p}&size=${s}`);
        console.log(res.data);
        return res.data;
    }catch(error){
        throw error;
    }
};

/** 특정 상품 조회 */
export const getProductById = async (id) => {
    const res = await axios.get(`${host}/${id}`);
    return res.data;
};

/** 특정 상품의 옵션 목록 조회 */
export const getItemOptions = async (itemId) => {
    const res = await axios.get(`${host}/itemOption/${itemId}`);
    return res.data;
};

/** 상품 등록 요청 */
export const registerProduct = async ({ itemDTO, categoryId, files }) => {
  const formData = new FormData();

  formData.append("itemDTO", new Blob([JSON.stringify(itemDTO)], { type: "application/json" }));
  formData.append("categoryId", String(categoryId));
  files.forEach(file => {
    formData.append("files", file);
  });

  try {
    const res = await axios.post(`${host}/add`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return res.data;
  } catch (error) {
    console.error("상품 등록 실패:", error.response?.data || error.message);
    throw error;
  }
};

  
/** 상품 수정 */
export const updateProduct = async ({ id, itemDTO, files = [] }) => {
  const formData = new FormData();
  formData.append('itemDTO', new Blob([JSON.stringify(itemDTO)], { type: 'application/json' }));
  files.forEach(file => {
    formData.append('files', file); // ✅ 여러 파일 처리
  });
  try{
    const response = await axios.put(`${host}/modify/${id}`, formData);
    return response.data;
  }catch(error){
    console.log(error);
    throw error;
  }
}

/** 상품삭제 */
export const deleteProduct = async (id) =>{
  console.log(id)
  try{
    const res = await axios.delete(`${host}/${id}`);
    return res.data;
  }catch(error){
    console.log(error);
    throw error;
  }
}