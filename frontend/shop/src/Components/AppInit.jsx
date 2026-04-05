// AppInit.jsx
import { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import { login } from '../slices/loginSlice'
import { getCookie } from '../util/cookieUtil'

const AppInit = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    if ('scrollRestoration' in window.history) {
      window.history.scrollRestoration = 'manual';
    }
  }, []);

  useEffect(() => {
    const member = getCookie("member");
    if (member) {
      dispatch(login(member)); // 이제 JSON.parse 필요 없음
    }
  }, []);

  return null; // 아무것도 렌더링하지 않음
}

export default AppInit;
