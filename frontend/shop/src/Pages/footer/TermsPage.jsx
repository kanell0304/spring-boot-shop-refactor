import BasicLayout from "../../layout/BasicLayout";
import Terms from "../../Components/info/terms";

const TermsPage = () => {
	return(
		<BasicLayout>
		<div className="innerWrap">
			<h2>이용약관</h2>
			<div className="discripton">
				<Terms/>
			</div>
		</div>
		</BasicLayout>
	)
}
export default TermsPage;