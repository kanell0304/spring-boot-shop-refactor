import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getMemberById, modifyMember} from '../../../../api/memberApi';
import { formatDateToDot } from '../../../../util/dateUtil';
import '../../../../static/css/signup.scss';
import AddressSearch from '../../../AddressSearch';

const DEFAULT_FORM = {
  email: '',
  password: '',
  memberName: '',
  phoneNumber: '',
  wtrSns: false,
  zip_code: '',
  default_address: '',
  detailed_address: '',
  stockMileage: '',
  memberShip: ''
};

const MemberModifyComponent = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState(DEFAULT_FORM);
  const [ userData, setData ] = useState({});
  const [address, setAddress] = useState({
    zip_code: '',
    default_address: '',
    detailed_address: ''
  });

  useEffect(() => {
    getMemberById(id).then(data => {
      setData(data);
      setForm({
        ...DEFAULT_FORM,
        email: data.email || '',
        password: '',
        memberName: data.memberName || '',
        phoneNumber: data.phoneNumber || '',
        wtrSns: data.social || false,
        zip_code: data.address?.zip_code || '',
        default_address: data.address?.default_address || '',
        detailed_address: data.address?.detailed_address || '',
        stockMileage: data.stockMileage || '',
        memberShip: data.memberShip || ''
      });
      setAddress({
        zip_code: data.address?.zip_code || '',
        default_address: data.address?.default_address || '',
        detailed_address: data.address?.detailed_address || ''
      });
    });
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleCheckChange = (e) => {
    const { name, checked } = e.target;
    setForm(prev => ({ ...prev, [name]: checked }));
  };

  const handleAddressComplete = (newAddress) => {
    setAddress(newAddress);
    setForm(prev => ({
      ...prev,
      zip_code: newAddress.zip_code,
      default_address: newAddress.default_address,
      detailed_address: newAddress.detailed_address
    }));
  };

  const handleSubmit = () => {
    modifyMember(form)
      .then(() => {
        alert('회원 정보가 수정되었습니다.');
        navigate(-1);
      })
      .catch(() => alert('수정 중 오류가 발생했습니다.'));
  };

  return (
    <>
      <h2 className="pageTitle">회원 - 상세 & 수정</h2>
      <div className="pageContainer">
        <div className="borderSection">
          <div className="formSection signupSection">

            <div className='inputWrap'>
              <div className="inputTitle">아이디(이메일 주소)</div>
              <div className='info'>{form.email}</div>
            </div>

            <div className='inputWrap'>
              <div className="inputTitle">가입일</div>
              <div className='info'>{formatDateToDot(userData?.joinDate)}</div>
            </div>

            <div className='inputWrap'>
              <div className="inputTitle">회원등급</div>
              <select name="memberShip" value={form.memberShip} onChange={handleChange}>
                <option value="">등급 선택</option>
                <option value="BRONZE">BRONZE</option>
                <option value="SILVER">SILVER</option>
                <option value="GOLD">GOLD</option>
                <option value="PLATINUM">PLATINUM</option>
              </select>
            </div>

            <div className='inputWrap'>
              <div className="inputTitle"><span className='point'></span>마일리지</div>
              <div className="inputBox">
                <input name="stockMileage" value={form.stockMileage} onChange={handleChange} placeholder="마일리지" type="number" />
              </div>
            </div>

            <div className='inputWrap'>
              <div className="inputTitle"><span className='point'>[필수]</span>이름</div>
              <div className="inputBox">
                <input name="memberName" value={form.memberName} onChange={handleChange} placeholder="이름" type="text" />
              </div>
            </div>

            <div className='inputWrap'>
              <div className="inputTitle"><span className='point'>[필수]</span>연락처</div>
              <div className="inputBox">
                <input name="phoneNumber" value={form.phoneNumber} onChange={handleChange} placeholder="숫자만 입력" type="text" />
              </div>
            </div>

            <div className='inputWrap address'>
              <div className="inputTitle">주소</div>
              <AddressSearch
                setingAddress={address}
                onComplete={handleAddressComplete}
              />
            </div>

            <div>
                <label>
                <input type="checkbox" name='wtrSns' checked={form.wtrSns} onChange={handleCheckChange} />
                <span>[선택]</span> SNS 수신동의
                </label>
            </div>

            <button className='btn bigBtn black bold' onClick={handleSubmit}>완료</button>

          </div>
        </div>
      </div>
    </>
  );
};

export default MemberModifyComponent;