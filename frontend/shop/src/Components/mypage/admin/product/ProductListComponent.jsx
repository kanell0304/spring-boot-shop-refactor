import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { NavLink, Link } from "react-router-dom";
import { getFormattedPrice } from "../../../../util/priecUtil";
import { formatDateToDot } from "../../../../util/dateUtil";
import { productList } from "../../../../api/productApi";
import Pagination from "../../../Pagination";
import defaultImg from "../../../../static/images/default.png";

const ProductListComponent = () => {
    const [productData, setData] = useState({});
    const [sortKey, setSortKey] = useState(null);
    const [sortDir, setSortDir] = useState('desc');
    const [delFlagFilter, setDelFlagFilter] = useState('ALL');
    const [searchParams] = useSearchParams();

    const page = parseInt(searchParams.get("page")) || 0;
    const size = parseInt(searchParams.get("size")) || 10;

    useEffect(() => {
        productList(page, size).then(setData);
    }, [page, size]);

    const handleSort = (key, dir) => {
        setSortKey(key);
        setSortDir(dir);
    };

    const getSortedFiltered = () => {
        let list = productData?.content ? [...productData.content] : [];
        if (delFlagFilter !== 'ALL') {
            list = list.filter(item => delFlagFilter === 'DELETED' ? item.delFlag : !item.delFlag);
        }
        if (sortKey) {
            list.sort((a, b) => {
                const av = a[sortKey] ?? 0;
                const bv = b[sortKey] ?? 0;
                return sortDir === 'asc' ? av - bv : bv - av;
            });
        }
        return list;
    };

    const displayList = getSortedFiltered();

    return (
        <>
            <h2 className="pageTitle">상품</h2>
            <div className="pageContainer">
                <div className="borderSection filter">
                    <strong>등록된 상품 갯수 : {productData?.totalElements || 0}개</strong>
                    <div className="btnsWrap">
                        <div>
                            <button
                                className={`btn line ${sortKey === 'salesVolume' && sortDir === 'desc' ? 'active' : ''}`}
                                type="button"
                                onClick={() => handleSort('salesVolume', 'desc')}
                            >높은판매순</button>
                            <button
                                className={`btn line ${sortKey === 'salesVolume' && sortDir === 'asc' ? 'active' : ''}`}
                                type="button"
                                onClick={() => handleSort('salesVolume', 'asc')}
                            >낮은판매순</button>
                        </div>
                        <div>
                            <button
                                className={`btn line ${sortKey === 'totalScore' && sortDir === 'desc' ? 'active' : ''}`}
                                type="button"
                                onClick={() => handleSort('totalScore', 'desc')}
                            >높은별점순</button>
                            <button
                                className={`btn line ${sortKey === 'totalScore' && sortDir === 'asc' ? 'active' : ''}`}
                                type="button"
                                onClick={() => handleSort('totalScore', 'asc')}
                            >낮은별점순</button>
                        </div>
                        <div>
                            <button
                                className={`btn line ${sortKey === 'price' && sortDir === 'desc' ? 'active' : ''}`}
                                type="button"
                                onClick={() => handleSort('price', 'desc')}
                            >높은가격순</button>
                            <button
                                className={`btn line ${sortKey === 'price' && sortDir === 'asc' ? 'active' : ''}`}
                                type="button"
                                onClick={() => handleSort('price', 'asc')}
                            >낮은가격순</button>
                        </div>
                        <div>
                            <button
                                className={`btn line ${delFlagFilter === 'DELETED' ? 'active' : ''}`}
                                type="button"
                                onClick={() => setDelFlagFilter(prev => prev === 'DELETED' ? 'ALL' : 'DELETED')}
                            >삭제상품</button>
                            <button
                                className={`btn line ${delFlagFilter === 'ACTIVE' ? 'active' : ''}`}
                                type="button"
                                onClick={() => setDelFlagFilter(prev => prev === 'ACTIVE' ? 'ALL' : 'ACTIVE')}
                            >정상상품</button>
                        </div>
                    </div>
                </div>

                <div className="tablePage">
                    <div className="itemSubMenu">
                        <Pagination pageInfo={productData} />
                        <Link className="btn black" to={'add'}>상품등록</Link>
                    </div>
                    <div className="itemTableWrap">
                        <table className="itemTable">
                            <thead className="itemThead">
                                <tr className="itemTr">
                                    <th className="itemNumber">번호</th>
                                    <th className="itemID">상품ID</th>
                                    <th className="itemInfo">상품정보</th>
                                    <th className="itemDiscountRate">할인율</th>
                                    <th className="itemPriceInfo">가격</th>
                                    <th className="itemTotalScore">별점</th>
                                    <th className="itemDate">등록일</th>
                                    <th className="itemSalesVolume">판매량</th>
                                    <th className="itemDelFlag">판매상태</th>
                                </tr>
                            </thead>
                            <tbody className="itemTbody">
                                {displayList.length > 0 ? (
                                    displayList.map((item, index) => {
                                        const displayIndex = productData.totalElements - (page * size + index);
                                        return (
                                            <tr className="itemTr" key={item.id}>
                                                <td className="itemNumber">{displayIndex}</td>
                                                <td className="itemID">{item.id}</td>
                                                <td className="itemInfo">
                                                    <div className="itemImg">
                                                        <img
                                                            src={
                                                                item.uploadFileNames?.length > 0
                                                                    ? `http://localhost:8081/upload/${item.uploadFileNames[0]}`
                                                                    : defaultImg
                                                            }
                                                            alt={item.name}
                                                        />
                                                    </div>
                                                    <div className="itemDetailInfo">
                                                        <p>{item.name}</p>
                                                        {item.options?.length > 0 && (
                                                            <p className="itemOption">
                                                                {item.options[0].optionName} :{" "}
                                                                {item.options.map(op => `${op.optionValue} (수량 : ${op.stockQty}개)`).join(", ")}
                                                            </p>
                                                        )}
                                                        <NavLink to={`/admin/mypage/product/modify/${item.id}`}>상품상세 보기</NavLink>
                                                    </div>
                                                </td>
                                                <td className="itemDiscountRate">{item.discountRate}%</td>
                                                <td className="itemPriceInfo">
                                                    {item.discountRate > 0 ? (
                                                        <>
                                                            <span className="itemOriginalPrice">{getFormattedPrice(item.price, 0).original}원</span>
                                                            <span className="itemPrice">{getFormattedPrice(item.price, item.discountRate).discounted}원</span>
                                                        </>
                                                    ) : (
                                                        <span className="itemPrice">{getFormattedPrice(item.price, 0).original}원</span>
                                                    )}
                                                </td>
                                                <td className="itemTotalScore">{item.totalScore}</td>
                                                <td className="itemDate">{formatDateToDot(item.dueDate)}</td>
                                                <td className="itemSalesVolume">{item.salesVolume}</td>
                                                <td className={`itemDelFlag ${item.delFlag ? "deleted" : "active"}`}>
                                                    {item.delFlag ? "판매중지" : "판매중"}
                                                </td>
                                            </tr>
                                        );
                                    })
                                ) : (
                                    <tr>
                                        <td colSpan={9} className="noDataView">등록된 상품이 없습니다.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                    <div className="itemSubMenu">
                        <Pagination pageInfo={productData} />
                        <Link className="btn black" to={'add'}>상품등록</Link>
                    </div>
                </div>
            </div>
        </>
    );
};

export default ProductListComponent;
