import router from "next/router";
import { useRouter } from "next/router";
const withAuth = (Component) => {
    const Auth = (props) => {
        // Login data added to props via redux-store (or use react context for example)
        const token = localStorage.getItem("scriptube-admin-token");
        // If user is not logged in, return login component
        if (!token) {
            router.push("/");
        }
        // If user is logged in, return original component
        return <Component {...props} />;
    };
    // Copy getInitial props so it will run as well
    if (Component.getInitialProps) {
        Auth.getInitialProps = Component.getInitialProps;
    }
    return Auth;
};
export default withAuth;
