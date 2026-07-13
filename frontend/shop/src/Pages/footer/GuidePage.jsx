import BasicLayout from "../../layout/BasicLayout";
import Guide from "../../Components/info/guide";

const GuidePage = () => {
  return (
    <BasicLayout>
        <div className="innerWrap">
          <h2>이용안내</h2>
          <div className="discripton">
            <Guide/>
          </div>
        </div>
    </BasicLayout>
  )
}
export default GuidePage;