import React, { useState, useEffect } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getEventList } from "../../api/BoardApi";
import Pagination from "../Pagination";

const MagazineListComponent = () => {
  const [events, setEvents] = useState([]);
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
    getEventList(page,size).then(setEvents);
  }



  return (
    <>
      <div className="rightNavLayoutWrap">
        <section className="rightNavLayoutContainer">
          <ul className="boradListWrap innerWrap">
          {events?.content?.length > 0 ? (
            events?.content?.map((item,index) => {
              const displayIndex = item.totalElements - (page * size + index);
              return(
                <li key={item.eventListId} className="item">
                  <div className="imageContainer">
                    <img src={`http://localhost:8081/upload/${item.uploadFileNames[0]}`} alt={`${events.title}`} />
                  </div>
                  <div className="textContainer">
                    <div className="textWrapper"> 
                      <Link to={`/event/detail/${item.eventListId}`}>
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
            <h2>EVENT</h2>
            {/* <p className="discription">NØRD documents inspiration<br /> drawn from everyday life and beyond fashion<br />
              Through interviews, essays, and visuals,<br />we explore brand philosophies and aesthetics.</p> */}
            <div className="searchBox">
              <input type="text" placeholder="SEARCH TEXT" />
              <button className="btn black">SEARCH</button>
            </div>
            <div className="paginationSection">
                <div className="totalCount">TOTAL : {events.totalElements}</div>
                <Pagination pageInfo={events}/>
            </div>
          </div>
        </aside>

      </div>
    </>
  );
};

export default MagazineListComponent;