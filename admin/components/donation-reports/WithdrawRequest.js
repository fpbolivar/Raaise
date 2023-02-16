import React from "react";
import FilteringTable from "../CustomTable/FilteringTable";
import { Container, Wrapper, Header,ExportButton} from "/components/users/Users.styled";
import { withRouter } from "next/router";
import WithdrawModal from "../helpers/modal/WithdrawModal";
import {DONATION} from "../../ApiConstant"
import axios from "../../utils/axios";
import styled from "styled-components";
import colors from "../../colors";
import CustomModal from "../helpers/modal/CustomModal";
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

class WithdrawRequest extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      withdrawData: [],
      columnsFilters: {},
      pageNumber: 1,
      dataLimit: 10,
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
            return row && row.original.userId && row.original.userId.name && row.original.userId.name[0].toUpperCase() + row.original.userId.name.substr(1) 
          },
        },
        {
          Header: "Email Address",
          accessor: "email",
          disableFilters: true,
          Cell: ({ cell: {row} }) => {
            return row &&  row.original.userId && row.original.userId.email 
          },
        },
        {
          Header: "Phone Number",
          accessor: "phonenumber",
          disableFilters: true,
          Cell: ({ cell: {row} }) => {
            return row && row.original.userId && row.original.userId.phoneNumber 
          },
        },
        {
          Header: "Status",
          accessor: "status",
          disableFilters: true,
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
          Header: "Amount",
          accessor: "amount",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{ row.original.amount ? "$"+ row.original.amount :"$"+0 }</span>
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
                <Button title="pay" onClick={() => this.withdrawalRequest(row.original._id, true) }>Pay</Button>
              </div>
            )
          },
        },
      ],
    };
  }

  //the content to ask from Admin to pay the withdrawal moneyu
  withdrawalRequest = (id, status) => {
    this.setState({
      openModal: {
        open: true,
        title: "Withdrawal Request",
        description: "Are you sure you want to PAY the money?",
        id: id,
        action: "pay",
      },
    });
  };

  // to open pay money modal
    modalAction = (status) => {
      const { openModal } = this.state;
      if (status) {
        if (openModal.id && openModal.action === "pay") { //to pay the money
        console.log("new modal")
          this.setState({
            openMessageModal: {
              open: true,
              title: "hello",
              description: "hello",
              action: "pay",
            },
          });
        }
        this.setState({
          openModal: {
          open: false,
          },
        });
      } else {
        console.log("falsee")
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

  //getData() function is called
  componentDidMount = () => {
    document.getElementById("custom-loader").style.display = "block";
    const { pageNumber, dataLimit ,startDate,endDate} = this.state;
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
        const { data } = await axios.post(`${DONATION.withdrawalRequest}`, {
          type: "export",
        });
        console.log(data,"datadonationReceived");
        if (data.status == 200) {
          const resData= data.data
          console.log(resData,"dataresData");
          const file = "withdrawrequest.csv";
          const arr = [];
          const col = ["Username", "Email Address ", "Phone Number", "Status","Amount"];
          resData && resData.length > 0 && resData.forEach((element) => {
            console.log(element,"element");
            const propertyValues = [
                    element.name,
                    element.email,
                    element.phoneNumber,
                    element.status,
                    element.amount?element.amount :0,
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
      let { pageNumber, dataLimit, columnsFilters,startDate,endDate  } = this.state;
      pageNumber = obj.pageNo ? obj.pageNo : pageNumber;
      dataLimit = obj.limit ? obj.limit : dataLimit;
      startDate = obj.startDate ? obj.startDate : startDate;
      endDate = obj.endDate ? obj.endDate : endDate;

      if (obj.column) {
        columnsFilters[obj.column] = obj.value;
        pageNumber = 1;
      }
      const { data } = await axios.post(`${DONATION.withdrawalRequest}`, { //API calling
        page: pageNumber,
        limit: dataLimit,
        startDate:startDate,
        endDate:endDate,
        ...columnsFilters,
      });
      if (data.status == 200) {
        this.setState({
          withdrawData: data.data,
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
    const {columns,withdrawData,pagination,openModal,openMessageModal} = this.state;
    return (
      <Container>
        {openModal.open && (
          <WithdrawModal
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
            <span className="title">Withdrawal Requests</span>
            <ExportButton onClick={() => this.csvExport()}>
              <span>EXPORT</span>
              <svg className="MuiSvgIcon-root MuiSvgIcon-fontSizeMedium MuiBox-root css-1om0hkc" focusable="false" width="25" height="25" fill="white" aria-hidden="true" viewBox="0 0 24 24"  data-testid="FileUploadIcon" ><path d="M9 16h6v-6h4l-7-7-7 7h4zm-4 2h14v2H5z"></path></svg>
            </ExportButton>
          </Header>
          <FilteringTable data={withdrawData} columns={columns} pagination={pagination} handleTable={this.getData} setDateFilter={this.setDateFilter} isDateFilter={true}/>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(WithdrawRequest);
