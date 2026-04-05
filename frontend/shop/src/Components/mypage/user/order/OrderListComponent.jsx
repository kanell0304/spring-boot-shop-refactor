import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { NavLink, useNavigate, useSearchParams } from "react-router-dom";
import { getIdOrderList, getOrderListByMemberDatePage } from "../../../../api/orderApi";
import { formatDateToDot } from "../../../../util/dateUtil";
import { addComma } from "../../../../util/priecUtil";
import { getOrderStatusText } from "../../../../util/orderStatus";
import Pagination from "../../../Pagination";
import defaultImg from "../../../../static/images/default.png";

const OrderComponent = () => {
    const navigate = useNavigate();
    const loginState = useSelector(state => state.loginSlice);
    const isLoggedIn = loginState && loginState.email !== '';
    const [searchParams, setSearchParams] = useSearchParams();
    const [orderList, setList] = useState({});
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [isFiltered, setIsFiltered] = useState(false);

    const page = parseInt(searchParams.get("page")) || 0;
    const size = parseInt(searchParams.get("size")) || 10;

    useEffect(() => {
        if (!isLoggedIn) {
            alert("로그인이 필요합니다.");
            navigate("/member/login");
            return;
        }
        if (!isFiltered) {
            getIdOrderList(loginState.memberId, page, size).then(setList);
        }
    }, [isLoggedIn, page, size]);

    const handleDateSearch = () => {
        if (!startDate || !endDate) {
            alert("시작일과 종료일을 모두 입력해주세요.");
            return;
        }
        if (new Date(startDate) > new Date(endDate)) {
            alert("시작일이 종료일보다 늦을 수 없습니다.");
            return;
        }
        setIsFiltered(true);
        setSearchParams({ page: 0, size });
        getOrderListByMemberDatePage(
            loginState.memberId,
            `${startDate}T00:00:00`,
            `${endDate}T23:59:59`,
            0,
            size
        ).then(setList).catch(console.error);
    };

    const handleDateReset = () => {
        setStartDate('');
        setEndDate('');
        setIsFiltered(false);
        setSearchParams({ page: 0, size });
        getIdOrderList(loginState.memberId, 0, size).then(setList);
    };

    return (
        <>
            <h2 className="pageTitle">주문</h2>
            <div className="pageContainer">
                <div className="borderSection filter">
                    <strong>누적 주문 : {orderList?.totalElements || 0}개</strong>
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
                        <Pagination pageInfo={orderList} />
                    </div>
                    <div className="itemTableWrap">
                        <table className="itemTable">
                            <thead className="itemThead">
                                <tr className="itemTr">
                                    <th className="itemDate">주문일</th>
                                    <th className="itemID">주문번호</th>
                                    <th className="itemInfo">주문정보</th>
                                    <th className="itemPriceInfo">마일리지</th>
                                    <th className="itemPriceInfo">결제금액</th>
                                    <th className="itemStatus">주문상태</th>
                                </tr>
                            </thead>
                            <tbody className="itemTbody">
                                {orderList?.content?.length > 0 ? (
                                    orderList.content.map((item, index) => {
                                        return (
                                            <tr className="itemTr" key={item.id}>
                                                <td className="itemDate">{formatDateToDot(item.orderDate)}</td>
                                                <td className="itemID">{item.id}</td>
                                                <td className="itemInfo">
                                                    <div className="itemImg">
                                                        <img
                                                            src={
                                                                item.orderItemList[0]?.imageName?.length > 0
                                                                    ? `http://localhost:8081/upload/${item.orderItemList[0].imageName}`
                                                                    : defaultImg
                                                            }
                                                            alt={item.orderItemList[0]?.itemName}
                                                        />
                                                    </div>
                                                    <div className="itemDetailInfo">
                                                        <NavLink to={`detail/${item.id}`}>주문상세보기</NavLink>
                                                        <p>
                                                            {item.orderItemList[0]?.itemName}
                                                            {item.orderItemList?.length > 1 && ` 외 (${item.orderItemList.length - 1})`}
                                                        </p>
                                                    </div>
                                                </td>
                                                <td className="itemPriceInfo">{addComma(item.addMileageAmount)}P</td>
                                                <td className="itemPriceInfo">{addComma(item.totalAmount)}원</td>
                                                <td className="itemStatus">{getOrderStatusText(item.deliveryStatus)}</td>
                                            </tr>
                                        );
                                    })
                                ) : (
                                    <tr>
                                        <td colSpan={6} className="noDataView">주문 내역이 없습니다.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                    <div className="itemSubMenu">
                        <Pagination pageInfo={orderList} />
                    </div>
                </div>
            </div>
        </>
    );
};

export default OrderComponent;
