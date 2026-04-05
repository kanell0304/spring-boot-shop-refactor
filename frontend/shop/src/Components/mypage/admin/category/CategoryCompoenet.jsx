import React, { useState, useEffect, useRef } from 'react';
import { createCategory, categoryList, categoryModify, categoryDelete } from '../../../../api/categoryApi';
import { DndContext, closestCenter } from '@dnd-kit/core';
import CategoryForm from './CategoryForm';
import CategoryList from './CategoryList';

const CategoryComponent = () => {
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({ categoryName: '', parentId: null, viewStatus: false });
  const [editingId, setEditingId] = useState(null);
  const [editingName, setEditingName] = useState('');
  const inputRef = useRef(null);

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    if (editingId !== null && inputRef.current) inputRef.current.focus();
  }, [editingId]);

  const fetchCategories = () => {
    categoryList().then(data => setCategories(data));
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const validateForm = () => {
    if (!form.categoryName) {
      alert("분류명을 입력해주세요");
      return false;
    }
    return true;
  };

  const handleSubmit = () => {
    if (!validateForm()) return;
    createCategory(form).then(() => {
      setForm({ categoryName: '', parentId: null, viewStatus: false });
      fetchCategories();
      window.dispatchEvent(new Event('categoryUpdated'));
    });
  };

  const handleEdit = (id, name) => {
    setEditingId(id);
    setEditingName(name);
    window.dispatchEvent(new Event('categoryUpdated'));
  };

  const handleEditChange = (e) => {
    setEditingName(e.target.value);
    window.dispatchEvent(new Event('categoryUpdated'));
  };

  const handleEditSave = (id) => {
    const payload = { categoryId: id, categoryName: editingName, parentId: null };
    categoryModify(payload).then(() => {
      alert('수정되었습니다.');
      setEditingId(null);
      setEditingName('');
      fetchCategories();
      window.dispatchEvent(new Event('categoryUpdated'));
    });
  };

  const handleEditCancel = () => {
    setEditingId(null);
    setEditingName('');
  };

  const handleDelete = (id) => {
    categoryDelete(id).then(() => {
      alert('삭제 되었습니다.');
      fetchCategories();
      window.dispatchEvent(new Event('categoryUpdated'));
    });
  };

  const handleDragEnd = (event) => {
    const { active, over } = event;
    if (!over) return;

    const draggedId = active.id;
    const targetId = over.id;

    const dragged = categories.find(c => c.id === draggedId) ||
      categories.flatMap(c => c.child || []).find(c => c.id === draggedId);

    const isDropOnRoot = targetId === "itemRoot";
    const target = isDropOnRoot ? null : categories.find(c => c.id === targetId);

    if (draggedId === targetId) return;
    if (dragged?.child?.length > 0) {
      alert("하위 카테고리가 있는 항목은 이동할 수 없습니다.");
      return;
    }
    if (!dragged) {
      console.warn("드래그된 항목을 찾을 수 없습니다.");
      return;
    }

    const payload = {
      categoryId: draggedId,
      categoryName: dragged.categoryName,
      parentId: isDropOnRoot ? null : targetId
    };

    categoryModify(payload).then(() => {
      fetchCategories();
      window.dispatchEvent(new Event('categoryUpdated'));
    });
  };

  return (
    <div className="myPageComponent">
      <h2 className="pageTitle">분류</h2>
      <div className="pageContainer">

        <div className="borderSection">
          <CategoryForm
            form={form}
            onChange={handleChange}
            onSubmit={handleSubmit}
          />
        </div>

        <div className="borderSection">
          <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
            <CategoryList
              categories={categories}
              editingId={editingId}
              editingName={editingName}
              inputRef={inputRef}
              onEdit={handleEdit}
              onEditChange={handleEditChange}
              onEditSave={handleEditSave}
              onEditCancel={handleEditCancel}
              onDelete={handleDelete}
            />
          </DndContext>
        </div>

      </div>
    </div>
  );
};

export default CategoryComponent;
