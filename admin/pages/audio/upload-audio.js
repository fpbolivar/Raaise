import React from "react";
import Main from "../../components/audio/UploadAudio";
import Layout from "../../components/layout";
import withAuth from "../../components/Auth";
class UploadAudio extends React.Component {
    render() {
        return (
            <Layout>
                <Main />
            </Layout>
        )
    }
}
export default withAuth(UploadAudio);