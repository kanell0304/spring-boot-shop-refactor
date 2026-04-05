import { useState, useEffect } from "react";
import { categoryList,getCategories } from "../api/categoryApi";

const CategorySelector = ({ onSelectCategory, selectedId }) => {
  const [categoryData, setCategoryData] = useState([]);
  const [selectedParent, setSelectedParent] = useState(null);
  const [selectedChild, setSelectedChild] = useState(null);
  const [registeredCategoryName, setRegisteredCategoryName] = useState("");
  const [registeredCategoryId, setRegisteredCategoryId] = useState(null);
  
  useEffect(() => {
    categoryList().then((data) => {
      setCategoryData(data);
  
      // ✅ selectedId로 초기 선택값 세팅
      if (selectedId) {
        let parentFound = null;
        let childFound = null;
  
        for (const parent of data) {
          if (parent.id === selectedId) {
            parentFound = parent;
            break;
          }
  
          const child = parent.child.find((c) => c.id === selectedId);
          if (child) {
            parentFound = parent;
            childFound = child;
            break;
          }
        }
  
        if (parentFound) setSelectedParent(parentFound);
        if (childFound) setSelectedChild(childFound);
  
        // ✅ 경로 및 ID 미리 표시
        const finalCategory = childFound || parentFound;
        if (finalCategory) {
          setRegisteredCategoryId(finalCategory.id);
          setRegisteredCategoryName(
            childFound
              ? `${parentFound.categoryName} > ${childFound.categoryName}`
              : parentFound.categoryName
          );
        }
      }
    });
  }, [selectedId]);

  const handleParentClick = (parent) => {
    setSelectedParent(parent);
    setSelectedChild(null);
  };

  const handleChildClick = (child) => {
    setSelectedChild(child);
  };

  const handleRegister = () => {
    const finalCategory = selectedChild || selectedParent;
    if (finalCategory) {
      setRegisteredCategoryId(finalCategory.id); // ✅ ID만 부모로 넘김
      setRegisteredCategoryName(
        selectedChild
          ? `${selectedParent.categoryName} > ${selectedChild.categoryName}`
          : selectedParent.categoryName
      );
      onSelectCategory(finalCategory.id); // ✅ 부모에 ID만 전달
    }
  };

  return (
    <div className="borderSection categorySelectWrap">
        <div className="inputTitle">분류등록</div>
        {categoryData?.length === 0 ? (
            <div>등록된 분류가 없습니다. 분류를 추가해주세요.</div>
        ) : (
            <>
            <div className="categorySelect">
                <div className="column">
                {categoryData.map((cat) => (
                    <button
                    key={cat.id}
                    className={`btn ${selectedParent?.id === cat.id ? "selected" : ""}`}
                    onClick={() => handleParentClick(cat)}
                    >
                        {selectedParent?.id === cat.id ? (`> ${cat.categoryName}`) : (cat.categoryName)}
                    </button>
                ))}
                </div>
                {selectedParent && selectedParent.child.length > 0 ? (
                <div className="column">
                    {selectedParent.child.map((child) => (
                    <button
                        key={child.id}
                        className={`btn ${selectedChild?.id === child.id ? "selected" : ""}`}
                        onClick={() => handleChildClick(child)}
                    >
                        {selectedChild?.id === child.id ? (`> ${child.categoryName}`):(child.categoryName)}
                    </button>
                    ))}
                </div>
                ) : null}

                <button className="btn black" onClick={handleRegister}>
                분류 등록
                </button>
            </div>
            <div className="selectedPath">
                적용된 분류: {registeredCategoryId ? registeredCategoryName : "선택된 분류가 없습니다."}
            </div>
            </>
        )}
    </div>
  );
};

export default CategorySelector;
