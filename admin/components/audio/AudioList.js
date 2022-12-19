import React from "react";
import axios from "../../utils/axios";
import { withRouter } from "next/router";
import {TableHeader} from "../CustomTable/Table.styled";
import FilteringTable from "../CustomTable/FilteringTable";
import { Container, Wrapper} from "../../components/users/Users.styled";
import { AUDIO } from "../../ApiConstant";
import CustomModal from "../helpers/modal/CustomModal";
import ViewMedia from "../helpers/modal/ViewMedia";

class AudioList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      audioList: [],
      columnsFilters: {},
      category: [],
      audioPageNo: 1,
      audiolimit: 10,
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
      openMedia: {
        open: false,
        type: '',
        data: '',
        name:""
      },
      columns: [
        {
          Header: "Serial No.",
          accessor: "serial",
          disableFilters: true,
        },
        {
          Header: "Song Name",
          accessor: "songName",
          disableFilters: true,
        },
        {
          Header: "Artist Name",
          accessor: "artistName",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value ? value
              .toLowerCase()
              .replace("_", " ")
              .split(" ")
              .map(function (word) {
                if(word){
                  return word[0].toUpperCase() + word.substr(1);
                }
              })
              .join(" ") : "";
          },
        },
        {
          Header: "Genre",
          accessor: "genreId",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value ? value.name : "";
          },
        },
        {
          Header: "Thumbnail",
          accessor: "Thumbnail",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value ? <img src={value} alt="" width={50} height={50} onClick={() => this.modalActionMedia(true, value, 'image')} style={{ cursor: "pointer" }} /> 
            : "";
          },
        },
        {
          Header: "Audio",
          accessor: "audio",
          disableFilters: true,
          Cell: ({ cell: { value } }) => {
            return value ? <img src={"/assets/images/music.svg"} alt="" width={50} height={50} onClick={() => this.modalActionMedia(true, value, 'audio')} style={{ cursor: "pointer" }} />
            : "";
          },
        },
      ],
    };
  }

  //getAudioList() function is called
  componentDidMount = async () => {
    const { audioPageNo, audiolimit } = this.state;
    await this.getAudioList({    
      pageNo: audioPageNo,
      limit: audiolimit,
    });
  };

  //to show the Thumbnail of song and to play audio in the Modal popup 
  modalActionMedia = (status, value = '', type = "") => {
    const { audioList } = this.state;
    if (type) {
      let res = value ? audioList.find(item => type === "image" ? item.Thumbnail === value : type === "audio" ? item.audio === value : "") : {}
      this.setState({
        openMedia: {
          open: status,
          type: type,
          data: type === "image" ? res.Thumbnail ? res.Thumbnail : '' : type === "audio" ? res.audio ? res.audio : "" : "",
          name:res.songName 
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

  //to show all the Audios in the table that are uploaded by the Admin  
  getAudioList = async (obj) => {
    try {
      this.setState((prevState) => ({
        pagination: {
          ...prevState.pagination,
          isLoading: true,
        },
      }));
      let { audioPageNo, audiolimit, audioList, columnsFilters } = this.state;
      audioPageNo = obj.pageNo ? obj.pageNo : audioPageNo;
      audiolimit = obj.limit ? obj.limit : audiolimit;
      if (obj.column) {
        columnsFilters[obj.column] = obj.value;
      }
      
      //API calling
      const { data } = await axios.post(`${AUDIO.getAudioList}`, {    
        page: audioPageNo,
        limit: audiolimit,
        ...columnsFilters,
      })
      if (data.status === 200) {
        this.setState({
          audioList: data.data,
          audioPageNo: audioPageNo,
          audiolimit: audiolimit,
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
    } catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log("error", e);
    }
  };

  render() {
    const {columns,audioList,pagination,openMedia} = this.state;
    return (
      <Container>
        {/* to show the thumbnail of song and to play audio in modal popup */}
        {openMedia.open && (
          <ViewMedia
            {...openMedia}
            type={openMedia.type}
            modalAction={(status) => this.modalActionMedia(status)}
          />
        )}
        <Wrapper>
          <TableHeader>
              <span className="title">Audio List</span>
          </TableHeader>
          <FilteringTable data={audioList} columns={columns} pagination={pagination} handleTable={this.getAudioList} />
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(AudioList);
