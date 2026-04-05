import React, { useState, useEffect } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getMagazineList } from "../../api/BoardApi";
import Pagination from "../Pagination";

const MagazineListComponent = () => {
  const [magazines, setMagazines] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();

  // const startIndex = currentPage * pageSize;
  // const endIndex = startIndex + pageSize;
  // const currentMagazines = magazines.slice(startIndex, endIndex);
  // const totalPages = Math.ceil(magazines.length / pageSize);

  const page = parseInt(searchParams.get("page")) || 0;
  const size = parseInt(searchParams.get("size")) || 10;

  useEffect(() => {
    fetchList();
  },[page,size])


  const fetchList = () => {
    getMagazineList(page,size).then(setMagazines);
  }



  return (
    <>
      <div className="rightNavLayoutWrap">
        <section className="rightNavLayoutContainer">
          <ul className="boradListWrap innerWrap">
          {magazines?.content?.length > 0 ? (
              magazines?.content?.map((item,index) => {
                const displayIndex = item.totalElements - (page * size + index);
                return(
                  <li key={item.magazineListId} className="item">
                    <div className="imageContainer">
                      <img src={`http://localhost:8081/upload/${item.uploadFileNames[0]}`} alt={`${item.title}`} />
                    </div>
                    <div className="textContainer">
                      <div className="textWrapper"> 
                        <Link to={`/magazine/detail/${item.magazineListId}`}>
                            <span className="volume">- VOL. {displayIndex}</span>
                            <h3 className="title">
                              {item.title.split('').map((char, idx) => (
                                <React.Fragment key={idx}>
                                  {char}
                                  {(char === '-' || char === '–') && <br />}
                                </React.Fragment>
                              ))}
                          </h3>
                          </Link>
                      </div>
                    </div>
                  </li>
              )})
          ) : (
            <li className="noDataView">등록된 글이 없습니다.</li>
          )}
          </ul>
        </section>

        <aside className="itemSidebar list">
          <div className="innerSiedbarWrap">
            <h2>MAGAZINE</h2>
            <p className="discription">NØRD documents inspiration<br /> drawn from everyday life and beyond fashion<br />
              Through interviews, essays, and visuals,<br />we explore brand philosophies and aesthetics.</p>
            <div className="searchBox">
              <input type="text" placeholder="SEARCH TEXT" />
              <button className="btn black">SEARCH</button>
            </div>
            <div className="paginationSection">
                <div className="totalCount">TOTAL : {magazines.totalElements}</div>
                <Pagination pageInfo={magazines}/>
            </div>
          </div>
        </aside>

      </div>
    </>
  );
};

export default MagazineListComponent;