import {Avatar, Button, Space, Typography, Upload, UploadProps} from "antd";
import styled from "styled-components";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "@/redux/store";
import {modifyProfileImage, UserState} from "@/redux/userSlice";
import {getProfileImageAPI, modifyProfileImageAPI} from "@/apis/user";
import {useEffect, useState} from "react";

const StyledSpace = styled(Space)`
  padding-right: 24px;
`;

const ImageButton = styled(Button)`
  width: 130px;
  padding: 0 20px;
`;

const StyledAvatar = styled(Avatar)`
  width: 128px;
  height: 128px;

  @media screen and (max-width: 768px) {
    width: 96px;
    height: 96px;
  }
`;

export default function Profile() {
	const {user} = useSelector<RootState, UserState>(state => state.user);
	const [profileImage, setProfileImage] = useState("");
	const [error, setError] = useState(false);
	const [loading, setLoading] = useState(false);
	const dispatch = useDispatch();

	useEffect(() => {
		(async () => {
			const binaryImg = await getProfileImageAPI(user?.id);

			setProfileImage(URL.createObjectURL(binaryImg));
		})();
	}, [user, profileImage]);

	const uploadImage = async options => {
		const {file} = options;
		const formData = new FormData();

		formData.append("profile", file);

		try {
			setError(false);
			setLoading(true);
			await modifyProfileImageAPI(formData, user.id);
			const userProfile = await getProfileImageAPI(user.id);
			const url = URL.createObjectURL(userProfile);

			setProfileImage(url);
			dispatch(modifyProfileImage(url));
		} catch (e) {
			setError(true);
		} finally {
			setLoading(false);
		}
	};

	const props: UploadProps = {
		name: "profile",
		customRequest: uploadImage,
		accept: "image/*",
		maxCount: 1,
		showUploadList: false,
	};

	return (
		// TODO: 프로파일 이미지 처리
		<StyledSpace direction="vertical" align="center">
			<StyledAvatar
				src={profileImage}
			/>

			<Upload {...props}>
				<ImageButton type="primary" loading={loading}>이미지 업로드</ImageButton>
			</Upload>
			<ImageButton>이미지 제거</ImageButton>
			{error && <Typography.Text type="danger">업로드 중 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.</Typography.Text>}
		</StyledSpace>
	);
}
