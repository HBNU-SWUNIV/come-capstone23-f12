import {PayloadAction, createSlice} from "@reduxjs/toolkit";
import {User} from "@/apis/user";

export interface UserState {
	user: User;
}

const initialState: UserState = {
	user: null,
};

const userSlice = createSlice({
	name: "user",
	initialState,
	reducers: {
		login: (state, action: PayloadAction<User>) => {
			state.user = action.payload;
		},
		logout: state => {
			state.user = null;
		},
		modifyBlogTitle: (state, action: PayloadAction<string>) => {
			state.user.blogTitle = action.payload;
		},
	},
});

const {actions, reducer: userReducer} = userSlice;

export const {login, logout, modifyBlogTitle} = actions;

export default userReducer;
