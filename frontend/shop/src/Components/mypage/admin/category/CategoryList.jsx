import React from 'react';
import { useDroppable, useDraggable } from '@dnd-kit/core';

const DroppableRoot = ({ children }) => {
  const { setNodeRef, isOver } = useDroppable({ id: "itemRoot" });

  return (
    <div
      ref={setNodeRef}
      className="categoryItemWrap"
      id="itemRoot"
      style={{
        padding: "19px 19px 100px",
        backgroundColor: isOver ? "#e0f7fa" : "#f9f9f9",
        transition: "0.3s",
      }}
    >
      {children}
    </div>
  );
};

const DroppableCategory = ({ category, children }) => {
  const { setNodeRef } = useDroppable({ id: category.id });

  return (
    <div ref={setNodeRef} className="categoryItemBox droppable">
      {children}
    </div>
  );
};

const DraggableCategory = ({ category, onEdit, onDelete }) => {
  const isDraggable = !category.child || category.child.length === 0;
  const { attributes, listeners, setNodeRef, transform } = useDraggable({
    id: category.id,
    disabled: !isDraggable,
  });

  const style = {
    transform: transform ? `translate(${transform.x}px, ${transform.y}px)` : undefined,
    opacity: isDraggable ? 1 : 0.5,
  };

  return (
    <div className='childItem' ref={setNodeRef} style={style}>
      <div className='categoryItem'>
        <div {...listeners} {...attributes} style={{ cursor: 'grab', flexGrow: 1 }}>
          {category.categoryName}
        </div>
        <button className='btn black' onClick={(e) => { e.stopPropagation(); onEdit(category.id, category.categoryName); }}>
          분류명 수정
        </button>
        <button className='btn line' onClick={(e) => { e.stopPropagation(); onDelete(category.id); }}>
          삭제
        </button>
      </div>
    </div>
  );
};

const CategoryList = ({
  categories,
  editingId,
  editingName,
  inputRef,
  onEdit,
  onEditChange,
  onEditSave,
  onEditCancel,
  onDelete
}) => {
  return (
    <DroppableRoot>
      {categories.length > 0 ? (
        categories.map((data) => (
          <DroppableCategory key={data.id} category={data}>
            {editingId === data.id ? (
              <div className="categoryItem">
                <input
                  type="text"
                  value={editingName}
                  onChange={onEditChange}
                  ref={inputRef}
                />
                <button className='btn black' onClick={() => onEditSave(data.id)}>저장</button>
                <button className='btn line' onClick={onEditCancel}>취소</button>
              </div>
            ) : (
              <>
                <DraggableCategory category={data} onEdit={onEdit} onDelete={onDelete} />
                {data.child && data.child.length > 0 && (
                  <div className="childCategoryWrap">
                    {data.child.map((child) => (
                      <DraggableCategory
                        key={child.id}
                        category={child}
                        onEdit={onEdit}
                        onDelete={onDelete}
                      />
                    ))}
                  </div>
                )}
              </>
            )}
          </DroppableCategory>
        ))
      ) : (
        <div className='noDataView'>등록된 분류가 없습니다.</div>
      )}
    </DroppableRoot>
  );
};

export default CategoryList;