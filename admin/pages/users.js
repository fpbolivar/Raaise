import React from "react";
import Main from "../components/users/ViewProfile";
import Layout from "../components/layout";
import withAuth from "../components/Auth";
class Users extends React.Component {
    render() {
        return (
            <div>
                <Layout>
                    <Main />
                </Layout>
            </div>
        );
    }
}
export default withAuth(Users);
