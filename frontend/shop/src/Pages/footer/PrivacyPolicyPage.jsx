import BasicLayout from "../../layout/BasicLayout";
import PrivacyPolicy from '../../Components/info/PrivacyPolicy'

const PrivacyPolicyPage = () => {
  return (
    <BasicLayout>
      <div className="innerWrap">
        <h2>개인정보 처리 방침</h2>
        <div className="discripton">
            <PrivacyPolicy/>
        </div>
      </div>
    </BasicLayout>
  );
};

export default PrivacyPolicyPage;