import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { formatDateToDot } from "../../util/dateUtil";
import { qnaList, addQna } from "../../api/qnaApi";
import Pagination from "../Pagination";

const getStatusText = (status) => {
    if (status === 'WAITING_ANSWER') return '답변대기';
    if (status === 'ANSWER') return '답변중';
    if (status === 'ANSWER_COMPLETED') return '답변완료';
    return '-';
};

const initForm = { title: '', content: '' };

const ProductQnAList = ({ memberId, productId }) => {
    const loginState = useSelector(state => state.loginSlice);
    const isLoggedIn = loginState && loginState.email !== '';

    const [qnaData, setQnaData] = useState({});
    const [page, setPage] = useState(0);
    const [showForm, setShowForm] = useState(false);
    const [form, setForm] = useState(initForm);
    const [submitting, setSubmitting] = useState(false);

    const fetchQna = () => {
        qnaList(page, 5).then(data => {
            const filtered = {
                ...data,
                content: data.content?.filter(item => String(item.itemId) === String(productId)) || []
            };
            setQnaData(filtered);
        }).catch(console.error);
    };

    useEffect(() => {
        fetchQna();
    }, [productId, page]);

    const handleChange = (e) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!form.title.trim() || !form.content.trim()) {
            alert('제목과 내용을 입력해주세요.');
            return;
        }
        setSubmitting(true);
        try {
            await addQna({
                title: form.title,
                writer: loginState.email,
                content: form.content,
                memberId: loginState.memberId,
                itemId: Number(productId),
                parentId: null,
            });
            alert('문의가 등록되었습니다.');
            setForm(initForm);
            setShowForm(false);
            fetchQna();
        } catch (error) {
            alert('문의 등록에 실패했습니다.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="tablePage product">
            <div className="itemTableWrap">
                <table className="itemTable">
                    <thead className="itemThead">
                        <tr className="itemTr">
                            <th className="itemWriter">작성자</th>
                            <th className="itemInfo">제목</th>
                            <th className="itemDate">등록일</th>
                            <th className="itemStatus">답변상태</th>
                        </tr>
                    </thead>
                    <tbody className="itemTbody">
                        {qnaData?.content?.length > 0 ? (
                            qnaData.content.map(item => (
                                <tr className="itemTr" key={item.qnaListId}>
                                    <td className="itemWriter">{item.writer}</td>
                                    <td className="itemInfo">
                                        <div className="itemDetailInfo">
                                            <p>{item.title}</p>
                                            <p className="itemSubText">{item.content}</p>
                                        </div>
                                    </td>
                                    <td className="itemDate">{formatDateToDot(item.date)}</td>
                                    <td className="itemStatus">{getStatusText(item.qnAListStatus)}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={4} className="noDataView">등록된 QnA가 없습니다.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            <div className="itemSubMenu">
                <Pagination pageInfo={qnaData} />
                {isLoggedIn && (
                    <button
                        type="button"
                        className="btn black"
                        onClick={() => setShowForm(prev => !prev)}
                    >
                        {showForm ? '취소' : '문의하기'}
                    </button>
                )}
            </div>

            {showForm && (
                <form className="reviewForm" onSubmit={handleSubmit}>
                    <input
                        name="title"
                        value={form.title}
                        onChange={handleChange}
                        placeholder="제목을 입력해주세요."
                        type="text"
                        className="reviewInput"
                    />
                    <textarea
                        name="content"
                        value={form.content}
                        onChange={handleChange}
                        placeholder="문의 내용을 입력해주세요."
                        className="reviewTextarea"
                        rows={4}
                    />
                    <button type="submit" className="btn black" disabled={submitting}>
                        {submitting ? '등록 중...' : '등록'}
                    </button>
                </form>
            )}
        </div>
    );
};

export default ProductQnAList;
