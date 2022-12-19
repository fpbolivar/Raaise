import React from "react";
import Main from "../../components/push-notification/PushNotification";
import Layout from "../../components/layout";
class PushNotifications extends React.Component{
    render() {
        return(
            <Layout>
               <Main/>
            </Layout>
        )
    }
}
export default PushNotifications;