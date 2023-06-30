import apiClient from "@/apis/apiClient";
import axios from "axios";

export interface User {
	id: number;
	username: string;
	email: string;
	profile: string;
	introduction: string;
	blogTitle: string;
	githubLink: string;
	instagramLink: string;
}

export const checkLoginStatus = async () => {
	try {
		return await apiClient.get("/users/login-status");
	} catch (e) {
		throw e;
	}
};

export const loginByEmailAPI = async userDetails => {
	try {
		return await apiClient.post("/login/email", userDetails, {
			headers: {
				"Content-Type": "application/json",
			},
		});
	} catch (e) {
		throw e;
	}
};

export const logoutAPI = async () => {
	try {
		await apiClient.post("/logout");
	} catch (e) {
		throw e;
	}
};

export const signoutAPI = async (id: number) => {
	let errorMsg;

	try {
		await apiClient.delete(`/users/${id}`);
	} catch (e) {
		switch (e.response.status) {
			case 401:
				errorMsg = "현재 로그인한 사용자가 아닌 사용자의 정보로 요청했습니다";
				break;
			case 404:
				errorMsg = "존재하지 않는 자원에 접근하였습니다.";
				break;
			case 500:
				errorMsg = "서버 에러입니다.";
				break;
			default:
				errorMsg = e.response.status;
				break;
		}
		throw new Error(errorMsg);
	}
};

export const modifyBlogTitleAPI = async (blogTitle: string, id: number) => {
	try {
		await apiClient.put(`/users/blogTitle/${id}`, {blogTitle});
	} catch (e) {
		throw new Error(e);
	}
};

export interface ModifyingSocialInfo {
	githubLink: string;
	instagramLink: string;
}

export const modifySocialInfoAPI = async (
	{githubLink, instagramLink}: ModifyingSocialInfo, id: number,
) => {
	try {
		await apiClient.put(`/users/social/${id}`, {githubLink, instagramLink});
	} catch (e) {
		throw new Error(e);
	}
};

export interface ModifyingBasicInfo {
	username: string;
	introduction: string;
}

export const modifyBasicInfoAPI = async (
	{username, introduction}: ModifyingBasicInfo, id: number,
) => {
	try {
		await apiClient.put(`/users/basic/${id}`, {username, introduction});
	} catch (e) {
		throw new Error(e);
	}
};

export const modifyProfileImageAPI = async (profile: FormData, id: number) => {
	try {
		const resp = await apiClient.put(`/users/profileImage/${id}`, profile, {
			headers: {
				"Content-Type": "multipart/form-data",
			},
		});

		return resp.data;
	} catch (e) {
		throw new Error(e);
	}
};

export const getProfileImageAPI = async (id: number) => {
	try {
		const resp = await apiClient.get(`/users/profile/${id}`, {
			responseType: "blob",
		});

		return resp.data;
	} catch (e) {
		throw new Error(e);
	}
};
