import { useState } from 'react';

const ReviewPopup = ({ onClose, onSave }) => {
  const [reviewTitle, setReviewTitle] = useState('');
  const [reviewContent, setReviewContent] = useState('');
  const [previews, setPreviews] = useState([]);
  const [files, setFiles] = useState([]);
  const [rating, setRating] = useState(0);

  const handleImageChange = (event) => {
    const selected = Array.from(event.target.files);
    if (files.length + selected.length > 4) {
      alert('이미지는 최대 4개까지 첨부 가능합니다.');
      return;
    }
    setFiles((prev) => [...prev, ...selected]);
    selected.forEach((file) => {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviews((prev) => [...prev, reader.result]);
      };
      reader.readAsDataURL(file);
    });
  };

  const handleRemoveImage = (index) => {
    setPreviews((prev) => prev.filter((_, i) => i !== index));
    setFiles((prev) => prev.filter((_, i) => i !== index));
  };

  const handleSaveClick = () => {
    if (!rating) {
      alert('별점을 선택해주세요.');
      return;
    }
    if (!reviewTitle.trim()) {
      alert('리뷰 제목을 입력해주세요.');
      return;
    }
    if (!reviewContent.trim()) {
      alert('리뷰 내용을 입력해주세요.');
      return;
    }
    onSave({ title: reviewTitle, content: reviewContent, files, rating });
  };

  return (
    <div className="popupBox">
      <div className="popupContainer">
        <div className="popupHeader">
          <h2>Review</h2>
        </div>

        <div className="ratingSection">
          <span>별점</span>
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
              type="button"
              className={`starButton ${star <= rating ? 'active' : ''}`}
              onClick={() => setRating(star)}
            >
              ★
            </button>
          ))}
          <span>{rating > 0 ? `${rating}점` : ''}</span>
        </div>

        <div className="addImageSection">
          <div className="imagePreview">
            {previews.map((src, index) => (
              <div key={index} className="previewImage">
                <img src={src} alt={`preview-${index}`} />
                <button type="button" className="removeImageBtn" onClick={() => handleRemoveImage(index)}>×</button>
              </div>
            ))}
          </div>
          <label htmlFor="imageInput" className="addImageButton">
            이미지 추가 ({files.length}/4)
          </label>
          <input
            id="imageInput"
            type="file"
            multiple
            accept="image/*"
            onChange={handleImageChange}
            style={{ display: 'none' }}
          />
        </div>

        <div className="inputTitleBox">
          <input
            type="text"
            placeholder="리뷰 제목을 입력해주세요."
            value={reviewTitle}
            onChange={(e) => setReviewTitle(e.target.value)}
          />
        </div>

        <div className="inputContentBox">
          <textarea
            placeholder="리뷰 내용을 입력해주세요."
            value={reviewContent}
            onChange={(e) => setReviewContent(e.target.value)}
          />
        </div>

        <div className="buttonBox">
          <button className="cancelButton" type="button" onClick={onClose}>CANCEL</button>
          <button className="saveButton" type="button" onClick={handleSaveClick}>SAVE</button>
        </div>
      </div>
    </div>
  );
};

export default ReviewPopup;
