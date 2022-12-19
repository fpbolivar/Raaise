import React from "react";
import Main from "../components/GeneralSetting";
import Layout from "../components/layout";
import withAuth from "../components/Auth";
class GeneralSetting extends React.Component {
    render() {
        return (
            <Layout>
                <Main />
            </Layout>
        )
    }
}
export default withAuth(GeneralSetting);