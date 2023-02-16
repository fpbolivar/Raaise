import React from "react";
import Main from "../../components/push-notification/NotificationLog";
import Layout from "../../components/layout";
class NotificationLog extends React.Component{
    render() {
        return(
            <Layout>
               <Main/>
            </Layout>
        )
    }
}
export default NotificationLog;