import {Space, Typography} from "antd";
import {GithubOutlined, InstagramOutlined} from "@ant-design/icons";
import {useSelector} from "react-redux";
import {RootState} from "@/redux/store";
import {UserState} from "@/redux/userSlice";
import {Container, RowContainer, StyledTitle} from "./Common";

const {Text} = Typography;

export default function SocialInfo() {
	const {user} = useSelector<RootState, UserState>(state => state.user);

	return (
		<>
			<Container>
				<RowContainer>
					<StyledTitle level={4}>
						소셜 정보
					</StyledTitle>
					<Space direction="vertical">
						<Typography.Text><GithubOutlined /> {user?.githubLink}</Typography.Text>
						<Typography.Text><InstagramOutlined /> {user?.instagramLink}</Typography.Text>
					</Space>
				</RowContainer>
				<div>
					<Text type="secondary">
						포스트 및 블로그에서 보여지는 프로필에 공개되는 소셜 정보입니다.
					</Text>
				</div>
			</Container>
		</>
	);
}
