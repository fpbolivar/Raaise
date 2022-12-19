import React from "react";
import { Modal, ModalContent, CloseButton} from "./CustomModal.styled";
import styled from "styled-components";
import { VIDEO } from "../../../ApiConstant";
import axios from "../../../utils/axios"

const Wrapper = styled.div`
    width: 100%;
    padding:20px;
    table {border: 1px solid #ccc;border-collapse: collapse;width: 100%;table-layout: fixed;}
    table caption {font-size: 24px;margin:15px;}
    table tr {background-color: #f8f8f8;border: 1px solid #ddd;padding: .35em;}
    table th,
    table td {padding: .625em;text-align: center;}
    table th {font-size: 14px; letter-spacing: .1em; text-transform: uppercase;background:#e9ebec;}
      @media screen and (max-width: 600px) {
        table {border: 0;}
        table caption {font-size: 1.3em;}
        table thead {border: none; clip: rect(0 0 0 0); height: 1px; margin: -1px; overflow: hidden; padding: 0;position: absolute; width: 1px;}
        table tr { border-bottom: 3px solid #ddd; display: block; margin-bottom: .625em;}
        table td::before {
          /*
          * aria-label has no advantage, it won't be read inside a table
          content: attr(aria-label);
          */
          content: attr(data-label);
          float: left;
          font-weight: bold;
          text-transform: uppercase;
        }
        table td:last-child {border-bottom: 0;}
      }
`;
class ReportedModal extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      reportedData:[]
     }
  }
  componentDidMount =()=>{
    document.getElementById("custom-loader").style.display = "block";
    this.getReportedVideo()
  }
  getReportedVideo = async()=>{
    const {id} = this.props
    try{
      const { data } = await axios.post(VIDEO.reportedVideo,{videoId:id});
      if(data.status == 200) {
        const resData= data.data
        this.setState({
            reportedData:resData
        })
      }
      document.getElementById("custom-loader").style.display = "none";
    }
    catch(e){
      document.getElementById("custom-loader").style.display = "none";
      console.log("error",e)
    }
  }
  render() {
    const { modalAction, data ,name} = this.props;
    const { reportedData} = this.state
    return (
        <Modal>
          <ModalContent width="60%" height="550px">
            <CloseButton>
            <h3>{name}</h3>
              <svg className="btn" focusable="false" aria-hidden="true" viewBox="0 0 24 24" data-testid="ClearIcon" fill="red" onClick={() => modalAction(false)}>
                <path d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"></path>
              </svg>
            </CloseButton>
                <video controls style={{objectFit: "cover",width: "100%",height: "300px",}}>
                  <source src={data}/>Your browser does not support HTML5 video.
                </video>
            <Wrapper>
              <table>
                <caption>Reported Videos</caption>
                <thead>
                  <tr>
                    <th scope="col">User Name</th>
                    <th scope="col">Reason</th>
                  </tr>
                </thead>
                <tbody>
                    { reportedData && reportedData.length > 0 && reportedData.map((item)=>{
                      return (
                          <tr>
                            <td data-label="User Name">{item.reportedBy.userName && item.reportedBy.userName}</td>
                            <td data-label="Reason">{item.reason && item.reason}</td>
                          </tr>
                        )
                      })
                    }
                </tbody>
              </table>
            </Wrapper>
        </ModalContent>
      </Modal>
    );
  }
}

export default ReportedModal;
