import React, { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useSelector } from "react-redux";
import { getCookie, removeCookie } from "../util/cookieUtil";
import { categoryList } from '../api/categoryApi';
import Logo from '../static/svg/logo.svg?react';
import LogoutComponent from './member/LogoutComponent';

const Header = ({ isMypage }) => {
  const navigate = useNavigate();
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

  const handleLogout = () => {
    removeCookie("member");
    localStorage.removeItem("isLoggedIn");
    navigate("/");
  };

  const mypageLink = memberInfo?.roleNames?.includes("ADMIN") ? "/admin/mypage" : "/user/mypage";

  return (
    <header className="header">
      <div className={`innerWrap ${isMypage ? 'mypage' : ''}`}>
        <h1 className="logo">
          <Link to="/">
            <Logo />
            <strong className="blind">NØRD</strong>
          </Link>
        </h1>

        <nav className="gnb">
          <ul className="cartegory">
            {categories.map((cat) => (
              <li key={cat.id}>
                <Link to={`/product/list/${cat.id}?page=0&size=9`}>{cat.categoryName}</Link>
              </li>
            ))}
            <li><Link to={`/magazine/list?page=0&size=10`}>MAGAZINE</Link></li>
            <li><Link to={`/event/list?page=0&size=10`}>EVENT</Link></li>
          </ul>
        </nav>

        <div className="utillMenu">
          <ul>
            <li><Link to="/search">SEARCH</Link></li>
            <li><Link to={isLoggedIn ? "/cart" : "/member/login"}>CART</Link></li>
            <li><Link to={isLoggedIn ? mypageLink : "/member/login"}>MYPAGE</Link></li>
            {isLoggedIn ? (
              <li><LogoutComponent /></li>
            ) : (
              <li><Link to="/member/login">LOGIN</Link></li>
            )}
          </ul>
        </div>
      </div>
    </header>
  );
};

export default Header;
