import React from "react";
import { Container, Logo,LogoImage,FlexNavigation,NavLink,Navigation,Wrapper} from "./Navbar.styled";
class Navbar extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      form:{
      name: "",
      image: null,
      },
    }
  }
  //to set the details of Admin in local storage so that API does not get hit again and again
  componentDidMount = () => {
    const data = localStorage.getItem("admin-details")
    if(data){
      this.setState({
          form: JSON.parse(localStorage.getItem("admin-details")),
      });
  }
  };
    
  render() {
    const {form}= this.state
    return (
      <Wrapper>
        <Container>
          <Logo href="/dashboard">
            <LogoImage src="/assets/images/logo.png" alt="ScripTube"/>
          </Logo>
          <Navigation>
            <FlexNavigation items="center" justify="center">
              <NavLink href="/admin-profile">
                <div className="admin-section">
                  {
                    form && form.image ?
                    <img src={form.image} alt="img" className="admin-img"  />
                    :
                    <svg height={30} width={30} fill="#ffffff"style={{ verticalAlign: "middle" }}  viewBox="0 0 24 24"><path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 3c1.93 0 3.5 1.57 3.5 3.5S13.93 13 12 13s-3.5-1.57-3.5-3.5S10.07 6 12 6zm7 13H5v-.23c0-.62.28-1.2.76-1.58C7.47 15.82 9.64 15 12 15s4.53.82 6.24 2.19c.48.38.76.97.76 1.58V19z"></path></svg>
                  }
                  <span className="tooltip">
                    <span className="tooltiptext">{form.name || ""}</span>
                    {form.name && form.name.length > 20 ? form.name.substring(0,20) + "...": form.name}
                  </span>
                </div>
              </NavLink>
            </FlexNavigation>
          </Navigation>
        </Container>
      </Wrapper>
    );
  }
}
export default Navbar;
