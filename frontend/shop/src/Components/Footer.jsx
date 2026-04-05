import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { useSelector } from "react-redux";
import { getCookie } from "../util/cookieUtil";
import { categoryList } from '../api/categoryApi';
import Logo from '../static/svg/logo.svg?react';

const Footer = ({ isMypage }) => {
  const loginState = useSelector(state => state.loginSlice);
  const isLoggedIn = loginState && loginState.email !== '';
  const [memberInfo, setInfo] = useState(null);
  const [categories, setCategories] = useState([]);

  const fetchCategories = useCallback(() => {
    categoryList().then(setCategories);
  }, []);

  useEffect(() => {
    fetchCategories();

    if (isLoggedIn) {
      const info = getCookie("member");
      setInfo(info);
    } else {
      setInfo(null);
    }

    const handleUpdate = () => fetchCategories();
    window.addEventListener('categoryUpdated', handleUpdate);

    return () => window.removeEventListener('categoryUpdated', handleUpdate);
  }, [isLoggedIn, fetchCategories]);

  const mypageLink = memberInfo?.roleNames?.includes("ADMIN") ? "/admin/mypage" : "/user/mypage";

  return (
    <footer className="footer">
      <div className={`innerWrap ${isMypage ? 'mypage' : ''}`}>
        <div className="f_content_top">
          <h2 className="logo">
            <Logo className="logo" />
            <strong className='blind'>NØRD</strong>
          </h2>
          <nav className="f_siteMap">
            <ul className="f_cartegory">
              {categories.map((cat) => (
                <li key={cat.id}>
                  <Link to={`/product/list/${cat.id}?page=0&size=9`}>{cat.categoryName}</Link>
                </li>
              ))}
              <li><Link to={`/magazine/list?page=0&size=10`}>MAGAZINE</Link></li>
              <li><Link to={`/event/list?page=0&size=10`}>EVENT</Link></li>
            </ul>
          </nav>
        </div>
        <div className="f_content_botton">
          <ul className="csBtn">
            <li><Link to="/terms">이용약관</Link></li>
            <li><Link to="/guide">이용안내</Link></li>
            <li><Link to="/privacy">개인정보처리방침</Link></li>
          </ul>
          <div className="infoDetail">
            <span>© NØRD </span>
            <span>대표 : 최흥수</span>
            <span>법인명 : 스카디 어패럴</span>
            <span>주소 : 서울특별시 마포구 서강로 136 아이비티워 2층,3층</span>
            <span>사업자 등록 번호 : 000-00-00000</span>
            <span>통신판매업 신고 번호 : 제0000 - 서울ㅇㅇ--0000호</span>
            <span>개인정보보호책임자 : 최흥수(gmdt89@naver.com)</span>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
