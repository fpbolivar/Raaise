import React from "react";
import FilteringTable from "../CustomTable/FilteringTable";
import {Container,Wrapper,Header,ExportButton,ActionSection} from "./Users.styled";
import { USERS } from "../../ApiConstant";
import axios from "../../utils/axios";
import { withRouter } from "next/router";
import CustomModal from "../helpers/modal/CustomModal";
import UserModal from "../helpers/modal/UserModal";
import { exportData } from "../helpers/exports";

class ViewProfile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedOption: null,
      selectedUserType: null,
      users: [],
      columnsFilters: {},
      category: [],
      userPageNo: 1,
      userlimit: 10,
      pagination: {
        current: 1,
        next: 1,
        totalData: 0,
        totalPages: 1,
        previous: 0,
        isLoading: true,
        sort_column: "",
        sort_by: "",
      },
      openModal: {
        open: false,
        title: "",
        description: "",
        id: "",
        action: "",
        isBlock: "",
      },
      openMedia: {
        open: false,
      },
      columns: [
        {
          Header: "Serial No.",
          accessor: "serial",
          disableFilters: true,
        },
        {
          Header: "Username",
          accessor: "userName",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value ? value
                  .toLowerCase()
                  .replace("_", " ")
                  .split(" ")
                  .map(function (word) {
                    return word[0].toUpperCase() + word.substr(1);
                  })
                  .join(" ")
            : "";
          },
        },
        {
          Header: "Email Address",
          accessor: "email",
          disableFilters: true,
        },
        {
          Header: "Phone Number",
          accessor: "phoneNumber",
          disableFilters: true,
        },
        {
          Header: "Status",
          accessor: "isBlock",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value ? "Block" : "Unblock";
          },
        },
        {
          Header: "Actions",
          accessor: "actions",
          disableFilters: true,
          disableSorting: true,
          Cell: ({ cell: { row } }) => {
            return (
              <>
                <ActionSection>
                  {row.original.isBlock ? (
                    <div className="cursor-pointer" title="Unblock" onClick={() =>this.handleBlockUnblock(row.original._id, false) }>
                      <svg width={22} fill="#54B435" aria-hidden="true" viewBox="0 0 24 24" data-testid="RemoveCircleOutlineIcon"><path d="M7 11v2h10v-2H7zm5-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"></path></svg>
                    </div>
                  ) : (
                    <div className = "cursor-pointer" title="Block" onClick={() => this.handleBlockUnblock(row.original._id, true) }>
                      <svg width={22} fill="red" aria-hidden="true" viewBox="0 0 24 24" data-testid="BlockIcon"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM4 12c0-4.42 3.58-8 8-8 1.85 0 3.55.63 4.9 1.69L5.69 16.9C4.63 15.55 4 13.85 4 12zm8 8c-1.85 0-3.55-.63-4.9-1.69L18.31 7.1C19.37 8.45 20 10.15 20 12c0 4.42-3.58 8-8 8z"></path></svg>
                    </div>
                  )}
                  <div className="cursor-pointer" title="View" onClick={() => this.modalActionUser(true, row)}>
                    <svg viewBox="0 0 24 24" width={22} fill="black"><path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"></path></svg>
                  </div>
                  </ActionSection>
              </>
            );
          },
        },
      ],
    };
  }
  //getUsers() function is called
  componentDidMount = () => {
    const { userPageNo, userlimit, userType } = this.state;
    this.getUsers({
      pageNo: userPageNo,
      limit: userlimit,
    });
  };

  //API calling in this function
  getUsers = async (obj) => {
    try {
      this.setState((prevState) => ({
        pagination: {
          ...prevState.pagination,
          isLoading: true,
        },
      }));
      let { userPageNo, userlimit, users, columnsFilters } = this.state;
      userPageNo = obj.pageNo ? obj.pageNo : userPageNo;
      userlimit = obj.limit ? obj.limit : userlimit;

      if (obj.column) {
        columnsFilters[obj.column] = obj.value;
        userPageNo = 1;
      }
      const { data } = await axios.post(`${USERS.getUsers}`, {
        page: userPageNo,
        limit: userlimit,
        ...columnsFilters,
      });
      if (data.status == 200) {
        this.setState({
          users: data.data,
          pageNo: userPageNo,
          userlimit: userlimit,
          columnsFilters: columnsFilters,
          pagination: {
            current: data.current || 0,
            next: data.next || 0,
            totalData: data.totalData || 0,
            totalPages: data.totalPages || 0,
            previous: data.previous,
            isLoading: false,
          },
        });
      }
    }catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log("error", e);
    }
  };

  //to open User Modal
  modalActionUser = (status, value = '') => {
    this.setState({
      openMedia: {
        open: status,
        userData:value.original,
        id:value ? value.original._id:""
      },
    });
  };

  // to block/unblock the user
  modalAction = (status) => {
    const { openModal } = this.state;
    if (status) {
      if (openModal.id && openModal.action === "block") {
        this.blockUser(openModal.id, openModal.isBlock);
      }
      this.setState({
        openModal: {
          open: false,
        },
      });
    }else {
      this.setState({
        openModal: {
          open: status,
        },
      });
    }
  };

  //to get all the user's listing without pagination on click of EXPORT button
  csvExport = async () => {
    document.getElementById("custom-loader").style.display = "block";
      try {
        const { data } = await axios.post(`${USERS.getUsers}`, {
          type: "export",
        });
        if (data.status == 200) {
          const resData= data.data
          const file = "users.csv";
          const arr = [];
          const col = ["Username", "Email Address ", "Phone Number", "Status","Login Via","Android/IOS User","Total Videos","Total Followers","Total Donation Received","Total Donation Given"];
          resData && resData.length > 0 && resData.forEach((element) => {
            const propertyValues = [
                    element.userName,
                    element.email,
                    element.phoneNumber,
                    element.isBlock ? "Block" : "Unblock",
                    element.loginType,
                    element.deviceType,
                    element.videoCount? element.videoCount :0,
                    element.followersCount ? element.followersCount : 0,
                    element.donationReceived ? element.donationReceived :0,
                    element.donationGiven ? element.donationGiven :0
            ];
            arr.push(propertyValues);
          });
          exportData(arr, col, file)
        }
        document.getElementById("custom-loader").style.display = "none";
      }catch (e) {
        document.getElementById("custom-loader").style.display = "none";
        console.log("error", e);
      }
  };

  //to show content in the Modal whether to block/unblock user
  handleBlockUnblock = (id, status) => {
    this.setState({
      openModal: {
        open: true,
        title: status ? "Block user" : " Unblock user",
        description: status
          ? "Are you sure you want to BLOCK this user?"
          : "Are you sure you want to UNBLOCK this user?",
        id: id,
        action: "block",
        isBlock: status,
      },
    });
  };

  //In this function,API calling to block and unblock user 
  blockUser = async (id, status) => {
    const { userPageNo, userlimit } = this.state;
    try {
      const { data } = await axios.post(USERS.userBlockUnblock, {
        id: id,
        isBlock: status,
      });
      if (data.status === 200) {
        this.getUsers({
          pageNo: userPageNo,
          limit: userlimit,
        });
      }
    } catch (e) {
      console.log("error", e);
    }
  };
  render() {
    const {columns,users,pagination,openModal,openMedia} = this.state;
    return (
      <Container>
        {openModal.open && (
          <CustomModal                //to ask from Admin whether to block/unblock user in this Modal
            {...openModal}
            modalAction={(status) => this.modalAction(status)}
          />
        )}
        
        {openMedia.open && (
          <UserModal                 //On click of Eye Button,User details will be shown in the User Modal
            {...openMedia}
            modalAction={(status) => this.modalActionUser(status)}
          />
        )}
        <Wrapper>
          <Header>
              <span className="title">Users</span>
              <ExportButton onClick={() => this.csvExport()}>
                <span>EXPORT</span>
                <svg className="MuiSvgIcon-root MuiSvgIcon-fontSizeMedium MuiBox-root css-1om0hkc" focusable="false" width="25" height="25" fill="white" aria-hidden="true" viewBox="0 0 24 24"  data-testid="FileUploadIcon" ><path d="M9 16h6v-6h4l-7-7-7 7h4zm-4 2h14v2H5z"></path></svg>
              </ExportButton>
          </Header>
          <FilteringTable data={users} name="User" columns={columns} pagination={pagination} handleTable={this.getUsers}/>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(ViewProfile);
