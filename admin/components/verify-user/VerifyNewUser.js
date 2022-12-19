import {  withRouter } from "next/router";
import React from "react";
import { TableHeader, Section,Container} from "../CustomTable/Table.styled"
import FilteringTable from "../CustomTable/FilteringTable";
import axios  from "../../utils/axios";
import { USERS } from "../../ApiConstant";
import { AsyncPaginate } from "react-select-async-paginate";
import c from "../../utils/Constants"
import styled from "styled-components";
import colors from "../../colors";

const Wrapper = styled.div`
& .select-user{
    display: flex;
    padding: 25px 0px;
    & .select-section{ display:flex; width:100%; column-gap:20px;}
    & .btn-div{
      width:10%;
      & .btn{ height:50px; width:100%; background:${colors.blueColor};color:${colors.white}; cursor:pointer; border:none; border-radius: 5px; font-size: 15px; font-weight: bold;}
    }
    & input{background:red line-height: 1.6; height: 39px; }
}
`;
const Button = styled.button`
  cursor: pointer;
  background-color: ${(props)=> props.bg ? "#c9302c":"#4C75A3" };
  border: none;
  border-radius: 5px;
  width: 50%;
  height: 30px;
  color: white;
  font-size: 14px;
`;
class VerifyNewUsers extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectOption: "",
      selectedOption: null,
      users: [],
      userIds:"",
      options: [],
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
        isLoading: false,
        sort_column: "",
        sort_by: "",
      },
      openModal: {
        open: false,
        title: "",
        description: "",
        id: "",
        action: "",
      },
      tempUser:null,
      columns:[
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
                  {row.original.isVerified ? (
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
  verifyUnVerifyUser = async (id, status,index) => {
    const {users} = this.state
    try {
      const { data } = await axios.post(USERS.verifyUnVerifyUser, {
        userId: id,
        isVerified: status,
      });
      if (data.status === 200) {
        this.setState({
          status: status,
        })
        users[index].isVerified = data.data.isVerified;
      }
    }catch (e) {
      console.log("error", e);
    }
  };
  getUsers = async () => {
    try {
      const { data } = await axios.get(`${USERS.searchUser}`);
      if (data.status == 200) {
        let arr= data.data.map(item => {
          return {value: item._id, id:item._id, label:item.userName, phoneNumber:item.phoneNumber, isVerified:item.isVerified }
        })
        this.setState({
          options: arr,
        });
      }
      document.getElementById("custom-loader").style.display = "none";
    } catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log("error", e);
    }
  };

  searchUser =  () => {
    const {tempUser} = this.state
    this.setState({
      users:tempUser,
      pageNo: 1,
      pagination: {
        isLoading: false,
      },
    })
  };

  loadOptions = async (searchQuery, loadedOptions, { page }) => {
    const { data } = await axios.get(
      `${c.API_BASE_URL}/admin/search-user?query=${searchQuery}&page=${page}`
    );
    return {
      options: data.data,
      hasMore: data.data.length >= 1,
      additional: {
        page: searchQuery ? 2 : page + 1
      }
    };
  };

  render() {
    const {columns,users,pagination,tempUser} = this.state;
    return (
      <>
        <Container>
            <Section>
              <TableHeader>
                <span className="title">Verify New User</span>
              </TableHeader>
              <Wrapper>
               <div className="select-user">
                <div className="select-section">
                  <div style={{width:"100%"}}>
                    <AsyncPaginate value={tempUser}
                      onChange={(user) => this.setState({tempUser:user})}
                      placeholder="Select User"
                      loadOptions={this.loadOptions}    
                      getOptionValue={(option) => option.userName}
                      getOptionLabel={(option) => option.userName}
                      additional={{
                          page: 1,
                      }}
                      isMulti />
                  </div>
                  <div className="btn-div">
                    <button className="btn" onClick={this.searchUser}>Go</button>
                  </div>
                </div>
               </div>
               <div className="user-div">
                  <FilteringTable data={users} columns={columns} pagination={pagination} handleTable={this.getUsers}/>
               </div>
              </Wrapper>
            </Section>
        </Container>
      </>
    );
  }
}
export default withRouter(VerifyNewUsers);
