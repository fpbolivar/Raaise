import React from "react";
import InputField from "./helpers/InputField";
import {PasswordVisibleIcon,PasswordInVisibleIcon} from "./../styles/InputField.styled";
import { AUTH } from "./../ApiConstant";
import axios from "./../utils/axios";
import { withRouter } from "next/router";
import styled from "styled-components";
import colors from "./../colors";

const Wrapper = styled.div`
    padding: 40px 30px;
    display: flex;
    justify-content: center;
    box-sizing: border-box;
    background: ${colors.gray1};
    width: 100%;
    
`;
const Form = styled.form`
    width: 70%;
    background-color: #fff;
    border-radius: 10px;
    padding: 30px;
    /* LAPTOP */
    @media only screen and (max-width: 1115px) {
        width: 100%;
    }
`;
class ChangePassword extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            form: {
                oldpassword: "",
                newpassword: "",
                confirmpassword: "",
            },
            errors: [],
            oldPasswordVisible: false,
            passwordVisible: false,
            confirmPasswordVisible: false,
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

    //in this function,API is called
    handleSubmit = async () => {
        try {
            const { router } = this.props;
            const { data } = await axios.post(AUTH.changePassword,this.state.form);//API calling
            if (data.status === 200) {
                router.push("/dashboard");
            } else if (data.status === 422) {
                this.setState({
                    errors: { [data.errors.param]: data.errors.message },
                    submitting: false,
                });
            }
            document.getElementById("custom-loader").style.display = "none";
        }
        catch (e) {
            document.getElementById("custom-loader").style.display = "none";
            console.log("error", e);
        }
    };

    //to check all the validations of the form
    validate = () => {
        const { form } = this.state;
        const newError = {};
        let positionFocus = "";
        if (!form.oldpassword || !form.oldpassword.trim()) {
            newError["oldpassword"] = "Required";
            positionFocus = positionFocus || "oldpassword";
        }
        if (!form.newpassword || !form.newpassword.trim()) {
            newError["newpassword"] = "Required";
            positionFocus = positionFocus || "newpassword";
        }
        if (!form.confirmpassword || !form.confirmpassword.trim()) {
            newError["confirmpassword"] = "Required";
            positionFocus = positionFocus || "confirmpassword";
        } else if (form.oldpassword == form.newpassword) {
            newError["newpassword"] =
                "Your new password cannot be the same as your current password.";
            positionFocus = positionFocus || "password";
        } else if (form.newpassword !== form.confirmpassword) {
            newError["confirmpassword"] = "Passwords must be same.";
            positionFocus = positionFocus || "confirmpassword";
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
        const { oldPasswordVisible, passwordVisible, confirmPasswordVisible, errors, form} = this.state;
        return (
           <Wrapper>
             <Form method="post" onSubmit={this.onSubmit}>
                    <span className="heading">Change Password</span>
                    <div className="form-group">
                    <span className="label">Old Password*</span>
                    <InputField name="oldpassword" placeholder="Old Password" type={!oldPasswordVisible ? "password" : "text"}
                    value={form.oldpassword} onChange={this.onChange} errmsg={errors.oldpassword || ""} focused={errors.oldpassword ? 1 : 0}
                    endAdornment={
                    oldPasswordVisible ? (
                    <PasswordVisibleIcon onClick={() =>this.setState({oldPasswordVisible:!oldPasswordVisible})} />
                    ) : (
                    <PasswordInVisibleIcon onClick={() =>this.setState({oldPasswordVisible:!oldPasswordVisible})} />
                    )
                    }
                    />
                </div>
                <div className="form-group">
                    <span className="label">New Password*</span>
                    <InputField name="newpassword" placeholder="New Password" type={!passwordVisible ? "password" : "text"} value={form.password} onChange={this.onChange}
                    errmsg={errors.newpassword || ""} focused={errors.newpassword ? 1 : 0}
                    endAdornment={
                    passwordVisible ? (
                    <PasswordVisibleIcon onClick={() =>this.setState({passwordVisible:!passwordVisible})} />
                    ) : (
                   <PasswordInVisibleIcon onClick={() =>this.setState({passwordVisible:!passwordVisible})} />
                    )}
                    />
                </div>
                <div className="form-group">
                   <span className="label">Confirm Password*</span>
                    <InputField name="confirmpassword" placeholder="Confirm Password" type={!confirmPasswordVisible ? "password" : "text"} value={form.confirmpassword}
                    onChange={this.onChange} errmsg={errors.confirmpassword || ""} focused={errors.confirmpassword ? 1 : 0}
                    endAdornment={
                    confirmPasswordVisible ? (
                    <PasswordVisibleIcon onClick={() =>this.setState({confirmPasswordVisible:!confirmPasswordVisible})} />
                     ) : (
                    <PasswordInVisibleIcon onClick={() =>this.setState({confirmPasswordVisible:!confirmPasswordVisible})} />)
                    }
                    />
                </div>
                <div className="btn-section"><button className="submit-btn">Save</button></div>
            </Form>
        </Wrapper>
        );
    }
}
export default withRouter(ChangePassword);
