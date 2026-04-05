import useCustomLogin from "../../hooks/useCustomLogin";

const LogoutComponent = () => {

  const {doLogout, moveToPath} = useCustomLogin()

  const handleClickLogout = (e) => {
    e.preventDefault();
    doLogout()
    alert("로그아웃되었습니다.")
    moveToPath("/")
  }


  return ( 
      <a href="#" onClick={handleClickLogout}>LOGOUT</a>
    );
}
 
export default LogoutComponent;
