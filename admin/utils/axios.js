import axios from "axios";
import c from "../utils/Constants";
const intance = axios.create({
    baseURL: c.API_BASE_URL,
    // baseURL: "https://net-vest.com/"
});
/* send user logged in token in header */
intance.interceptors.request.use(
    (request) => {
        const token = localStorage.getItem("scriptube-admin-token");
        if (token) {
            request.headers.authorization = `${token}`;
        }
        return request;
    },
    (error) => {
        return Promise.reject(error);
    }
);

/**Check if user is logged in or  not if user is not logged in . it will logout the user */
intance.interceptors.response.use(
    (response) => response,
    async (error) => {
        if (error.response.data.status === 401) {
            localStorage.removeItem("scriptube-admin-token");
            localStorage.removeItem("scriptube-admin-details");
            localStorage.removeItem("admin-details");
            if (window.location.pathname !== "/") {
                window.location.href = "/";
            } else {
                window.location.reload();
            }
        }
        return Promise.reject(error);
    }
);
export default intance;
