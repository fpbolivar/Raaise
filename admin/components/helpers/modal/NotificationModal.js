import React from "react";
import {Modal,ModalContent,CloseButton} from "./CustomModal.styled";
import styled from "styled-components";
import colors from "../../../colors";

const Wrapper = styled.div`
    width: 100%;
    padding:20px;
    table { border: 1px solid #ccc; border-collapse: collapse; width: 100%;}
    table caption { font-size: 24px; margin: 10px;}
    table tr {border: 2px solid #e0e0e0;padding: .35em;border-bottom: 3px solid #e0e0e0;}
    table th,
    table td { padding: .625em; text-align: center;border-bottom: 3px solid #e0e0e0;border-right: 3px solid #e0e0e0}
    table th { padding: .625em; font-size: 14px; letter-spacing: .1em; background:${colors.white}; text-align:center; border-right:3px solid #e0e0e0;}
    @media screen and (max-width: 600px) {
     table {border: 0;}
     table thead {border: none; clip: rect(0 0 0 0); height: 1px; margin: -1px; overflow: hidden; padding: 0; position: absolute; width: 1px;}
     table tr {border-bottom: 3px solid #e0e0e0;display: block;}
     table td { border-bottom: 2px solid #e0e0e0; display: block; font-size: .8em; text-align: right;}
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
     & .capitalize {
      text-transform: capitalize;
     }
`;

const Image = styled.div`
  text-align: center;
  & img{border-radius: 50%; width: 150px; height: 150px;}
`;

class NotificationModal extends React.Component {
  constructor(props) {
    super(props);
    this.state={
    }
  }

  render() {
    const {  modalAction,userData} =this.props;
    return (
      <Modal>
        <ModalContent width="70%" height="400px" top="60px">
          <CloseButton>
            <span className="title">User Details</span>
            <svg className="btn" focusable="false" aria-hidden="true" viewBox="0 0 24 24" data-testid="ClearIcon" fill="red" onClick={() => modalAction(false)}> <path d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"></path> </svg>
          </CloseButton>
          <Wrapper>
            <table>
              <tbody>
                <tr>
                    <th scope="col">Serial No.</th>
                    <th scope="col">Username</th>
                    <th scope="col">Email Address</th>
                    <th scope="col">Phone Number</th>
                </tr>
                {userData.sendTo.map((item,i) => {
                  return(
                  <tr>
                    <td scope="col">{i+1}</td>
                    <td scope="col" className="capitalize">{item.userName}</td>
                    <td scope="col">{item.email}</td>
                    <td scope="col">{item.phoneNumber}</td>
                  </tr>
                  )
                })}
              </tbody>
            </table>
          </Wrapper>
        </ModalContent>
      </Modal>
    );
  }
}

export default NotificationModal;
