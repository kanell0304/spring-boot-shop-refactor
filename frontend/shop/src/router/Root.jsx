import { Suspense, lazy } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import memberRouter from "./memberRouter";
import adminRouter from "./mypage/adminRouter";
import userRouter from "./mypage/userRouter";
import ProductRouter from "./product/ProductRouter";
import cartRouter from "./cart/cartRouter";
import magazineRouter from "./magazine/magazineRouter";
import eventRouter from "./event/eventRouter";

const Loading = <div>Loading....</div>;
const Main = lazy(() => import("../Pages/main/MainPage"));
const Guide = lazy(() => import("../Pages/footer/GuidePage"));
const Privacy = lazy(() => import("../Pages/footer/PrivacyPolicyPage"));
const Terms = lazy(() => import("../Pages/footer/TermsPage"));
const Order = lazy(() => import("../Pages/order/OrderPage"));
const OrderComplete = lazy(() => import("../Pages/order/OrderCompletePage"));
const Search = lazy(() => import("../Pages/search/SerachPage"));
const Shop = lazy(() => import("../Pages/shop/ShopPage"));

const rootRouter = createBrowserRouter([
  {
    path: "/",
    element: <Suspense fallback={Loading}><Main /></Suspense>
  },
  {
    path: "product",
    children: ProductRouter()
  },
  {
    path: "guide",
    element: <Suspense fallback={Loading}><Guide /></Suspense>
  },
  {
    path: "privacy",
    element: <Suspense fallback={Loading}><Privacy /></Suspense>
  },
  {
    path: "terms",
    element: <Suspense fallback={Loading}><Terms /></Suspense>
  },
  {
    path: "member",
    children: memberRouter()
  },
  {
    path: "admin",
    children: adminRouter()
  },
  {
    path: "user",
    children: userRouter()
  },
  {
    path: "cart",
    children: cartRouter(),
  },
  {
    path: "magazine",
    children: magazineRouter(),
  },
  {
    path: "event",
    children: eventRouter(),
  },
  {
    path: "order",
    element: <Suspense fallback={Loading}><Order /></Suspense>
  },
  {
    path: "orderComplete",
    element: <Suspense fallback={Loading}><OrderComplete /></Suspense>
  },
  {
    path: "search",
    element: <Suspense fallback={Loading}><Search /></Suspense>
  },
  {
    path: "shop",
    element: <Suspense fallback={Loading}><Shop /></Suspense>
  }
]);

const Root = () => {
  return <RouterProvider router={rootRouter} />;
};

export default Root;
