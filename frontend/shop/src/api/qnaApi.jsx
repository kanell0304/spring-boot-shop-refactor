import axios from 'axios';
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/qnaList`;

/** QnA 등록
 * { title, writer, content, parentId, memberId, itemId }
 */
export const addQna = async (form) => {
    try {
        const res = await axios.post(`${host}/add`, form);
        return res.data;
    } catch (error) {
        throw error;
    }
};

/** QnA 수정
 * { qnaListId, memberId, title, writer, content, delFlag, qnAListStatus, parentId }
 */
export const modifyQna = async (form) => {
    try {
        const res = await axios.put(`${host}/edit`, form);
        return res.data;
    } catch (error) {
        throw error;
    }
};

/** QnA 삭제 (논리적 삭제)
 * { qnaListId, memberId }
 */
export const deleteQna = async (data) => {
    try {
        const res = await axios.delete(`${host}/delete`, { data });
        return res.data;
    } catch (error) {
        throw error;
    }
};

/** QnA 단건 조회 (삭제 포함) */
export const getOneQna = async (id) => {
    try {
        const res = await axios.get(`${host}/list/${id}`);
        return res.data;
    } catch (error) {
        throw error;
    }
};

/** QnA 단건 조회 (삭제 미포함) */
export const getOneQnaWithDelFlag = async (id) => {
    try {
        const res = await axios.get(`${host}/listWithDelFlag/${id}`);
        return res.data;
    } catch (error) {
        throw error;
    }
};

/** 모든 QnA 조회 (삭제 미포함, 유저용) */
export const qnaList = async (page = 0, size = 10) => {
    try {
        const res = await axios.get(`${host}/listPageWithDelFlag`, {
            params: { page, size }
        });
        return res.data;
    } catch (error) {
        throw error;
    }
};

/** 모든 QnA 조회 (삭제 포함, 관리자용) */
export const qnaListAll = async (page = 0, size = 10) => {
    try {
        const res = await axios.get(`${host}/listPage`, {
            params: { page, size }
        });
        return res.data;
    } catch (error) {
        throw error;
    }
};
