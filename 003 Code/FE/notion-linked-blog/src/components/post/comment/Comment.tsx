import Image from "next/image";
import {Typography} from "antd";
import {PlusSquareOutlined} from "@ant-design/icons";
import styled from "styled-components";
import {CommentGetResponse, requestDeleteCommentAPI} from "@/apis/comment";
import {useSelector} from "react-redux";
import {UserState} from "@/redux/userSlice";
import {RootState} from "@/redux/store";

const Container = styled.div`
  border-top: 0.8px solid rgb(205, 205, 205);
  padding: 1.5rem;

  @media screen and (max-width: 768px) {
    padding: 1rem;
  }
`;

const StyledParagraph = styled(Typography.Paragraph)`
  font-size: 1.125rem;

  @media screen and (max-width: 768px) {
    font-size: 1rem;
  }
`;

const StyledText = styled(Typography.Text)`
  font-size: 1rem;

  @media screen and (max-width: 768px) {
    font-size: 0.875rem;
  }

  :hover {
    cursor: pointer;
  }
`;

const CommentWriterDetail = styled.div`
  display: flex;
  flex-direction: row;
  margin-bottom: 1.5rem;
`;

const CommentWriterAvatar = styled(Image)`
  @media screen and (max-width: 768px) {
    width: 40px;
    height: 40px;
  }
`;

const CommentWritingInfo = styled.div`
  display: flex;
  flex-direction: column;
  margin-left: 1rem;
`;

const CommentWriter = styled(Typography.Text)`
  font-size: 1rem;
  font-weight: bold;

  @media screen and (max-width: 768px) {
    font-size: 0.875rem;
  }
`;

const CommentModifier = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-left: auto;

  span {
    color: #868E96;
    font-size: 0.875rem;
    cursor: pointer;
    margin-left: 0.5rem;
  }
`;

interface CommentProp {
	values: CommentGetResponse;
	postId: number;
}

export default function Comment({values, postId}: CommentProp) {
	const {comment, createdAt, author, authorId, authorProfileLink, child} = values;
	const {user} = useSelector<RootState, UserState>(state => state.user);

	const handleClickDeletingBtn = async () => {
		await requestDeleteCommentAPI(postId);
	};

	return (
		<Container>
			<CommentWriterDetail>
				<CommentWriterAvatar
					src={authorProfileLink}
					width={54}
					height={54}
					alt="CommentWriterAvatar"
				/>
				<CommentWritingInfo>
					<CommentWriter>{author}</CommentWriter>
					<Typography.Text>{createdAt}</Typography.Text>
				</CommentWritingInfo>
				{user?.id === authorId &&
					<CommentModifier>
						<span>수정</span>
						<span onClick={handleClickDeletingBtn}>삭제</span>
					</CommentModifier>
				}
			</CommentWriterDetail>
			<StyledParagraph>
				{comment}
			</StyledParagraph>
			<StyledText strong><PlusSquareOutlined /> {child ? `${child.length}개의 답글` : "답글 달기"}</StyledText>
		</Container>
	);
}
