
import React, { useState, useEffect } from "react";
import { NavLink, useSearchParams, useOutletContext , useParams} from 'react-router-dom';
import { wishAdd } from "../../api/wishApi";
import { getFormattedPrice } from "../../util/priecUtil";
import defaultImg from "../../static/images/default.png"; 
import { fetchItems } from "../../api/categoryItemApi";
import { getCategories } from "../../api/categoryApi";
import SidebarComponent from "./SidebarComponent";


const ProductListComponent = () => {
  const { memberInfo } = useOutletContext();
  const [searchParams] = useSearchParams();
  const { categoryId } = useParams();
  const page = parseInt(searchParams.get("page") || "0", 10);
  const size = parseInt(searchParams.get("size") || "9", 10);
  const [items, setItems] = useState([]);
  const [categoryName, setCategoryName] = useState("");
  const [childCategories, setChildCategories] = useState([]);
  const [sort, setSort] = useState("NEWEST");
  
  useEffect(() => {
    fetchItems(categoryId, page, size).then(setItems);
  }, [categoryId, page, size]);

  useEffect(() => {
    const loadCategoryData = async () => {
      const current = await getCategories(categoryId);
      setCategoryName(current.categoryName);
      if (current?.parentId) {
        const parent = await getCategories(current.parentId);
        setChildCategories(parent.child);
      } else {
        setChildCategories(current.child || []);
      }
    };
    if (categoryId) loadCategoryData();
  }, [categoryId]);


  const handleAddCart = (itemId, e) => {
    e.stopPropagation();
    e.preventDefault();
    if (!isLoggedIn) {
      alert("로그인을 하셔야 사용이 가능합니다.");
      return;
    }

    // wishAdd(loginState.memberId, itemId).then((res) => {
    //   if(res === "삭제됨"){
    //     alert("장바구니에서 삭제되었습니다.");
    //   }else{
    //     alert("장바구니에 추가되었습니다.");
    //   }
    // });
  };

  const handleAddWishlist = async (itemId, e) => {
    e.stopPropagation();
    e.preventDefault();
    if (!isLoggedIn) {
      alert("로그인을 하셔야 사용이 가능합니다.");
      return;
    }
    wishAdd(loginState.memberId, itemId).then((res) => {
      if(res === "삭제됨"){
        alert("위시리스트에서 삭제되었습니다.");
      }else{
        alert("위시리스트에 추가되었습니다.");
      }
    });
  };

  return (
    <>
    <div className="rightNavLayoutContainer">
        <div className="itemListSection">
          {items?.content?.length > 0 ? (
            <ul>
              {items.content.map((item) => {
                const priceInfo = getFormattedPrice(item.price, item.discountRate);
                return (
                  <li className="itemCard" key={item.itemId}>
                    <NavLink to={`/product/detail/${item.itemId}`}>
                      <div className="itemImageWrapper">
                        <img
                          src={item.uploadFileNames !== "default.png"
                            ? `http://localhost:8081/upload/${item.uploadFileNames}`
                            : defaultImg}
                          alt={item.itemName}
                        />
                        <div className="itemButtonGroup">
                          <button onClick={(e) => handleAddWishlist(item.itemId, e)}>WISH</button>
                          <button onClick={(e) => handleAddCart(item.itemId, e)}>CART</button>
                        </div>
                      </div>
                      <div className="itemInfo">
                        <div className="itemName">{item.itemName}</div>
                        <div className="space">
                          <span className="itemSalePrice">{priceInfo.discounted}KRE</span>
                          <span className="itemOriginalPrice">{priceInfo.original}</span>
                          <span className="itemDiscount">{priceInfo.discountRate}</span>
                        </div>
                      </div>
                    </NavLink>
                  </li>
                );
              })}
            </ul>
          ) : (
            <div className='innerWrap noDataView'>등록된 상품이 없습니다.</div>
          )}
        </div>
      </div>
      <SidebarComponent
        items={items}
        childCategorys={childCategories}
        categoryName={categoryName}
        page={page}
        size={size}
        setSort={setSort}
      />
    </>
  );
};

export default ProductListComponent;
