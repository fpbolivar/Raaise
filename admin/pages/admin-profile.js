import React from "react";
import Main from "../components/admin-profile/AdminProfile";
import Layout from "../components/layout";
import withAuth from "../components/Auth";
class AdminProfile extends React.Component {
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
export default withAuth(AdminProfile);
