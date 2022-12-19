import React from "react";
import Layout from "../../components/layout";
import TableData from "../../components/donation-reports/PendingTransactions";
class PendingTransactions extends React.Component{
    render() {
        return(
            <Layout>
                <TableData/>
            </Layout>
        )
    }
}
export default PendingTransactions;