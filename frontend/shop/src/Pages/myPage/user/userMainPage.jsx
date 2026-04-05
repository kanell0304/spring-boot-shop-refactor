import React,{ useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from "react-redux";
import BasicLayout from '../../../layout/BasicLayout';
import MyNav from '../../../components/mypage/MyNavComponent';
import MySection from '../../../components/mypage/MySectionComponent';

import { getCookie } from "../../../util/cookieUtil";

import '../../../static/css/myNav.scss';
import '../../../static/css/mypage.scss';

const UserMainPage =()=>{
  const navigate = useNavigate();
  const loginState = useSelector(state => state.loginSlice);
  const isLoggedIn = loginState && loginState.email !== '';
  const [memberInfo, setInfo] = useState(null);

  useEffect(() => {
    if (isLoggedIn) {
      try {
        const raw = getCookie("member");
        const parsed = typeof raw === "string" ? JSON.parse(raw) : raw;
        console.log(parsed.roleNames)
        setInfo(parsed);
      } catch (err) {
        console.error("❌ 쿠키 파싱 실패:", err);
        setInfo(null);
      }
    } else {
      setInfo(null);
    }
  }, [isLoggedIn]);

  useEffect(() => {
    if (!isLoggedIn) {
      //alert("로그인이 필요합니다.");
      navigate("/");
      return;
    }
    
    // if (memberInfo && (!Array.isArray(memberInfo.roleNames) || !memberInfo.roleNames.includes("USER"))) {
    //   alert("로그인이 필요합니다.");
    //   navigate("/member/login");
    // }
  }, [isLoggedIn, memberInfo, navigate]);

  if (!isLoggedIn || !memberInfo) return null; // 렌더링 차단 (필요 시 로딩 처리 가능)

    return(
      <BasicLayout>
          <div className="mypageWrap">
            {memberInfo && (
                <MyNav memberInfo={memberInfo} />
              )}
              <MySection/>
          </div>
      </BasicLayout>
    )
}
export default UserMainPage;