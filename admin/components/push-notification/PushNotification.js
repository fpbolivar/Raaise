import { withRouter } from "next/router";
import React from "react";
import InputField from "../helpers/InputField";
import { USERS } from "../../ApiConstant";
import axios from "../../utils/axios";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { AsyncPaginate } from "react-select-async-paginate";
import c from "../../utils/Constants"
import styled from "styled-components";
import colors from "../../colors";

const Section = styled.div`
  margin: 20px;
  padding: 30px;
  background: ${colors.white};
  border-radius: 10px;
`; 
const Wrapper = styled.div`
  & .push-heading{
    border-bottom: 1px solid #e0e8f3;
    & .title{font-size: 24px; font-weight: 700; margin-bottom: 15px; }
  }
  & .select-user{padding-top: 15px ;}
  & .schedule-notification{
    display: flex;
    column-gap: 20px;
    padding: 10px 2px;
    & .want-notification{
        width: 50%;
        & .select-valid{ width: 100%; height: 56px; padding: 14px; font-size: 14px; font-weight: 600; color: #3d3b48; border-radius: 5px; border: 2px ${colors.blueColor} solid;
          &:focus{outline: ${colors.blueColor}  !important;}
        }
  }
  & .date-picker{
    width: 50%;
      &  input{ width: 100%; height: 56px; padding: 14px; font-size: 14px; font-weight: 600;  border-radius: 5px; border: 2px ${colors.blueColor} solid;
        &:focus{outline: ${colors.blueColor}  !important;}
      }
  }
  & .title-div{width: 50%;}
  }
  & .btn-div{
    display: flex;
    justify-content: end;
    padding-top:10px ;
    column-gap:15px;
    .btn-blue { border: none;color: ${colors.white}; font-size: 16px; background-color: ${colors.blueColor}; padding: 10px 24px;border-radius: 8px;cursor: pointer; }
  }
  `;
class PushNotification extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      startDate: new Date(),
      selectOption: "",
      selectedOption: null,
      selectedUserType: null,
      users: [],
      options: [],
      columnsFilters: {},
      category: [],
      userPageNo: 1,
      userlimit: 10,
      tempUser:null,
      pagination: {
        current: 1,
        next: 1,
        totalData: 0,
        totalPages: 1,
        previous: 0,
        isLoading: true,
        sort_column: "",
        sort_by: "",
        showDate: true,
      },
    };
  }

  handleOptionChange = (e) => {   //to select options whether to send notification now or to schedule notification
    this.setState({
      selectOption: e.target.value,
    });
  };

  handleDate = (date) => {
    this.setState({startDate:date});
  };

  // to search users to send notification
  loadOptions = async (searchQuery, { page }) => {
    const { data } = await axios.get(
      `${c.API_BASE_URL}/admin/search-user?query=${searchQuery}&page=${page}`
    );
    return {
      options: data.data,
      hasMore: data.data.length >= 1,
      additional: {
        page: searchQuery ? 2 : page + 1
      }
    };
  };

  render() {
    const {selectOption,startDate,tempUser} = this.state;
    const customStyles = {
      control: (provided, state) => ({
        ...provided,
        background: '#fff',
        borderColor: '#4C75A3 !important',
        minHeight: '50px',
        height: '50px',
        borderWidth:' 2px',
        boxShadow: '0 0 1px #4C75A3',

      }),
    };
    return (
      <>
         <Wrapper>
            <Section>
              <div className="push-heading">
                <span className="title">Push Notifications</span>
              </div>
              <div className="select-user">
                <span className="label">Select User</span>
                  <AsyncPaginate value={tempUser} onChange={(user) => this.setState({tempUser:user})} loadOptions={this.loadOptions} getOptionValue={(option) => option.userName} getOptionLabel={(option) => option.userName}
                  additional={{page: 1,}} isMulti styles={customStyles}
                  />
              </div>
              <div className="schedule-notification">
                <div className="want-notification">
                  <span className="label">Want To Schedule Notification</span>
                  <select className="select-valid" id="is_sent" name="is_sent" onClick={this.handleOptionChange} data-placeholder="Select Tag">
                    <option selected="true" value="2">Send Notification Now</option>
                    <option value="1">Schedule Notification</option>
                  </select>
                </div>
                {selectOption == 1 && (
                  <div className="date-picker">
                    <span className="label">Schedule Date</span>
                    <DatePicker selected= {startDate} onChange= {this.handleDate} minDate= {new Date()} 
                    showTimeSelect  timeIntervals={5}  timeFormat= "HH:mm"  dateFormat= "MMMM d, yyyy h:mm aa"
                    />
                  </div>
                )}

                <div className="title-div">
                  <span className="label">Title</span>
                  <InputField type="text" placeholder="Enter title" name="title" />
                </div>
              </div>
              <div>
                  <span className="label">Message</span>
                  <textarea cols="5" rows="7" name="message" />
              </div>
              <div className="btn-div">
              <button className="btn-blue" type="submit">Submit</button>
            </div>
            </Section>
          </Wrapper>
      </>
    );
  }
}
export default withRouter(PushNotification);
