import {Link} from "react-router-dom";
import { getKakaoLoginLink } from "../../api/kakaoApi";

const KakaoLoginComponent = () => {
    const link = getKakaoLoginLink();
    return (
        <Link className="btn bigBtn bold kakao" to={link}>KAKAO LOGIN</Link>
    )
}
export default KakaoLoginComponent;