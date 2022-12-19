import React from "react";
import Main from "../../components/verify-user/VerifiedUsers";
import Layout from "../../components/layout";
import withAuth from "../../components/Auth";
class VerifiedUsers extends React.Component {
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
export default withAuth(VerifiedUsers);
