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
          Header: "Login Via",
          accessor: "loginType",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return (
              <>
                <ActionSection>
                  {row.original.loginType === "google" ? (
                    <svg width="22" height="22" title="Google" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><g clip-path="url(#clip0_17_40)"> <path d="M47.532 24.5528C47.532 22.9214 47.3997 21.2811 47.1175 19.6761H24.48V28.9181H37.4434C36.9055 31.8988 35.177 34.5356 32.6461 36.2111V42.2078H40.3801C44.9217 38.0278 47.532 31.8547 47.532 24.5528Z" fill="#4285F4"/><path d="M24.48 48.0016C30.9529 48.0016 36.4116 45.8764 40.3888 42.2078L32.6549 36.2111C30.5031 37.675 27.7252 38.5039 24.4888 38.5039C18.2275 38.5039 12.9187 34.2798 11.0139 28.6006H3.03296V34.7825C7.10718 42.8868 15.4056 48.0016 24.48 48.0016Z" fill="#34A853"/><path d="M11.0051 28.6006C9.99973 25.6199 9.99973 22.3922 11.0051 19.4115V13.2296H3.03298C-0.371021 20.0112 -0.371021 28.0009 3.03298 34.7825L11.0051 28.6006Z"  fill="#FBBC04"/><path d="M24.48 9.49932C27.9016 9.44641 31.2086 10.7339 33.6866 13.0973L40.5387 6.24523C36.2 2.17101 30.4414 -0.068932 24.48 0.00161733C15.4055 0.00161733 7.10718 5.11644 3.03296 13.2296L11.005 19.4115C12.901 13.7235 18.2187 9.49932 24.48 9.49932Z" fill="#EA4335"/></g> <defs> <clipPath id="clip0_17_40"> <rect width="48" height="48" fill="white" /></clipPath></defs></svg>
                    )
                    :
                    row.original.loginType === "facebook" ?(
                      <svg width="22" height="22" title="Facebook" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><g clip-path="url(#clip0_17_24)"><path d="M48 24C48 10.7452 37.2548 0 24 0C10.7452 0 0 10.7452 0 24C0 35.9789 8.77641 45.908 20.25 47.7084V30.9375H14.1562V24H20.25V18.7125C20.25 12.6975 23.8331 9.375 29.3152 9.375C31.9402 9.375 34.6875 9.84375 34.6875 9.84375V15.75H31.6613C28.68 15.75 27.75 17.6002 27.75 19.5V24H34.4062L33.3422 30.9375H27.75V47.7084C39.2236 45.908 48 35.9789 48 24Z" fill="#1877F2"/><path d="M33.3422 30.9375L34.4062 24H27.75V19.5C27.75 17.602 28.68 15.75 31.6613 15.75H34.6875V9.84375C34.6875 9.84375 31.9411 9.375 29.3152 9.375C23.8331 9.375 20.25 12.6975 20.25 18.7125V24H14.1562V30.9375H20.25V47.7084C22.7349 48.0972 25.2651 48.0972 27.75 47.7084V30.9375H33.3422Z" fill="white"/></g><defs><clipPath id="clip0_17_24"><rect width="48" height="48" fill="white" /></clipPath></defs></svg>
                    )
                    :
                    (
                    <svg width="22" height="22" title="Scriptube" viewBox="0 0 1024 1024" fill="#4C75A3" xmlns="http://www.w3.org/2000/svg"><rect width="1024" height="1024" rx="512" fill="#4C75A3"/> <path d="M754.637 469.986L396.672 257.693C365.521 239.201 326.627 262.316 326.627 299.209V723.795C326.627 760.687 365.521 783.802 396.672 765.31L754.637 553.017C785.788 534.525 785.788 488.386 754.637 469.986Z" fill="white"/><path d="M555.888 502.804L474.835 454.073C468.455 450.238 460.368 455.008 460.368 462.771V560.233C460.368 567.997 468.365 572.767 474.835 568.932L555.888 520.201C562.268 516.366 562.268 506.732 555.888 502.804Z" fill="#4C75A3"/></svg>
                    )
                  } 
                </ActionSection>
              </>
            );
          },
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
    const { userPageNo, userlimit} = this.state;
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
                    element.walletCreditAmount ? element.walletCreditAmount :0,
                    element.donatedAmount ? element.donatedAmount :0
            ];
            arr.push(propertyValues);
          });
          exportData(arr, col, file)
        }
        document.getElementById("custom-loader").style.display = "none";
      }catch (e) {
        document.getElementById("custom-loader").style.display = "none";
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
