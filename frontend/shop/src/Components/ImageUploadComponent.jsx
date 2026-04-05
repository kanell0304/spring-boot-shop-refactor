import { useRef, useEffect } from 'react';
import defaultImg from "../static/images/default.png"

const ImageUploadComponent = ({ images, setImages }) => {
  const fileInputRefs = useRef([]);

  // useEffect(() => {
  //   fileInputRefs.current = new Array(4).fill(null);
  // }, []);

  const handleFileChange = (e, index) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      const newImages = [...images];
      newImages[index] = { file, preview: reader.result };
      setImages(newImages);
    };
    reader.readAsDataURL(file);

    e.target.value = '';
  };

  const handleRemoveImage = (index) => {
    const newImages = [...images];
    newImages[index] = null;
    setImages(newImages);
  };

  const triggerFileInput = (index) => {
    if (fileInputRefs.current[index]) {
      fileInputRefs.current[index].click();
    }
  };

  return (
    <div className="borderSection imageswrap">
      <div className="inputWrap">
        <div className="thumbnail">
          <div className="inputTitle"><span className="point">[필수]</span>섬네일 등록</div>
          <div className="thumbArea">
            <div className="imageSlot thumb">
              {images[0] ? (
                <div className="imagePreview">
                  <img
                    src={images[0]?.preview || images[0]?.url || defaultImg}
                    alt="섬네일 이미지"
                  />
                  <button type="button" className="imagDelete" onClick={() => handleRemoveImage(0)}>✕</button>
                </div>
              ) : (
                <div className="imagePlaceholder" onClick={() => triggerFileInput(0)}>이미지 등록</div>
              )}
              <input
                type="file"
                accept="image/*"
                ref={el => fileInputRefs.current[0] = el}
                style={{ display: 'none' }}
                onChange={(e) => handleFileChange(e, 0)}
              />
            </div>
          </div>
        </div>

        <div className="extraArea">
          <div className="inputTitle">추가이미지</div>
          <div className="extraImages">
            {[1, 2, 3].map(idx => (
              <div key={idx} className="imageSlot extra">
                {images[idx] ? (
                  <div className="imagePreview">
                    <img
                      src={images[idx]?.preview || images[idx]?.url || defaultImg}
                      alt={`추가 이미지 ${idx}`}
                    />
                    <button type="button" className="imagDelete" onClick={() => handleRemoveImage(idx)}>✕</button>
                  </div>
                ) : (
                  <div className="imagePlaceholder" onClick={() => triggerFileInput(idx)}>이미지 등록</div>
                )}
                <input
                  type="file"
                  accept="image/*"
                  ref={el => fileInputRefs.current[idx] = el}
                  style={{ display: 'none' }}
                  onChange={(e) => handleFileChange(e, idx)}
                />
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ImageUploadComponent;
