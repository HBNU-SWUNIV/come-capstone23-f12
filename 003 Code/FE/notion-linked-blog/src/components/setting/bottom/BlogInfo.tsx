import {RootState} from "@/redux/store";
import {UserState} from "@/redux/userSlice";
import {Typography} from "antd";
import {useSelector} from "react-redux";
import {Container, RowContainer, StyledTitle} from "./Common";

const {Text} = Typography;

export default function BlogInfo() {
	const {user} = useSelector<RootState, UserState>(state => state.user);

	return (
		<>
			<Container>
				<RowContainer>
					<StyledTitle level={4}>
						블로그 제목
					</StyledTitle>
					<Text>{user?.blogTitle}</Text>
				</RowContainer>
				<div>
					<Text type="secondary">
						개인 페이지의 좌측 상단에 나타나는 페이지 제목입니다.
					</Text>
				</div>
			</Container>
		</>
	);
}
