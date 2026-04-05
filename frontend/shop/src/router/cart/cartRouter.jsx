import { Suspense, lazy } from "react";

const Loading = <div>Loading...</div>;

const CartPage = lazy(() => import("../../Pages/cart/CartPage"));

const cartRouter = () => {
  return [
    {
      path: "/cart",
      element: (
        <Suspense fallback={Loading}><CartPage /></Suspense>
      ),
    },
  ];
};

export default cartRouter;
