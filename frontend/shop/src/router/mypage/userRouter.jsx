import { Suspense, lazy } from "react";
import { Navigate } from "react-router-dom"; // ✅ 추가
const Loading = () => <div>Loading....</div>;

const UserMainPage = lazy(() => import("../../Pages/myPage/user/UserMainPage"));
const OrderComponent = lazy(() => import("../../Components/mypage/user/order/OrderComponent"));
const OrderListComponent = lazy(() => import("../../Components/mypage/user/order/OrderListComponent"));
const OrderDetailComponent = lazy(() => import("../../Components/mypage/user/order/OrderDetailComponent"));

const InquiryComponent = lazy(() => import("../../Components/mypage/user/inquiry/InquiryComponent"));
const InquiryListComponent = lazy(() => import("../../Components/mypage/user/inquiry/InquiryListComponent"));
const MileageComponent = lazy(() => import("../../Components/mypage/user/mileage/MileageComponent"));
const MileageListComponent = lazy(() => import("../../Components/mypage/user/mileage/MileageListComponent"));
const WishCompoent = lazy(() => import("../../Components/mypage/user/wish/WishCompoent"));
const WishListCompoent = lazy(() => import("../../Components/mypage/user/wish/WishListCompoent"));
const ProfileComponent = lazy(() => import("../../Components/mypage/user/profile/ProfileComponent"));

const uesrRouter = () => {
    return [
        {
            path: "mypage",
            element: (
                <Suspense fallback={<Loading />}><UserMainPage /></Suspense>
            ),
            children: [
                {
                    index: true, // ✅ 추가 : mypage 들어오면 바로 order로 이동
                    element: <Navigate to="order" replace />
                },
                {
                    path: "order",
                    element: (
                        <Suspense fallback={<Loading />}><OrderComponent /></Suspense>
                    ),
                    children: [
                        {
                            index: true,
                            element: <OrderListComponent />
                        },
                        {
                            path : "detail/:orderId",
                            element: <OrderDetailComponent />
                        }
                    ]
                },
                {
                    path: "inquiry",
                    element: (
                        <Suspense fallback={<Loading />}><InquiryComponent /></Suspense>
                    ),
                    children: [
                        {
                            index: true,
                            element: <Suspense fallback={<Loading />}><InquiryListComponent /></Suspense>
                        }
                    ]
                },
                {
                    path: "mileage/:id",
                    element: (
                        <Suspense fallback={<Loading />}><MileageComponent /></Suspense>
                    ),
                    children: [
                        {
                            index: true,
                            element: <Suspense fallback={<Loading />}><MileageListComponent /></Suspense>
                        },
                    ]
                },
                {
                    path: "wish/:id",
                    element: (
                        <Suspense fallback={<Loading />}><WishCompoent /></Suspense>
                    ),
                    children: [
                        {
                            index: true,
                            element: <Suspense fallback={<Loading />}><WishListCompoent /></Suspense>
                        },
                    ]
                },
                {
                    path: "profile/:id",
                    element: (
                        <Suspense fallback={<Loading />}><ProfileComponent /></Suspense>
                    ),
                },
            ]
        }
    ];
};

export default uesrRouter;
