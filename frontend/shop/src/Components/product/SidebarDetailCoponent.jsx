import { useParams } from "react-router-dom";
import React, { useState, useEffect } from "react";
import { getFormattedPrice, addComma } from "../../util/priecUtil";
import { getProductById, getItemOptions } from "../../api/productApi";
import { addCartItem } from "../../api/cartApi";
import { useOutletContext } from 'react-router-dom';


const SidebarDetailComponent = () => {
  const { productId } = useParams();
  const { memberInfo } = useOutletContext() || {};
  const [product, setProduct] = useState(null);
  const [options, setOptions] = useState([]);
  const [selectedOptions, setSelectedOptions] = useState([]);
  const [mileage, setMileage] = useState(0);

  useEffect(() => {
    getProductById(productId).then(setProduct);
    getItemOptions(productId).then(setOptions).catch(() => setOptions([]));
  }, [productId]);

  useEffect(() => {
    if (!product || !memberInfo) return;
    const discountPrice = Math.floor(product.price * (1 - product.discountRate / 100));
    const rateMap = { BRONZE: 0.01, SILVER: 0.02, GOLD: 0.03, PLATINUM: 0.05 };
    const rate = rateMap[memberInfo.memberShip] || 0;
    setMileage(Math.round(discountPrice * rate));
  }, [product, memberInfo]);

  const handleOptionClick = (option) => {
    setSelectedOptions(prev => {
      const exists = prev.find(o => o.optionId === option.optionId);
      return exists
        ? prev.filter(o => o.optionId !== option.optionId)
        : [...prev, { ...option, quantity: 1 }];
    });
  };

  const updateQuantity = (optionId, delta) => {
    setSelectedOptions(prev =>
      prev.map(o => {
        if (o.optionId === optionId) {
          const newQty = Math.max(1, Math.min(o.quantity + delta, o.stockQty));
          return { ...o, quantity: newQty };
        }
        return o;
      })
    );
  };

  const calculateTotal = () => {
    if (!product) return 0;
    const basePrice = Math.floor(product.price * (1 - product.discountRate / 100));
    return selectedOptions.reduce((sum, option) => {
      return sum + ((basePrice + option.optionPrice) * option.quantity);
    }, 0);
  };

  const handleAddToCart = async () => {
    if (!memberInfo) {
      alert("로그인 후 이용해주세요.");
      return;
    }
  
    if (selectedOptions.length === 0) {
      alert("옵션을 선택해주세요.");
      return;
    }
  
    try {
      for (const opt of selectedOptions) {
        const cartData = {
          memberId: memberInfo.id,
          itemId: product.id,
          optionId: opt.optionId,
          qty: opt.quantity,
        };
        console.log("등록 요청:", cartData);
        await addCartItem(cartData);
      }
  
      alert("장바구니에 등록되었습니다.");
    } catch (error) {
      console.error("장바구니 등록 실패", error);
      alert("장바구니 등록 중 오류가 발생했습니다.");
    }
  };


  if (!product) return <div className="itemSidebar">Loading...</div>;
  const formatted = getFormattedPrice(product.price, product.discountRate);
  const uniqueOptionNames = [...new Set(options.map(o => o.optionName))];

  return (
    <aside className="itemSidebar productDetail">
      <div className="sidebarInnerWrap">
        <div className="productMeta">
          <span className="productId">NO.{product.id}</span>
          <div className="actionButtons">
            <button type="button" className="btnWish">WISH</button>
            <button type="button" className="btnCart">CART</button>
          </div>
        </div>

        <h2 className="productTitle">{product.name}</h2>

        <div className="priceSection">
          <span className="priceNow">{formatted.discounted}KRE</span>
          {product.discountRate > 0 && (
            <>
              <span className="priceOriginal">{formatted.original}</span>
              <span className="priceDiscount">{formatted.discountRate}</span>
            </>
          )}
        </div>

        <div className="rating">⭐ {product.totalScore} / 5 </div>
        {memberInfo && (
          <div className="mileage">
            MILEAGE : {addComma(mileage)}P ({memberInfo.memberShip})
          </div>
        )}

        {uniqueOptionNames.map(name => (
          <div key={name} className="optionGroup">
            <div className="optionLabel">{name} :</div>
            <div className="optionValues">
              {options
                .filter(o => o.optionName === name)
                .map(option => (
                  <button
                    key={option.optionId}
                    onClick={() => handleOptionClick(option)}
                    disabled={option.stockQty === 0}
                    className={`btn line ${selectedOptions.find(o => o.optionId === option.optionId) ? "active" : ""}`}
                  >
                    {option.optionValue} {option.optionPrice > 0 && `(+${option.optionPrice})`}
                  </button>
                ))}
            </div>
          </div>
        ))}

        <div className="selectedOptions">
          {selectedOptions.map(option => {
            const dcPrice = Math.floor(product.price * (1 - product.discountRate / 100));
            const total = (dcPrice + option.optionPrice) * option.quantity;
            return (
              <div className="selectedItem" key={option.optionId}>
                <div className="optionInfo">
                  {product.name} / {option.optionValue}
                  <span className="optionPrice">{addComma(total)}KRE</span>
                </div>
                <div className="quantityControl">
                  QUANTITY :
                  <button onClick={() => updateQuantity(option.optionId, -1)} className="qtyBtn">-</button>
                  <span className="qty">{option.quantity}</span>
                  <button onClick={() => updateQuantity(option.optionId, 1)} className="qtyBtn">+</button>
                </div>
              </div>
            );
          })}
        </div>

        <div className="totalSection">
          <span className="totalLabel">TOTAL : </span>
          <strong className="totalPrice">{addComma(calculateTotal())}KRE</strong>
        </div>

        <div className="payBtn">
          <button type="button" className="btn black" onClick={handleAddToCart}>PAYMENT {addComma(calculateTotal())} KRE</button>
        </div>
      </div>
    </aside>
  );
};

export default SidebarDetailComponent;