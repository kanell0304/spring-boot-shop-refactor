import { useState } from "react"
import useCustomLogin from "../../hooks/useCustomLogin"
import KakaoLoginComponent from "./KakaoLoginComponent";

const initState = {
  email:'',
  pw:''
}

const LoginComponent = () => {
  /**로그인 정보 변수 */
  const [loginParam, setLoginParam] = useState({...initState});
  

  /**서버 로그인 및 이동 */
  const {doLogin, moveToPath} = useCustomLogin()
  
  /** 비밀번호 검증 */
  const isValidPassword = (pw) => {
    return /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&]).{6,}$/.test(pw);
  };

  /**검증 */
  const [error, setError] = useState("");

  /** 로그인 정보 initState 저장&갱신 */
  const handleChange = (e) => {
    loginParam[e.target.name] = e.target.value;
    setLoginParam({...loginParam});
  }

  const handleClickLogin = (e) => {
    // if (!loginParam.email || !loginParam.pw) {
    //   setError("이메일과 비밀번호를 모두 입력해주세요.");
    //   return;
    // }

    // if (!isValidPassword(!loginParam.pw)) {
    //   //setError("비밀번호는 최소 6자 이상, 영문/숫자/특수문자를 포함해야 합니다.");
    //   return;
    // }

    doLogin(loginParam) // loginSlice의 비동기 호출
    .then(data => {
      console.log(data)
      
      if(data.error) {
        alert("이메일과 패스워드를 다시 확인하세요")
      }else {
        alert("로그인 성공");
        moveToPath('/');
      }
    })
  }

  return (
    <form className="form">
      <p className="loginLabel">LOGIN YOUR ACCOUNT</p>

      <div>
        <input
          name="email"
          type="email"
          value={loginParam.email}
          onChange={handleChange}
          placeholder="E-MAIL ADDRESS"
          autoComplete="on"
        />
      </div>

      <div>
        <input
          name="pw"
          type="password"
          value={loginParam.pw}
          onChange={handleChange}
          placeholder="PASSWORD"
          autoComplete="on"
        />
      </div>

    {error && <p className="errorMessage">{error}</p>}

    <div className="loginLinks">
      <button
        className="btn bigBtn bold black"
        type="button"
        onClick={handleClickLogin}
      >LOGIN
      </button>
      <div className="login-or">OR</div>
      <KakaoLoginComponent></KakaoLoginComponent>
    </div>
  </form>
  );
}
 
export default LoginComponent;
