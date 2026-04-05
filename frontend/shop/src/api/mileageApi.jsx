import axios from "axios";
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/mileage`;

/** 회원 마일리지 이력 조회 (페이징) */
export const getMileageListByMember = async (memberId, page = 0, size = 10) => {
    try {
        const res = await axios.get(`${host}/page/${memberId}`, {
            params: { page, size }
        });
        return res.data;
    } catch (error) {
        console.error('마일리지 조회 실패:', error.response?.data || error.message);
        throw error;
    }
};

/** 회원 + 기간 마일리지 이력 조회 (페이징) */
export const getMileageListByMemberAndDate = async (memberId, startDate, endDate, page = 0, size = 10) => {
    try {
        const res = await axios.get(`${host}/duringMemberPage`, {
            params: { page, size },
            data: { memberId, startDate, endDate },
            headers: { 'Content-Type': 'application/json' }
        });
        return res.data;
    } catch (error) {
        throw error;
    }
};

/** 전체 마일리지 이력 조회 (관리자, 페이징) */
export const getMileageListAll = async (page = 0, size = 10) => {
    try {
        const res = await axios.get(`${host}/listPage`, {
            params: { page, size }
        });
        return res.data;
    } catch (error) {
        console.error('전체 마일리지 조회 실패:', error.response?.data || error.message);
        throw error;
    }
};

/** 마일리지 이력 삭제 */
export const deleteMileage = async (mileageId) => {
    try {
        const res = await axios.delete(`${host}/${mileageId}`);
        return res.data;
    } catch (error) {
        console.error('마일리지 삭제 실패:', error.response?.data || error.message);
        throw error;
    }
};
