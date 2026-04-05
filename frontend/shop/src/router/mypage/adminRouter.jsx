import { Suspense, lazy } from "react";

const Loading = () => <div>Loading....</div>;

const AdminMainPage = lazy(() => import("../../Pages/myPage/admin/AdminMainPage"));

const OrderComponent = lazy(() => import("../../Components/mypage/admin/order/OrderComponent"));
const OrderListComponent = lazy(() => import("../../Components/mypage/admin/order/OrderListComponent"));
const OrderDetailComponent = lazy(() => import("../../Components/mypage/admin/order/OrderDetailCompoent"));

const ProductComponent = lazy(() => import("../../Components/mypage/admin/product/ProductComponent"));
const ProductListComponent = lazy(()=>import("../../Components/mypage/admin/product/ProductListComponent"));
const ProductFormComponent = lazy(() => import("../../Components/mypage/admin/product/ProductFormComponent"));

const ReviewComponent = lazy(()=>import("../../Components/mypage/admin/review/ReviewComponent"));
const ReviewListComponent = lazy(()=>import("../../Components/mypage/admin/review/ReviewListComponent"));

const MemberComponent = lazy(() => import("../../Components/mypage/admin/member/MemberComponent"));
const MemberListCompenet = lazy(() => import("../../Components/mypage/admin/member/MemberListCompoenet"));
const MemberModifyCompoenet = lazy(() => import("../../Components/mypage/admin/member/MemberModifyCompoenet"));

const CategoryCompoenet = lazy(() => import("../../Components/mypage/admin/category/CategoryCompoenet"));

const InquiryComponent = lazy(() => import("../../Components/mypage/admin/inquiry/InquiryCompoenet"));
const InquiryListComponent = lazy(() => import("../../Components/mypage/admin/inquiry/InquiryListCompoenet"));

const EventComponent = lazy(() => import("../../Components/mypage/admin/event/EventComponent"));
const EventListComponent = lazy(() => import("../../Components/mypage/admin/event/EventListComponent"));
const EventFormComponent = lazy(() => import("../../Components/mypage/admin/event/EventFormComponent"));

const MagazineComponent = lazy(() => import("../../Components/mypage/admin/magazine/MagazineComponent"));
const MagazineListComponent = lazy(() => import("../../Components/mypage/admin/magazine/MagazineListComponent"));
const MagazineFormComponent = lazy(() => import("../../Components/mypage/admin/magazine/MagazineFormComponent"));



const adminRouter = () => {
  return [
    {
      path: "mypage",
      element: (
        <Suspense fallback={<Loading />}><AdminMainPage /></Suspense>
      ),
      children: [
        {
          path: "order",
          element: (
            <OrderComponent />
          ),
          children: [
            {
              index: true,  // <- 기본 라우트
              element: <OrderListComponent />
            },
            {
              path: "detail/:orderId",  // <- 기본 라우트
              element: <OrderDetailComponent />
            },
          ]
        },
        {
          path: "product",
          element: (
            <ProductComponent />
          ),
          children:[
            {
              index: true,  // <- 기본 라우트
              element: <ProductListComponent />
            },
            {
              path: "add",
              element :(
                <ProductFormComponent />
              )
            },
            {
              path: "modify/:id",
              element: (
                <ProductFormComponent />
              )
            }
          ]
        },
        {
          path: "review",
          element: (
            <ReviewComponent />
          ),
          children:[
            {
              index: true,  // <- 기본 라우트
              element: <ReviewListComponent />
            },
          ]
        },
        {
          path: "member",
          element : (
            <MemberComponent />
          ),
          children:[
            {
              index : true,
              element : (
                <MemberListCompenet />
              )
            },
            {
              path : "modify/:id",
              element : (
                <MemberModifyCompoenet />
              )
            },
          ]
        },
        {
          path: "category",
          element: (
            <CategoryCompoenet />
          ),
        },
        {
          path: "event",
          element: (
            <EventComponent />
          ),
          children:[
            {
              index: true,  // <- 기본 라우트
              element: <EventListComponent />
            },
            {
              path: "add",
              element :(
                <EventFormComponent />
              )
            },
            {
              path: "modify/:id",
              element: (
                <EventFormComponent />
              )
            }
          ]
        },
        {
          path: "Magazine",
          element: (
            <MagazineComponent />
          ),
          children:[
            {
              index: true,  // <- 기본 라우트
              element: <MagazineListComponent />
            },
            {
              path: "add",
              element :(
                <MagazineFormComponent />
              )
            },
            {
              path: "modify/:id",
              element: (
                <MagazineFormComponent />
              )
            }
          ]
        },
        {
          path: "inquiry",
          element: (
            <InquiryComponent />
          ),
          children: [
            {
              index: true,  // <- 기본 라우트
              element: <InquiryListComponent />
            },
          ]
        },
      ],
    },
  ];
};

export default adminRouter;
