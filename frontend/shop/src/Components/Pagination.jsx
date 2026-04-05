import { useNavigate, useLocation, useSearchParams } from 'react-router-dom';
import '../static/css/Pagination.scss';

const Pagination = ({ pageInfo }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams] = useSearchParams();

  const currentPage = pageInfo.number;
  const totalPages = pageInfo.totalPages;
  const size = parseInt(searchParams.get("size")) || pageInfo.size || 5;

  const pageRange = 5;
  const startPage = Math.floor(currentPage / pageRange) * pageRange;
  const endPage = Math.min(startPage + pageRange, totalPages);

  const moveTo = (page) => {
    const params = new URLSearchParams(searchParams);
    params.set("page", page);
    params.set("size", size);
    navigate({ pathname: location.pathname, search: `?${params.toString()}` });
  };

  return (
    <div className="pagination">
      {/* <button disabled={currentPage === 0} onClick={() => moveTo(0)}>처음</button> */}
      {/* <button disabled={startPage === 0} onClick={() => moveTo(startPage - pageRange)}>이전페이지</button> */}
      <button disabled={currentPage === 0} onClick={() => moveTo(currentPage - 1)}>PREV</button>
      <div className='pageNum'>
      {Array.from({ length: endPage - startPage }, (_, i) => {
        const page = startPage + i;
        return (
          <button
            key={page}
            onClick={() => moveTo(page)}
            className={page === currentPage ? 'active' : ''}
          >
            {page + 1}
          </button>
        );
      })}
      </div>
      <button disabled={currentPage + 1 >= totalPages} onClick={() => moveTo(currentPage + 1)}>NEXT</button>
      {/* <button disabled={endPage >= totalPages} onClick={() => moveTo(startPage + pageRange)}>다음페이지</button> */}
      {/* <button disabled={currentPage === totalPages - 1} onClick={() => moveTo(totalPages - 1)}>끝</button> */}
    </div>
  );
};

export default Pagination;
