import React from "react";
import FilteringTable from "../CustomTable/FilteringTable";
import { Container, Wrapper, Header, ExportButton} from "../users/Users.styled";
import { withRouter } from "next/router";
import {DONATION} from "../../ApiConstant"
import axios from "../../utils/axios";
import { exportData } from "../helpers/exports";
import moment from "moment/moment";


class AdminCommission extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      commissionData: [],
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
            return row && row.original.userId && row.original.userId.name[0].toUpperCase() + row.original.userId.name.substr(1) 
          },
        },
        {
          Header: "Email Address",
          accessor: "email",
          disableFilters: true,
          Cell: ({ cell: {row} }) => {
            return row && row.original.userId &&row.original.userId.email 
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
          Header: "Donated From",
          accessor: "donate",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return row.original.donatedBy.name && row.original.donatedBy.name[0].toUpperCase() + row.original.donatedBy.name.substr(1) 
          },
        },
        {
          Header: "Date",
          accessor: "",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{(row && row.original.createdAt && moment(row.original.createdAt).format('YYYY/MM/DD HH:mm'))}</span> 
            )
          },
        },
        {
          Header: "Status",
          accessor: "status",
          disableFilters: true,
        },
        {
          Header: "User Amount",
          accessor: "userAmount",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{ row.original.userAmount ? "$"+ row.original.userAmount :"$"+0 }</span>
            )
          }
        },
        {
          Header: "Admin Commission",
          accessor: "admin",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{ row.original.adminAmount ? "$"+ row.original.adminAmount :"$"+0 }</span>
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
      ],
    };
  }
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
      const { data } = await axios.post(`${DONATION.adminCommission}`, {
        type: "export",
      });
      console.log(data,"datadonationReceived");
      if (data.status == 200) {
        const resData= data.data
        console.log(resData,"dataresData");
        const file = "admincommission.csv";
        const arr = [];
        const col = ["Username", "Email Address ", "Phone Number", "Donated From","Status","User Amount","Admin Commission","Total Amount"];
        resData && resData.length > 0 && resData.forEach((element) => {
          console.log(element,"element");
          const propertyValues = [
                  element.userId && element.userId.name,
                  element.userId && element.userId.email,
                  element.userId && element.userId.phoneNumber,
                  element.donatedBy.name,
                  element.status,
                  element.userAmount?element.userAmount :0,
                  element.adminAmount?element.adminAmount :0,
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
      let { pageNumber, dataLimit,startDate,endDate, columnsFilters } = this.state;
      pageNumber = obj.pageNo ? obj.pageNo : pageNumber;
      dataLimit = obj.limit ? obj.limit : dataLimit;
      startDate = obj.startDate ? obj.startDate : startDate;
      endDate = obj.endDate ? obj.endDate : endDate;

      if (obj.column) {
        columnsFilters[obj.column] = obj.value;
        pageNumber = 1;
      }
      const { data } = await axios.post(`${DONATION.adminCommission}`, {  //API calling
        page: pageNumber,
        limit: dataLimit,
        startDate:startDate,
        endDate:endDate,
        ...columnsFilters,
      });
      if (data.status == 200) {
        this.setState({
          commissionData: data.data,
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

  //to set startdate and enddate
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
    const {columns, commissionData, pagination} = this.state;
    return (
      <Container>
        <Wrapper>
           <Header>
              <span className="title">Admin Commission</span>
              <ExportButton onClick={() => this.csvExport()}>
                <span>EXPORT</span>
                <svg className="MuiSvgIcon-root MuiSvgIcon-fontSizeMedium MuiBox-root css-1om0hkc" focusable="false" width="25" height="25" fill="white" aria-hidden="true" viewBox="0 0 24 24"  data-testid="FileUploadIcon" ><path d="M9 16h6v-6h4l-7-7-7 7h4zm-4 2h14v2H5z"></path></svg>
              </ExportButton>
            </Header>
          <FilteringTable data={commissionData} columns={columns} pagination={pagination} handleTable={this.getData} setDateFilter={this.setDateFilter} isDateFilter={true}/>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(AdminCommission);
