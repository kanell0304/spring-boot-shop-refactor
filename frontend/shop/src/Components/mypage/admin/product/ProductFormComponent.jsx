import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import CategorySelector from '../../../CategorySelector';
import ImageUploadComponent from '../../../ImageUploadComponent';
import EditorComponent from '../../../EditorComponent';
import useCustomLogin from '../../../../hooks/useCustomLogin';
import { getProductById, deleteProduct, updateProduct, registerProduct } from '../../../../api/productApi';
import '../../../../static/css/adminProduct.scss';

const DEFAULT_FORM = {
  name: '',
  description: '',
  totalScore: '',
  price: 0,
  discountRate: 0,
  delFlag: false,
  options: [],
  info: { 브랜드: '노드', 원산지: '한국' }
};

const ProductFormComponent = () => {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const [form, setForm] = useState(DEFAULT_FORM);
  const [selectedCategoryId, setSelectedCategoryId] = useState(null);
  const [images, setImages] = useState([null, null, null, null]);
  const [optionName, setOptionName] = useState('');
  const [options, setOptions] = useState([{ optionValue: '', optionPrice: '', stockQty: '' }]);
  const [content, setContent] = useState('');
  const { moveToPath } = useCustomLogin();

  useEffect(() => {
    if (isEdit) {
      getProductById(id).then(data => {
        setForm({ ...DEFAULT_FORM, ...data, info: data.info || { 브랜드: '', 원산지: '' } });
        setSelectedCategoryId(data.categoryId);
        const serverImages = (data.uploadFileNames || []).map(name => ({
          url: `http://localhost:8081/upload/${name}`,
          file: null
        }));
        setImages(serverImages.concat(Array(4).fill(null)).slice(0, 4));
        setOptions(data.options || [{ optionValue: '', optionPrice: 0, stockQty: 0 }]);
        setOptionName(data.options?.[0]?.optionName || '');
        setContent(data.description || '');
      });
    }
  }, [id, isEdit]);

  const handleInputChange = ({ target: { name, value } }) => setForm(prev => ({ ...prev, [name]: value }));

  const handleInfoChange = (key, value) => setForm(prev => ({ ...prev, info: { ...prev.info, [key]: value } }));

  const handleOptionChange = (index, key, value) => {
    const newOptions = [...options];
    newOptions[index][key] = value;
    setOptions(newOptions);
  };

  const handleRemoveOption = (index) => setOptions(options.filter((_, i) => i !== index));

  const handleDelete = () => deleteProduct(id).then(() => alert("상품이 삭제(판매중지) 되었습니다."));

  const handleFormSubmit = async () => {
    if (!form.name.trim()) return alert('상품명을 입력해주세요.');
    if (!form.price || isNaN(Number(form.price))) return alert('원가를 숫자로 입력해주세요.');
    if (!selectedCategoryId) return alert('카테고리를 선택해주세요.');
    if (!optionName.trim()) return alert('옵션명을 입력해주세요.');
    if (options.length === 0 || options.some(opt => !opt.optionValue.trim())) return alert('모든 옵션 값을 입력해주세요.');
    if (!images[0] && !isEdit) return alert('최소 하나의 이미지를 등록해주세요.');

    const dto = {
      ...form,
      description: content,
      discountRate: Number(form.discountRate),
      options: options.map(opt => ({
        optionName,
        optionValue: opt.optionValue,
        optionPrice: Number(opt.optionPrice),
        stockQty: Number(opt.stockQty)
      }))
    };

    const filesToUpload = images.filter(img => img?.file).map(img => img.file);

    try {
      if (isEdit) {
        dto.uploadFileNames = images.filter(img => img && !img.file && img.url).map(img => img.url.split('/').pop());
        await updateProduct({ id, itemDTO: dto, files: filesToUpload });
        alert('상품 수정이 완료되었습니다.');
      } else {
        await registerProduct({ itemDTO: dto, categoryId: selectedCategoryId, files: filesToUpload });
        alert('상품 등록이 완료되었습니다.');
      }
      navigate(-1);
    } catch (err) {
      console.error(err);
      alert('상품 처리 중 오류가 발생했습니다.');
    }
  };

  return (
    <>
      <h2 className="pageTitle">상품 - {isEdit ? '상품 수정' : '상품 등록'}</h2>
      <div className="pageContainer product">
        <CategorySelector selectedId={selectedCategoryId} onSelectCategory={setSelectedCategoryId} />
        <ImageUploadComponent images={images} setImages={setImages} />
        <div className="borderSection">
          <div className='inputWrap'>
            <div className="inputTitle"><span className='point'>[필수]</span>상품명</div>
            <div className="inputBox">
              <input name="name" value={form.name} onChange={handleInputChange} placeholder="상품명을 입력해주세요." type="text" />
            </div>
          </div>
        </div>
        <div className="borderSection">
          <div className='inputWrap'>
            <div className="inputTitle"><span className='point'>[필수]</span>원가</div>
            <div className="inputBox">
              <input name="price" value={form.price} onChange={handleInputChange} placeholder="원가를 입력해주세요." type="number" />
            </div>
          </div>
        </div>
        <div className="borderSection">
          <div className='inputWrap'>
            <div className="inputTitle">할인율</div>
            <div className="inputBox">
              <input name="discountRate" value={form.discountRate} onChange={handleInputChange} placeholder="할인율 입력해주세요." type="number" />
            </div>
          </div>
        </div>
        <div className="borderSection optionWrap">
          <div className="inputWrap">
            <div className="inputTitle"><span className="point">[필수]</span>옵션</div>
            <div className="inputBox">
              <input type="text" placeholder="옵션명 예) SIZE" value={optionName} onChange={e => setOptionName(e.target.value)} />
              <button className='btn black' type="button" onClick={() => setOptions([...options, { optionValue: '', optionPrice: 0, stockQty: 0 }])}>옵션 추가</button>
            </div>
            <div className='optionItemsWrap'>
              {options.map((opt, idx) => (
                <div key={idx} className="optionItem">
                  <div>
                    <span>해당 옵션명</span>
                    <input type="text" placeholder="옵션" value={opt.optionValue} onChange={e => handleOptionChange(idx, 'optionValue', e.target.value)} />
                  </div>
                  <div>
                    <span>가격(원)</span>
                    <input type="number" placeholder="가격(원)" value={opt.optionPrice} onChange={e => handleOptionChange(idx, 'optionPrice', e.target.value)} />
                  </div>
                  <div>
                    <span>재고수량</span>
                    <input type="number" placeholder="재고수량" value={opt.stockQty} onChange={e => handleOptionChange(idx, 'stockQty', e.target.value)} />
                  </div>
                  <button className='btn black' type="button" onClick={() => handleRemoveOption(idx)}>삭제</button>
                </div>
              ))}
            </div>
          </div>
        </div>
        <div className="borderSection">
          <div className='inputWrap'>
            <div className="inputTitle">제조정보</div>
          </div>
          <div className='innerInput'>
            <div className='inputWrap'>
              <div className="inputTitle">브랜드</div>
              <div className="inputBox">
                <input value={form.info.브랜드} onChange={e => handleInfoChange('브랜드', e.target.value)} placeholder="브랜드명을 입력해주세요." type="text" />
              </div>
            </div>
            <div className='inputWrap'>
              <div className="inputTitle">제조국</div>
              <div className="inputBox">
                <input value={form.info.원산지} onChange={e => handleInfoChange('원산지', e.target.value)} placeholder="제조국을 입력해주세요." type="text" />
              </div>
            </div>
          </div>
        </div>
        <div className="borderSection">
          <div className='inputWrap'>
            <div className="inputTitle">상세정보</div>
            <EditorComponent value={content} onChange={setContent} />
          </div>
        </div>
      </div>
      <div className='itemSubMenu'>
        {isEdit && <button className='btn line' onClick={handleDelete}>상품삭제</button>}
        <button className='btn black' onClick={handleFormSubmit}>{isEdit ? '상품 수정' : '상품 등록'}</button>
      </div>
    </>
  );
};

export default ProductFormComponent;