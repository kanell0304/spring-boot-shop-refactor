import React, { useState } from 'react';
import AddressSearch from '../AddressSearch';
import { memberRegister, memberEmailSearch} from '../../api/memberApi';
import useCustomLogin from '../../hooks/useCustomLogin';
import '../../static/css/signup.scss';

const SignupForm = ({ onBack, snsAgree }) => {

  /** 이메일 중복확인  */
  const [findMember,findSet] = useState(false);

  /** 서버 로그인 및 이동 */
  const {doLogin, moveToPath} = useCustomLogin()

  /** 사용자 입력 정보 */
  const [form, setForm] = useState({
    // email: 'gmdtn89@naver.com',
    // password: 'user@1234',
    // memberName: '최흥수',
    // phoneNumber: '01040434240',
    // address: {
    //   zip_code: '07792',
    //   default_address: '서울 강서구 강서로 429',
    //   detailed_address: '218호호'
    // },
    // wtrSns: snsAgree,
    // social: false,
    // roleNames: ["USER"]
    email: '',
    password: '',
    memberName: '',
    phoneNumber: '',
    address: {
      zip_code: '',
      default_address: '',
      detailed_address: ''
    },
    wtrSns: snsAgree,
    social: false,
    roleNames: ["USER"]
  });

  // 입력 핸들링
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  // 주소 결과 받기
  const handleAddressComplete = (newAddress) => {
    setForm(prev => ({
      ...prev,
      address: {
        ...prev.address,
        ...newAddress
      }
    }));
  };

  // 상세주소 핸들링
  const handleAddressDetailChange = (e) => {
    const { value } = e.target;
    setForm(prev => ({
      ...prev,
      address: {
        ...prev.address,
        detailed_address: value
      }
    }));
  };

  // 유효성 검사 함수
  const validateForm = () => {

    const { email, password, memberName, phoneNumber } = form;
    if(!findMember) {
      alert("아이디 중복을 확인해주세요.");
      return false;
    };

    if (!email || !password || !memberName || !phoneNumber) {
      alert("필수 항목을 모두 입력해 주세요.");
      return false;
    }

    // 비밀번호 정규식 검사
    const pwRule = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_\-+=<>?{}[\]~])[A-Za-z\d!@#$%^&*()_\-+=<>?{}[\]~]{6,}$/;
    if (!pwRule.test(password)) {
      alert("비밀번호는 6자 이상이며, 영문/숫자/특수문자를 포함해야 합니다.");
      return false;
    }

    // 전화번호 숫자만
    const phoneRule = /^[0-9]{9,13}$/;
    if (!phoneRule.test(phoneNumber)) {
      alert("연락처는 숫자만 입력해야 합니다.");
      return false;
    }

    // // 주소 필수 체크 (우편번호, 기본주소)
    // if (!address.zip_code || !address.default_address) {
    //   alert("주소를 입력해주세요.");
    //   return false;
    // }

    return true;
  };

  const userEmailSearch = (email)=> {
    memberEmailSearch(form.email).then(
      data=>{
        if(data){
          findSet(true);
          alert("사용가능한 아이디 입니다.");
        }else{
          findSet(false);
          alert("이미 가입된 아이디 입니다.");
        }
      }
    )
  }


  const handleSubmit = () => {
    if (!validateForm()) return;
    console.log("회원가입 데이터:", form);
    // TODO: axios POST 요청으로 회원가입 전송

    memberRegister(form)
    .then(data=>{
      console.log(data)
      if(data.error) {
        alert("에러")
      }else {
        //alert("가입성공");
        doLogin({
          email :form.email,
          pw : form.password
        })
        .then(data=>{
          if(data.error) {
            alert("로그인 에러")
          }else {
            alert("가입이 완료");
            moveToPath('/member/welcome');
          }
        })

      }
    })


  };

  return (
    <div className="formSection signupSection">
      <h2>회원가입 <span>(2/2)</span></h2>

      <div className='inputWrap'>
        <div className="inputTitle"><span className='point'>[필수]</span>아이디</div>
        <div className="inputBox"><input name="email" value={form.email} onChange={handleChange} placeholder="이메일 입력" type="email" /><button type="button" className='btn black' onClick={userEmailSearch}>중복확인</button></div>
      </div>
      <div className='inputWrap'>
        <div className="inputTitle"><span className='point'>[필수]</span>비밀번호</div>
        <div className="inputBox"><input name="password" value={form.password} onChange={handleChange} placeholder="비밀번호" type="password" /></div>
      </div>
      <div className='inputWrap'>
        <div className="inputTitle"><span className='point'>[필수]</span>이름</div>
        <div className="inputBox"><input name="memberName" value={form.memberName} onChange={handleChange} placeholder="이름" type="text" /></div>
      </div>
      <div className='inputWrap'>
        <div className="inputTitle"><span className='point'>[필수]</span>연락처</div>
        <div className="inputBox"><input name="phoneNumber" value={form.phoneNumber} onChange={handleChange} placeholder="숫자만 입력" type="text" /></div>
      </div>
      <div className='inputWrap address'>
        <div className="inputTitle">주소</div>
        <AddressSearch onComplete={handleAddressComplete} />
      </div>

      <button className='btn bigBtn black bold' onClick={handleSubmit}>완료</button>
    </div>
  );
};

export default SignupForm;
