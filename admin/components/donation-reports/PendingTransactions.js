import React from "react";
import FilteringTable from "../CustomTable/FilteringTable";
import { Container, Wrapper, Header,ExportButton} from "/components/users/Users.styled";
import { withRouter } from "next/router";
import CustomModal from "../helpers/modal/CustomModal";
import {DONATION} from "../../ApiConstant"
import axios from "../../utils/axios";
import styled from "styled-components";
import colors from "../../colors";
import MessageModal from "../helpers/modal/MessageModal";
import { exportData } from "../helpers/exports";
import moment from "moment/moment";

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
      pendingData: [],
      columnsFilters: {},
      category: [],
      pageNumber: 1,
      dataLimit: 10,
      startDate:"",
      endDate:"",
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
      openMessageModal: {
        open: false,
        msgtitle: "",
        message: "",
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
          Header: "Bank Account",
          accessor: "accountId",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{ row.original.stripeAccountId ? "Linked":"Not Linked" }</span>
            )
          }
        },
        {
          Header: "Date",
          accessor: "",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{(row && row.original.createdAt && moment(row.original.createdAt).format('YYYY/MM/DD HH:mm'))}</span> 
            )
          }
        },
        {
          Header: "Credit Amount",
          accessor: "walletCreditAmount",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{ row.original.walletCreditAmount ? "$"+ row.original.walletCreditAmount:"$"+0 }</span>
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
              {row.original.stripeAccountId ? <Button  title="pay" onClick={() => this.withdrawalRequest(row.original._id, true)}>Pay</Button> : <Button style={{  border:"1px solid #999999",backgroundColor: "#cccccc",color: "#666666"}} disabled={true}  title="Disable Payment">Pay</Button>}
              </div>
          )},
        },
      ],
    };
  }

  //the content to ask from Admin to delete the Video Category
  withdrawalRequest = (id, status) => {
    this.setState({
      openModal: {
        open: true,
        title: "Pending Transaction",
        description: "Are you sure you want to PAY the money?",
        id: id,
        action: "pay",
      },
    });
  };

  // to open Delete Category Modal
  modalAction = (status) => {
    const { openModal } = this.state;
    if (status) {
      if (openModal.id && openModal.action === "pay") { //to Delete the Video Category
        this.payPendingTransaction(openModal.id);
      }
      this.setState({
        openModal: {
        open: false,
        },
      });
      this.setState({
        openMessageModal: {
        open: false,
        },
      });
    } else {
      this.setState({
      openModal: {
      open: status,
      },
      });
      this.setState({
        openMessageModal: {
        open: status,
        },
      });
    }
  };

  //to pay Pending Transaction 
  payPendingTransaction = async (id, status) => {
    const { router } = this.props;
    const { pageNumber, dataLimit } = this.state;
    try {
      const { data } = await axios.post(DONATION.payPendingTransaction, {
        userId: id,
      });
      if (data.status == 200) {
        console.log("200 success")
        this.setState({
          openMessageModal: {
            open: true,
            msgtitle: "Pending Transactions",
            message: "Your transaction is done successfully ",
            action: "pay",
          },
        });
        // router.push("/donations/transfer-transactions");
        this.getData({
          pageNo: pageNumber,
          limit: dataLimit,
        });
      }
      else if (data.status == 422 || data.status == 500 ) {
        console.log(data,"dataa");
        this.setState({
          openMessageModal: {
            open: true,
            msgtitle: "Pending Transactions",
            message: data.message,
            action: "pay",
          },
        });
      }
    }
    catch (e) {
      console.log("error", e.message);
    }
  };

  //getData() function is called
  componentDidMount = () => {
    document.getElementById("custom-loader").style.display = "block";
    const { pageNumber, dataLimit,startDate,endDate } = this.state;
    this.getData({
      pageNo: pageNumber,
      limit: dataLimit,
      startDate:startDate,
      endDate:endDate
    });
  };
  
    //to get all the donation received listing without pagination on click of EXPORT button
    csvExport = async () => {
      document.getElementById("custom-loader").style.display = "block";
        try {
          const { data } = await axios.post(`${DONATION.pendingTransaction}`, {
            type: "export",
          });
          console.log(data,"datadonationReceived");
          if (data.status == 200) {
            const resData= data.data
            console.log(resData,"dataresData");
            const file = "pendingtransactions.csv";
            const arr = [];
            const col = ["Username", "Email Address ", "Phone Number", "Bank Account","Credit Amount"];
            resData && resData.length > 0 && resData.forEach((element) => {
              console.log(element,"element");
              const propertyValues = [
                      element.name,
                      element.email,
                      element.phoneNumber,
                      element.accountId?"Linked":"Not Linked",
                      element.walletCreditAmount?element.walletCreditAmount :0,
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

  //API calling in this function
  getData = async (obj) => {
    try {
      this.setState((prevState) => ({
        pagination: {
          ...prevState.pagination,
          isLoading: true,
        },
      }));
      let { pageNumber, dataLimit,startDate,endDate, columnsFilters } = this.state;
      pageNumber = obj.pageNo ? obj.pageNo : pageNumber;
      dataLimit = obj.limit ? obj.limit : dataLimit;
      startDate = obj.startDate ? obj.startDate : startDate;
      endDate = obj.endDate ? obj.endDate : endDate;

      if (obj.column) {
        columnsFilters[obj.column] = obj.value;
        pageNumber = 1;
      }
      const { data } = await axios.post(`${DONATION.pendingTransaction}`, {  //API calling
        page: pageNumber,
        limit: dataLimit,
        startDate:startDate,
          endDate:endDate,
        ...columnsFilters,
      });
      if (data.status == 200) {
        this.setState({
          pendingData: data.data,
          pageNo: pageNumber,
          dataLimit: dataLimit,
          startDate:startDate,
          endDate:endDate,
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

  setDateFilter=(date)=>{
    const { dataLimit } = this.state;
    this.setState((prevState) => ({
     ...prevState,
     startDate:moment(date.startDate).format('YYYY-MM-DD'),
     endDate:moment(date.endDate).format('YYYY-MM-DD'),
     pageNumber:1
    }))
    this.getData({
      pageNo: 1,
      limit: dataLimit,
      startDate:moment(date.startDate).format('YYYY-MM-DD'),
      endDate:moment(date.endDate).format('YYYY-MM-DD')
    });
  }


  render() {
    const {columns,pendingData,pagination,openModal,openMessageModal} = this.state;
    return (
      <Container>
        {openModal.open && (
          <CustomModal
            {...openModal}
            modalAction={(status) => this.modalAction(status)}
          />
        )}
        {openMessageModal.open && (
          <MessageModal
            {...openMessageModal}
            modalAction={(status) => this.modalAction(status)}
          />
        )}
        <Wrapper>
          <Header>
              <span className="title">Pending Transactions</span>
              <ExportButton onClick={() => this.csvExport()}>
                <span>EXPORT</span>
                <svg className="MuiSvgIcon-root MuiSvgIcon-fontSizeMedium MuiBox-root css-1om0hkc" focusable="false" width="25" height="25" fill="white" aria-hidden="true" viewBox="0 0 24 24"  data-testid="FileUploadIcon" ><path d="M9 16h6v-6h4l-7-7-7 7h4zm-4 2h14v2H5z"></path></svg>
              </ExportButton>
          </Header>
          <FilteringTable data={pendingData} columns={columns} pagination={pagination} handleTable={this.getData} setDateFilter={this.setDateFilter} isDateFilter={true}/>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(PendingTransactions);
