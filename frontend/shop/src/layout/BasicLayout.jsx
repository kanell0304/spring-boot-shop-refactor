import Header from "../Components/Header";
import Footer from "../Components/Footer";
import { useLocation } from 'react-router-dom';

const BasicLayout = ({children}) => {
  const location = useLocation();
  const isMypage = location.pathname.startsWith('/admin/mypage') || location.pathname.startsWith('/user/mypage') ;
  return (
    <>
      <Header isMypage={isMypage} />
      <div className="container">
        <main className="main">{children}</main>
      </div>
      <Footer isMypage={isMypage} />
    </>
  )
};

export default BasicLayout;
