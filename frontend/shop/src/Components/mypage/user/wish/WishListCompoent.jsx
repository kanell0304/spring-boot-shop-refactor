import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { getwishList, deleteWish } from '../../../../api/wishApi';
import { getFormattedPrice } from '../../../../util/priecUtil';
import { formatDateToDot } from '../../../../util/dateUtil';
import Pagination from '../../../Pagination';
import defaultImg from '../../../../static/images/default.png';

const WishListComponent = () => {
    const navigate = useNavigate();
    const loginState = useSelector(state => state.loginSlice);
    const isLoggedIn = loginState && loginState.email !== '';
    const [searchParams, setSearchParams] = useSearchParams();
    const page = parseInt(searchParams.get('page')) || 0;
    const size = parseInt(searchParams.get('size')) || 10;

    const [wishData, setData] = useState({});

    useEffect(() => {
        if (!isLoggedIn) {
            alert('로그인이 필요합니다.');
            navigate('/member/login');
            return;
        }
        fetchList();
    }, [isLoggedIn, page, size]);

    const fetchList = () => {
        getwishList(loginState.memberId, page, size).then(setData).catch(console.error);
    };

    const handleDelete = (wishListId) => {
        if (confirm('관심 상품을 해제하시겠습니까?')) {
            deleteWish(wishListId)
                .then(() => fetchList())
                .catch(() => alert('관심 해제에 실패했습니다.'));
        }
    };

    const toItemDetailPage = (itemId) => {
        console.log(itemId);
        navigate(`/product/detail/${itemId}`)
    }

    return (
        <>
            <h2 className="pageTitle">관심상품</h2>
            <div className="pageContainer">
                <div className="tablePage">
                    <div className="itemTableWrap">
                        <table className="itemTable">
                            <thead className="itemThead">
                                <tr className="itemTr">
                                    <th className="itemNumber">번호</th>
                                    <th className="itemInfo">상품</th>
                                    <th className="itemDiscountRate">할인율</th>
                                    <th className="itemPriceInfo">가격</th>
                                    <th className="itemTotalScore">평점</th>
                                    <th className="itemDate">등록일</th>
                                    <th className="itemWish">관심</th>
                                </tr>
                            </thead>
                            <tbody className="itemTbody">
                                {wishData?.content?.length > 0 ? (
                                    wishData.content.map((item, index) => {
                                        const displayIndex = wishData.totalElements - (page * size + index);
                                        return (
                                            <tr className="itemTr" key={item.id}>
                                                <td className="itemNumber">{displayIndex}</td>
                                                <td className="itemInfo" onClick={() => toItemDetailPage(item.itemId)}>
                                                    <div className="itemImg">
                                                        <img
                                                            src={
                                                                item.itemImage?.length > 0
                                                                    ? `http://localhost:8081/upload/${item.itemImage}`
                                                                    : defaultImg
                                                            }
                                                            alt={item.itemName}
                                                        />
                                                    </div>
                                                    <div className="itemDetailInfo">
                                                        <p className="itemName">{item.itemName}</p>
                                                    </div>
                                                </td>
                                                <td className="itemDiscountRate">{item.itemDiscountRate}%</td>
                                                <td className="itemPriceInfo">
                                                    {item.itemDiscountRate > 0 ? (
                                                        <>
                                                            <span className="itemOriginalPrice">
                                                                {getFormattedPrice(item.itemPrice, 0).original}원
                                                            </span>
                                                            <span className="itemPrice">
                                                                {getFormattedPrice(item.itemPrice, item.itemDiscountRate).discounted}원
                                                            </span>
                                                        </>
                                                    ) : (
                                                        <span className="itemPrice">
                                                            {getFormattedPrice(item.itemPrice, 0).original}원
                                                        </span>
                                                    )}
                                                </td>
                                                <td className="itemTotalScore">{item.itemScore}</td>
                                                <td className="itemDate">{formatDateToDot(item.dueDate)}</td>
                                                <td className="itemWish">
                                                    <button
                                                        type="button"
                                                        className="btn"
                                                        onClick={() => handleDelete(item.wishListId)}
                                                    >
                                                        관심해제
                                                    </button>
                                                </td>
                                            </tr>
                                        );
                                    })
                                ) : (
                                    <tr>
                                        <td colSpan={7} className="noDataView">관심으로 등록한 상품이 없습니다.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                    <div className="itemSubMenu">
                        <Pagination pageInfo={wishData} />
                    </div>
                </div>
            </div>
        </>
    );
};

export default WishListComponent;
