import React from "react";
import { withRouter } from "next/router";
import FilteringTable from "../CustomTable/FilteringTable";
import {Container,Wrapper} from "/components/users/Users.styled";
import { USERS } from "../../ApiConstant";
import axios from "../../utils/axios";
import styled from "styled-components";
import { TableHeader} from "../CustomTable/Table.styled"
const Button = styled.button`
  cursor: pointer;
  background-color: ${(props)=> props.bg ? "#c9302c":"#4C75A3" };
  border: none;
  border-radius: 5px;
  width: 65%;
  height: 30px;
  color: white;
  font-size: 14px;
`;
class VerifiedUsers extends React.Component {
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
            },
            {
              Header: "Phone Number",
              accessor: "phoneNumber",
              disableFilters: true,
            },
            {
              Header: "Total Video",
              accessor: "videoCount",
              disableFilters: true,
              Cell: ({ cell: { row } }) => {     
                return (
                  <>
                    {row.original.videoCount ? (row.original.videoCount) : (0)}
                  </>
                );
              },
            },
            {
              Header: "Status",
              accessor: "",
              disableFilters: true,
              Cell: ({ cell: { row } }) => {     
                return (
                  <>
                    {row.original.isVerified ? (<span>Verified</span>) : (<span>Unverified</span>)}
                  </>
                );
              },
            },  
            {
              Header: "Action",
              accessor: "isVerified",
              disableFilters: true,
              Cell: ({ cell: { row } }) => {  
                return (
                  <>
                    {row.original.isVerified ? 
                      (
                        <Button bg="red" title="unverify" onClick={() =>this.verifyUnVerifyUser(row.original._id, false,row.index)}>   
                          UNVERIFY 
                        </Button>
                        ) : (
                        <Button title="verify" onClick={() =>this.verifyUnVerifyUser(row.original._id,true,row.index)}>
                          VERIFY
                        </Button>
                    )}
                  </>
                );
              },
            },
        ],
    };
  }

  //getVerifiedUser() fucntion is called .
  componentDidMount = () => {
    const { userPageNo, userlimit } = this.state;
    this.getVerifiedUser({
      pageNo: userPageNo,
      limit: userlimit,
    });
  };

  // in this function,API is called
  verifyUnVerifyUser = async (id, status,index) => {
    const {users} = this.state
    try {
      const { data } = await axios.post(USERS.verifyUnVerifyUser, {   // API calling
        userId: id,
        isVerified: status,
      });
      if (data.status == 200) {
        this.setState({
          status: status,
          users:users.filter(item=>item._id!==id)
        })
      }
      document.getElementById("custom-loader").style.display = "none";
    }catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log("error", e);
    }
  };
      
  //In this, function API is called
    getVerifiedUser = async (obj) => {
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
        const { data } = await axios.post(`${USERS.getVerifiedUser}`, {
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
        document.getElementById("custom-loader").style.display = "none";
      }catch (e) {
        document.getElementById("custom-loader").style.display = "none";
        console.log("error", e);
      }
    };
    render() {
        const {columns,users,pagination} = this.state;
        return (
          <Container>
            <Wrapper>
                <TableHeader>
                  <span className="title">Verified Users</span>
                  <div className="push-notif-div">
                    <a href="/verification/verify-new-user">+Verify New User</a>
                  </div>
                </TableHeader>
              <FilteringTable data={users} columns={columns} pagination={pagination} handleTable={this.getVerifiedUser}/>
            </Wrapper>
          </Container>
        );
    }
}
export default withRouter (VerifiedUsers);
