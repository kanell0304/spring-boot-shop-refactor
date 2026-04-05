import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { formatDateToDot } from "../../../../util/dateUtil";
import { qnaListAll } from "../../../../api/qnaApi";
import Pagination from "../../../Pagination";

const getStatusText = (status) => {
    if (status === 'WAITING_ANSWER') return '답변대기';
    if (status === 'ANSWER') return '답변중';
    if (status === 'ANSWER_COMPLETED') return '답변완료';
    return status || '-';
};

const getStatusClass = (status) => {
    if (status === 'WAITING_ANSWER') return 'waiting';
    if (status === 'ANSWER_COMPLETED') return 'completed';
    return '';
};

const InquiryListCompoenet = () => {
    const [qnaData, setQnaData] = useState({});
    const [statusFilter, setStatusFilter] = useState('ALL');
    const [searchParams, setSearchParams] = useSearchParams();

    const page = parseInt(searchParams.get("page")) || 0;
    const size = parseInt(searchParams.get("size")) || 10;

    useEffect(() => {
        qnaListAll(page, size).then(setQnaData).catch(console.error);
    }, [page, size]);

    const filtered = statusFilter === 'ALL'
        ? qnaData?.content
        : qnaData?.content?.filter(item => item.qnAListStatus === statusFilter);

    return (
        <>
            <h2 className="pageTitle">문의</h2>
            <div className="pageContainer">
                <div className="borderSection filter">
                    <strong>등록된 문의 : {qnaData?.totalElements || 0}개</strong>
                    <div className="btnsWrap">
                        <div>
                            <button
                                className={`btn line ${statusFilter === 'ALL' ? 'active' : ''}`}
                                type="button"
                                onClick={() => setStatusFilter('ALL')}
                            >전체</button>
                            <button
                                className={`btn line ${statusFilter === 'WAITING_ANSWER' ? 'active' : ''}`}
                                type="button"
                                onClick={() => setStatusFilter('WAITING_ANSWER')}
                            >미답변</button>
                            <button
                                className={`btn line ${statusFilter === 'ANSWER_COMPLETED' ? 'active' : ''}`}
                                type="button"
                                onClick={() => setStatusFilter('ANSWER_COMPLETED')}
                            >답변완료</button>
                        </div>
                    </div>
                </div>

                <div className="tablePage">
                    <div className="itemSubMenu">
                        <Pagination pageInfo={qnaData} />
                    </div>
                    <div className="itemTableWrap">
                        <table className="itemTable">
                            <thead className="itemThead">
                                <tr className="itemTr">
                                    <th className="itemNumber">번호</th>
                                    <th className="itemInfo">제목</th>
                                    <th className="itemWriter">작성자</th>
                                    <th className="itemDate">등록일</th>
                                    <th className="itemStatus">답변상태</th>
                                    <th className="itemDelFlag">상태</th>
                                </tr>
                            </thead>
                            <tbody className="itemTbody">
                                {filtered?.length > 0 ? (
                                    filtered.map((item, index) => {
                                        const displayIndex = qnaData.totalElements - (page * size + index);
                                        return (
                                            <tr className="itemTr" key={item.qnaListId}>
                                                <td className="itemNumber">{displayIndex}</td>
                                                <td className="itemInfo">
                                                    <div className="itemDetailInfo">
                                                        <p>{item.title}</p>
                                                        {item.content && <p className="itemSubText">{item.content}</p>}
                                                    </div>
                                                </td>
                                                <td className="itemWriter">{item.writer}</td>
                                                <td className="itemDate">{formatDateToDot(item.date)}</td>
                                                <td className={`itemStatus ${getStatusClass(item.qnAListStatus)}`}>
                                                    {getStatusText(item.qnAListStatus)}
                                                </td>
                                                <td className={`itemDelFlag ${item.delFlag ? "deleted" : "active"}`}>
                                                    {item.delFlag ? "삭제됨" : "게시중"}
                                                </td>
                                            </tr>
                                        );
                                    })
                                ) : (
                                    <tr>
                                        <td colSpan={6} className="noDataView">등록된 문의가 없습니다.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                    <div className="itemSubMenu">
                        <Pagination pageInfo={qnaData} />
                    </div>
                </div>
            </div>
        </>
    );
};

export default InquiryListCompoenet;
