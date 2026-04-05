import { useSearchParams } from "react-router-dom";
import { getAccessToken, getMemberWithAccessToken } from "../../api/kakaoApi";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { login } from "../../slices/loginSlice";
import useCustomLogin from "../../hooks/useCustomLogin";

const KakaoRedirectPage = () => {
    const [searchParams] = useSearchParams();

    const {moveToPath} = useCustomLogin();

    const dispatch = useDispatch();

    const authCode = searchParams.get("code"); // 인가코드 추출

    useEffect(() => {
        // 나 -> 카카오
        getAccessToken(authCode).then(accessToken => {
            console.log(accessToken);
            getMemberWithAccessToken(accessToken).then(memberInfo => {
                console.log(memberInfo);
                dispatch(login(memberInfo));

                // 소셜 회원이 아니라면
                if (memberInfo && !memberInfo.social) {
                    moveToPath("/")
                } else {
                    // moveToPath("/member/modify")
                    moveToPath("/")
                }
            })
        })
    }, [authCode]) // 인가코드가 변경 될 때마다, 새로운 access token을 발급 받음

    return (
        <div>
            <h2>Kakao Login Redirect</h2>
            <div>{authCode}</div>
        </div>
    )
}

export default KakaoRedirectPage;