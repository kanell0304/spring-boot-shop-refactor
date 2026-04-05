import React from "react";
import { Link } from 'react-router-dom';
import BasicLayout from "../../layout/BasicLayout";
import visual from "../../static/images/mainpage/Mask_group1.png"
import banner from "../../static/images/mainpage/Mask_group2.png"
import itemImage_1 from "../../static/images/mainpage/Product_1.png"
import itemImage_2 from "../../static/images/mainpage/Product_2.png"
import itemImage_3 from "../../static/images/mainpage/Product_3.png"
import itemImage_4 from "../../static/images/mainpage/Product_4.png"
import itemImage_5 from "../../static/images/mainpage/Product_5.png"
import itemImage_6 from "../../static/images/mainpage/Product_6.png"
import "../../static/css/main.scss"



const MainPage = () => {
  return (
    <BasicLayout>
        {/* 배너 이미지 파일 */}
        <div className="visual">
          <div>
            <img src={visual} width={1920} height={1041} alt="메인 배너" loading="lazy"/>
          </div>
        </div>

        {/* 인트로 텍스트 + 배너 */}
        <section className="sectionContent_1">
          <div className="discrioton">
            <div className="innerWrap">
              <h2>INTRODUCTION</h2>
              <p>
                NØRD is built on minimalism and functionality, embracing the philosophy that clothing is not just a trend - but a part of life.<br/>
                Each piece is a refined balance of practicality and style, crafted as a wearable work of art.
              </p>
              <Link className="link" to="shop">SHOP NOW</Link>
            </div>
          </div>
          <div className="visual">
            <img src={banner} alt="서브 배너" loading="lazy"/>
          </div>
        </section>

        {/* CORE VALUES + 아이템 사진 */}
        <section className="sectionContent_2">
          <div className="discrioton">
            <div className="innerWrap">
              <h2>OUR CORE VALUES</h2>
              <ul>
                <li>Quality Over Quantity</li>
                <li>Innovation & Tradition</li>
                <li>Minimal but Bold</li>
              </ul>
            </div>
          </div>

          <div className="productItemsWrap">

            <div className="productsImage leftContent">
              <div className="imagWrap"><img src={itemImage_1} alt="아이템1" loading="lazy"/></div>
              <div className="imagWrap"><img src={itemImage_2} alt="아이템2" loading="lazy"/></div>
              <div className="imagWrap"><img src={itemImage_3} alt="아이템3" loading="lazy"/></div>
            </div>

            <div className="productsImage rightContetn">
              <div className="imagWrap"><img src={itemImage_4} alt="아이템4" loading="lazy"/></div>
              <div className="imagWrap"><img src={itemImage_5} alt="아이템5" loading="lazy"/></div>
              <div className="imagWrap"><img src={itemImage_6} alt="아이템6" loading="lazy"/></div>
            </div>

          </div>
        </section>
      </BasicLayout>
  )
}

export default MainPage;
