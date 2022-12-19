import React from "react";
import Main from "../components/dashboard/Section1";
import Layout from "../components/layout";
import withAuth from "../components/Auth";
import styled from "styled-components";
import { DASHBOARD } from "../ApiConstant";
import axios from "../utils/axios";
import Graph from "../components/dashboard/Graph";

const GraphsSection = styled.div`
  padding: 0px 20px 20px 20px;
  width: 100%;
  & .graphs-div {
    display: flex;
    column-gap: 20px;
    row-gap: 20px;
    width: 100%;
    @media only screen and (max-width: 768px) {
      flex-direction: column;
      overflow-y: scroll;
    }
  }
`;
class Dashboard extends React.Component {
  constructor(props) {
    super(props);
      this.state = {
        graph:{
          userRegistrationGraph:[],
          videoCountGraph:[],
          withdrawGraph:[],
          donationGraph:[],
        }
      };
  }

  //getGraphData() function is called
  componentDidMount() {
    document.getElementById("custom-loader").style.display = "block";
    this.getGraphData();
  }

  //In this function,API is called
  getGraphData = async () => {
    try{
        const { data } = await axios.get(`${DASHBOARD.getGraphData}`);  //API calling
        if (data.status == 200){
            this.setState({
                graph:{userRegistrationGraph: data.UserRegistrationGraph,
                videoCountGraph: data.VideoCountGraph,
                withdrawGraph: data.WithdrawGraph,
                donationGraph: data.donationData}
            });
        }
      document.getElementById("custom-loader").style.display = "none";
    } 
    catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log(e,"e")
    }
  };
  render() {
    const {graph} = this.state;
    return(
      <div>
        <Layout>
          <Main />
            <GraphsSection>
              <div className="graphs-div">
                <Graph data={graph.userRegistrationGraph} name={"User Registration Graph"}/>
                <Graph data={graph.withdrawGraph} name={"Withdraw Amount Graph"}/>
              </div>
            </GraphsSection>
            <GraphsSection>
              <div className="graphs-div">
                <Graph data={graph.donationGraph} name={"Donation Graph"}/>
                <Graph data={graph.videoCountGraph} name={"Video Count Graph"}/>
              </div>
            </GraphsSection>
        </Layout>
      </div>
    );
  }
}
export default withAuth(Dashboard);
