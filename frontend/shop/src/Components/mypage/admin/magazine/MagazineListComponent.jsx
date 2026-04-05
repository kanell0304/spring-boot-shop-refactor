import { useState, useEffect } from 'react';
import { Link, NavLink, useParams, useSearchParams } from 'react-router-dom';
import { formatDateToDot } from "../../../../util/dateUtil";
import { getMagazineListAll } from '../../../../api/BoardApi';
import Pagination from "../../../Pagination";
import defaultImg from "../../../../static/images/default.png";

const MagazineListComponent = () => {
  const [boardData, setData] = useState({});
  const [keyword, setKeyword] = useState("");
  const [searchParams, setSearchParams] = useSearchParams();
  const page = parseInt(searchParams.get("page")) || 0;
  const size = parseInt(searchParams.get("size")) || 10;

  useEffect(() => {
    fetchList();
    console.log(boardData)
  }, [page, size]);

  const fetchList = () => {
    getMagazineListAll(page,size).then(setData);
  }

  const handleKeywordSearch = () => {
    setSearchParams({ page: 0, size, keyword });
};
  


  return(
    <>
    <h2 className="pageTitle">매거진</h2>
    <div className="pageContainer">
    <div className="borderSection filter">
                <strong>등록된 작성글 : {boardData?.totalElements || 0}개</strong>
                <div className='inputWrap'>
                    <div className="inputBox">
                        <input
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                        placeholder="검색어를 입력해주세요."
                        type="text"
                        />
                        <button className="btn black" type="button" onClick={handleKeywordSearch}>검색</button>
                    </div>
                </div>
            </div>



        <div className="tablePage">
            <div className="itemSubMenu">
                <Pagination pageInfo={boardData}/>
                <Link className="btn black" to={'add'}>글 작성</Link>
            </div>
            <div className="itemTableWrap">
                <table className="itemTable">
                    <thead className="itemThead">
                        <tr className="itemTr">
                            <th className="itemNumber">번호</th>
                            <th className="itemInfo">작성글</th>
                            <th className="itemWriter">작성자</th>
                            <th className="itemViewCount">조회수</th>
                            <th className="itemDate">등록일</th>
                            <th className='itemDelFlag'>상태</th>
                        </tr>
                    </thead>
                    <tbody className="itemTbody">
                    {boardData?.content?.length > 0 ? (
                        boardData.content.map((item, index) => {
                            const displayIndex = boardData.totalElements - (page * size + index);
                            return (
                                <tr className="itemTr" key={item.magazineListId}>
                                  <td className="itemNumber">{displayIndex}</td>
                                  <td className="itemInfo">
                                    <div className="itemImg">
                                        <img
                                        src={
                                            item.uploadFileNames?.length > 0
                                            ? `http://localhost:8081/upload/${item.uploadFileNames[0]}`
                                            : defaultImg
                                        }
                                        alt={item.title}
                                        />
                                    </div>
                                    <div className="itemDetailInfo">
                                        <NavLink to={`modify/${item.magazineListId}`} className="itemName">{item.title}</NavLink>
                                    </div>
                                  </td>
                                  <td className="itemWriter">{item.writer}</td>
                                  <td className="itemViewCount">{item.viewCount}</td>
                                  <td className="itemDate">{formatDateToDot(item.date)}</td>
                                  <td className={`itemDelFlag ${item.delFlag ? "deleted" : "active"}`}>
                                    {item.delFlag ? "대기중" : "게시중"}
                                  </td>
                                </tr>
                            );
                        })
                        ) : (
                            <tr>
                                <td colSpan={6} className='noDataView'>등록한 게시물이 없습니다.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
            <div className="itemSubMenu">
                <Pagination pageInfo={boardData}/>
                <Link className="btn black" to={'add'}>글 작성</Link>
            </div>
        </div>
    </div>
    </>
  )
}

export default MagazineListComponent;