import React, { useState } from 'react';
import Terms from '../info/Terms';
import PrivacyPolicy from '../info/PrivacyPolicy';
import '../../static/css/signup.scss';

const TermsAgreement = ({ onNext }) => {
  /** 체크 상태 확인 */
  const [checked, setChecked] = useState({
    all: false,
    terms: false,
    privacy: false,
    age: false,
    sns: false
  });

  /** 전체 동의 체크 시 모든 항목 변경 */
  const handleAllChange = () => {
    const next = !checked.all;
    setChecked({
      all: next,
      terms: next,
      privacy: next,
      age: next,
      sns: next
    });
  };

  // 개별 체크 변경
  const handleCheckChange = (key) => {
    const next = { ...checked, [key]: !checked[key] };
    // 필수항목이 모두 true면 all 도 true
    const allChecked = next.terms && next.privacy && next.age && next.sns;
    const requiredAllChecked = next.terms && next.privacy && next.age;
    next.all = allChecked;
    setChecked(next);
  };

  // 다음 버튼 클릭 시 필수항목 유효성 검사
  const handleNext = () => {
    if (!(checked.terms && checked.privacy && checked.age)) {
      alert('필수 약관에 모두 동의해 주세요.');
      return;
    }
    onNext(checked.sns);
  };

  return (
    <div className="termsSection signupSection">
      <h2>회원가입 <span>(1/2)</span></h2>

      <div className='selectInputWrap checkbox'>
        <input id='all' type="checkbox" checked={checked.all} onChange={handleAllChange} />
        <label for="all">이용약관 전체동의</label>
      </div>

      <div className='discriptionWrap'>
        <div className='selectInputWrap checkbox'>
          <input id='terms' type="checkbox" checked={checked.terms} onChange={()=>handleCheckChange('terms')} />
          <label for="terms"><span className="point">[필수]</span> 쇼핑몰 이용약관</label>
        </div>
        <div className="discription">
          <Terms />
        </div>
      </div>

      <div className='discriptionWrap'>
        <div className='selectInputWrap checkbox '>
          <input id='privacy' type="checkbox" checked={checked.privacy} onChange={() => handleCheckChange('privacy')} />
          <label for="privacy"><span className="point">[필수]</span> 개인정보 수집 및 이용</label>
        </div>
        <div className="discription">
          <PrivacyPolicy />
        </div>
      </div>

      <div className='discriptionWrap'>
        <div className='selectInputWrap checkbox'>
          <input id='age' type="checkbox" checked={checked.age} onChange={() => handleCheckChange('age')} />
          <label for="age"><span className="point">[필수]</span> 만 14세 이상</label>
        </div>
      </div>

      <div>
        <div className='selectInputWrap checkbox'>
          <input id='sns' type="checkbox" checked={checked.sns} onChange={() => handleCheckChange('sns')} />
          <label for="sns"><span>[선택]</span> SNS 수신동의</label>
        </div>
      </div>

      <button className='btn bigBtn bold black' onClick={handleNext}>다음</button>
    </div>
  );
};

export default TermsAgreement;
