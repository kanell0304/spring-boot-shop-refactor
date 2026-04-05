import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

import BasicLayout from "../../layout/BasicLayout";
import LoginComponent from "../../Components/member/LoginComponent";
import LoginImage from "../../static/images/login/Mask_group4.png";
import "../../static/css/login.scss";


const LoginPage = () => {
  return (
  <BasicLayout>
    <div className="loginContainer">
      <div className="imageBox">
        <img src={LoginImage} alt="로그인 이미지" width={960} height={1080}/>
      </div>

      <div className="loginBox">
        <div className="setting">
        <div className="text">
          <h2>SIMPLICITY<br/>SPEAKS</h2>
          <p>WHAT YOU WEAR SAYS MORE THAN WORDS</p>
        </div>

        <LoginComponent></LoginComponent>

        <div className="findLinks">
          <div>
            <Link to="/member/signup">CREATE ACCOUNT</Link>
          </div>
          <div>
            FORGOT YOUR <Link to="/findEmail">E-MAIL?</Link>
          </div>
          <div>
            FORGOT YOUR <Link to="/resetPW">PASSWORD?</Link>
          </div>
        </div>
        </div>
      </div>
    </div>
  </BasicLayout>
  )
}

export default LoginPage;
