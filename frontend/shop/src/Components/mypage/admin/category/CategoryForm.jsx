import React from 'react';

const CategoryForm = ({ form, onChange, onSubmit }) => {
  return (
    <div className='inputWrap'>
      <div className="inputTitle">분류등록</div>
      <div className="inputBox">
        <input
          name="categoryName"
          value={form.categoryName}
          onChange={onChange}
          placeholder="분류명을 입력해주세요."
          type="text"
        />
        <button type="button" className='btn black' onClick={onSubmit}>분류등록</button>
      </div>
    </div>
  );
};

export default CategoryForm;