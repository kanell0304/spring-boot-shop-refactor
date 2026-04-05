import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ImageUploadComponent from '../../../ImageUploadComponent';
import EditorComponent from '../../../EditorComponent';
import { getEvent, postEvent, putEvent, deleteEvent } from '../../../../api/BoardApi';
import '../../../../static/css/adminProduct.scss';

const DEFAULT_FORM = {
  eventListId: "",
  title: "",
  writer: "관리자",
  content: "",
};

const EventFormComponent = () => {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [form, setForm] = useState(DEFAULT_FORM);
  const [images, setImages] = useState([null, null, null, null]);
  const [content, setContent] = useState('');

  useEffect(() => {
    if (isEdit) {
      getEvent(id).then(data => {
        setForm({ ...DEFAULT_FORM, ...data });
        setContent(data.content || '');

        const serverImages = (data.uploadFileNames || []).map(name => ({
          url: `http://localhost:8081/upload/${name}`,
          file: null,
        }));

        setImages([...serverImages, ...Array(4).fill(null)].slice(0, 4));
      });
    }
  }, [id, isEdit]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleDelete = () => {
    deleteEvent(id).then(() => {
      alert("글 삭제(게시중단) 되었습니다.");
      navigate(-1);
    });
  };

  const handleFormSubmit = async () => {
    if (!form.writer.trim()) return alert('작성자를 입력해주세요');
    if (!form.title.trim()) return alert('글제목을 입력해주세요');



    try {
      if (isEdit) {
        const formData = new FormData();
        formData.append("eventListId", form.eventListId);
        formData.append("title", form.title);
        formData.append("writer", form.writer);
        formData.append("content", content);
        formData.append("delFlag", false);
    
        images.forEach(img => {
          if (img?.file) {
            formData.append("files", img.file);
          }
        });


        formData.append("eventListId", form.eventListId);
        await putEvent(formData);
        alert('글 수정이 완료되었습니다.');
      } else {
        const formData = new FormData();
        formData.append("title", form.title);
        formData.append("writer", form.writer);
        formData.append("content", content);
        images.forEach(img => {
          if (img?.file) {
            formData.append("files", img.file);
          }
        });

        await postEvent(formData);
        alert('글 등록이 완료되었습니다.');
      }
      navigate(-1);
    } catch (err) {
      console.error(err);
      alert('처리 중 오류가 발생했습니다.');
    }
  };

  return (
    <>
      <h2 className="pageTitle">이벤트 - {isEdit ? '글 수정' : '글 등록'}</h2>
      <div className="pageContainer product">
        <ImageUploadComponent images={images} setImages={setImages} />

        <div className="borderSection">
          <div className='inputWrap'>
            <div className="inputTitle"><span className='point'>[필수]</span>작성자</div>
            <div className="inputBox">
              <input name="writer" value={form.writer} onChange={handleInputChange} placeholder="작성자" type="text" />
            </div>
          </div>
        </div>

        <div className="borderSection">
          <div className='inputWrap'>
            <div className="inputTitle"><span className='point'>[필수]</span>글제목</div>
            <div className="inputBox">
              <input name="title" value={form.title} onChange={handleInputChange} placeholder="글제목을 입력해주세요." type="text" />
            </div>
          </div>
        </div>

        <div className="borderSection">
          <div className='inputWrap'>
            <div className="inputTitle">글 내용</div>
            <EditorComponent value={content} onChange={setContent} />
          </div>
        </div>
      </div>

      <div className='itemSubMenu'>
        {isEdit && (<button className='btn line' onClick={handleDelete}>글삭제</button>)}
        <button className='btn black' onClick={handleFormSubmit}>{isEdit ? '글 수정' : '글 등록'}</button>
      </div>
    </>
  );
};

export default EventFormComponent;