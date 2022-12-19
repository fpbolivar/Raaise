import React from "react";
import InputField from "../helpers/InputField";
import {Form,Wrapper,FormWrapper,Container,LoginBtn,ContentWrapper,} from "./Login.styled";
import {PasswordVisibleIcon,PasswordInVisibleIcon,} from "./../../styles/InputField.styled";
import { AUTH } from "../../ApiConstant";
import axios from "../../utils/axios";
import { withRouter } from "next/router";

class LoginForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      form: {
        email: "",
        password: "",
      },
      errors: [],
      passwordVisible: false,
      submitting: false,
    };
  }
  //to check all validations
  validate = () => {
    const { form } = this.state;
    const newError = {};
    //Email Regex Code
    const emailRE = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (!form.email || !form.email.trim()) {
      newError["email"] = "Required";
    } else if (!emailRE.test(form.email)) {
      newError["email"] = "Please enter a valid email";
    }
    if (!form.password || !form.password.trim()) {
      newError["password"] = "Required";
    }
    this.setState({
      errors: newError,
    });
    if (Object.keys(newError).length > 0) {
      return false;
    }
    return true;
  };
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
  //API calling in this function
  handleSubmit = async () => {
    try {
      const { form } = this.state;
      const { router } = this.props;
      const { data } = await axios.post(AUTH.signIn, form);
      if (data.status == 200) {
        localStorage.setItem("scriptube-admin-token", data.token);
        router.push("/dashboard");
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
    const { form, passwordVisible, errors } = this.state;
    return (
      <Container>
          <ContentWrapper>
            <img src="/assets/images/login.png" alt="logo" />
          </ContentWrapper>
        <Wrapper>
          <Form method="post" onSubmit={this.onSubmit}>
            <h1>Admin Login</h1>
            <InputField name="email" placeholder="Email" value={form.name} onChange={this.onChange} errmsg={errors.email || ""} focused={errors.email ? 1 : 0} />
            <InputField name="password" placeholder="Password" value={form.password} onChange={this.onChange} errmsg={errors.password || ""} focused={errors.password ? 1 : 0} type={!passwordVisible ? "password" : "text"}
              endAdornment={
                passwordVisible ? (
                  <PasswordVisibleIcon onClick={() => this.setState({passwordVisible: !passwordVisible,})}/>
                ) : (
                  <PasswordInVisibleIcon onClick={() => this.setState({passwordVisible: !passwordVisible,})} />
                )
              }
            />
            <FormWrapper>
              <LoginBtn>Login</LoginBtn>
            </FormWrapper>
          </Form>
        </Wrapper>
      </Container>
    );
  }
}
export default withRouter(LoginForm);
