import { useState, useEffect } from "react";
import { useSearchParams, NavLink } from "react-router-dom";
import { addComma } from "../../../../util/priecUtil";
import { getOrderStatusText } from "../../../../util/orderStatus";
import { formatDateToDot } from "../../../../util/dateUtil";
import { getOrderList, orderDelete, editStatus, getOrderListByDatePage } from "../../../../api/orderApi";
import Pagination from "../../../Pagination";
import defaultImg from "../../../../static/images/default.png";

const DELIVERY_STATUS_OPTIONS = [
    { value: 'PENDING', label: '주문 완료' },
    { value: 'PREPARING', label: '상품 준비중' },
    { value: 'SHIPPED', label: '배송 시작' },
    { value: 'OUT_FOR_DELIVERY', label: '배송중' },
    { value: 'DELIVERED', label: '배송 완료' },
    { value: 'DELIVERY_FAILED', label: '배송 실패' },
    { value: 'RETURN_SHIPPING', label: '반품 배송중' },
    { value: 'RETURNED', label: '반품 완료' },
    { value: 'EXCHANGE_SHIPPING', label: '교환상품 배송중' },
    { value: 'EXCHANGED', label: '교환 완료' },
];

const OrderListComponent = () => {
    const [orderData, setData] = useState({});
    const [searchParams, setSearchParams] = useSearchParams();
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [isFiltered, setIsFiltered] = useState(false);

    const page = parseInt(searchParams.get("page")) || 0;
    const size = parseInt(searchParams.get("size")) || 10;

    useEffect(() => {
        fetchList();
    }, [page, size]);

    const fetchList = () => {
        getOrderList(page, size).then(setData);
    };

    const handleDateSearch = () => {
        if (!startDate || !endDate) {
            alert('시작일과 종료일을 모두 입력해주세요.');
            return;
        }
        if (new Date(startDate) > new Date(endDate)) {
            alert('시작일이 종료일보다 늦을 수 없습니다.');
            return;
        }
        setIsFiltered(true);
        setSearchParams({ page: 0, size });
        getOrderListByDatePage(
            `${startDate}T00:00:00`,
            `${endDate}T23:59:59`,
            0,
            size
        ).then(setData).catch(console.error);
    };

    const handleDateReset = () => {
        setStartDate('');
        setEndDate('');
        setIsFiltered(false);
        setSearchParams({ page: 0, size });
        getOrderList(0, size).then(setData);
    };

    const handleOrderDelete = (orderId) => {
        if (confirm("정말로 삭제 하시겠습니까?")) {
            orderDelete(orderId).then(() => fetchList());
        }
    };

    const handleStatusChange = (orderId, newStatus) => {
        if (confirm(`배송 상태를 변경하시겠습니까?`)) {
            editStatus(orderId, newStatus)
                .then(() => fetchList())
                .catch(() => alert('상태 변경에 실패했습니다.'));
        }
    };

    return (
        <>
            <h2 className="pageTitle">주문</h2>
            <div className="pageContainer">
                <div className="borderSection filter">
                    <strong>주문 : {orderData?.totalElements || 0}개</strong>
                    <div className="inputWrap">
                        <div className="inputBox">
                            <input
                                value={startDate}
                                onChange={(e) => setStartDate(e.target.value)}
                                placeholder="시작일"
                                type="date"
                            />
                            ~
                            <input
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                                placeholder="종료일"
                                type="date"
                            />
                            <button className="btn black" type="button" onClick={handleDateSearch}>검색</button>
                            {isFiltered && (
                                <button className="btn" type="button" onClick={handleDateReset}>초기화</button>
                            )}
                        </div>
                    </div>
                </div>

                <div className="tablePage">
                    <div className="itemSubMenu">
                        <Pagination pageInfo={orderData} />
                    </div>
                    <div className="itemTableWrap">
                        <table className="itemTable">
                            <thead className="itemThead">
                                <tr className="itemTr">
                                    <th className="itemDate">주문일</th>
                                    <th className="itemID">주문번호</th>
                                    <th className="itemInfo">상품정보</th>
                                    <th className="itemPriceInfo">적립포인트</th>
                                    <th className="itemPriceInfo">주문금액</th>
                                    <th className="itemState">배송상태</th>
                                    <th className="itemUtil">주문관리</th>
                                </tr>
                            </thead>
                            <tbody className="itemTbody">
                                {orderData?.content?.length > 0 ? (
                                    orderData.content.map((item, index) => {
                                        const displayIndex = orderData.totalElements - (page * size + index);
                                        return (
                                            <tr className="itemTr" key={item.id}>
                                                <td className="itemDate">{formatDateToDot(item.orderDate)}</td>
                                                <td className="itemID">{item.id}</td>
                                                <td className="itemInfo">
                                                    <div className="itemImg">
                                                        <img
                                                            src={
                                                                item.orderItemList[0]?.imageName
                                                                    ? `http://localhost:8081/upload/${item.orderItemList[0].imageName}`
                                                                    : defaultImg
                                                            }
                                                            alt={item.orderItemList[0]?.itemName}
                                                        />
                                                    </div>
                                                    <div className="itemDetailInfo">
                                                        <p>주문 회원 : {item.memberEmail}</p>
                                                        <p>
                                                            {item.orderItemList[0]?.itemName}
                                                            {item.orderItemList?.length > 1 && ` / 외 (${item.orderItemList.length})`}
                                                        </p>
                                                        <NavLink to={`detail/${item.id}`}>주문상세보기</NavLink>
                                                    </div>
                                                </td>
                                                <td className="itemPriceInfo">{addComma(item.addMileageAmount)}P</td>
                                                <td className="itemPriceInfo">{addComma(item.totalAmount)}원</td>
                                                <td className="itemState">
                                                    <select
                                                        value={item.deliveryStatus || ''}
                                                        onChange={(e) => handleStatusChange(item.id, e.target.value)}
                                                        className="statusSelect"
                                                    >
                                                        {DELIVERY_STATUS_OPTIONS.map((opt) => (
                                                            <option key={opt.value} value={opt.value}>
                                                                {opt.label}
                                                            </option>
                                                        ))}
                                                    </select>
                                                </td>
                                                <td className="itemUtil">
                                                    <button
                                                        type="button"
                                                        className="btn gray"
                                                        onClick={() => handleOrderDelete(item.id)}
                                                    >
                                                        주문삭제
                                                    </button>
                                                </td>
                                            </tr>
                                        );
                                    })
                                ) : (
                                    <tr>
                                        <td colSpan={7} className="noDataView">주문이 없습니다.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                    <div className="itemSubMenu">
                        <Pagination pageInfo={orderData} />
                    </div>
                </div>
            </div>
        </>
    );
};

export default OrderListComponent;
