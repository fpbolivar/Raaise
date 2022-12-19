import React from "react";
import Layout from "../../components/layout";
import TableData from "../../components/donation-reports/WithdrawRequest";
class WithdrawRequests extends React.Component{
    render() {
        return(
            <Layout>
                <TableData/>
            </Layout>
        )
    }
}
export default WithdrawRequests;