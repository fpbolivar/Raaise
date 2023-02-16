import React from "react";
import axios from "../../utils/axios";
import { withRouter } from "next/router";
import FilteringTable from "../CustomTable/FilteringTable";
import {Container, Wrapper,Header,ActionSection} from "/components/users/Users.styled";
import { VIDEO } from "./../../ApiConstant";
import ReportedModal from "../helpers/modal/ReportedModal";
import CustomModal from "../helpers/modal/CustomModal";
import {getFormattedDate} from "../../components/helpers/GlobalHelpers"

class ReportedVideo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedOption: null,
      selectedUserType: null,
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
      openMedia: {
        open: false,
        title: "",
        description: "",
        id: "",
        action: "",
        name:""
      },
      openModal: {
        open: false,
        title: "",
        description: "",
        id: "",
        action: "",
        isBlock: "",
      },
      columns: [
        {
          Header: "Serial No.",
          accessor: "serial",
          disableFilters: true,
        },
        {
          Header: "Video Name",
          accessor: "videoCaption",
          disableFilters: true,
        },

        {
          Header: "Date Of Report",
          accessor: "",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              // to show Date of Reported Video in the format "month/day/year"
              <span>{getFormattedDate(row && row.original.updatedAt ? row.original.updatedAt : row.original.createdAt)}</span> 
            )
          }
        },
        {
          Header: "No. of Users Reported",
          accessor: "videoReportCount",
          disableFilters: true,
        },
        {
          Header: "Donation ($)",
          accessor: "donationAmount",
          disableFilters: true,
          Cell: ({ cell: { row } }) => {
            return(
              <span>{ row.original.totalDanotionAmount ? "$"+ row.original.totalDanotionAmount :"$"+0 }</span>
            )
          }
        },
        {
          Header: "Status",
          accessor: "isBlock",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value ? "Block" : "Unblock";
          },
        },
        {
          Header: "Actions",
          accessor: "_id",
          disableFilters: true,
          disableSorting: true,
          Cell: ({ cell: { row } }) => {
            return (
              <ActionSection>
                <div className="cursor-pointer" title="View"  onClick={() => this.modalActionMedia(true, row, 'video')}>
                  <svg viewBox="0 0 24 24" width={22} fill="black"><path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"></path></svg>
                </div>

                <div className="cursor-pointer" title="Ignore Video" onClick={() =>this.handleVideoIgnore(row.original._id) }>
                  <svg viewBox="0 0 24 24" width={22} fill="rgba(0, 0, 0, 0.64)"> <path d="m21 6.5-4 4V7c0-.55-.45-1-1-1H9.82L21 17.18V6.5zM3.27 2 2 3.27 4.73 6H4c-.55 0-1 .45-1 1v10c0 .55.45 1 1 1h12c.21 0 .39-.08.54-.18L19.73 21 21 19.73 3.27 2z"></path></svg>
                </div>

                {row.original.isBlock ? (
                  <div title="Unblock" className="cursor-pointer" onClick={() => this.handleBlockUnblock(row.original._id, false)}>                    
                    <svg className="MuiSvgIcon-root MuiSvgIcon-fontSizeMedium MuiSvgIcon-root MuiSvgIcon-fontSizeLarge css-zjt8k" focusable="false" aria-hidden="true" viewBox="0 0 24 24"data-testid="RemoveCircleOutlineIcon" width={22} fill="#54B435" ><path d="M7 11v2h10v-2H7zm5-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"></path></svg>
                  </div>
                ) : (
                  <div title="Block" className="cursor-pointer" onClick={() => this.handleBlockUnblock(row.original._id, true)} >
                    <svg   focusable="false" aria-hidden="true" viewBox="0 0 24 24" data-testid="BlockIcon" width={22} fill="red"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM4 12c0-4.42 3.58-8 8-8 1.85 0 3.55.63 4.9 1.69L5.69 16.9C4.63 15.55 4 13.85 4 12zm8 8c-1.85 0-3.55-.63-4.9-1.69L18.31 7.1C19.37 8.45 20 10.15 20 12c0 4.42-3.58 8-8 8z"></path></svg>
                  </div>
                )}
              </ActionSection>
            );
          },
        },
      ],
    };
  }

  //to ask from the Admin whether to Block/Unblock/Ignore the Reported Video in the Modal Popup. 
  modalAction = (status) => {
    const { openModal } = this.state;
    if (status) {
    if (openModal.id && openModal.action === "ignore") { //to Ignore the Reported Video
        this.deleteVideo(openModal.id);
      }
    else if (openModal.id && openModal.action === "block") { //to Block/Unblock the Reported Video
        this.blockVideo(openModal.id, openModal.isBlock);
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

//to show content in the Modal popup to Block/Unblock the Reported Video 
  handleBlockUnblock = (id, status) => {
    this.setState({
      openModal: {
        open: true,
        title: status ? "Block video" : " Unblock video",
        description: status
          ? "Are you sure you want to BLOCK this video?"
          : "Are you sure you want to UNBLOCK this video?",
        id: id,
        action: "block",
        isBlock: status,
      },
    });
  };

  //to show content in the Modal popup to Ignore the Reported Video
  handleVideoIgnore = (id, status) => {
    this.setState({
      openModal: {
        open: true,
        title: "Ignore Video",
        description: "Are you sure you want to IGNORE this video?",
        id: id,
        action: "ignore",
        isBlock: status,
      },
    });
  };

  //to Block the Reported Video
  blockVideo = async (id, status) => {
    const { userPageNo, userlimit } = this.state;
    try {
      //API calling
      const { data } = await axios.post(VIDEO.blockUnblockVideo, {
        id: id,
        isBlock: status,
      });
      if (data.status === 200) {
        this.getReportedVideoList({   //to get list of Reported Videos
          pageNo: userPageNo,
          limit: userlimit,
        });
      }
    } catch (e) {
      console.log("error", e);
    }
  };

  //to ignore the Reported Video and remove it from the list.
  deleteVideo = async (id, status) => {
    const { userPageNo, userlimit } = this.state;
    try {
      const { data } = await axios.post(VIDEO.ignoreVideo, {
        id: id,
      });
      if (data.status === 200) {
        this.getReportedVideoList({
          pageNo: userPageNo,
          limit: userlimit,
        });
      }
    } catch (e) {
      console.log("error", e);
    }
  };

  componentDidMount = () => {
    document.getElementById("custom-loader").style.display = "block";
    const { videoPageNo, videolimit, } = this.state;
    this.getReportedVideoList({pageNo: videoPageNo,limit: videolimit,}); //get all Reported Video Listing
  };

  // API call for video listing
  getReportedVideoList = async (obj) => {
    try {
      this.setState((prevState) => ({pagination: {...prevState.pagination,isLoading: true,},}));
      let { videoPageNo, videolimit, columnsFilters } = this.state;
      videoPageNo = obj.pageNo ? obj.pageNo : videoPageNo;
      videolimit = obj.limit ? obj.limit : videolimit;
      if (obj.column) {
        columnsFilters[obj.column] = obj.value;
        videoPageNo = 1;
      }
      //API calling
      const { data } = await axios.post(`${VIDEO.getAllVideoList}`, {
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
        document.getElementById("custom-loader").style.display = "none";
      }
    } catch (e) {
      document.getElementById("custom-loader").style.display = "none";
    }
  };

  //to show/play the Reported Video in the Modal Popup
  modalActionMedia = (status, value = '', type = "") => {
    if (type || value) {
      this.setState({
        openMedia: {
          open: status,
          type: type,
          data: type === "video" && value.original.videoLink  ,
          name:value.original.videoCaption && value.original.videoCaption,
          id:value.original._id && value.original._id
        },
      });
    } else {
      this.setState({
        openMedia: {
          open: false,
          type: type,
          data: '',
          name:""
        },
      });
    }
  };
  render() {
    const {columns,videoList,pagination,openMedia,openModal} = this.state;
    return (
      <Container>
           {openModal.open && (
          <CustomModal
            {...openModal}
            modalAction={(status) => this.modalAction(status)}
          />
        )}

        {/* to play the Reported video in Modal Popup */}
         {openMedia.open && (
          <ReportedModal 
          {...openMedia} type={openMedia.type} 
          modalAction={(status) => this.modalActionMedia(status)}/>
         )}
        <Wrapper>
          <Header>
            <div className="header" >
              <span className="title">Reported Video</span>
            </div>
          </Header>
          <FilteringTable data={videoList}  columns={columns} pagination={pagination} handleTable={this.getReportedVideoList}/>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(ReportedVideo);
