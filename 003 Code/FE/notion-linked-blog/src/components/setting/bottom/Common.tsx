import {Typography} from "antd";
import styled from "styled-components";

const {Title} = Typography;

export const Container = styled.div`
	display: flex;
	flex-direction: column;
	width: 100%;
	padding: 1rem 0;
	border-top: 0.8px solid rgb(205, 205, 205);
	gap: 0.5rem;
`;

export const RowContainer = styled.div`
	width: 100%;
	display: flex;
	flex-direction: row;
`;

export const StyledTitle = styled(Title)`
	width: 150px;
	margin: 0;
`;
