import { Link } from "react-router-dom";
import BasicLayout from "../../layout/BasicLayout";
import '../../static/css/signup.scss'

const SigunpComplete =()=>{
    return(
        <BasicLayout>
            <div className="complete innerWrap">
                <div className="completeTextbox">
                    <div>
                        <h2>WELCOME</h2>
                        <p>Your journey in mindful style begins here.</p>
                    </div>
                    <div>
                        <h3>환영합니다.</h3>
                        <p>당신의 스타일 여정이 지금 시작됩니다.</p>
                    </div>
                    <Link className="btn bigBtn bold" to="/">SHOP NOW</Link>
                </div>
            </div>
        </BasicLayout>
    );
}
export default SigunpComplete;