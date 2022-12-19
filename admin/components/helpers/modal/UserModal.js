import React from "react";
import {Modal,ModalContent,CloseButton} from "./CustomModal.styled";
import styled from "styled-components";
import colors from "../../../colors";

const Wrapper = styled.div`
    width: 100%;
    padding:20px;
    table { border: 1px solid #ccc; border-collapse: collapse; width: 100%;}
    table caption { font-size: 24px; margin: 10px;}
    table tr {background-color: #f8f8f8;border: 2px solid #e0e0e0;padding: .35em;}
    table th,
    table td { padding: .625em; text-align: center;}
    table th { font-size: 14px; letter-spacing: .1em; background:${colors.white}; text-align: left; border-right: 3px solid #e0e0e0;}
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
`;

const Image = styled.div`
  display: table-caption;
  text-align: center;
  & img{border-radius: 50%; width: 150px; height: 150px;}
`;

class UserModal extends React.Component {
  constructor(props) {
    super(props);
    this.state={
    }
  }

  render() {
    const {  modalAction,userData} =this.props;
    return (
        <Modal>
          <ModalContent width="60%" height="560px" top="60px">
            <CloseButton>
              <span className="title">User Details</span>
              <svg className="btn" focusable="false" aria-hidden="true" viewBox="0 0 24 24" data-testid="ClearIcon" fill="red" onClick={() => modalAction(false)}> <path d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"></path> </svg>
            </CloseButton>
            <Wrapper>
                <table>
                    <Image>
                      <img  src={userData.profileImage ? userData.profileImage : "/assets/images/profile.png"} />
                    </Image>
                    <caption>{userData.name}</caption>
                    <tbody>
                        <tr>
                            <th scope="col">Username</th>
                            <th scope="col">{userData.userName }</th>
                        </tr>
                        <tr>
                            <th scope="col">Email Address</th>
                            <th scope="col">{userData.email}</th>
                        </tr>
                        <tr>
                            <th scope="col">Phone Number</th>
                            <th scope="col">{userData.phoneNumber}</th>
                        </tr>
                        <tr>
                            <th scope="col">Login Via</th>
                            <th scope="col">{userData.loginType}</th>
                        </tr>
                        <tr>
                            <th scope="col">Android/IOS User</th>
                            <th scope="col">{userData.deviceType}</th>
                        </tr>
                        <tr>
                            <th scope="col">Total Videos</th>
                            <th scope="col">{userData.videoCount ? userData.videoCount : 0}</th>
                        </tr>
                        <tr>
                            <th scope="col">Total Followers</th>
                            <th scope="col">{userData.followersCount ? userData.followersCount : 0}</th>
                        </tr>
                        <tr>
                            <th scope="col">Total Donation Received</th>
                            <th scope="col">{userData.donationReceived ? userData.donationReceived : 0}</th>
                        </tr>
                        <tr>
                            <th scope="col">Total Donation Given</th>
                            <th scope="col">{userData.donationGiven ? userData.donationGiven : 0}</th>
                        </tr>
                    </tbody>
                </table>
            </Wrapper>
          </ModalContent>
        </Modal>
    );
  }
}

export default UserModal;
