import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { NavLink } from "react-router-dom";
import { formatDateToDot } from "../../../../util/dateUtil";
import { reviewListAll } from "../../../../api/reviewApi";
import Pagination from "../../../Pagination";
import defaultImg from "../../../../static/images/default.png";

const ReviewListComponent = () => {
  const [reviewList, setReviewList] = useState({});
  const [delFlagFilter, setDelFlagFilter] = useState('ALL');
  const [searchParams] = useSearchParams();

  const page = parseInt(searchParams.get("page")) || 0;
  const size = parseInt(searchParams.get("size")) || 10;

  useEffect(() => {
    reviewListAll(page, size).then(setReviewList);
  }, [page, size]);

  const filtered = delFlagFilter === 'ALL'
    ? reviewList?.content
    : reviewList?.content?.filter(item =>
        delFlagFilter === 'ACTIVE' ? !item.delFlag : item.delFlag
      );

  return (
    <>
      <h2 className="pageTitle">리뷰</h2>
      <div className="pageContainer">
        <div className="borderSection filter">
          <strong>등록된 리뷰 : {reviewList?.totalElements || 0}개</strong>
          <div className="btnsWrap">
            <div>
              <button
                className={`btn line ${delFlagFilter === 'ALL' ? 'active' : ''}`}
                type="button"
                onClick={() => setDelFlagFilter('ALL')}
              >전체</button>
              <button
                className={`btn line ${delFlagFilter === 'ACTIVE' ? 'active' : ''}`}
                type="button"
                onClick={() => setDelFlagFilter('ACTIVE')}
              >게시중</button>
              <button
                className={`btn line ${delFlagFilter === 'DELETED' ? 'active' : ''}`}
                type="button"
                onClick={() => setDelFlagFilter('DELETED')}
              >미게시</button>
            </div>
          </div>
        </div>

        <div className="tablePage">
          <div className="itemSubMenu">
            <Pagination pageInfo={reviewList} />
          </div>
          <div className="itemTableWrap">
            <table className="itemTable">
              <thead className="itemThead">
                <tr className="itemTr">
                  <th className="itemNumber">번호</th>
                  <th className="itemInfo">리뷰</th>
                  <th className="itemWriter">작성자</th>
                  <th className="itemTotalScore">별점</th>
                  <th className="itemDate">등록일</th>
                  <th className="itemDelFlag">상태</th>
                </tr>
              </thead>
              <tbody className="itemTbody">
                {filtered?.length > 0 ? (
                  filtered.map((item, index) => {
                    const displayIndex = reviewList.totalElements - (page * size + index);
                    return (
                      <tr className="itemTr" key={item.id}>
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
                            <p>{item.title}</p>
                            <p>{item.content}</p>
                            <NavLink to={`/product/detail/${item.itemId}`}>상품상세 보기</NavLink>
                          </div>
                        </td>
                        <td className="itemWriter">{item.writer}</td>
                        <td className="itemTotalScore">{item.score}</td>
                        <td className="itemDate">{formatDateToDot(item.date)}</td>
                        <td className={`itemDelFlag ${item.delFlag ? "deleted" : "active"}`}>
                          {item.delFlag ? "미게시" : "게시중"}
                        </td>
                      </tr>
                    );
                  })
                ) : (
                  <tr>
                    <td colSpan={6} className="noDataView">등록된 리뷰가 없습니다.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          <div className="itemSubMenu">
            <Pagination pageInfo={reviewList} />
          </div>
        </div>
      </div>
    </>
  );
};

export default ReviewListComponent;
