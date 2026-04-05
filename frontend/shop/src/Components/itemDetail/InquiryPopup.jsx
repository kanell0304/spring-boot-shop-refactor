<<<<<<< HEAD
import React, { useState } from 'react';

const InquiryPopup = ({ onClose, onSave }) => {
  const [inquiryTitle, setInquiryTitle] = useState('');
  const [inquiryContent, setInquiryContent] = useState('');

  const handleTitleChange = (event) => {
    setInquiryTitle(event.target.value);
  };

  const handleContentChange = (event) => {
    setInquiryContent(event.target.value);
  };

  const handleCancelClick = () => {
    onClose();
  };

  const handleSaveClick = () => {
    onSave(inquiryTitle, inquiryContent);
  };

  return (
    <div className="PopupBox">
      <div className="PopupContainer">
        <div className="popupHeader">
          <h2>문의 작성</h2>
        </div>
        <div className="inputTitleBox">
          <input
            type="text"
            placeholder="문의 제목을 입력해주세요."
            value={inquiryTitle}
            onChange={handleTitleChange}
          />
        </div>
        <div className="inputContentBox">
          <textarea
            placeholder="문의 내용을 입력해주세요."
            value={inquiryContent}
            onChange={handleContentChange}
          />
        </div>
        <div className="buttonBox">
          <button className="cancelButton" onClick={handleCancelClick}>
            CANCEL
          </button>
          <button className="saveButton" onClick={handleSaveClick}>
            SAVE
          </button>
        </div>
      </div>
    </div>
  )
}

=======
import React, { useState } from 'react';

const InquiryPopup = ({ onClose, onSave }) => {
  const [inquiryTitle, setInquiryTitle] = useState('');
  const [inquiryContent, setInquiryContent] = useState('');

  const handleTitleChange = (event) => {
    setInquiryTitle(event.target.value);
  };

  const handleContentChange = (event) => {
    setInquiryContent(event.target.value);
  };

  const handleCancelClick = () => {
    onClose();
  };

  const handleSaveClick = () => {
    onSave(inquiryTitle, inquiryContent);
  };

  return (
    <div className="PopupBox">
      <div className="PopupContainer">
        <div className="popupHeader">
          <h2>문의 작성</h2>
        </div>
        <div className="inputTitleBox">
          <input
            type="text"
            placeholder="문의 제목을 입력해주세요."
            value={inquiryTitle}
            onChange={handleTitleChange}
          />
        </div>
        <div className="inputContentBox">
          <textarea
            placeholder="문의 내용을 입력해주세요."
            value={inquiryContent}
            onChange={handleContentChange}
          />
        </div>
        <div className="buttonBox">
          <button className="cancelButton" onClick={handleCancelClick}>
            CANCEL
          </button>
          <button className="saveButton" onClick={handleSaveClick}>
            SAVE
          </button>
        </div>
      </div>
    </div>
  )
}

>>>>>>> 6c80c21440fd34d348db1950f2af8e1e895ca51a
export default InquiryPopup;