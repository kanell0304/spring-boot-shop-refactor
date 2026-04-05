import axios from "axios";
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER_HOST;
const host = `${API_SERVER_HOST}/api/delivery`;

/** 배송 상태 변경 */
export const editDeliveryStatus = async (deliveryId, deliveryStatus) => {
    try {
        const res = await axios.put(`${host}/editStatus`, { id: deliveryId, deliveryStatus });
        return res.data;
    } catch (error) {
        console.error('배송 상태 변경 실패:', error.response?.data || error.message);
        throw error;
    }
};
