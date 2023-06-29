import styled from "styled-components";
import {RootState} from "@/redux/store";
import {UserState} from "@/redux/userSlice";
import {Typography} from "antd";
import {useSelector} from "react-redux";
import {Container, EditBtn, RowContainer, SpaceBetweenContainer, StyledTitle} from "./Common";

const {Text} = Typography;

const StyledText = styled(Text)`
	font-size: 1rem;
`;

export default function BlogInfo() {
	const {user} = useSelector<RootState, UserState>(state => state.user);

	return (
		<>
			<Container>
				<RowContainer>
					<StyledTitle level={4}>
						블로그 제목
					</StyledTitle>
					<SpaceBetweenContainer>
						<StyledText>{user?.blogTitle}</StyledText>
						<EditBtn>수정</EditBtn>
					</SpaceBetweenContainer>
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
