import { Outlet } from "react-router-dom";

const MySection = () => {
  return (
    <div className="myContainer">
      <section className="mysSection innerWrap">
        <Outlet />
      </section>
    </div>
  );
};

export default MySection;
