import React, {useCallback, useEffect, useState} from "react";
import {Button, Col, Layout, Modal, Row, Typography} from "antd";
import LoginForm from "@/components/auth/LoginForm";
import SignupForm from "@/components/auth/SignupForm";
import {checkLoginStatus} from "@/apis/user";
import {UserState, login} from "@/redux/userSlice";
import styled from "styled-components";
import Link from "next/link";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "@/redux/store";
import MenuItemsDropdown from "./MenuItemsDropdown";

const {Header} = Layout;

const {Text} = Typography;

const StyledText = styled(Text)`
`;

const StyledHeader = styled(Header)`
  display: flex;
  justify-content: center;
  padding: 0;
	background-color: inherit;
`;

const StyledHeaderRow = styled(Row)`
  display: flex;
  max-width: 1728px;
  justify-content: space-between;
	width: 100%;

	@media screen and (max-width: 1872px) {
		width: calc(352px * 4 - 32px);
	}

	@media screen and (max-width: 1520px) {
		width: calc(352px * 3 - 32px);
	}

	@media screen and (max-width: 1058px) {
		width: 100vw;
		padding: 0 16px;
	}
`;

const StyledCol = styled(Col)`
	display: flex;
	align-items: center;
`;

const StyledLink = styled(Link)`
	display: flex;
	justify-content: space-between;
  align-items: center;
	width: 70px;
`;

const StyledImg = styled.img`
	width: 32px;
`;

function AppHeader() {
	const [isModalOpen, setIsModalOpen] = useState(false);
	const [existAccount, setExistAccount] = useState(true);
	const {user} = useSelector<RootState, UserState>(state => state.user);
	const dispatch = useDispatch();

	useEffect(() => {
		(async () => {
			const response = await checkLoginStatus();

			if (!user && response.status === 200) {
				dispatch(login(response.data.user));
			}
		})();
	}, []);

	const showModal = useCallback(() => {
		setIsModalOpen(true);
	}, [isModalOpen]);

	const handleCancel = useCallback(() => {
		setIsModalOpen(false);
	}, [isModalOpen]);

	const switchForm = useCallback(() => {
		setExistAccount(!existAccount);
	}, [existAccount]);

	return (
		<StyledHeader>
			<StyledHeaderRow>
				<StyledCol>
					<StyledLink href="/">
						<StyledImg src="https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png" alt="Logo" />
						<StyledText>
							NLB
						</StyledText>
					</StyledLink>
				</StyledCol>
				<Col>
					{!user ?
						<Button type="primary" onClick={showModal}>로그인</Button> :
						<MenuItemsDropdown />
					}
					<Modal title={existAccount ? "로그인" : "회원가입"} open={isModalOpen} footer={null} onCancel={handleCancel}>
						{existAccount ?
							<LoginForm switchForm={switchForm} setIsModalOpen={setIsModalOpen} /> :
							<SignupForm switchForm={switchForm} />
						}
					</Modal>
				</Col>
			</StyledHeaderRow>
		</StyledHeader>
	);
}

export default React.memo(AppHeader);