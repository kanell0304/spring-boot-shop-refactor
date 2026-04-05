import { Suspense, lazy } from "react";
const ProductListPage = lazy(() => import("../../Pages/product/ProductListPage"));
const ProductListComponent = lazy(() => import("../../Components/product/ProductListComponent"));
const ProductDetailPage = lazy(() => import("../../Pages/product/ProductDetailPage"));
const ProductDetailComponent = lazy(() => import("../../Components/product/ProductDetailComponent"));

const Loading = () => <div>Loading....</div>

const ProductRouter = () => {
  return [
    {
      path: "list/:categoryId", // ✅ 부모
      element: (
        <Suspense fallback={<Loading />}><ProductListPage /></Suspense>
      ),
      children: [
        {
          index: true, // 기본 자식
          element: (
            <Suspense fallback={<Loading />}><ProductListComponent /></Suspense>
          )
        }
      ]
    },
    {
      path: "detail/:productId",
      element: (
        <Suspense fallback={<Loading />}><ProductDetailPage /></Suspense>
      ),
      children: [
        {
          index: true, // 기본 자식
          element: (
            <Suspense fallback={<Loading />}><ProductDetailComponent /></Suspense>
          )
        }
      ]
    }
  ];
};
export default ProductRouter;