import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { fetchItems } from '../../api/categoryItemApi';
import BasicLayout from '../../layout/BasicLayout';
import Pagination from '../../components/Pagination';

const CategoryPage = () => {
  const { categoryId } = useParams();
  const [categoryData, setCategoryData] = useState(null);

  useEffect(() => {
    fetchItems(categoryId).then(data => setCategoryData(data));
  }, [categoryId]);

  if (!categoryData) return <div>Loading...</div>;

  const category = categoryData.content[0];
  const pageInfo = categoryData

  console.log(pageInfo.number)


  


  return (
    <BasicLayout>
      <div className="innerWrap">
        <h2>{category.categoryName} 카테고리의 상품</h2>
        <div className="itemList">
          {category.categoryItems.map(({ id, item }) => (
            <div key={id} className="itemCard">
              <h3>{item.name}</h3>
              <p>{item.description}</p>
              <p>가격: {item.price}원</p>
              <p>할인율: {item.discountRate}%</p>
              <ul>
                {item.options.map((opt) => (
                  <li key={opt.id}>
                    {opt.optionName} - {opt.optionValue} ({opt.stockQty}개)
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
        <Pagination
  pageInfo={pageInfo}
  onPageChange={(page) => setPage(page - 1)}
/>
      </div>
    </BasicLayout>
  );
};

export default CategoryPage;
