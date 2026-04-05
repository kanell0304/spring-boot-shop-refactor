/** 가격 콤마 및 할인 가격 계산 */
export const getFormattedPrice = (price, discountRate) => {
    const discountPrice = Math.floor(price * (1 - discountRate / 100));
  
    const format = (num) => num.toLocaleString('ko-KR');
  
    return {
      original: format(price),
      discountRate: `${discountRate}%`,
      discounted: format(discountPrice),
    };
  };

/** 천단위 콤마 */
export const addComma = (number) => {
  return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};