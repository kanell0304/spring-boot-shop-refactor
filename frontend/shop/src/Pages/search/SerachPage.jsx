import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import BasicLayout from '../../layout/BasicLayout';
import { searchItems } from '../../api/searchApi';
import { categoryList } from '../../api/categoryApi';

const SearchPage = () => {
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();
    const [searchTerm, setSearchTerm] = useState(searchParams.get('q') || '');
    const [results, setResults] = useState(null);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        categoryList().then(setCategories).catch(console.error);
    }, []);

    useEffect(() => {
        const q = searchParams.get('q');
        if (q) {
            setSearchTerm(q);
            fetchResults(q);
        }
    }, [searchParams]);

    const fetchResults = (term) => {
        if (!term.trim()) return;
        setLoading(true);
        searchItems(term)
            .then(setResults)
            .catch(() => setResults({ itemName: [], categoryName: [] }))
            .finally(() => setLoading(false));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!searchTerm.trim()) {
            alert('검색어를 입력해주세요.');
            return;
        }
        setSearchParams({ q: searchTerm.trim() });
    };

    const getCategoryIdByName = (name) => {
        const cat = categories.find((c) => c.categoryName === name);
        return cat ? cat.id : null;
    };

    const totalCount = results
        ? (results.itemName?.length || 0) + (results.categoryName?.length || 0)
        : 0;

    return (
        <BasicLayout>
            <div className="searchPage">
                <div className="searchHeader">
                    <form onSubmit={handleSubmit} className="searchForm">
                        <input
                            type="text"
                            placeholder="검색어를 입력하세요"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <button type="submit">검색</button>
                    </form>
                </div>

                {loading && <p className="searchLoading">검색 중...</p>}

                {results && !loading && (
                    <div className="searchResults">
                        <p className="searchCount">
                            <strong>"{searchParams.get('q')}"</strong> 검색 결과 총 {totalCount}건
                        </p>

                        {results.categoryName?.length > 0 && (
                            <section className="searchSection">
                                <h3>카테고리 ({results.categoryName.length})</h3>
                                <ul className="searchList">
                                    {results.categoryName.map((name, idx) => {
                                        const catId = getCategoryIdByName(name);
                                        return (
                                            <li key={idx}>
                                                {catId ? (
                                                    <Link to={`/product/list/${catId}?page=0&size=9`}>
                                                        {name}
                                                    </Link>
                                                ) : (
                                                    <span>{name}</span>
                                                )}
                                            </li>
                                        );
                                    })}
                                </ul>
                            </section>
                        )}

                        {results.itemName?.length > 0 && (
                            <section className="searchSection">
                                <h3>상품 ({results.itemName.length})</h3>
                                <ul className="searchList">
                                    {results.itemName.map((name, idx) => (
                                        <li key={idx}>
                                            <span
                                                className="searchItemLink"
                                                onClick={() => navigate(`/search?q=${encodeURIComponent(name)}`)}
                                            >
                                                {name}
                                            </span>
                                        </li>
                                    ))}
                                </ul>
                            </section>
                        )}

                        {totalCount === 0 && (
                            <p className="noDataView">검색 결과가 없습니다.</p>
                        )}
                    </div>
                )}
            </div>
        </BasicLayout>
    );
};

export default SearchPage;
