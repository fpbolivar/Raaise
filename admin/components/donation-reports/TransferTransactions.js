import React from "react";
import FilteringTable from "../CustomTable/FilteringTable";
import { Container, Wrapper, Header} from "/components/users/Users.styled";
import { withRouter } from "next/router";
import CustomModal from "../helpers/modal/CustomModal";
import {DONATION} from "../../ApiConstant"
import axios from "../../utils/axios";

class TransferTransactions extends React.Component {
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
        // isLoading: true, //to show data in the table 
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
          accessor: "username",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return  row.original.userId.name && row.original.userId.name[0].toUpperCase() + row.original.userId.name.substr(1) 
          },
        },

        {
          Header: "Phone Number",
          accessor: "phonenumber",
          disableFilters: true,
          Cell: ({ cell: {row} }) => {
            return row && row.original.userId.phoneNumber 
          },
        },
        {
          Header: "Email Address",
          accessor: "email",
          disableFilters: true,
          Cell: ({ cell: {row} }) => {
            return row && row.original.userId.email 
          },
        },
        {
            Header: "Transaction Id",
            accessor: "transferId",
            disableFilters: true,
          },
          {
            Header: "Status",
            accessor: "status",
            disableFilters: true,
          },
          {
            Header: "Amount",
            accessor: "amount",
            disableFilters: true,
            Cell: ({ cell: { row } }) => {
              return(
                <span>{ row.original.amount ? "$"+ row.original.amount :"$"+0 }</span>
              )
            }
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
      const { data } = await axios.post(`${DONATION.transferredTransaction}`, {
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
              <span className="title">Transferred Transactions</span>
          </Header>
          <FilteringTable data={responseData} columns={columns} pagination={pagination} handleTable={this.getData}/>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(TransferTransactions);
