import axios from "axios";
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/search`;

/** 검색어로 상품명 & 카테고리명 검색 */
export const searchItems = async (searchWord) => {
    try {
        const res = await axios.get(`${host}/${encodeURIComponent(searchWord)}`, {
            params: { searchWord }
        });
        return res.data;
    } catch (error) {
        throw error;
    }
};
