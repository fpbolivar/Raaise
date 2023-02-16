import React from "react";
import styled from "styled-components";
import { withRouter } from "next/router";
import colors from "../../colors";

// section 2 css
const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    & .bi-row-group { display: flex; flex: 1; column-gap: 20px; }
`;
const Row = styled.div`
    display: flex;
    column-gap: 20px;
    li {list-style: none;}
    .col-md-4 { flex-grow: 1; width: 33.33%; background: white; min-height: 100px;}
    .col-md-12 {width: 100%;}
    .column {display: flex;flex-wrap: wrap;row-gap: 20px;background-color: ${colors.gray1};}
    .bg-white {background: white !important;}
    .p-10 {padding: 10px;}
    .br-10 {border-radius: 10px;}

    @media (max-width: 768px) {
        flex-direction: column;
        grid-gap: 10px;
        .col-sm-12 {width: 100%;}
    }
`;
const Box = styled.div`
    display: flex;
    flex-direction: column;
    padding: 10px;
    text-align:left;
}
`;
const BoxHeader = styled.div`
    display: block;
    width: 100%;
    padding: 10px 0px;
    .title { font-size: 16px; font-weight: 700;}
`;
const BoxBody = styled.div`
    display: flex;
    width: 100%;
    ul,
    li { width: 100%; display: flow-root; margin: 12px 0px;}
    .partition-block { background: #8080801f; border: 0.3px solid #ff000000;}
    .title-section { float: left; color: #06152b; font-size: 14px; font-weight: 500;}
    .count-section { float: right; color: #1a2b88; font-size: 13px; font-weight: 500;}
`;

class Section2 extends React.Component {
    constructor(props) {
        super(props);
    }
    redirectPage = (type) => {
        const { router } = this.props;
        const url = "/users";
        router.push({ pathname: url, query: { user_type: type } });
    };
    render() {
        const { user } = this.props;
        return (
            <Wrapper className="mb-10">
                <Row>
                    <div className="col-md-4 col-sm-12 column">
                        <div className="col-md-12 bg-white p-10 br-10">
                            <Box>
                                <BoxHeader>
                                    <span className="title">Total Users</span>
                                    <span className="count-section tooltip  float-right">
                                        <span className="tooltiptext">
                                            Total number of users :{" "}
                                            {user.totalUser || 0}
                                        </span>
                                    {user.totalUser || 0}
                                    </span>
                                </BoxHeader>
                                <BoxBody>
                                    <ul>
                                        <li className="cursor-pointer" onClick={() => this.redirectPage("user")}>
                                            <span className="title-section">Active Users</span>
                                            <span className="count-section tooltip cursor-pointer">
                                                <span className="tooltiptext">
                                                    Total number of Active Users :{user.ActiveUser || 0}
                                                </span>
                                            {user.ActiveUser || 0}
                                            </span>
                                        </li>

                                        <div className="partition-block" />
                                        <li className="">
                                            <span className="title-section">Blocked Users</span>
                                            <span className="count-section tooltip ">
                                                <span className="tooltiptext">
                                                    Total number of Blocked Users :{user.blockUser || 0}
                                                </span>
                                            {user.blockUser || 0}
                                            </span>
                                        </li>
                                    </ul>
                                </BoxBody>
                            </Box>
                        </div>
                        <div className="col-md-12 bg-white p-10 br-10">
                            <Box>
                                <BoxHeader>
                                    <span className="title">Total Admin Comission</span>
                                </BoxHeader>
                                <BoxBody>
                                    <ul>
                                        <li className="tooltip">
                                            <span className="title-section ">
                                              Admin Comission
                                            </span>
                                            <span className="count-section tooltip ">
                                                <span className="tooltiptext">
                                                    Total Admin Comission:{user.TotalAdminComission || 0}
                                                </span>
                                            {user.TotalAdminComission || 0}
                                            </span>
                                        </li>
                                    </ul>
                                </BoxBody>
                            </Box>
                        </div>
                    </div>
                    <div className="col-md-4 col-sm-12 column">
                        <div className="col-md-12 bg-white p-10 br-10">
                            <Box>
                                <BoxHeader>
                                    <span className="title">Most Used Devices</span>
                                </BoxHeader>
                                <BoxBody>
                                    <ul>
                                        <li className="tooltip ">
                                            <span className="tooltiptext">
                                                Total number of Android Users :{user.andriodUser || 0}
                                            </span>
                                            <span className="title-section">Android Users</span>
                                            <span className="count-section">{user.andriodUser || 0}</span>
                                        </li>
                                        <div className="partition-block" />
                                        <li className="tooltip ">
                                            <span className="tooltiptext">
                                                Total number of IOS Users :{user.iosUser || 0}
                                            </span>
                                            <span className="title-section">IOS Users</span>
                                            <span className="count-section">{user.iosUser || 0}</span>
                                        </li>
                                    </ul>
                                </BoxBody>
                            </Box>
                        </div>
                        <div className="col-md-12 bg-white p-10 br-10">
                            <Box>
                                <BoxHeader>
                                    <span className="title">Pending Withdrawal Transaction</span>
                                </BoxHeader>
                                <BoxBody>
                                    <ul>
                                        <li className="tooltip ">
                                            <span className="tooltiptext">
                                                Total Pending Withdrawal Transaction:{user.pendingWithdrawalTransaction || 0}
                                            </span>
                                            <span className="title-section">Pending Transaction</span>
                                            <span className="count-section">{user.pendingWithdrawalTransaction || 0}</span>
                                        </li>
                                    </ul>
                                </BoxBody>
                            </Box>
                        </div>
                    </div>
                    <div className="col-md-4 col-sm-12 column">
                        <div className="col-md-12 bg-white p-10 br-10">
                            <Box>
                                <BoxHeader>
                                    <span className="title">Social Media Users</span>
                                </BoxHeader>
                                <BoxBody>
                                    <ul>
                                        <li className="tooltip ">
                                            <span className="tooltiptext">
                                                Total number of Facebook Users:{user.facebookUser || 0}
                                            </span>
                                            <span className="title-section">Facebook Users</span>
                                            <span className="count-section">{user.facebookUser || 0}</span>
                                        </li>
                                        <div className="partition-block" />
                                        <li className="tooltip ">
                                            <span className="tooltiptext">
                                                Total number of Google Users:{user.googleUser || 0}
                                            </span>
                                            <span className="title-section">Google Users</span>
                                            <span className="count-section">{user.googleUser || 0}</span>
                                        </li>
                                    </ul>
                                </BoxBody>
                            </Box>
                        </div>
                        <div className="col-md-12 bg-white p-10 br-10">
                            <Box>
                                <BoxHeader>
                                    <span className="title">User Activity</span>
                                </BoxHeader>
                                <BoxBody>
                                    <ul>
                                        <li className="tooltip ">
                                            <span className="tooltiptext">
                                                Total Deactivated Account: {user.deActiveUserLength || 0}
                                            </span>
                                            <span className="title-section">Deactivated Account</span>
                                            <span className="count-section">{user.deActiveUserLength || 0}</span>
                                        </li>
                                    </ul>
                                </BoxBody>
                            </Box>
                        </div>
                    </div>
                </Row>
            </Wrapper>
        );
    }
}
export default withRouter(Section2);
