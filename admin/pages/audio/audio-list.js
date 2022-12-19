import React from "react";
import Main from "../../components/audio/AudioList";
import Layout from "../../components/layout";
import withAuth from "../../components/Auth";
class AudioList extends React.Component {
    render() {
        return (
            <Layout>
                <Main />
            </Layout>
        );
    }
}
export default withAuth(AudioList);
