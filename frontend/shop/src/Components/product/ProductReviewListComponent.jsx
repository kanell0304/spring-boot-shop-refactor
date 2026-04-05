import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { formatDateToDot } from "../../util/dateUtil";
import { purchaseCheck, getProductReviewList } from "../../api/reviewApi";
import { addScore } from "../../api/scoreApi";
import { addReview } from "../../api/reviewApi";
import ReviewPopup from "../itemDetail/ReviewPopup";
import defaultImg from "../../static/images/default.png";
import Pagination from "../Pagination";

const ProductReviewList = ({ productId }) => {
    const loginState = useSelector(state => state.loginSlice);
    const memberId = loginState?.memberId;

    const [reviewList, setReviewList] = useState({});
    const [pCheck, setPCheck] = useState(false);
    const [showPopup, setShowPopup] = useState(false);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        if (memberId && productId) {
            purchaseCheck(memberId, productId).then(setPCheck).catch(() => setPCheck(false));
        }
    }, [memberId, productId]);

    useEffect(() => {
        if (productId) {
            getProductReviewList(productId).then(setReviewList).catch(console.error);
        }
    }, [productId]);

    const handleReviewSave = async (reviewData) => {
        if (submitting) return;
        setSubmitting(true);
        try {
            const formData = new FormData();
            formData.append('memberId', memberId);
            formData.append('title', reviewData.title);
            formData.append('content', reviewData.content);
            formData.append('score', reviewData.rating);
            reviewData.files?.forEach((file) => formData.append('uploadFiles', file));

            await addReview(productId, formData);
            await addScore(productId, memberId, reviewData.rating);

            setShowPopup(false);
            getProductReviewList(productId).then(setReviewList);
            alert('리뷰가 등록되었습니다.');
        } catch (error) {
            alert('리뷰 등록에 실패했습니다.');
            console.error(error);
        } finally {
            setSubmitting(false);
        }
    };

    const renderStars = (score) => {
        return [1, 2, 3, 4, 5].map((s) => (
            <span key={s} style={{ color: s <= score ? '#f6ad55' : '#e2e8f0' }}>★</span>
        ));
    };

    return (
        <div className="tablePage product">
            <div className="itemTableWrap">
                <table className="itemTable">
                    <tbody className="itemTbody">
                        {reviewList?.content?.length > 0 ? (
                            reviewList.content.map((item) => (
                                <tr className="itemTr" key={item.reviewId}>
                                    <td className="itemInfo">
                                        <div className="itemImg">
                                            <img
                                                src={
                                                    item.uploadFileNames?.length > 0 && item.uploadFileNames[0] !== 'default.png'
                                                        ? `http://localhost:8081/upload/${item.uploadFileNames[0]}`
                                                        : defaultImg
                                                }
                                                alt={item.title}
                                            />
                                        </div>
                                        <div className="itemDetailInfo">
                                            <p>{item.title}</p>
                                            <p style={{ fontSize: '0.85em', color: '#718096' }}>{item.content}</p>
                                        </div>
                                    </td>
                                    <td className="itemWriter">{item.writer || item.memberName}</td>
                                    <td className="itemDate">{formatDateToDot(item.date)}</td>
                                    <td className="itemTotalScore">
                                        <div className="rating">{renderStars(item.score)}</div>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={4} className="noDataView">등록된 리뷰가 없습니다.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            <div className="itemSubMenu">
                <Pagination pageInfo={reviewList} />
                {pCheck && (
                    <button
                        type="button"
                        className="btn black"
                        onClick={() => setShowPopup(true)}
                        disabled={submitting}
                    >
                        리뷰등록
                    </button>
                )}
            </div>

            {showPopup && (
                <ReviewPopup
                    onClose={() => setShowPopup(false)}
                    onSave={handleReviewSave}
                />
            )}
        </div>
    );
};

export default ProductReviewList;
