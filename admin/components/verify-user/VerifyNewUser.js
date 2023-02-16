import { withRouter } from "next/router";
import React from "react";
import { TableHeader, Section, Container } from "../CustomTable/Table.styled"
import FilteringTable from "../CustomTable/FilteringTable";
import axios from "../../utils/axios";
import { USERS } from "../../ApiConstant";
import { AsyncPaginate } from "react-select-async-paginate";
import c from "../../utils/Constants"
import styled from "styled-components";
import colors from "../../colors";

const Wrapper = styled.div`
& .select-user{
  font-size: 14px;
    font-weight: 600;
    color: #3d3b48;
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
  background-color: ${(props) => props.bg ? "#c9302c" : "#4C75A3"};
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
      userIds: "",
      options: [],
      columnsFilters: {},
      category: [],
      userPageNo: 1,
      userlimit: 10,
      search: "",
      pagination: {
        current: 1,
        next: 0,
        totalData: 0,
        totalPages: 1,
        previous: 0,
        isLoading: false,
        sort_column: "",
        sort_by: "",
        value: '',
        isSearchValue: false
      },
      openModal: {
        open: false,
        title: "",
        description: "",
        id: "",
        action: "",
      },
      tempUser: [],
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
                  <Button bg="red" title="unverify" onClick={() => this.verifyUnVerifyUser(row.original._id, false, row.index)}>
                    UNVERIFY
                  </Button>
                ) : (
                  <Button title="verify" onClick={() => this.verifyUnVerifyUser(row.original._id, true, row.index)}>
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
  verifyUnVerifyUser = async (id, status, index) => {
    const { users } = this.state
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
    } catch (e) {
      console.log("error", e);
    }
  };
  getUsers = async () => {
    try {
      const { data } = await axios.get(`${USERS.searchUser}`);
      if (data.status == 200) {
        let arr = data.data.map(item => {
          return { value: item._id, id: item._id, label: item.userName, phoneNumber: item.phoneNumber, isVerified: item.isVerified }
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

  searchUser = (obj = {}) => {
    const { tempUser, userlimit, userPageNo, pagination, search } = this.state
    console.log('userlimit', obj);
    let limit = obj.limit ? obj.limit : userlimit
    let pageNo = obj.pageNo ? obj.pageNo : userPageNo
    let startIndex = (pageNo * limit) - limit
    let lastIndex = (pageNo * limit)
    let data = tempUser
    let searchData = obj.value ? obj.value : ""
    if (searchData) {
      data = data.filter(item => (item.userName.toLowerCase().indexOf(searchData.toLowerCase()) === 0) || (item.phoneNumber.toLowerCase().indexOf(searchData.toLowerCase()) === 0))
    }
    this.setState((prevState) => ({
      users: data.slice(startIndex, lastIndex),
      userPageNo: pageNo,
      userlimit: obj.limit ? obj.limit : userlimit,
      pagination: {
        ...prevState.pagination,
        isLoading: false,
        totalData: data.length,
        current: pageNo,
        previous: pageNo > 1 ? pageNo - 1 : 0,
        totalPages: Math.ceil(parseInt(tempUser.length) / limit),
        next: Math.ceil(parseInt(tempUser.length) / limit) > pageNo ? parseInt(pageNo) + parseInt(1) : 0,
        value: searchData,
      },
    }))
  };

  loadOptions = async (searchQuery, loadedOptions, { page }) => {
    const { data } = await axios.get(
      `${USERS.searchUser}?query=${searchQuery}&page=${page}`
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
    const { columns, users, pagination, tempUser } = this.state;
    const customStyles = {
      control: (provided, state) => ({
        ...provided,
        background: '#fff',
        borderColor: '#4C75A3 !important',
        // minHeight: '50px',
        // height: '50px',
        borderWidth: ' 2px',
        boxShadow: '0 0 1px #4C75A3',
      }),
    };
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
                  <div style={{ width: "100%" }}>
                    <AsyncPaginate value={tempUser}
                      onChange={(user) => this.setState({ tempUser: user })}
                      placeholder="Select User..."
                      loadOptions={this.loadOptions}
                      getOptionValue={(option) => option.userName}
                      getOptionLabel={(option) => option.userName}
                      additional={{
                        page: 1,
                      }}
                      isMulti styles={customStyles}/>
                  </div>
                  <div className="btn-div">
                    <button className="btn" onClick={this.searchUser}>Go</button>
                  </div>
                </div>
              </div>
              <div className="user-div">
                <FilteringTable data={users} columns={columns} pagination={pagination} handleTable={this.searchUser} />
              </div>
            </Wrapper>
          </Section>
        </Container>
      </>
    );
  }
}
export default withRouter(VerifyNewUsers);
