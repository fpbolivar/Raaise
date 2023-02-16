import React from "react";
import InputField from "../helpers/InputField";
import {AUDIO } from "../../ApiConstant";
import axios from "../../utils/axios";
import { withRouter } from "next/router";
import {FileDiv,Picture,Wrapper} from "../../components/admin-profile/AdminProfile.styled"
import styled from "styled-components";
import colors from "../../colors";

const Form = styled.form`
  width: 70%;
  background-color: ${colors.white};
  border-radius: 10px;
  padding: 30px;
  & .img-section { display: flex; flex-direction: column; width: 100%; align-items: center;}
  & .err { display:flex; justify-content: center;margin-bottom: 15px;}
  & .select-genre {border: 2px ${colors.blueColor} solid;width: 100%;padding: 14px;font-size: 14px;font-weight: 600;color: #757575;border-radius: 5px;&:focus{outline: ${colors.blueColor};}}
`;
class UploadAudio extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      form: {
        Thumbnail: null,
        audio: null,
        artistName: "",
        genreId: "",
        songName: '',
        loadImage: null,
      },
      genre: [],
      errors: [],
    };
    this.uploadAudioFile = this.uploadAudioFile.bind(this)
  }

  //getGenre() function is called
  componentDidMount = () => {
    document.getElementById("custom-loader").style.display = "block";
    this.getGenre();
  };

  //to get genre from the database in the option menu
  getGenre = async () => {
    try {
      const { data } = await axios.get(AUDIO.getGenre);
      const resData = data.data;
      if (data.status == 200) {
        this.setState({genre: resData});
      }
      document.getElementById("custom-loader").style.display = "none";
    } catch (e) {
      document.getElementById("custom-loader").style.display = "none";
    }
  };

  //to upload the Thumbnail of the song
  uploadFile = () => {
    document.getElementById("file-upload").click();
  };

  //to upload the audio of 30secs for making video
  uploadAudioFile = (e) => {
    let file = e.target.files[0];
    let type = e.target.files[0].type.split("/")[0]; 
    if (type === "audio") {
      //get the duration of audio and show validation
      const reader = new FileReader();
      reader.onload = () => {
      const aud = new Audio(reader.result);
      aud.onloadedmetadata = () => {
      if(aud.duration >= 31){
          e.target.value = null
          this.setState((prevState) => ({
          form: {
          ...prevState.form,
          audio: null
          },
         errors: {
         ...prevState.errors,
         audio: "Please upload the audio less than 30 seconds",
         },
        }));
         }
      }}    
      reader.readAsDataURL(file);	
      this.setState((prevState) => ({
        form: {
          ...prevState.form,
          audio: file
        },
        errors: {
          ...prevState.errors,
          audio: "",
        },
      }));
    
    } else {
      e.target.value = null
      this.setState((prevState) => ({
        form: {
          ...prevState.form,
          audio: null
        },
        errors: {
          ...prevState.errors,
          audio: "Invalid format",
        },
      }));
    }
  }

  //to preview the image that is uploaded as thumbnail of the song
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
          Thumbnail: file,
          loadImage: blob,
        },
        errors: {
          ...prevState.errors,
          Thumbnail: "",
        },
      }));
    } else {
      this.setState((prevState) => ({
        form: {
          ...prevState.form,
          Thumbnail: null,
          loadImage: null,
        },
        errors: {
          ...prevState.errors,
          Thumbnail: "Invalid format",
        },
      }));
    }
    }
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

  //to check all the validations
  validate = () => {
    const { form } = this.state;
    const newError = {};
    let positionFocus = "";

    if (!form.Thumbnail) {
      newError["Thumbnail"] = "Required";
      positionFocus = positionFocus || "Thumbnail";
    }
    if (!form.audio) {
      newError["audio"] = "Required";
      positionFocus = positionFocus || "audio";
    }
    if (!form.genreId) {
      newError["genreId"] = "Required";
      positionFocus = positionFocus || "genreId";
    }
    if (!form.songName || !form.songName.trim()) {
      newError["songName"] = "Required";
      positionFocus = positionFocus || "songName";
    }
    if (!form.artistName || !form.artistName.trim()) {
      newError["artistName"] = "Required";
      positionFocus = positionFocus || "artistName";
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

  //In this function,API is called
  handleSubmit = async () => {
    try {
      const { router } = this.props;
      const {form} = this.state
      let formData = new FormData();
      formData.append("Thumbnail",form.Thumbnail);
      formData.append("audio",form.audio);
      formData.append("songName",form.songName);
      formData.append("artistName",form.artistName);
      formData.append("genreId",form.genreId);
      const { data } = await axios.post(AUDIO.uploadAudio, formData); //API calling
      if (data.status === 200) {
        router.push("/audio/audio-list");
      } else if (data.status === 422) {
        this.setState({
          errors: { [data.param]: data.message },
          submitting: false,
        });
      }
      document.getElementById("custom-loader").style.display = "none";
    } catch (e) {
      document.getElementById("custom-loader").style.display = "none";
    }
  };

  render() {
    const { errors, form, genre } = this.state;
    return (
      <Wrapper>
        <Form method="post" onSubmit={this.onSubmit}>
          <span className="heading"> Upload Audio</span>
            <Picture >
              <img src={ form.loadImage ? form.loadImage :  "/assets/images/audiosign.svg" } alt="Thumbnail" />
              <span className="has-cust-error err">{errors.Thumbnail || ""}</span>
            </Picture>

            <FileDiv onClick={this.uploadFile} id="Thumbnail">
              <div>
              <label>Select Thumbnail</label>
              <input type="file" id="file-upload" accept="image/*" hidden onChange={this.loadImage}/>
              </div>
            </FileDiv>

            <div className="form-group">
              <span className="label">Choose Music File</span>
              <InputField name="audio" placeholder="Choose Music File" type="file" id="audio" accept=".mp3,audio/*" onChange={this.uploadAudioFile} errmsg={errors.audio || ""} focused={errors.audio ? 1 : 0} />
            </div>

            <div className="form-group">
              <span className="label">Select Genre</span>
              <select className={ "select-genre"} name="genreId" id="genreId" onChange={this.onChange}>
                <option hidden>Select Genre</option>
                {genre.map(item => {
                  return (<option value={item._id} key={item._id}>{item.name}</option>)
                })}
              </select>
              <span className="has-cust-error">{errors.genreId || ""}</span>
            </div>

            <div className="form-group">
              <span className="label">Enter Song Name</span>
              <InputField name="songName" placeholder="Enter Song Name" type="text" id="songName" value={form.songName} onChange={this.onChange} errmsg={errors.songName || ""} focused={errors.songName ? 1 : 0} />
            </div>

            <div className="form-group">
              <span className="label">Artist Name</span>
              <InputField name="artistName" placeholder="Artist Name" type="text" id="artistName" value={form.artistName} onChange={this.onChange} errmsg={errors.artistName || ""} focused={errors.artistName ? 1 : 0} />
            </div>

            <div className="btn-section"><button className="submit-btn">Submit</button></div>
        </Form>
      </Wrapper>
    );
  }
}
export default withRouter(UploadAudio);
