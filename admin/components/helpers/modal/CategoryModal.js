import React from "react";
import {Modal,ModalContent,ModalFooter} from "./CustomModal.styled";
import InputField from "../InputField";
import { FileDiv,Picture } from "../../admin-profile/AdminProfile.styled";
import axios from "../../../utils/axios"
import { VIDEO } from "../../../ApiConstant";
class CategoryModal extends React.Component {
  constructor(props) {
    super(props);
    this.state={
       form :{
        name: "",
        image:"",
        loadImage: null,
        type:"",
        categoryId:''
       },
       errors: [],
    }
  }
  componentDidMount = () => {
    const {data}=this.props
    if(data){
      this.setState((prevState)=>({
        form:{...prevState.form,name:data.name?data.name:"",image:data.image?data.image:"",categoryId:data._id?data._id:""}
      }))
    }
  };
  onSubmit = (e) => {
    const{data}=this.props
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

//In this function,addVideoCategory and editVideoCategory API is called
handleSubmit = async () => {
  console.log("category handle")
  const {type,getVideoCategories} = this.props;
  console.log(type,"typeeeeeuui")
  if(type == "add"){
    try {
      const { router,modalAction,getVideoCategories} = this.props;
      const {form} = this.state;
      let formData = new FormData();
      formData.append("name",form.name);
      formData.append("image",form.image);
      const { data } = await axios.post(VIDEO.addVideoCategory, formData); //API calling
      if (data.status === 200) {
        getVideoCategories(1,10)
        modalAction(false)
        this.setState({
          submitting: false,
        });
        router.push("/video-categories");
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
  }
  else{
    try {
      const { router,modalAction,getVideoCategories } = this.props;
      const {form} = this.state;
      let formData = new FormData();
      formData.append("categoryId",form.categoryId);
      formData.append("name",form.name);
      formData.append("image",form.image);
      const { data } = await axios.post(VIDEO.editVideoCategory, formData); //API calling
      if (data.status === 200) {
        getVideoCategories(1)
        modalAction(false)
        router.push("/video-categories");
        this.setState({
          submitting: false,
        });
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
  }
};

//to check all the validations
  validate = () => {
    const { form } = this.state;
    const newError = {};
    let positionFocus = "";
    if (!form.name || !form.name.trim()) {
        newError["name"] = "Required";
        positionFocus = positionFocus || "name";
    }
    if (!form.image) {
      newError["image"] = "Required";
      positionFocus = positionFocus || "image";
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

  onChange = (e) => { 
    const name = e.target.name;
    const value = e.target.value;
    this.setState((prevState) => ({
        form: { ...prevState.form, [name]: value },
    }));
  }

  //to preview image of Video Category
  loadImage = (e) => {
    let file = e.target.files[0];
    if(file){
      let type = e.target.files[0].type.split("/")[0];
    if (type === "image") {
      let img = document.createElement("img");
      let blob = URL.createObjectURL(file);
      this.setState((prevState) => ({
        form: {
          ...prevState.form,
          image: file,
          loadImage: blob,
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
        },
        errors: {
          ...prevState.errors,
          image: "Invalid format",
        },
      }));
    } 
    }
  };
  
  render() {
    const {  modalAction, closeButton,type} =this.props;
    const { form,errors} = this.state
    return (
      <Modal>
        <ModalContent width="70%" height="480px">
          <span className="title">{ type == "add" ? "Add New Category" : "Edit  Category"}</span>
            <form method="post" >
              <div className="form-group">
                <label className="label">Name*</label>
                <InputField name="name" placeholder="Name" type="text" value={form.name} onChange={this.onChange}   errmsg={errors.name || ""} focused={errors.name ? 1 : 0} />
              </div>
              <div className="form-group">
                <span className="label">Upload File*</span>
                <InputField name="image" placeholder="Choose File"  type="file" id="file" accept="image/*" onChange={this.loadImage} errmsg={errors.image || ""} focused={errors.image ? 1 : 0}  />
              </div>
              {form.image &&
              <div className="form-group">
                  <img alt="Profile Picture" className="img-round"
                    src={
                      form.loadImage ? form.loadImage
                      : 
                      form.image ? form.image : ""
                    }
                  />
              </div>
              }
            </form>
            <ModalFooter>
              {!closeButton && (<button className="btn-blue" onClick={() => modalAction(false)}>Cancel</button>)}
              <button className="btn-blue" onClick={this.onSubmit}>Save</button>
            </ModalFooter>
        </ModalContent>
      </Modal>
    );
  }
}

export default CategoryModal;
