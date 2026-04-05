import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { getMileageListByMember, getMileageListByMemberAndDate } from '../../../../api/mileageApi';
import { formatDateToDot } from '../../../../util/dateUtil';
import { addComma } from '../../../../util/priecUtil';
import Pagination from '../../../Pagination';

const getMileageStatusText = (status) => {
    if (status === 'NO_REDEEM') return '적립';
    if (status === 'REDEEM') return '사용';
    return status;
};

const MileageComponent = () => {
    const navigate = useNavigate();
    const loginState = useSelector(state => state.loginSlice);
    const isLoggedIn = loginState && loginState.email !== '';
    const [searchParams, setSearchParams] = useSearchParams();
    const page = parseInt(searchParams.get('page')) || 0;
    const size = parseInt(searchParams.get('size')) || 10;

    const [mileageData, setMileageData] = useState({});
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [isFiltered, setIsFiltered] = useState(false);

    useEffect(() => {
        if (!isLoggedIn) {
            alert('로그인이 필요합니다.');
            navigate('/member/login');
            return;
        }
        fetchList();
    }, [isLoggedIn, page, size]);

    const fetchList = () => {
        if (isFiltered && startDate && endDate) {
            getMileageListByMemberAndDate(
                loginState.memberId,
                `${startDate}T00:00:00`,
                `${endDate}T23:59:59`,
                page,
                size
            ).then(setMileageData).catch(console.error);
        } else {
            getMileageListByMember(loginState.memberId, page, size)
                .then(setMileageData)
                .catch(console.error);
        }
    };

    const handleSearch = () => {
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
        getMileageListByMemberAndDate(
            loginState.memberId,
            `${startDate}T00:00:00`,
            `${endDate}T23:59:59`,
            0,
            size
        ).then(setMileageData).catch(console.error);
    };

    const handleReset = () => {
        setStartDate('');
        setEndDate('');
        setIsFiltered(false);
        setSearchParams({ page: 0, size });
        getMileageListByMember(loginState.memberId, 0, size)
            .then(setMileageData)
            .catch(console.error);
    };

    return (
        <>
            <h2 className="pageTitle">마일리지</h2>
            <div className="pageContainer">
                <div className="borderSection filter">
                    <strong>총 {mileageData?.totalElements || 0}건</strong>
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
                            <button className="btn black" type="button" onClick={handleSearch}>검색</button>
                            {isFiltered && (
                                <button className="btn" type="button" onClick={handleReset}>초기화</button>
                            )}
                        </div>
                    </div>
                </div>
                <div className="tablePage">
                    <div className="itemTableWrap">
                        <table className="itemTable">
                            <thead className="itemThead">
                                <tr className="itemTr">
                                    <th className="itemDate">일자</th>
                                    <th className="itemInfo">주문번호</th>
                                    <th className="itemMileage">구분</th>
                                    <th className="itemMileage">마일리지</th>
                                </tr>
                            </thead>
                            <tbody className="itemTbody">
                                {mileageData?.content?.length > 0 ? (
                                    mileageData.content.map((item) => (
                                        <tr className="itemTr" key={item.id}>
                                            <td className="itemDate">{formatDateToDot(item.mileageDate)}</td>
                                            <td className="itemInfo">{item.orderId || '-'}</td>
                                            <td className="itemMileage">{getMileageStatusText(item.mileageStatus)}</td>
                                            <td className="itemMileage">
                                                <span style={{ color: item.mileageStatus === 'REDEEM' ? '#e53e3e' : '#2b6cb0' }}>
                                                    {item.mileageStatus === 'REDEEM' ? '-' : '+'}{addComma(item.amount)}P
                                                </span>
                                            </td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={4} className="noDataView">마일리지 적립 및 사용 이력이 없습니다.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                    <div className="itemSubMenu">
                        <Pagination pageInfo={mileageData} />
                    </div>
                </div>
            </div>
        </>
    );
};

export default MileageComponent;
