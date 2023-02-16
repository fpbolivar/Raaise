import React from "react";
import FilteringTable from "../CustomTable/FilteringTable";
import { Container, Wrapper, Header, ActionSection} from "../users/Users.styled";
import { withRouter } from "next/router";
import {NOTIFICATION} from "../../ApiConstant";
import axios from "../../utils/axios";
import {getFormattedDate} from "../../components/helpers/GlobalHelpers"
import NotificationModal from "../helpers/modal/NotificationModal";
import moment from "moment";

class NotificationLog extends React.Component {
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
          Header: "Title",
          accessor: "title",
          disableFilters: true,
        },
        {
          Header: "Message",
          accessor: "desc",
          disableFilters: true,
        },
        {
          Header: "Schedule",
          accessor: "type",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{(row && row.original.type=="send-now" ? <span>Send Now</span> :<span>Scheduled</span> )}</span> 
            )
          }
        },
        // {
        //     Header: "Status",
        //     accessor: "",
        //     disableFilters: true,
        //     Cell: ({ cell: { row } }) => {   
        //         console.log(row.original,"row.original")  
        //         return (
        //           <>
        //             {row.original.isSend=="true" ? (<span>Sent</span>) : (<span>Pending</span>)}
        //           </>
        //         );
        //       },
        //   },
        {
          Header: "Date",
          accessor: "date",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{(row && row.original.scheduleTime && moment(row.original.scheduleTime).format('YYYY/MM/DD HH:mm'))}</span> 
            //   <span>{getFormattedDate(row && row.original.scheduleTime)}</span> 
            )
          }
        },
        {
          Header: "Users",
          accessor: "totalUser",
          disableFilters: true,
          // Cell: ({ cell: { row } }) => {
          //   return row && row.original.userName[0].toUpperCase() + row.original.userName.substr(1) 
          // },
        },
        {
          Header: "Action",
          accessor: "",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return (
              <>
                <ActionSection>
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
   //getData() function is called
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
      const { data } = await axios.post(`${NOTIFICATION.logNotification}`,{ 
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
    const {columns, responseData, pagination, openModal,openMedia} = this.state;
    return (
      <Container>
         {openMedia.open && (
          <NotificationModal                 //On click of Eye Button,User details will be shown in the User Modal
            {...openMedia}
            modalAction={(status) => this.modalActionUser(status)}
          />
        )}
        <Wrapper>
          <Header>
            <span className="title">View Notification Logs</span>
          </Header>
          <div className="notification-log">
            <FilteringTable data={responseData} columns={columns} pagination={pagination} handleTable={this.getData}/>
          </div>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(NotificationLog);
