import React from "react";
import axios from "../utils/axios";
import { VIDEO } from "./../ApiConstant";
import { withRouter } from "next/router";
import FilteringTable from "./CustomTable/FilteringTable"
import {Container, Wrapper,Header,ActionSection} from "./users/Users.styled";
import CategoryModal from "./helpers/modal/CategoryModal";
import CustomModal from "./helpers/modal/CustomModal";
import ViewMedia from "./helpers/modal/ViewMedia";
import colors from "../colors";
class Videocategories extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      videoList: [],
      columnsFilters: {},
      category: [],
      videoPageNo: 1,
      videolimit: 10,
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
        isBlock: "",
      },
      openMedia: {
        open: false,
        type: '',
        data: '',
        name:""
      },
      openCategoryModal: {
        open: false,
        type: '',
        data: ''
      },
      columns: [
        {
          Header: "Serial No.",
          accessor: "serial",
          disableFilters: true,
        },
        {
          Header: "Image",
          accessor: "image",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value ? <img src={value} alt="Category Image" width={50} height={50} onClick={() => this.modalImage(true, value, 'image')} style={{ cursor: "pointer" }} /> 
            : "";
          },
        },
        {
          Header: "Name",
          accessor: "name",
          disableFilters: true,
        },
        {
          Header: "Actions",
          accessor: "_id",
          disableFilters: true,
          disableSorting: true,
          Cell: ({ cell: { row } }) => {
            return (
              <ActionSection>
                <div className="cursor-pointer"   title="Edit"  onClick={() => this.modalActionMedia(true,"edit",row.original)}>
                <svg viewBox="0 0 24 24" width={22} fill={colors.blueColor}><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34a.9959.9959 0 0 0-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"></path></svg>
                </div>

                <div className="cursor-pointer" title="Delete" onClick={() => this.handleDelete(row.original._id, true)} >
                <svg className="MuiSvgIcon-root MuiSvgIcon-fontSizeMedium MuiBox-root css-1om0hkc"  width={22} fill={colors.red} focusable="false" aria-hidden="true" viewBox="0 0 24 24" data-testid="DeleteIcon"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"></path></svg>          
                </div>                
              </ActionSection>
            );
          },
        },
      ],
    };
  }

  //to show Image of Video Category in modal popup.
  modalImage = (status, value = '', type = "") => {
    const { videoList } = this.state;
    if (type) {
      let res = value && videoList.find(item => type === "image" && item.image === value ) 
      this.setState({
        openMedia: {
          open: status,
          type: type,
          data:  res.image && res.image ,
          name:res.name, 
        },
      });
    } else {
      this.setState({
        openMedia: {
          open: status,
          type: type,
          data: '',
          name:"",
        },
      });
    }
  };

  //the content to ask from Admin to delete the Video Category
  handleDelete = (id, status) => {
    this.setState({
      openModal: {
        open: true,
        title: "Delete Video Category",
        description: "Are you sure you want to DELETE this Video Category?",
        id: id,
        action: "delete",
        buttontitle:"DELETE",
      },
    });
  };

  //to open CategoryModal to Add and Edit Category
  modalActionMedia = (status,type,data) => {
    if (status) {
      this.setState({
        openCategoryModal: {
        open: status,
        type: type,
        data: data
        },
      });
    } else {
      this.setState({
        openCategoryModal: {
        open: false,
        data: ''
        },
      });
    }
  };

  // to open Delete Category Modal
  modalAction = (status) => {
    const { openModal } = this.state;
    if (status) {
      if (openModal.id && openModal.action === "delete") { //to Delete the Video Category
        this.deleteVideo(openModal.id);
      }
      this.setState({
        openModal: {
        open: false,
        },
      });
    } else {
      this.setState({
      openModal: {
      open: status,
      },
      });
    }
  };

  //to delete the Video Category from the list.
  deleteVideo = async (id, status) => {
    const { videoPageNo, videolimit } = this.state;
    try {
      const { data } = await axios.post(VIDEO.deleteVideoCategory, {
        categoryId: id,
      });
      if (data.status == 200) {
        this.getVideoCategories({
          pageNo: videoPageNo,
          limit: videolimit,
        });
      }
    } catch (e) {
      console.log("error", e);
    }
  };

  //getVideoCategories() API is called 
  componentDidMount = () => {
    const { videoPageNo, videolimit } = this.state;
    this.getVideoCategories({
      pageNo: videoPageNo,
      limit: videolimit,
    });
  };

  // API call for video category listing
  getVideoCategories = async (obj) => {
    try {
      this.setState((prevState) => ({
        pagination: {
        ...prevState.pagination,
        isLoading: true,
        },
      }));
      let { videoPageNo, videolimit,  columnsFilters } = this.state;
      videoPageNo = obj.pageNo ? obj.pageNo : videoPageNo;
      videolimit = obj.limit ? obj.limit : videolimit;
      if (obj.column) {
        columnsFilters[obj.column] = obj.value;
        videoPageNo = 1;
      }
      const { data } = await axios.post(`${VIDEO.getVideoCategories}`, {   //API calling
        page: videoPageNo,
        limit: videolimit,
        ...columnsFilters,
      });
      if (data.status == 200) {
        this.setState({
          videoList: data.data,
          pageNo: videoPageNo,
          videolimit: videolimit,
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
    }catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log("error", e);
    }
  };
 
  render() {
    const {columns,videoList,pagination,openModal,openCategoryModal,openMedia} = this.state;
    return (
      <Container>
        {openMedia.open && (
          <ViewMedia                        //Modal Popup to view Image of Video Category
            {...openMedia}
            modalAction={(status) => this.modalImage(status)}
          />
        )}
        {openModal.open && (
          <CustomModal                      //Modal Popup to Delete Video Category
            {...openModal}
            modalAction={(status) => this.modalAction(status)}
          />
        )}
        {openCategoryModal.open && (
          <CategoryModal                    //Modal Popup to Add and Edit Video Category.
            {...openCategoryModal}
            modalAction={(status) => this.modalActionMedia(status)}
            getVideoCategories={this.getVideoCategories}
            type={openCategoryModal.type}
            data={openCategoryModal.data}
          />
        )}
        <Wrapper>
          <Header>
            <span className="title">Video Categories</span>
            <div className="push-notif-div"  onClick={() => this.modalActionMedia(true,"add")}>               
              <button className="category-btn">+Add New Category</button>
            </div>
          </Header>
          <div  className="custom-table">
            <FilteringTable data={videoList} columns={columns} pagination={pagination} handleTable={this.getVideoCategories}/>
          </div>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(Videocategories);
