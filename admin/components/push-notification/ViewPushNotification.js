import React from "react";
import FilteringTable from "../CustomTable/FilteringTable";
import { Container, Wrapper, Header} from "../users/Users.styled";
import { withRouter } from "next/router";
import CustomModal from "../helpers/modal/CustomModal";

class ViewPushNotification extends React.Component {
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
          Header: "Send To",
          accessor: "sendTo",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value
              .toLowerCase()
              .replace("_", " ")
              .split(" ")
              .map(function (word) {
                return word[0].toUpperCase() + word.substr(1);
              })
              .join(" ");
          },
        },
        {
          Header: "Title",
          accessor: "title",
          disableFilters: true,
        },
        {
          Header: "Message",
          accessor: "message",
          disableFilters: true,
        },
        {
          Header: "Schedule",
          accessor: "schedule",
          disableFilters: true,
        },
        {
          Header: "Date",
          accessor: "date",
          disableFilters: true,
        },
      ],
    };
  }

  render() {
    const {columns, users, pagination, openModal,} = this.state;
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
             <span className="title">View Push Notifications</span>
               <div className="push-notif-div">               
                <a href="/notification/push-notification">+Push Notification</a>
              </div>
          </Header>
          <FilteringTable data={users} columns={columns} pagination={pagination} handleTable={this.getUsers}/>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(ViewPushNotification);
