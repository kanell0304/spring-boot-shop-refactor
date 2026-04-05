import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getMagazine, getMagazineList, incrementMagazineViewCount } from "../../api/BoardApi";
import { formatDateToDot } from "../../util/dateUtil";

const MagazineDetailComponent = () => {
  const { id } = useParams();
  const [magazine, setMagazine] = useState(null);
  const [magazineList, setMagazineList] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(null);
  const navigate = useNavigate();

  const isPrevDisabled = currentIndex === magazineList.length - 1;
  const isNextDisabled = currentIndex === 0;
  const displayIndex = magazineList.length - currentIndex;

  const fetchMagazineList = async () => {
    try {
      const response = await getMagazineList(0, 100);
      setMagazineList(response.content || []);
    } catch (error) {
      console.error("매거진 목록 가져오기 실패:", error);
    }
  };

  const handlePrev = () => {
    if (currentIndex < magazineList.length - 1) {
      const prevId = magazineList[currentIndex + 1]?.magazineListId;
      navigate(`/magazine/detail/${prevId}`);
    }
  };

  const handleNext = () => {
    if (currentIndex > 0) {
      const nextId = magazineList[currentIndex - 1]?.magazineListId;
      navigate(`/magazine/detail/${nextId}`);
    }
  };

  const handleList = () => {
    navigate('/magazine/list?page=0&size=10');
  };

  useEffect(() => {
    getMagazine(id).then(setMagazine);
    fetchMagazineList();
    incrementMagazineViewCount(id);
  }, [id]);

  useEffect(() => {
    if (id && magazineList.length > 0) {
      const index = magazineList.findIndex((m) => m.magazineListId === parseInt(id));
      setCurrentIndex(index);
    }
  }, [id, magazineList]);

  if (!magazine) return null;

  return (
    <>
      <div className="rightNavLayoutWrap">
        <section className="rightNavLayoutContainer">
          <div className="itemContentWrap innerWrap">
            <div className="itemImage">
              <img src={`http://localhost:8081/upload/${magazine.uploadFileNames[0]}`} alt={magazine.title} />
            </div>
            <div className="itemDiscripton" dangerouslySetInnerHTML={{ __html: magazine?.content }} />
          </div>
        </section>

        <aside className="itemSidebar borad">
          <div className="innerSiedbarWrap">
            <h2>MAGAZINE</h2>
            <span className="number">- VOL. {displayIndex}</span>
            <h3 className="title">
              {magazine.title.split('').map((char, idx) => (
                <React.Fragment key={idx}>
                  {char}
                  {(char === '-' || char === '–') && <br />}
                </React.Fragment>
              ))}
            </h3>
            <span className="date">{formatDateToDot(magazine.date)}</span>
            <div className="pagination">
              <button onClick={handlePrev} disabled={isPrevDisabled}>PREV</button>
              <button onClick={handleList}>LIST</button>
              <button onClick={handleNext} disabled={isNextDisabled}>NEXT</button>
            </div>
          </div>
        </aside>
      </div>
    </>
  );
};

export default MagazineDetailComponent;
