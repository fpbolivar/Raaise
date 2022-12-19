import React from "react";
import Main from "../../components/push-notification/ViewPushNotification";
import Layout from "../../components/layout";
class ViewNotifications extends React.Component{
    render() {
        return(
            <Layout>
               <Main/>
            </Layout>
        )
    }
}
export default ViewNotifications;