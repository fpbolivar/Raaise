import React from "react";
import {Modal,ModalContent,ModalFooter} from "./CustomModal.styled";
import InputField from "../InputField";
import TextEditor from "../TextEditor";
import axios from "../../../utils/axios"
import {GENERALSETTING} from "../../../ApiConstant"
class PrivacyModal extends React.Component {
  constructor(props) {
    super(props);
    this.state={
       form :{
        title:"",
        description:"",
        type:""
       },
       errors: [],
       submitting:""
    }
  }

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

//In this function, editGeneralSetting API is called.
  handleSubmit = async () => {
    const { modalAction,getGeneralListing} = this.props
    try{
      const { data } = await axios.post(GENERALSETTING.editGeneralSetting,this.state.form);
      if (data.status == 200) {
        getGeneralListing();
        modalAction(false)
      }
      else if (data.status == 422) {
        this.setState({
          errors: { [data.errors.param]: data.errors.message },
          submitting: false,
        });
      }
      document.getElementById("custom-loader").style.display = "none";
    }
    catch (e) {
      document.getElementById("custom-loader").style.display = "none";
    }
  };

//to check all the validations
  validate = () => {
    const { form } = this.state;
    const newError = {};
    let positionFocus = "";
    if (!form.title || !form.title.trim()) {
        newError["title"] = "Required";
        positionFocus = positionFocus || "title";
    }
    if (!form.description || !form.description.trim()) {
      newError["description"] = "Required";
      positionFocus = positionFocus || "description";
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

  // getSettingByType() function is called
  componentDidMount =()=>{
    document.getElementById("custom-loader").style.display = "block";
    this.getSettingByType()
  }

  //in this function,getSettingByType API is called
  getSettingByType = async()=>{
    const {type} = this.props
    try{
      const { data } = await axios.post(GENERALSETTING.getSettingByType,{type:type});
      if(data.status == 200) {
        const resData= data.data
        this.setState({
          form:{
          title: resData.title || "" ,
          description:resData.description || "",
          type :type || ""
          }
        })
      }
      document.getElementById("custom-loader").style.display = "none";
    }
     catch(e){
      document.getElementById("custom-loader").style.display = "none";
      console.log("error",e)
     }
  }

  //to get the value of Description of General Settings
  handleCustom = (value,name) => {
    this.setState((prevState) => ({
        form: { ...prevState.form, [name]: value },
    }));
  };

  onChange = (e) => {
    const name = e.target.name;
    const value = e.target.value;
    this.setState((prevState) => ({
        form: { ...prevState.form, [name]: value },
    }));
  }

  render() {
    const {  modalAction} =this.props;
    const { form,errors} = this.state
    return (
        <Modal>
          <ModalContent width="70%" height="490px">
                <span className="title">Edit General Settings</span>
                <form method="post" >
                  <div className="form-group">
                    <label className="label">Title</label>
                    <InputField name="title" placeholder="Title" type="text" value={form.title} onChange={this.onChange} errmsg={errors.title || ""} focused={errors.title ? 1 : 0} />
                  </div>
                  <div className="form-group">
                    <label className="label">Description</label>
                    <TextEditor name="description" placeholder={'Write something...'} handleChange={(html) => this.handleCustom(html,"description")} value={form.description}/>
                    <span className="has-cust-error">{(errors.description  && errors.description)}</span>
                  </div>
                </form>
                <ModalFooter>
                  <button className="btn-blue" onClick={() => modalAction(false)}>Cancel</button>
                  <button className="btn-blue" onClick={this.onSubmit}>Save</button>
                </ModalFooter>
          </ModalContent>
        </Modal>
    );
  }
}
export default PrivacyModal;
