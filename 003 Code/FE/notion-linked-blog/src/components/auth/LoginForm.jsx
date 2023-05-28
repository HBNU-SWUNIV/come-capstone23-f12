import {Button, Form, Input, Typography} from "antd";
import styled from "styled-components";
import {StyledDiv, StyledSpace, StyledText} from "@/components/auth/AuthForm";
import {useState} from "react";
import {handleInput} from "@/components/auth/common";
import {loginByEmailAPI} from "@/apis/user";
import {useAppDispatch} from "@/hooks/hooks";
import {login} from "@/redux/userSlice";

const {Text} = Typography;

const StyledFormItem = styled(Form.Item)`
  display: flex;
  justify-content: flex-end;
`;

export default function LoginForm({switchForm, setIsModalOpen}) {
	const [email, onChangeEmail] = handleInput("");
	const [password, onChangePassword] = handleInput("");
	const [loading, setLoading] = useState(false);
	const [isCorrect, setIsCorrect] = useState(true);
	const dispatch = useAppDispatch();

	const handleSubmit = async () => {
		setLoading(true);
		try {
			const response = await loginByEmailAPI({email, password});

			dispatch(login(response.data.user));
			setIsModalOpen(false);
			setIsCorrect(true);
		} catch (e) {
			console.log("로그인 실패", e);
			setIsCorrect(false);
		} finally {
			setLoading(false);
		}
	};

	return (
		<StyledSpace direction="vertical" size="large">
			<Text>이메일로 로그인</Text>
			<Form
				labelCol={{span: 5}}>
				<Form.Item
					label="이메일"
					name="email"
				>
					<Input onChange={onChangeEmail} placeholder="이메일을 입력하세요" value={email}/>
				</Form.Item>
				<Form.Item
					label="비밀번호"
					name="password"
				>
					<Input.Password onChange={onChangePassword} placeholder="비밀번호를 입력하세요" value={password}/>
				</Form.Item>
				{isCorrect === true ? null : <div style={{textAlign:"center", color:"red"}}>이메일 또는 비밀번호가 일치하지 않습니다</div>}
				<StyledFormItem>
					<Button type="primary" htmlType="submit" onClick={handleSubmit} loading={loading}>로그인</Button>
				</StyledFormItem>
			</Form>
			<StyledDiv>
				<StyledText>
					아직 계정이 없으신가요?<Button type="link" onClick={switchForm}>회원가입</Button>
				</StyledText>
			</StyledDiv>
		</StyledSpace>
	);
}
