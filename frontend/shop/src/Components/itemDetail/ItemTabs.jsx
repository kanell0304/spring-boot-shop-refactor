<<<<<<< HEAD
import React, { useState } from 'react';
import ItemShippingRefund from '../../Pages/shop/ItemShippingRefund';
import ItemQnA from '../../Pages/shop/ItemQnA'
import ItemDetails from '../../Pages/shop/ItemDetails';
import ItemReview from '../../Pages/shop/ItemReview';

const ItemTabs = () => {
  const [activeTab, setActiveTab] = useState('DETAILS');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  return (
    <div>
      <div className="tabMenu">
        <button
          className={activeTab === 'DETAILS' ? 'active' : ''}
          onClick={() => handleTabClick('DETAILS')}
        >
          DETAILS
        </button>
        <button
          className={activeTab === 'REVIEWS' ? 'active' : ''}
          onClick={() => handleTabClick('REVIEWS')}
        >
          REVIEWS
        </button>
        <button
          className={activeTab === 'Q&A' ? 'active' : ''}
          onClick={() => handleTabClick('Q&A')}
        >
          Q&A
        </button>
        <button
          className={activeTab === 'SHIPPING & REFUNDS' ? 'active' : ''}
          onClick={() => handleTabClick('SHIPPING & REFUNDS')}
        >
          SHIPPING & REFUNDS
        </button>
      </div>

      <div className="tabContent">
        {activeTab === 'DETAILS' && <ItemDetails/>}
        {activeTab === 'REVIEWS' && <ItemReview/>}
        {activeTab === 'Q&A' && <ItemQnA/>}
        {activeTab === 'SHIPPING & REFUNDS' && <ItemShippingRefund/>}
      </div>
    </div>
  )
}

=======
import React, { useState } from 'react';
import ItemShippingRefund from '../../Pages/shop/ItemShippingRefund';
import ItemQnA from '../../Pages/shop/ItemQnA'
import ItemDetails from '../../Pages/shop/ItemDetails';
import ItemReview from '../../Pages/shop/ItemReview';

const ItemTabs = () => {
  const [activeTab, setActiveTab] = useState('DETAILS');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  return (
    <div>
      <div className="tabMenu">
        <button
          className={activeTab === 'DETAILS' ? 'active' : ''}
          onClick={() => handleTabClick('DETAILS')}
        >
          DETAILS
        </button>
        <button
          className={activeTab === 'REVIEWS' ? 'active' : ''}
          onClick={() => handleTabClick('REVIEWS')}
        >
          REVIEWS
        </button>
        <button
          className={activeTab === 'Q&A' ? 'active' : ''}
          onClick={() => handleTabClick('Q&A')}
        >
          Q&A
        </button>
        <button
          className={activeTab === 'SHIPPING & REFUNDS' ? 'active' : ''}
          onClick={() => handleTabClick('SHIPPING & REFUNDS')}
        >
          SHIPPING & REFUNDS
        </button>
      </div>

      <div className="tabContent">
        {activeTab === 'DETAILS' && <ItemDetails/>}
        {activeTab === 'REVIEWS' && <ItemReview/>}
        {activeTab === 'Q&A' && <ItemQnA/>}
        {activeTab === 'SHIPPING & REFUNDS' && <ItemShippingRefund/>}
      </div>
    </div>
  )
}

>>>>>>> 6c80c21440fd34d348db1950f2af8e1e895ca51a
export default ItemTabs;