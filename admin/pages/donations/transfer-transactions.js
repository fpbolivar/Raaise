import React from "react";
import Layout from "../../components/layout";
import TableData from "../../components/donation-reports/TransferTransactions";
class TransferTransactions extends React.Component{
    render() {
        return(
            <Layout>
                <TableData/>
            </Layout>
        )
    }
}
export default TransferTransactions;