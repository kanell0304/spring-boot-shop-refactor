import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { NavLink, Link } from "react-router-dom";
import { memberList } from "../../../../api/memberApi";
import { formatDateToDot } from "../../../../util/dateUtil";
import Pagination from "../../../Pagination";

const MemberListCompenet = () =>{
    const [memberData, setData] = useState({});
    const [keyword, setKeyword] = useState("");
    const [searchParams, setSearchParams] = useSearchParams();

    const page = parseInt(searchParams.get("page") || 0);
    const size = parseInt(searchParams.get("size") || 5);

    useEffect(() => {
        fetchList();
    }, [page, size, searchParams]);


    const fetchList = () =>{
        memberList(page,size).then(setData)
    }

    const handleKeywordSearch = () => {
        setSearchParams({ page: 0, size, keyword });
    };

    return(
        <>
        <h2 className="pageTitle">회원</h2>
        <div className="pageContainer">
            <div className="borderSection filter">
                <strong>회원수 : {memberData?.totalElements || 0}명</strong>
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
                    <Pagination pageInfo={memberData}/>
                </div>
                <div className="itemTableWrap">
                    <table className="itemTable">
                        <thead className="itemThead">
                            <tr className="itemTr">
                                <th className="itemNumber">번호</th>
                                <th className="itemID">아이디값</th>
                                <th className="itemInfo">회원아이디(이메일)</th>
                                <th className="itemDiscountRate">이름</th>
                                <th className="itemMemberShip">등급</th>
                                <th className="itemDate">가입일</th>
                                <th className="itemDelFlag">상태</th>
                            </tr>
                        </thead>
                        <tbody className="itemTbody">
                        {memberData?.content?.length > 0 ? (
                            memberData.content.map((item, index) => {
                                const displayIndex = memberData.totalElements - (page * size + index);
                                return (
                                    <tr className="itemTr" key={item.id}>
                                        <td className="itemNumber">{displayIndex}</td>
                                        <td className="itemID">{item.id}</td>
                                        <td className="itemInfo"><NavLink to={`modify/${item.id}`}>{item.email}</NavLink></td>
                                        <td className="itemDiscountRate">{item.memberName}</td>
                                        <td className="itemMemberShip">{item.memberShip}</td>
                                        <td className="itemDate">{formatDateToDot(item.joinDate)}</td>
                                        <td className={`itemDelFlag ${item.delFlag ? "deleted" : "active"}`}>{item.delFlag ? "탈퇴대기" : "정상"}</td>
                                    </tr>
                                );
                            })
                            ) : (
                                <tr>
                                    <td colSpan={7}>등록된 회원이 없습니다.</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
                <div className="itemSubMenu">
                    <Pagination pageInfo={memberData}/>
                </div>
            </div>
        </div>
        </>
    )
}
export default MemberListCompenet;