import React from "react";
import InputField from "../helpers/InputField";
import {Form, Wrapper,Picture,FileDiv} from "./AdminProfile.styled";
import { AUTH } from "../../ApiConstant";
import axios from "../../utils/axios";
import { withRouter } from "next/router";
import { Error } from "../../styles/InputField.styled";
class AdminProfile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      form: {
        name: "",
        email: "",
        image: null,
        loadImage: null,
        photo: "",
        isFile: false
      },
      errors: [],
      name: false,
      email: false,
      photo: false,
      submitting: false,
    };
  }

  onChange = (e) => {
    const name = e.target.name;
    const value = e.target.value;
    this.setState((prevState) => ({
      form: {
        ...prevState.form,
        [name]: value,
      },
    }));
  };

  //to upload the profile picture of Admin
  uploadFile = () => {
    document.getElementById("file-upload").click();
  };

  // to preview the profile picture of Admin in the form
  loadImage = (e) => {
    let file = e.target.files[0];
    if(file){
    let type = e.target.files[0].type.split("/")[0];
    if (type === "image") {
      let blob = URL.createObjectURL(file);
      this.setState((prevState) => ({
        form: {
          ...prevState.form,
          image: file,
          loadImage: blob,
          isFile: true
        },
        errors: {
          ...prevState.errors,
          image: "",
        },
      }));
    } else {
      this.setState((prevState) => ({
        form: {
          ...prevState.form,
          image: null,
          loadImage: null,
          isFile: false
        },
        errors: {
          ...prevState.errors,
          image: "Invalid format",
        },
      }));
    }
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

  //to get the previous data(name,email,profile picture) of Admin in the Edit Profile's form by default
  getData = async () => {
    try {
      const { data } = await axios.get(AUTH.getAdmin); //API calling
      const resData = data.data;
      if (data.status == 200) {
        this.setState({
          form: {
            name: resData.name ? resData.name : "",
            email: resData.email ? resData.email : "",
            image: resData.image ? resData.image : null,
            isFile: resData.image ? true : false,
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
  componentDidMount =  () => {
    document.getElementById("custom-loader").style.display = "block";
    this.getData(); 
  };

  // In this function,API is called
  handleSubmit = async () => {
    try {
      const { router } = this.props;
      const {form} = this.state
      let formData = new FormData();
      formData.append("name",form.name);
      formData.append("email",form.email);
      formData.append("image",form.image);
      const { data } = await axios.post(AUTH.editAdmin, formData); //API calling
      if (data.status === 200) {
        this.setState({
          submitting: false,
        });
        router.push("/dashboard");
      } else if (data.status === 422) {
        this.setState({
          errors: { [data.param]: data.message },
          submitting: false,
        });
      }
      document.getElementById("custom-loader").style.display = "none";
    } catch (e) {
      document.getElementById("custom-loader").style.display = "none";
      console.log("error", e);
    }
  };

  //to check all the validations
  validate = () => {
    const { form } = this.state;
    const newError = {};
    let positionFocus = "";
    //regex code for Email
    const emailRE = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (!form.isFile) {
      newError["image"] = "Required";
      positionFocus = positionFocus || "image";
    }
    if (!form.name || !form.name.trim()) {
      newError["name"] = "Required";
      positionFocus = positionFocus || "name";
    }
    if (!form.email || !form.email.trim()) {
      newError["email"] = "Required";
      positionFocus = positionFocus || "email";
    } else if (!emailRE.test(form.email)) {
      newError["email"] = "Please enter a valid email";
      positionFocus = positionFocus || "email";
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

  render() {
    const { errors, form } = this.state;
    return (
        <Wrapper>
          <Form method="post" onSubmit={this.onSubmit}>
          <span className="heading"> Edit Profile </span>
            <Picture>
              <img alt="Profile Picture"
                src={
                  form.loadImage ? form.loadImage
                  : 
                  form.image ? form.image : "/assets/images/profile.png"
                }
              />
              <Error style={{ display: "flex", justifyContent: "center" }}>{errors.image || ""}</Error>
            </Picture>
            <FileDiv onClick={this.uploadFile}>
              <label className="label">Select a New Photo</label>
              <input type="file" id="file-upload" accept="image/*" hidden onChange={this.loadImage}/>
              <span className="file-chosen" value={form.photo} errmsg={errors.photo || ""} focused={errors.photo ? 1 : 0}/>
            </FileDiv>

            <div className="form-group">
              <span className="label">Name*</span>
              <InputField name="name" placeholder="Enter Name" type="text" value={form.name} onChange={this.onChange} errmsg={errors.name || ""} focused={errors.name ? 1 : 0} />
            </div>
            <div className="form-group">
              <label className="label">Email*</label>
              <InputField name="email" placeholder="Enter Email" value={form.email} onChange={this.onChange} errmsg={errors.email || ""} focused={errors.email ? 1 : 0} />
            </div>
            <div className="btn-section">
              <button className="submit-btn">Save</button>
            </div>
          </Form>
        </Wrapper>
    );
  }
}
export default withRouter(AdminProfile);
