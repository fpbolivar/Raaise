import React from "react";
import styled from "styled-components";
import Section2 from "./Section2";
import axios from "../../utils/axios";
import { DASHBOARD } from "../../ApiConstant";
import { withRouter } from "next/router";
import colors from "../../colors";
const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    .col-md-2 {width: 18.66%;background: ${colors.white};min-height: 100px;}
    .col-md-12 {width: 100%;}
    .bg-white {background:${colors.white};}
    .p-10 {padding: 10px;}
    .br-10 {border-radius: 10px;}
    .label {color: ${(props) => props.txtColor || "black"};margin-top: 7px;font-size: 16px;justify-content: center;}
    .count-text {color: ${(props) => props.txtColor || "#1a2b88"};font-weight: 700;margin-top: 11px;font-size: 16px;text-align:center;}
    /* Tablet */
    @media only screen and (max-width: 768px) {
        flex-direction: column;
        overflow-y: scroll;
        display: flex;
        flex-wrap: wrap;
    }
`;
const Container = styled.div`
    background-color: lightgrey;
    display: flex;
    padding: 20px 20px 0px 20px;
    flex-direction: column;
    gap: 2%;
    .mb-10 {
        margin-bottom: 20px;
    }
`;
const Row = styled.div`
    display: flex;
    gap: 2%;

    @media only screen and (max-width: 768px) {
        flex-direction: column;
        grid-gap: 10px;
        flex-wrap: wrap;
        height: 400px;
        .col-sm-12 {width: 50%;}
    }
    @media only screen and (max-width: 460px) {
        flex-direction: row;
        height: 786px;
        padding-bottom: 20px;
        .col-sm-12 {width: 100%;}
    }
`;
class Section1 extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: {},
        };
    }
    componentDidMount() {
        document.getElementById("custom-loader").style.display = "block";
        this.getDashboardData();  //getDashboardData() function is called
    }
    getDashboardData = async () => {
        try {
            const { data } = await axios.get(`${DASHBOARD.getDashboardData}`); //API calling
            if (data.status == 200) {
                this.setState({
                    user: data.data,
                });
            }
        document.getElementById("custom-loader").style.display = "none";
        } catch (e) {
            document.getElementById("custom-loader").style.display = "none";
        }
    };
    render() {
        const {user} = this.state;
        return(
              <Container>
                    <Wrapper className="mb-10">
                        <Row>
                            <div className="col-md-2 col-sm-12 p-10 br-10 tooltip">
                                <span className="tooltiptext">Total number of Blocked Videos:{user.blockedVideos || 0}</span>
                                <h3 className="label">Blocked Videos</h3>
                                <h1 className="count-text">{user.blockedVideos || 0}</h1>
                            </div>

                            <div className="col-md-2 col-sm-12 p-10 br-10 tooltip">
                                <span className="tooltiptext">Total number of Videos:{user.totalVideos || 0}</span>
                                <h3 className="label">Total Videos</h3>
                                <h1 className="count-text">{user.totalVideos || 0}</h1>
                            </div>

                            <div className="col-md-2 col-sm-12 p-10 br-10 tooltip">
                                <span className="tooltiptext">Total number of Audios:{user.TotalAudios || 0}</span>
                                <h3 className="label">Total Audios</h3>
                                <h1 className="count-text">{user.TotalAudios || 0}</h1>
                            </div>

                            <div className="col-md-2 col-sm-12 p-10 br-10 tooltip">
                                <span className="tooltiptext">Total Transferred Donation:{user.TranferredDonation || 0}</span>
                                <h3 className="label">Transferred Donation</h3>
                                <h1 className="count-text">{user.TranferredDonation || 0}</h1>
                            </div>

                            <div className="col-md-2 col-sm-12 p-10 br-10 tooltip">
                                <span className="tooltiptext">Total Donation Received :{user.TotalDonationReceived || 0}</span>
                                <h3 className="label">Total Donation Received</h3>
                                <h1 className="count-text">{user.TotalDonationReceived || 0}</h1>
                            </div>
                        </Row>
                    </Wrapper>
                    <Section2 user={user} />
                </Container>
         );
    }
}
export default withRouter(Section1);
