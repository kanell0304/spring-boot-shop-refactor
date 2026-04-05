import axios from "axios";
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const hostEvent = `${API_SERVER_HOST}/api/eventList`;
const hostMagazine = `${API_SERVER_HOST}/api/magazineList`;


/** 이벤트 게시물 등록 */
export const postEvent = async (formData) => {
  try {
    const res = await axios.post(`${hostEvent}/add`, formData, {
      headers: {'Content-Type': 'multipart/form-data'}
    });
    return res.data;
  } catch (error) {
    console.error("이벤트 등록 실패:", error.response?.data || error.message);
    throw error;
  }
};

/** 이벤트 리스트 조회 페이징 (삭제 미포함)*/
export const getEventList = async (page, size) => {
  const p = page ? page : 0;
  const s = size ? size : 5;
  try {
    const res = await axios.get(`${hostEvent}/listPage?page=${p}&size=${s}`);
    return res.data;
  } catch (error) {
    console.error("이벤트 조회 실패:", error.response?.data || error.message);
    throw error;
  }
}

/** 이벤트 리스트 조회 페이징 (삭제 포함)*/
export const getEventListAll = async (page, size) => {
  const p = page ? page : 0;
  const s = size ? size : 5;
  try {
    const res = await axios.get(`${hostEvent}/listPageWithDelFlag?page=${p}&size=${s}`);
    return res.data;
  } catch (error) {
    console.error("이벤트 조회 실패:", error.response?.data || error.message);
    throw error;
  }
}

/** 이벤트 게시물 조회 */
export const getEvent = async (id) => {
  try {
    const res = await axios.get(`${hostEvent}/list/${id}`);
    return res.data;
  } catch (error) {
    console.error("이벤트 조회 실패:", error.response?.data || error.message);
    throw error;
  }
}

/** 이벤트 게시물 수정 */
export const putEvent = async (formData) => {
  try {
    const res = await axios.put(`${hostEvent}/edit`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return res.data;
  } catch (error) {
    console.error("이벤트 수정 실패:", error.response?.data || error.message);
    throw error;
  }
};

/** 이벤트 게시물 삭제 */
export const deleteEvent = async (id) => {
    try {
        const res = await axios.delete(`${hostEvent}/${id}`);
        return res.data;
      } catch (error) {
        console.error("삭제 실패:", error.response?.data || error.message);
        throw error;
      }
}

/** 매거진 게시물 등록 */
export const postMagazine = async (formData) => {
  try {
    const res = await axios.post(`${hostMagazine}/add`, formData, {
      headers: {'Content-Type': 'multipart/form-data'}
    });
    return res.data;
  } catch (error) {
    console.error("매거진 등록 실패:", error.response?.data || error.message);
    throw error;
  }
}

/** 매거진 리스트 조회 페이징 (삭제 미포함)*/
export const getMagazineList = async (page, size) => {
  const p = page ? page : 0;
  const s = size ? size : 5;
  try {
    const res = await axios.get(`${hostMagazine}/listPage?page=${p}&size=${s}`);
    return res.data;
  } catch (error) {
    console.error("매거진 조회 실패:", error.response?.data || error.message);
    throw error;
  }
}

/** 매거진 리스트 조회 페이징 (삭제 포함)*/
export const getMagazineListAll = async (page,size) => {
  const p = page ? page : 0;
  const s = size ? size : 5;
  try {
    const res = await axios.get(`${hostMagazine}/listPageWithDelFlag?page=${p}&size=${s}`);
    return res.data;
  } catch (error) {
    console.error("매거진 조회 실패:", error.response?.data || error.message);
    throw error;
  }
}

/** 메거진 게시물 조회 */
export const getMagazine = async (id) => {
    try {
      const res = await axios.get(`${hostMagazine}/list/${id}`);
      return res.data;
    } catch (error) {
      console.error("매거진 조회 실패:", error.response?.data || error.message);
      throw error;
    }
  }

/** 매거진 게시물 수정 */
export const putMagazine = async (formData) => {
  console.log(formData)
  try {
    const res = await axios.put(`${hostMagazine}/edit`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return res.data;
  } catch (error) {
    console.error("매거진 수정 실패:", error.response?.data || error.message);
    throw error;
  }
}

/** 매거진 게시물 삭제 */
export const deleteMagazine = async (id) => {
    try {
        const res = await axios.delete(`${hostMagazine}/${id}`);
        return res.data;
      } catch (error) {
        console.error("삭제 실패:", error.response?.data || error.message);
        throw error;
      }
}

/** 이벤트 조회수 증가 */
export const incrementEventViewCount = async (id) => {
    try {
        const res = await axios.put(`${hostEvent}/incrementViewCount/${id}`);
        return res.data;
    } catch (error) {
        console.error("이벤트 조회수 증가 실패:", error.response?.data || error.message);
    }
}

/** 매거진 조회수 증가 */
export const incrementMagazineViewCount = async (id) => {
    try {
        const res = await axios.put(`${hostMagazine}/incrementViewCount/${id}`);
        return res.data;
    } catch (error) {
        console.error("매거진 조회수 증가 실패:", error.response?.data || error.message);
    }
}