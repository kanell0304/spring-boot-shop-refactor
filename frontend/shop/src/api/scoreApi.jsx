import axios from "axios";
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/scoreList`;

/** 평점 등록 */
export const addScore = async (itemId, memberId, score) => {
    try {
        const res = await axios.post(`${host}/add`, { itemId, memberId, score });
        return res.data;
    } catch (error) {
        console.error('평점 등록 실패:', error.response?.data || error.message);
        throw error;
    }
};

/** 상품 + 회원 기준 평점 조회 */
export const getScoreByItemAndMember = async (itemId, memberId) => {
    try {
        const res = await axios.post(`${host}/getScoreByItemIdAndMemberId`, { itemId, memberId });
        return res.data;
    } catch (error) {
        console.error('평점 조회 실패:', error.response?.data || error.message);
        throw error;
    }
};

/** 상품 기준 평점 목록 조회 */
export const getScoresByItem = async (itemId) => {
    try {
        const res = await axios.post(`${host}/getScoreByItemId`, { itemId });
        return res.data;
    } catch (error) {
        console.error('상품 평점 목록 조회 실패:', error.response?.data || error.message);
        throw error;
    }
};
