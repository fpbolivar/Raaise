import React from "react";
import Main from "../components/ChangePassword";
import Layout from "../components/layout";
import withAuth from "../components/Auth";
class ChangePassword extends React.Component {
    render() {
        return (
            <Layout>
              <Main />
            </Layout>
        );
    }
}
export default withAuth(ChangePassword);
