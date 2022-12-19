import React from "react";
import FilteringTable from "./CustomTable/FilteringTable";
import { Container, Wrapper, Header} from "./users/Users.styled";
import { GENERALSETTING } from "../ApiConstant";
import axios from "../utils/axios";
import { withRouter } from "next/router";
import CustomModal from "./helpers/modal/CustomModal";
import PrivacyModal from "./helpers/modal/PrivacyModal";
import colors from "../colors";

class GeneralSetting extends React.Component {
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
      openModal: {
        open: false,
        title: "",
        description: "",
        id: "",
        action: "",
      },
      openPrivacyModal: {
        open: false,
        type: '',
        data: ''
      },
      columns: [
        {
          Header: "Title",
          accessor: "title",
          disableFilters: true,
        },
        {
          Header: "Actions",
          accessor: "actions",
          disableFilters: true,
          disableSorting: true,
          Cell: ({ cell: { row } }) => {
            return (
                <div className="cursor-pointer" title="View" onClick={() => this.modalActionMedia(true,row.original.type)}>
                  <svg viewBox="0 0 24 24" width={22} fill={colors.blueColor}><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34a.9959.9959 0 0 0-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"></path></svg>
                </div>
            );
          },
        },
      ],
    };
  }
  modalActionMedia = (status, type) => {
    if (status) {
      this.setState({
        openPrivacyModal: {
          open: status,
          type: type,
          data: ''
        },
      });
    } else {
      this.setState({
        openPrivacyModal: {
          open: false,
          data: ''
        },
      });
    }
  };

  // In this function, API is called
  getGeneralListing=async()=>{
   try{
    const { data } = await axios.get(GENERALSETTING.getlisting); //API calling
    if(data.status == 200) {
        this.setState({
          users: data.data,
          pagination: {
            current: 0,
            next: 0,
            totalData: 0,
            totalPages: 0,
            previous: 0,
            isLoading: false,
          },
        });
    }
    document.getElementById("custom-loader").style.display = "none";
  }
   catch(e){
    document.getElementById("custom-loader").style.display = "none";
    console.log("error",e)
   }
  }

  //getGeneralListing() function is called
  componentDidMount =()=>{
    document.getElementById("custom-loader").style.display = "block";
    this.getGeneralListing()
  }
  render() {
    const {columns,users,pagination,openModal,openPrivacyModal} = this.state;
    return (
      <>
      <Container>
        {openModal.open && (
          <CustomModal
            {...openModal}
            modalAction={(status) => this.modalAction(status)}
          />
        )}
          {openPrivacyModal.open && (
          <PrivacyModal                     //to Edit the Privacy Policy,Terms of Privacy,Copyright Policy Content.
            {...openPrivacyModal}
            modalAction={(status) => this.modalActionMedia(status)}
            getGeneralListing={this.getGeneralListing}
          />
        )}
        <Wrapper>
          <Header>
              <span className="title ">General Settings</span>
          </Header>
          <FilteringTable data={users} columns={columns}  pagination={pagination} handleTable={this.getGeneralListing}  filterStatus={true}/>
        </Wrapper>
      </Container>
      </>
    );
  }
}
export default withRouter(GeneralSetting);
