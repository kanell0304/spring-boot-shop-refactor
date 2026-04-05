import { useState, useEffect } from "react";
import { NavLink } from "react-router-dom";
import { getMemberById, modifyMember} from '../../api/memberApi';

const MyNav = ({ memberInfo }) => {
  const pageParam = `?page=0&size=10`;
  const [ userInfo, setUserInfo ] = useState(null);
  const [ isUser, setIsUSer ] = useState({});
  const isAdmin = Array.isArray(memberInfo?.roleNames) && memberInfo.roleNames.includes("ADMIN");

  useEffect(() => {
    setUserInfo(memberInfo);
  }, [memberInfo]);


  useEffect(() => {
    if(!userInfo?.memberId) return;
    getMemberById(userInfo?.memberId)
      .then((data) => {
        setIsUSer(data);
        console.log("isUser", isUser);
      })
  }, [userInfo]);


  return (
    <aside className='myNavWrap'>
      {isAdmin ? (
      <div className="asideNav">
        <h2>ADMIN</h2>
        <div className="userRollInfo">
          <strong>관리자</strong>
          <p>오늘도 화이팅하세요.</p>
        </div>
          <nav className="userNav">
            <ul>
              <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`order${pageParam}`}>주문</NavLink></li>
              <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`inquiry${pageParam}`}>문의</NavLink></li>
              <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`review${pageParam}`}>리뷰</NavLink></li>
              <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`product${pageParam}`}>상품</NavLink></li>
              <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to="category">분류</NavLink></li>
              <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`member${pageParam}`}>회원</NavLink></li>
              <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`event${pageParam}`}>이벤트</NavLink></li>
              <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`magazine${pageParam}`}>매거진</NavLink></li>
              {/* <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to="adminInfo">관리자정보</NavLink></li> */}
            </ul>
          </nav>
        </div>
        ) : (
          <div className="asideNav">
          <h2>MYPAGE</h2>
          <div className="userRollInfo">
            <strong>{isUser.memberShip}</strong>
            <p>{isUser.memberName}님 안녕하세요!</p>
          </div>
            <nav className="userNav">
              <ul>
                <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to="order?page=0&size=10">주문</NavLink></li>
                <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to="inquiry?page=0&size=10">문의</NavLink></li>
                <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`mileage/${userInfo?.memberId}?page=0&size=10`}>마일리지</NavLink></li>
                <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`wish/${userInfo?.memberId}?page=0&size=10`}>관심상품</NavLink></li>
                <li><NavLink className={({ isActive }) => (isActive ? 'active' : '')} to={`profile/${userInfo?.memberId}`}>개인정보</NavLink></li>
              </ul>
            </nav>
          </div>
      )}
    </aside>
  );
};

export default MyNav;
