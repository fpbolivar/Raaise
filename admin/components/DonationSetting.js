import React from "react";
import { AUTH } from "./../ApiConstant";
import axios from "../utils/axios";
import InputField from "./helpers/InputField";
import styled from "styled-components";
import colors from "../colors";
import MessageModal from "./helpers/modal/MessageModal";

const Container = styled.div`
  display: flex;
  justify-content: center;
  padding: 40px;
  width: 100%;
  `;
const Wrapper = styled.div`
  width: 70%;
  border-radius: 10px;
  background-color: ${colors.white};
  padding: 24px;
  box-shadow: 0px 2px 2px rgb(0 0 0 / 14%), 0px 3px 1px rgb(0 0 0 / 12%), 0px 1px 5px rgb(0 0 0 / 20%);
 `;
class DonationSetting extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      form: {
        adminCommision: "",
      },
      openMessageModal: {
        open: false,
        msgtitle: "",
        message: "",
        action: "",
      },
      errors: [],
      submitting: false,
    };
  }

 // to open donation setting modal
 modalAction = (status) => {
  const { openMessageModal } = this.state;
  if (status) {    
    this.setState({
      openMessageModal: {
      open: false,
      },
    });
  } else {
    this.setState({
      openMessageModal: {
      open: status,
      },
    });
  }
};

  //to get the previous Admin Comission in the Donation Setting form
  getData = async () => {
    try {
      const { data } = await axios.get(AUTH.getDonationSetting); //API calling
      const resData = data.data;
      if (data.status == 200) {
        this.setState({
          form: {
            adminCommision: resData.adminCommision ? resData.adminCommision : "",
          },
          submitting: false,
        });
      }
      document.getElementById("custom-loader").style.display = "none";
    } catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log("error", e);
    }
  };

  //getData() function is called
  componentDidMount = () => {
    document.getElementById("custom-loader").style.display = "block";
    this.getData(); 
  };

  //In this function,API is called
  handleSubmit = async () => {
    try {
      const { form } = this.state;
      const { data } = await axios.post(AUTH.donationSetting, form); //API calling
      if (data.status == 200) {
        this.setState({
          openMessageModal: {
            open: true,
            msgtitle: "Donation Setting",
            message: "Your donation is set successfully ",
          },
          submitting: false,
        });
      } else if (data.status == 422) {
        this.setState({
          errors: { [data.errors.param]: data.errors.message },
          submitting: false,
        });
      }
      document.getElementById("custom-loader").style.display = "none";
    } catch (e) {
      document.getElementById("custom-loader").style.display = "none";
    }
  };

  onChange = (e) => {
    const donationRE = /^[1-9][0-9]*$/ ;  //regex code for setting 1-100 as donation
    const value = e.target.value;
      if (value === '' || donationRE.test(value) && value <= 100 ) {
        this.setState((prevState) => ({
          form: {
            ...prevState.form,
            adminCommision: value,
            value: e.target.value
          },
        }));
      }
  };


  validate = () => {
    const { form } = this.state;
    const newError = {};
    let positionFocus = "";
    if (!form.adminCommision || !form.adminCommision.trim()) {
        newError["adminCommision"] = "Required";
        positionFocus = positionFocus || "adminCommision";
    }
    this.setState({
        errors: newError,
    });
    if (positionFocus) {
        return false;
    } else {
        return true;
    }
};

  onSubmit = (e) => {
    e.preventDefault();
    if (this.state.submitting) {
      return;
    }
    if (!this.validate()) {
      return;
    }
    document.getElementById("custom-loader").style.display = "block";
    this.setState({ submitting: true }, this.handleSubmit);
  };

  render() {
    const { errors, form,openMessageModal } = this.state
    return (
        <Container>
          {openMessageModal.open && (
          <MessageModal
            {...openMessageModal}
            modalAction={(status) => this.modalAction(status)}
          />
        )}
          <Wrapper>
          <span className="heading">Donation Setting</span>
            <form method="post" onSubmit={this.onSubmit} >
              <span className="label">Donation Setting in %age</span>
              <InputField aria-invalid="false" errmsg={errors.adminCommision || ""} value={form.adminCommision} focused={errors.adminCommision ? 1 : 0} onChange={this.onChange}  placeholder="Enter %age " name="adminCommision" type="text" />
              <div className="btn-section"><button className="submit-btn" >Save</button></div>
            </form>
          </Wrapper>
        </Container>
    )
  }
}
export default DonationSetting;
