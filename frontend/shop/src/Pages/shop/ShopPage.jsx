import React, { useState, useEffect } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useSelector } from "react-redux";
import BasicLayout from "../../layout/BasicLayout";
import { productList } from "../../api/productApi";
import { wishAdd } from "../../api/wishApi";
import { categoryList } from "../../api/categoryApi";
import { getFormattedPrice } from "../../util/priecUtil";
import Pagination from "../../Components/Pagination";
import defaultImg from "../../static/images/default.png";
import "../../static/css/shop.scss";
import "../../static/css/siderbar.scss";

const SORT_OPTIONS = [
    { label: "NEWEST", key: "dueDate", dir: "desc" },
    { label: "PRICE HIGH", key: "price", dir: "desc" },
    { label: "PRICE LOW", key: "price", dir: "asc" },
];

const ShopPage = () => {
    const loginState = useSelector(state => state.loginSlice);
    const isLoggedIn = loginState && loginState.email !== '';
    const [searchParams, setSearchParams] = useSearchParams();
    const [items, setItems] = useState({});
    const [categories, setCategories] = useState([]);
    const [sort, setSort] = useState(SORT_OPTIONS[0]);

    const page = parseInt(searchParams.get("page")) || 0;
    const size = parseInt(searchParams.get("size")) || 9;

    useEffect(() => {
        productList(page, size).then(setItems);
    }, [page, size]);

    useEffect(() => {
        categoryList().then(setCategories);
    }, []);

    const handleWish = (e, itemId) => {
        e.preventDefault();
        e.stopPropagation();
        if (!isLoggedIn) {
            alert("로그인을 하셔야 사용이 가능합니다.");
            return;
        }
        wishAdd(loginState.memberId, itemId).then(res => {
            alert(res === "삭제됨" ? "위시리스트에서 삭제되었습니다." : "위시리스트에 추가되었습니다.");
        });
    };

    const getSorted = () => {
        const list = items?.content ? [...items.content] : [];
        return list.sort((a, b) => {
            const av = a[sort.key] ?? 0;
            const bv = b[sort.key] ?? 0;
            return sort.dir === "asc" ? av - bv : bv - av;
        });
    };

    const sortedItems = getSorted();

    return (
        <BasicLayout>
            <div className="rightNavLayoutWrap">
                <div className="rightNavLayoutContainer">
                    <div className="itemListSection">
                        {sortedItems.length > 0 ? (
                            <ul>
                                {sortedItems.map(item => {
                                    const priceInfo = getFormattedPrice(item.price, item.discountRate);
                                    return (
                                        <li className="itemCard" key={item.id}>
                                            <Link to={`/product/detail/${item.id}`}>
                                                <div className="itemImageWrapper">
                                                    <img
                                                        src={
                                                            item.uploadFileNames?.length > 0
                                                                ? `http://localhost:8081/upload/${item.uploadFileNames[0]}`
                                                                : defaultImg
                                                        }
                                                        alt={item.name}
                                                    />
                                                    <div className="itemButtonGroup">
                                                        <button onClick={(e) => handleWish(e, item.id)}>WISH</button>
                                                    </div>
                                                </div>
                                                <div className="itemInfo">
                                                    <div className="itemName">{item.name}</div>
                                                    <div className="space">
                                                        <span className="itemSalePrice">{priceInfo.discounted}KRE</span>
                                                        <span className="itemOriginalPrice">{priceInfo.original}</span>
                                                        <span className="itemDiscount">{priceInfo.discountRate}</span>
                                                    </div>
                                                </div>
                                            </Link>
                                        </li>
                                    );
                                })}
                            </ul>
                        ) : (
                            <div className="innerWrap noDataView">등록된 상품이 없습니다.</div>
                        )}
                    </div>
                </div>

                <aside className="itemSidebar">
                    <div className="innerSiedbarWrap">
                        <h2 className="pageTitle">SHOP</h2>

                        <nav>
                            <ul className="categoryList">
                                {categories.map(cat => (
                                    <li key={cat.id}>
                                        <Link to={`/product/list/${cat.id}?page=0&size=9`}>
                                            {cat.categoryName}
                                        </Link>
                                    </li>
                                ))}
                            </ul>
                        </nav>

                        <div className="paginationSection">
                            <div className="totalCount">TOTAL : {items?.totalElements || 0}</div>
                            <div className="sortButtons">
                                {SORT_OPTIONS.map(opt => (
                                    <button
                                        key={opt.label}
                                        type="button"
                                        className={`btn line ${sort.label === opt.label ? "active" : ""}`}
                                        onClick={() => setSort(opt)}
                                    >
                                        {opt.label}
                                    </button>
                                ))}
                            </div>
                            <Pagination pageInfo={items} />
                        </div>
                    </div>
                </aside>
            </div>
        </BasicLayout>
    );
};

export default ShopPage;
