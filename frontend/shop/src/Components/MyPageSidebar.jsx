<<<<<<< HEAD
import React from "react";
import { Link } from 'react-router-dom';

const MyPageSidebar = ({ userName, membership }) => {
  const membershipGrade = {
    BRONZE: "브론즈",
    SILVER: "실버",
    GOLD: "골드",
    PLATINUM: "플레티넘"
  };

  const menuItems = [
    { label: "주문내역", href: "/mypage/orders" },
    { label: "문의내역", href: "/mypage/inquiry" },
    { label: "마일리지", href: "/mypage/mileage" },
    { label: "관심상품", href: "/mypage/wishlist" },
    { label: "개인정보", href: "/mypage/profile" },
  ];

  return (
    <aside className="sidebar">
      <h2>MYPAGE</h2>
      <p>{membershipGrade[membership]}</p>
      <p>{userName} 고객님 반갑습니다.</p>
      <nav className="siderbarMenu">
        <ul>
          {menuItems.map((item) => (
            <li key={item.href}>
              <Link to={item.href}>{item.label}</Link>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  )
}

export default MyPageSidebar;
=======
import React from "react";
import { Link } from 'react-router-dom';

const MyPageSidebar = ({ userName, membership }) => {
  const membershipGrade = {
    BRONZE: "브론즈",
    SILVER: "실버",
    GOLD: "골드",
    PLATINUM: "플레티넘"
  };

  const menuItems = [
    { label: "주문내역", href: "/mypage/orders" },
    { label: "문의내역", href: "/mypage/inquiry" },
    { label: "마일리지", href: "/mypage/mileage" },
    { label: "관심상품", href: "/mypage/wishlist" },
    { label: "개인정보", href: "/mypage/profile" },
  ];

  return (
    <aside className="sidebar">
      <h2>MYPAGE</h2>
      <p>{membershipGrade[membership]}</p>
      <p>{userName} 고객님 반갑습니다.</p>
      <nav className="siderbarMenu">
        <ul>
          {menuItems.map((item) => (
            <li key={item.href}>
              <Link to={item.href}>{item.label}</Link>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  )
}

export default MyPageSidebar;
>>>>>>> 6c80c21440fd34d348db1950f2af8e1e895ca51a
