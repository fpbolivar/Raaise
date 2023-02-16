import { withRouter } from "next/router";
import React from "react";
import InputField from "../helpers/InputField";
import { USERS } from "../../ApiConstant";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { AsyncPaginate } from "react-select-async-paginate";
import styled from "styled-components";
import colors from "../../colors";
import { NOTIFICATION } from "../../ApiConstant";
import axios from "../../utils/axios";
import moment from 'moment'
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
  & .select-user{padding-top: 15px; font-size: 14px;font-weight: 600;color: #3d3b48;
  }
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
      &  input{ width: 100%; height: 56px; padding: 14px; font-size: 14px; font-weight: 600;  border-radius: 5px; border: 2px ${colors.blueColor} solid; color: #3d3b48 !important;
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
      options: [],
      form: {
        title: "",
        desc: "",
        type: "now",
        scheduleTime: moment().format('YYYY/MM/DD HH:mm:ss'),
        scheduleDateTime: new Date()
      },
      tempUser: "",
      errors: [],
      submitting: false
    };
  }
  //to select options whether to send notification now or to schedule notification
  handleOptionChange = (e) => {
    this.setState({
      form: {
        type: e.target.value,
      }
    });
  };

  handleDate = (date) => {
    const myDate = new Date(date);
    console.log(myDate);
    this.setState((prevState) => ({
      form: {
        ...prevState.form,
        scheduleTime: moment(date).format('YYYY/MM/DD HH:mm:ss'),
        scheduleDateTime: date
      }
    }))
  };

  // to search users to send notification
  loadOptions = async (searchQuery, loadedOptions, { page }) => {
    const { data } = await axios.get(
      `${USERS.searchUser}?query=${searchQuery}&page=${page}`
    );
    return {
      options: data.data,
      hasMore: data.data.length >= 1,
      additional: {
        page: searchQuery ? 2 : page + 1
      }
    };
  };
  onSubmit = (e) => {
    e.preventDefault()
    if (this.state.submitting) {
      return;
    }
    if (!this.validate()) {
      return;
    }
    document.getElementById("custom-loader").style.display = "block";
    this.setState({ submitting: true }, this.handleSubmit);
  }
  handleSubmit = async () => {
    let { form, tempUser } = this.state
    const { router } = this.props
    let invitedUsers = tempUser && tempUser.length > 0 && tempUser.map((item) => { return item._id; });
    form = { ...form, sendTo: invitedUsers };
    try {
      const response = await axios.post(NOTIFICATION.pushNotification, form);
      if (response.data.status === 200) {
        this.setState({ submitting: false })
        router.push("/notification/view-notification");
      } else if (response.data.status === 422) {
        this.setState({
          errors: { [response.param]: response.message },
          submitting: false,
        });
      }
      document.getElementById("custom-loader").style.display = "none";
    }
    catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log(e)
    }
  }
  //to check all validations
  validate = () => {
    const { form, tempUser } = this.state
    const newError = {}
    let positionFocus = "";
    if (!tempUser) {
      newError["user"] = "Required"
      positionFocus = positionFocus || "user";
    }
    if (!form.title || !form.title.trim()) {
      newError["title"] = "Required"
      positionFocus = positionFocus || "title"
    }
    if (!form.desc || !form.desc.trim()) {
      newError["desc"] = "Required"
      positionFocus = positionFocus || "desc"
    }
    this.setState({
      errors: newError
    })
    if (positionFocus) {
      return false;
    } else {
      return true;
    }
  }
  onChange = (e) => {
    const name = e.target.name;
    const value = e.target.value
    this.setState((prevState) => ({
      form: {
        ...prevState.form,
        [name]: value
      }
    }))
  }
  filterPassedTime = (time) => {
    const currentDate = new Date();
    const selectedDate = new Date(time);
    return currentDate.getTime() < selectedDate.getTime();
  };
  render() {
    const { tempUser, errors, form } = this.state;
    const customStyles = {
      control: (provided, state) => ({
        ...provided,
        background: '#fff',
        borderColor: '#4C75A3 !important',
        minHeight: '56px',
        // height: '50px',
        borderWidth: ' 2px',
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
              <AsyncPaginate placeholder="Select User..." value={tempUser} onChange={(user) => this.setState({ tempUser: user })} loadOptions={this.loadOptions} getOptionValue={(option) => option.userName} getOptionLabel={(option) => option.userName}
                additional={{page: 1,}}
                isMulti styles={customStyles} />         
              <span className="has-cust-error err">{errors.user || ""}</span>
            </div>
            <div className="schedule-notification">
              <div className="want-notification">
                <span className="label">Want To Schedule Notification</span>
                <select className="select-valid" id="is_sent" name="is_sent" onClick={this.handleOptionChange} data-placeholder="Select Tag">
                  <option selected="selected" value="now" >Send Notification Now</option>
                  <option value="schedule">Schedule Notification</option>
                </select>
              </div>
              {form.type == "schedule" && (
                <div className="date-picker">
                  <span className="label">Schedule Date</span>
                  <DatePicker placeholderText="Select Date" selected={form.scheduleDateTime} onChange={this.handleDate} minDate={new Date()}
                    filterTime={this.filterPassedTime}
                    showTimeSelect timeIntervals={5} timeFormat="HH:mm" dateFormat="yyyy ,MMMM d h:mm aa" />
                </div>
              )}
              <div className="title-div">
                <span className="label">Title</span>
                <InputField type="text" placeholder="Enter title" name="title" value={form.title} onChange={this.onChange} errmsg={errors.title || ""} focused={errors.title ? 1 : 0} />
              </div>
            </div>
            <div>
              <span className="label">Message</span>
              <textarea cols="5" rows="7" name="desc" value={form.desc} onChange={this.onChange} />
              <span className="has-cust-error err">{errors.desc || ""}</span>
            </div>
            <div className="btn-div">
              <button className="btn-blue" type="submit" onClick={this.onSubmit}>Submit</button>
            </div>
          </Section>
        </Wrapper>
      </>
    );
  }
}
export default withRouter(PushNotification);
