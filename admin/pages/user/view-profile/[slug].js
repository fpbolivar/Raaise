import React from "react";
import Main from "../../../components/users/ViewProfile";
import Layout from "../../../components/layout";
import { withRouter } from "next/router";

class ViewProfile extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        const { router } = this.props;
        const { slug } = router.query;
        return (
            <div>
                <Layout>{slug ? <Main slug={slug} /> : null}</Layout>
            </div>
        );
    }
}
export default withRouter(ViewProfile);
