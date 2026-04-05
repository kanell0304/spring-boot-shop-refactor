import React, { useState, useEffect } from "react";
import { NavLink, useSearchParams, useOutletContext , useParams} from 'react-router-dom';
import { wishAdd } from "../../api/wishApi";
import { getFormattedPrice } from "../../util/priecUtil";
import { getProductById  } from "../../api/productApi";
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation, Pagination, Scrollbar, A11y } from 'swiper/modules';
import ProductReviewList from "./ProductReviewListComponent";
import ProductQnAList from "./ProductQnAListComponent"
import SidebarDetailCoponent from "../../Components/product/SidebarDetailCoponent";

import defaultImg from "../../static/images/default.png";
import 'swiper/scss';
// import 'swiper/scss/navigation';
// import 'swiper/scss/pagination';
import 'swiper/scss/scrollbar';
import '../../static/css/productDetail.scss';

const tabData = [
  'DETAILS','REVIEWS','Q&A','SHIPPING &  REFUNDS'
]


const TabBar = ( { tabData, activeIndex })=>{
  return(
    <div className="tabBar" id={`tabBar_${activeIndex}`}>
      <ul>
        {tabData.map((item, index) => (
          <li key={index} className={activeIndex == index ? 'active' : ''} >
            <a href={`#tabBar_${index}`}>{item}</a>
          </li>
        ))}
      </ul>
    </div>
  )
}

const ProductDetailComponent = () => {
  const { memberInfo } = useOutletContext() || {};
  const { productId } = useParams();
  const [product, setProduct] = useState(null);
  const [imgSave , setImgSave] = useState([]);

  useEffect(() => {
    getProductById(productId).then((res) => {
      setProduct(res);
    });  
  }, [productId,memberInfo]);

  useEffect(() => {
    if (!product?.uploadFileNames) return;
  
    const originalImages = [...product.uploadFileNames];
    const lastImage = originalImages[originalImages.length - 1];
  
    while (originalImages.length < 4) {
      originalImages.push(lastImage);
    }
  
    setImgSave(originalImages);
  }, [product]);

  // console.log('product : '+product)
  // console.log('memberInfo:'+memberInfo.id)

  return (
    <>
    <div className="rightNavLayoutContainer">
      <div className="productDetailContainer">
        <div className="productImages">
          {product?.uploadFileNames.length > 0 ? (
            <Swiper
              // install Swiper modules
              modules={[Scrollbar]}
              slidesPerView={'auto'}
              autoHeight={true}
              scrollbar={{ draggable: true }}
              onSwiper={(swiper) => console.log(swiper)}
              onSlideChange={() => console.log('slide change')}
            >
            {imgSave.map((fileName,idx) => {
              
              return(
              <SwiperSlide key={idx}><img src={`http://localhost:8081/upload/${fileName}`} alt="Product Image" /></SwiperSlide>
            )}

            )}
            </Swiper>
            ) : (
              ''
              // <div>오류</div>
            )}
        </div>
        <div className="productInfo innerWrap">
          <TabBar tabData={tabData} activeIndex={0}/>
          <div className="productDiscription"
              dangerouslySetInnerHTML={{ __html: product?.description }}>
          </div>
          <TabBar tabData={tabData} activeIndex={1}/>
          {productId && (
            <ProductReviewList
              memberId={memberInfo?.id}
              productId={productId}
            />
          )}
          <TabBar tabData={tabData} activeIndex={2}/>
            <ProductQnAList
              memberId={memberInfo?.id}
              productId={productId}
            />
          <TabBar tabData={tabData} activeIndex={3}/>
          <div className="shippingRefunds">
            <div>
              <strong>교환 및 반품 주소</strong>
              <p>서울특별시 마포구 서강로 136 아이비티워 2층,3층</p>
            </div>
            <div>
              <strong>교환 및 반품이 가능한 경우</strong>
              <p>1.계약내용에 관한 서면을 받은 날부터 7일. 단, 그 서면을 받은 때보다 재화등의 공급이 늦게 이루어진 경우에는 재화등을 공급받거나 재화등의 공급이 시작된 날부터 7일 이내</p>
              <p>2.공급받으신 상품 및 용역의 내용이 표시.광고 내용과 다르거나 계약내용과 다르게 이행된 때에는 당해 재화 등을 공급받은 날 부터 3월이내, 그사실을 알게 된 날 또는 알 수 있었던 날부터 30일이내</p>
            </div>
            <div>
              <strong>교환 및 반품이 불가능한 경우</strong>
              <p>1.이용자에게 책임 있는 사유로 재화 등이 멸실 또는 훼손된 경우(다만, 재화 등의 내용을 확인하기 위하여 포장 등을 훼손한 경우에는 청약철회를 할 수 있습니다)</p>
              <p>2.이용자의 사용 또는 일부 소비에 의하여 재화 등의 가치가 현저히 감소한 경우</p>
              <p>3.시간의 경과에 의하여 재판매가 곤란할 정도로 재화등의 가치가 현저히 감소한 경우</p>
              <p>4.복제가 가능한 재화등의 포장을 훼손한 경우</p>
              <p>5.개별 주문 생산되는 재화 등 청약철회시 판매자에게 회복할 수 없는 피해가 예상되어 소비자의 사전 동의를 얻은 경우</p>
              <p>6.디지털 콘텐츠의 제공이 개시된 경우, (다만, 가분적 용역 또는 가분적 디지털콘텐츠로 구성된 계약의 경우 제공이 개시되지 아니한 부분은 청약철회를 할 수 있습니다.)</p>
              <small>※ 고객님의 마음이 바뀌어 교환, 반품을 하실 경우 상품반송 비용은 고객님께서 부담하셔야 합니다. (색상 교환, 사이즈 교환 등 포함)</small>
            </div>
              
          </div>
        </div>
      </div>
    </div>
    <SidebarDetailCoponent
      memberInfo={memberInfo}
    />
    </>
  );
}

export default ProductDetailComponent;