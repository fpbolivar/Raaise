import React from "react";
import FilteringTable from "../CustomTable/FilteringTable";
import { Container, Wrapper, Header} from "/components/users/Users.styled";
import { withRouter } from "next/router";
import CustomModal from "../helpers/modal/CustomModal";
import {DONATION} from "../../ApiConstant"
import axios from "../../utils/axios";
import styled from "styled-components";
import colors from "../../colors";

const Button = styled.button`
  cursor: pointer;
  background-color: ${colors.blueColor};
  border: none;
  border-radius: 5px;
  width: 65%;
  height: 30px;
  color: white;
  font-size: 14px;
`;
class PendingTransactions extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedOption: null,
      selectedUserType: null,
      responseData: [],
      columnsFilters: {},
      category: [],
      pageNumber: 1,
      dataLimit: 10,
      pagination: {
        current: 1,
        next: 1,
        totalData: 0,
        totalPages: 1,
        previous: 0,
        // isLoading: true,
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
      columns: [
        {
          Header: "Serial No.",
          accessor: "serial",
          disableFilters: true,
        },
        {
          Header: "Username",
          accessor: "name",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return row.original.name && row.original.name[0].toUpperCase() + row.original.name.substr(1) 
          },
        },

        {
          Header: "Phone Number",
          accessor: "phoneNumber",
          disableFilters: true,
        },
        {
          Header: "Email Address",
          accessor: "email",
          disableFilters: true,
        },
        {
            Header: "Credit Amount",
            accessor: "walletCreditAmount",
            disableFilters: true,
            Cell: ({ cell: { row } }) => {
              return(
                <span>{ row.original.walletCreditAmount ? "$"+ row.original.walletCreditAmount :"$"+0 }</span>
              )
            }
          },
          {
            Header: "Actions",
            accessor: "action",
            disableFilters: true,
            Cell: ({ cell: { row } }) => {
                  return (
                    <div>
                      <Button  title="pay" >   
                           Pay
                      </Button>
                      </div>
                   )},
        },
      ],
    };
  }
  componentDidMount = () => {
    document.getElementById("custom-loader").style.display = "block";
    const { pageNumber, dataLimit } = this.state;
    this.getData({
      pageNo: pageNumber,
      limit: dataLimit,
    });
  };
  //API calling in this function
  getData = async (obj) => {
    try {
      this.setState((prevState) => ({
        pagination: {
          ...prevState.pagination,
          isLoading: true,
        },
      }));
      let { pageNumber, dataLimit, columnsFilters } = this.state;
      pageNumber = obj.pageNo ? obj.pageNo : pageNumber;
      dataLimit = obj.limit ? obj.limit : dataLimit;

      if (obj.column) {
        columnsFilters[obj.column] = obj.value;
        pageNumber = 1;
      }
      const { data } = await axios.post(`${DONATION.pendingTransaction}`, {
        page: pageNumber,
        limit: dataLimit,
        ...columnsFilters,
      });
      if (data.status == 200) {
        this.setState({
          responseData: data.data,
          pageNo: pageNumber,
          dataLimit: dataLimit,
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
    const {
      columns,
      responseData,
      pagination,
      selectedOption,
      category,
      userType,
      selectedUserType,
      openModal,
      columnsFilters,
    } = this.state;

    return (
      <Container>
        {openModal.open && (
          <CustomModal
            {...openModal}
            modalAction={(status) => this.modalAction(status)}
          />
        )}
        <Wrapper>
          <Header>
              <span className="title">Pending Transactions</span>
          </Header>
          <FilteringTable data={responseData} columns={columns} pagination={pagination} handleTable={this.getData}/>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(PendingTransactions);
