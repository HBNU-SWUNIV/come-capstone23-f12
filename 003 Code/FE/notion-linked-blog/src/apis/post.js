import {apiClient} from "@/apis/apiClient";

export const requestSubmitPostAPI = async post => {
  try {
    await apiClient.post("/posts", post, {
      withCredentials: true,
      headers: {
        "Content-Type": "application/json",
      },
    });
  } catch (e) {
    // 400 : Bad Request
    // 401 : Unauthorized
    throw e;
  }
};