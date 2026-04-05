import React, { Suspense, lazy } from "react";

const Loading = () => <div>Loading....</div>;

const EventPage = lazy(() => import("../../Pages/event/EventPage"));
const EventDetailPage = lazy(() => import("../../Pages/event/EventDetailPage"));
const EventListComponent = lazy(() => import("../../Components/event/EventListComponent"));
const EventDetailComponent = lazy(() => import("../../Components/event/EventDetailComponent"));

const eventRouter = () => [
  {
    path: "list",
    element: (
      <Suspense fallback={<Loading />}><EventPage /></Suspense>
    ),
    children: [
      {
        index: true,
        element: (
          <EventListComponent />
        )
      }
    ]
  },
  {
    path: "detail/:id",
    element: (
      <Suspense fallback={<Loading />}><EventDetailPage /></Suspense>
    ),
    children: [
      {
        index: true,
        element: (
          <EventDetailComponent />
        )
      }
    ]
  },
];

export default eventRouter;
