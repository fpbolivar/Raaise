import React from "react";
import {NavLi,NavLinkActive,Hamburger,Container,UserSideBar,SideBarOpacity,Content,SubNav,ToogleSubMenu,} from "./Sidebar.styled";
import { router, withRouter } from "next/router";
class SideBar extends React.PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: true,
      toogleNav: {
        donation: false,
        audio: false,
        video:false,
        notification:false,
      },
    };
  }

  //In this function,Togglebar(to remain open) functionality for Donation Reports,Audio,Reported Video is made.
  componentDidMount = () => {
    const donationArr = ["/donations/donation-received", "/donations/admin-commission","/donations/pending-transactions","/donations/transfer-transactions","/donations/withdraw-requests"];
    const audioArr = ["/audio/upload-audio", "/audio/audio-list"];
    const videoArr= ["/reported-video"];
    const notificationArr= ["/notification/view-notification","/notification/notification-log"];


    if (donationArr.includes(router.pathname)) { //Donation Reports Menu to remain open
      this.setState((prevState) => ({
        ...prevState,
        toogleNav: {
          ...prevState.toogleNav,
          donation: true,
        },
      }));
    } else if (audioArr.includes(router.pathname)) {  //Audio Menu to remain open
      this.setState((prevState) => ({
        ...prevState,
        toogleNav: {
          ...prevState.toogleNav,
          audio: true,
        },
      }));
    } else if (videoArr.includes(router.pathname)) {  //Video Menu to remain open
      this.setState((prevState) => ({
        ...prevState,
        toogleNav: {
          ...prevState.toogleNav,
          video: true,
        },
      }));
    }
    else if (notificationArr.includes(router.pathname)) {  //Video Menu to remain open
      this.setState((prevState) => ({
        ...prevState,
        toogleNav: {
          ...prevState.toogleNav,
          notification: true,
        },
      }));
    }
  };

  //to logout Admin
  handleLogout = () => {
    localStorage.removeItem("scriptube-admin-token");
    localStorage.removeItem("admin-details");
    localStorage.removeItem("scriptube-admin-details");
    router.push("/");
  };

  toogleBar = (name) => {
    this.setState((prevState) => ({
      toogleNav: {
        ...prevState.toogleNav,
        [name]: !prevState.toogleNav[name],
      },
    }));
  };

  render() {
    const { isOpen, toogleNav } = this.state;
    const { router } = this.props;
    return (
      <>
        <Container>
          <UserSideBar open={isOpen}>
            {!isOpen ? (
              <>
                <Hamburger onClick={() => this.setState({isOpen: !isOpen, })} />
                <div className="mobile-list">
                  <NavLi>
                    <NavLinkActive onClick={() => router.push("/dashboard")} className="cursor-pointer" active={router.pathname == "/dashboard" ? true : false}>
                      <svg width="20" height="18" viewBox="0 0 100 100"><defs><clipPath id="clip-_2"><rect width="100" height="100" /></clipPath></defs>
                        <g clipPath="url(#clip-_2)"> 
                          <g transform="translate(5.767 5.767)">
                              <path d="M0,20.123V4.685C0,1.562,1.587,0,4.71,0H35.537c3.173,0,4.735,1.562,4.735,4.76V35.487c0,3.224-1.562,4.76-4.81,4.76H4.836C1.511,40.247,0,38.71,0,35.386ZM8.135,8.11V32.137H32.162V8.11Z" transform="translate(0)" fill={router.pathname == "/dashboard"  ? "#ffffff"  : "#000000"} />   <path d="M211.823,0h15.464c3.073,0,4.659,1.587,4.659,4.634v31c0,3-1.612,4.609-4.584,4.609h-31.1c-2.922,0-4.559-1.612-4.559-4.533V4.533c0-2.871,1.637-4.508,4.483-4.508Q204-.013,211.823,0ZM199.785,32.137h24.027V8.11H199.785Z" transform="translate(-143.419)"  fill={router.pathname == "/dashboard"  ? "#ffffff"  : "#000000"}/> <path d="M40.247,211.8v15.363c0,3.148-1.562,4.71-4.685,4.71H4.659c-3.047,0-4.659-1.587-4.659-4.634v-31c0-3,1.612-4.609,4.584-4.634h31.1c2.972,0,4.584,1.612,4.584,4.634C40.272,201.422,40.247,206.611,40.247,211.8Zm-8.11,11.963V199.735H8.11v24.027Z" transform="translate(0 -143.344)" fill={router.pathname == "/dashboard"  ? "#ffffff"  : "#000000"}/> <path d="M212.408,231.871a20.119,20.119,0,1,1,19.569-20.7A20.093,20.093,0,0,1,212.408,231.871Zm-.5-32.162A12.064,12.064,0,1,0,223.968,211.7,12.06,12.06,0,0,0,211.9,199.709Z" transform="translate(-143.474 -143.369)" fill={router.pathname == "/dashboard"  ? "#ffffff"  : "#000000"}/>
                          </g>
                        </g>
                      </svg>
                    </NavLinkActive>
                  </NavLi>

                  <NavLi>
                    <NavLinkActive onClick={() => router.push("/users")} className="cursor-pointer" active={router.pathname == "/users" ? true : false}>
                      <svg width="20" height="18" viewBox="0 0 100 100"><defs><clipPath id="clip-_3"><rect width="100" height="100" /></clipPath></defs>
                        <g clipPath="url(#clip-_3)">
                          <g transform="translate(4.058 7.487)">
                            <path d="M55.866,68.942v9.841c2.22,0,4.413-.027,6.579,0a3.268,3.268,0,0,1,.6,6.469,4.227,4.227,0,0,1-1.014.082h-32.1c-2.166,0-3.673-1.343-3.673-3.289s1.48-3.262,3.673-3.262h6.113V68.942H10.609C4.276,68.942,0,64.665,0,58.333v-47.7C0,4.276,4.249,0,10.609,0H81.277c6.414,0,10.636,4.249,10.636,10.691V58.278c0,6.414-4.249,10.663-10.663,10.663H55.866ZM45.916,62.39H81.2c2.878,0,4.167-1.261,4.167-4.112V10.663c0-2.8-1.288-4.112-4.057-4.112h-70.7c-2.741.027-4.03,1.316-4.03,4.112V58.278c0,2.823,1.288,4.112,4.139,4.112ZM42.736,69v9.759h6.442V69Z" fill={router.pathname == "/users"  ? "#ffffff"  : "#000000"}  /> <path d="M179.832,63.017h6.387c.959,5.455,4.249,8.443,8.909,8.141a8.266,8.266,0,0,0,7.593-7.675c.247-4.66-2.769-7.922-8.141-8.8V48.351c4.167-.493,9.814,2.3,12.61,6.963a14.77,14.77,0,0,1-18.257,21.3C183.423,74.366,179.448,68.445,179.832,63.017Z" transform="translate(-130.518 -35.056)" fill={router.pathname == "/users"  ? "#ffffff"  : "#000000"} /> <path d="M66.037,179.668H80.4c2.193,0,3.673,1.316,3.7,3.262a3.335,3.335,0,0,1-3.646,3.317H51.646A3.335,3.335,0,0,1,48,182.93c.027-1.946,1.508-3.262,3.7-3.262C56.47,179.64,61.24,179.668,66.037,179.668Z" transform="translate(-34.842 -130.408)" fill={router.pathname == "/users"  ? "#ffffff"  : "#000000"} /> <path d="M57.8,138.352h-6.36a3.333,3.333,0,0,1-3.536-3.317,3.293,3.293,0,0,1,3.509-3.235H64.128a3.281,3.281,0,1,1,.027,6.551Z" transform="translate(-34.77 -95.671)" fill={router.pathname == "/users"  ? "#ffffff"  : "#000000"}/>
                          </g>
                        </g>
                      </svg>
                    </NavLinkActive>
                  </NavLi>
                  <NavLi>
                    <NavLinkActive onClick={() => router.push("/donation-setting")} className="cursor-pointer" active={router.pathname == "/donation-setting" ? true : false}>
                      <svg viewBox="0 0 24 24" width="20" height="18"data-testid="AttachMoneyOutlinedIcon"><path fill={router.pathname == "/donation-setting"  ? "#ffffff"  : "#000000"} d="M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z"></path></svg>
                    </NavLinkActive>
                  </NavLi>
                  <NavLi>
                    <NavLinkActive onClick={() => router.push("/upload-audio")} className="cursor-pointer" active={router.pathname == "/upload-audio" ? true : false}>
                      <svg viewBox="0 0 24 24" width="20" height="18"><path fill={router.pathname == "/upload-audio"  ? "#ffffff"  : "#000000"} d="M12 3v9.28c-.47-.17-.97-.28-1.5-.28C8.01 12 6 14.01 6 16.5S8.01 21 10.5 21c2.31 0 4.2-1.75 4.45-4H15V6h4V3h-7z"></path> </svg>
                    </NavLinkActive>
                  </NavLi>
                  <NavLi>
                    <NavLinkActive onClick={() => router.push("/reported-video")} className="cursor-pointer" active={router.pathname == "/reported-video" ? true : false}>
                      <svg width="20" height="18" viewBox="0 0 24 24"> <path fill={router.pathname == "/" ? "#ffffff" : "#000000"} d="M21 3H3c-1.11 0-2 .89-2 2v12c0 1.1.89 2 2 2h5v2h8v-2h5c1.1 0 1.99-.9 1.99-2L23 5c0-1.11-.9-2-2-2zm0 14H3V5h18v12zm-5-6-7 4V7z"></path></svg>
                    </NavLinkActive>
                  </NavLi>
                  <NavLi>
                    <NavLinkActive onClick={() =>router.push("/notification/view-notification")} className="cursor-pointer" active={ router.pathname == "/notification/view-notification"   ? true   : false}>
                      <svg width="20" height="18" viewBox="0 0 24 24"><path fill={router.pathname == "/notification/view-notification"  ? "#ffffff"  : "#000000"} d="M10 20h4c0 1.1-.9 2-2 2s-2-.9-2-2zm4-11c0 2.61 1.67 4.83 4 5.66V17h2v2H4v-2h2v-7c0-2.79 1.91-5.14 4.5-5.8v-.7c0-.83.67-1.5 1.5-1.5s1.5.67 1.5 1.5v.7c.71.18 1.36.49 1.95.9C14.54 6.14 14 7.51 14 9zm10-1h-3V5h-2v3h-3v2h3v3h2v-3h3V8z"></path></svg>
                    </NavLinkActive>
                  </NavLi>
                  <NavLi>
                    <NavLinkActive onClick={() =>router.push("/verification/verified-users")} className="cursor-pointer" active={router.pathname == "/verification/verified-users"  ? true  : false}>
                      <svg width="20" height="18" viewBox="0 0 24 24"><path fill={router.pathname == "/verification/verified-users"  ? "#ffffff"  : "#000000"} d="M12 1 3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm-2 16-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z"></path></svg>
                    </NavLinkActive>
                  </NavLi>
                  <NavLi>
                    <NavLinkActive onClick={() => router.push("/change-password")} className="cursor-pointer" active={router.pathname == "/change-password" ? true : false}>
                      <svg viewBox="0 0 24 24" width="20" height="18" fill={router.pathname == "/change-password"  ? "#ffffff"  : "#000000"} ><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"></path></svg>
                    </NavLinkActive>
                  </NavLi>
                  <NavLi>
                    <NavLinkActive onClick={() => router.push("/general-setting")} className="cursor-pointer" active={router.pathname == "/general-setting" ? true : false}>
                      <svg viewBox="0 0 24 24" width="20" height="18" fill={router.pathname == "/general-setting"? "#ffffff": "#000000"}><path d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"></path></svg>                    
                    </NavLinkActive>
                  </NavLi>

                  <NavLi onClick={this.handleLogout}>
                    <NavLinkActive>
                      <svg viewBox="0 0 24 24" width="20" height="18"><path d="m17 7-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z"></path></svg>
                    </NavLinkActive>
                  </NavLi>
                </div>
              </>
            ) : (
              <>
                {/* <NavLi>
                  <NavLinkActive onClick={() =>this.setState({isOpen: !isOpen,})} className="cursor-pointer">
                    <svg viewBox="0 0 48 48"  width="20" height="18" xmlns="http://www.w3.org/2000/svg"><path d="M0 0h48v48h-48z" fill="none" /><path d="M40 22h-24.34l11.17-11.17-2.83-2.83-16 16 16 16 2.83-2.83-11.17-11.17h24.34v-4z" /></svg>
                    <span className="font-size-0-80rem">Collapse</span>
                  </NavLinkActive>
                </NavLi> */}

                <NavLi>
                  <NavLinkActive onClick={() => router.push("/dashboard")} className="cursor-pointer" active={router.pathname == "/dashboard" ? true : false}>
                    <svg width="20" height="18" viewBox="0 0 100 100"><defs><clipPath id="clip-_2"><rect width="100" height="100" /></clipPath></defs>
                      <g clipPath="url(#clip-_2)">
                        <g transform="translate(5.767 5.767)">
                          <path d="M0,20.123V4.685C0,1.562,1.587,0,4.71,0H35.537c3.173,0,4.735,1.562,4.735,4.76V35.487c0,3.224-1.562,4.76-4.81,4.76H4.836C1.511,40.247,0,38.71,0,35.386ZM8.135,8.11V32.137H32.162V8.11Z" transform="translate(0)" fill={router.pathname == "/dashboard"  ? "#ffffff"  : "#000000"}/> <path d="M211.823,0h15.464c3.073,0,4.659,1.587,4.659,4.634v31c0,3-1.612,4.609-4.584,4.609h-31.1c-2.922,0-4.559-1.612-4.559-4.533V4.533c0-2.871,1.637-4.508,4.483-4.508Q204-.013,211.823,0ZM199.785,32.137h24.027V8.11H199.785Z" transform="translate(-143.419)" fill={ router.pathname == "/dashboard"   ? "#ffffff"   : "#000000"} /> <path d="M40.247,211.8v15.363c0,3.148-1.562,4.71-4.685,4.71H4.659c-3.047,0-4.659-1.587-4.659-4.634v-31c0-3,1.612-4.609,4.584-4.634h31.1c2.972,0,4.584,1.612,4.584,4.634C40.272,201.422,40.247,206.611,40.247,211.8Zm-8.11,11.963V199.735H8.11v24.027Z" transform="translate(0 -143.344)" fill={router.pathname == "/dashboard"  ? "#ffffff"  : "#000000"}/> <path d="M212.408,231.871a20.119,20.119,0,1,1,19.569-20.7A20.093,20.093,0,0,1,212.408,231.871Zm-.5-32.162A12.064,12.064,0,1,0,223.968,211.7,12.06,12.06,0,0,0,211.9,199.709Z" transform="translate(-143.474 -143.369)" fill={ router.pathname == "/dashboard"   ? "#ffffff"   : "#000000"}/>
                        </g>
                      </g>
                    </svg>
                    <span className="font-size-0-80rem">Dashboard</span>
                  </NavLinkActive>
                </NavLi>

                <NavLi>
                  <NavLinkActive onClick={() => router.push("/video-categories")} className="cursor-pointer" active={router.pathname == "/video-categories" ? true : false}>
                  <svg width="20" height="18"  focusable="false" aria-hidden="true" viewBox="0 0 24 24" data-testid="ViewListIcon"><path fill={ router.pathname == "/video-categories"   ? "#ffffff"   : "#000000"} d="M3 14h4v-4H3v4zm0 5h4v-4H3v4zM3 9h4V5H3v4zm5 5h13v-4H8v4zm0 5h13v-4H8v4zM8 5v4h13V5H8z"></path></svg>
                    <span className="font-size-0-80rem">Categories</span>
                  </NavLinkActive>
                </NavLi>

                <NavLi>
                  <NavLinkActive onClick={() => router.push("/users")} className="cursor-pointer" active={router.pathname == "/users" ? true : false}>
                    <svg width="20" height="18" viewBox="0 0 100 100"> <defs> <clipPath id="clip-_3"><rect width="100" height="100" /></clipPath> </defs>
                      <g clipPath="url(#clip-_3)">
                        <g transform="translate(4.058 7.487)">
                          <path d="M55.866,68.942v9.841c2.22,0,4.413-.027,6.579,0a3.268,3.268,0,0,1,.6,6.469,4.227,4.227,0,0,1-1.014.082h-32.1c-2.166,0-3.673-1.343-3.673-3.289s1.48-3.262,3.673-3.262h6.113V68.942H10.609C4.276,68.942,0,64.665,0,58.333v-47.7C0,4.276,4.249,0,10.609,0H81.277c6.414,0,10.636,4.249,10.636,10.691V58.278c0,6.414-4.249,10.663-10.663,10.663H55.866ZM45.916,62.39H81.2c2.878,0,4.167-1.261,4.167-4.112V10.663c0-2.8-1.288-4.112-4.057-4.112h-70.7c-2.741.027-4.03,1.316-4.03,4.112V58.278c0,2.823,1.288,4.112,4.139,4.112ZM42.736,69v9.759h6.442V69Z" fill={ router.pathname == "/users"   ? "#ffffff"   : "#000000"}/><path d="M179.832,63.017h6.387c.959,5.455,4.249,8.443,8.909,8.141a8.266,8.266,0,0,0,7.593-7.675c.247-4.66-2.769-7.922-8.141-8.8V48.351c4.167-.493,9.814,2.3,12.61,6.963a14.77,14.77,0,0,1-18.257,21.3C183.423,74.366,179.448,68.445,179.832,63.017Z" transform="translate(-130.518 -35.056)" fill={router.pathname == "/users" ? "#ffffff" : "#000000"}/> <path d="M66.037,179.668H80.4c2.193,0,3.673,1.316,3.7,3.262a3.335,3.335,0,0,1-3.646,3.317H51.646A3.335,3.335,0,0,1,48,182.93c.027-1.946,1.508-3.262,3.7-3.262C56.47,179.64,61.24,179.668,66.037,179.668Z" transform="translate(-34.842 -130.408)" fill={router.pathname == "/users"? "#ffffff": "#000000"}/> <path d="M57.8,138.352h-6.36a3.333,3.333,0,0,1-3.536-3.317,3.293,3.293,0,0,1,3.509-3.235H64.128a3.281,3.281,0,1,1,.027,6.551Z" transform="translate(-34.77 -95.671)" fill={router.pathname == "/users"? "#ffffff": "#000000"}/>
                        </g>
                      </g>
                    </svg>
                    <span className="font-size-0-80rem">User List</span>
                  </NavLinkActive>
                </NavLi>

                <NavLi>
                  <NavLinkActive onClick={() => router.push("/donation-setting")} className="cursor-pointer" active={router.pathname == "/donation-setting" ? true : false}>
                    <svg viewBox="0 0 24 24" width="20" height="18"> <path fill={router.pathname == "/donation-setting"? "#ffffff": "#000000"} d="M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z"></path> </svg>
                    <span className="font-size-0-80rem">Donation Setting</span>
                  </NavLinkActive>
                </NavLi>

                <NavLi>
                  <NavLinkActive onClick={() => {this.toogleBar("donation");}} className="cursor-pointer" active={router.pathname == "/donation-reports" ? true : false}>
                  <svg width="20" height="18"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 400 400"
                    id="vector">
                    <path
                        id="path"
                        d="M 124.25 168.9 C 120.55 172 116.25 173.2 110.15 173.8 L 110.15 140.8 C 117.45 143.2 122.35 145.7 125.45 148.1 C 127.85 150.5 129.75 154.2 129.75 158.5 C 129.151 162.8 127.351 166.4 124.25 168.9 Z M 97.951 82.6 C 92.451 83.2 88.151 85 85.051 87.5 C 81.951 90.6 80.151 94.2 80.151 97.9 C 80.151 102.2 81.351 105.2 83.851 107.7 C 86.251 110.1 91.151 112.6 97.951 115 L 97.951 82.6 Z M 385.55 152.4 L 385.55 355.6 C 385.55 380.7 365.351 400.901 340.251 400.901 L 60.651 400.901 C 35.551 400.901 15.351 380.7 15.351 355.6 L 15.351 45.3 C 15.351 20.2 35.551 0 60.651 0 L 235.651 0 C 253.35 0 261.95 4.9 272.95 16.5 L 368.45 113.2 C 381.251 125.4 385.55 133.4 385.55 152.4 Z M 184.25 107.1 L 251.549 107.1 C 259.549 107.1 265.651 101 265.651 93.6 L 265.651 83.2 C 265.651 75.9 259.549 69.7 251.549 69.7 L 184.25 69.7 L 184.25 107.1 Z M 97.951 137.7 L 97.951 173.8 C 86.951 172 76.551 167.1 66.151 157.9 L 52.651 173.8 C 66.151 185.4 81.451 192.2 97.951 194 L 97.951 207.4 L 109.551 207.4 L 109.551 193.9 C 122.451 193.3 132.151 189.6 140.151 182.9 C 147.451 176.2 151.751 167 151.751 156.6 C 151.751 145.6 148.651 137.6 141.951 132.1 C 135.251 126.6 124.851 121.7 110.751 118.6 L 110.151 118.6 L 110.151 83.7 C 119.951 84.9 128.551 89.2 136.451 94.7 L 148.651 77.6 C 136.451 69.6 123.551 64.7 110.051 64.1 L 110.051 53.7 L 98.451 53.7 L 98.451 62.9 C 86.851 63.5 77.051 67.2 69.651 73.9 C 62.351 80.6 58.051 89.8 58.051 100.2 C 58.051 110.6 61.751 118.6 67.851 124.7 C 74.051 129.7 83.851 134.6 97.951 137.7 Z M 349.45 336 C 349.45 328.699 343.35 322.5 335.35 322.5 L 64.25 322.5 C 56.25 322.5 50.75 328.6 50.75 336 L 50.75 346.4 C 50.75 353.699 56.85 359.9 64.25 359.9 L 335.35 359.9 C 342.651 359.9 349.45 353.8 349.45 346.4 L 349.45 336 Z M 349.45 251.5 C 349.45 244.199 343.35 238 335.35 238 L 64.25 238 C 56.25 238 50.75 244.1 50.75 251.5 L 50.75 261.9 C 50.75 269.199 56.85 275.4 64.25 275.4 L 335.35 275.4 C 342.651 275.4 349.45 269.3 349.45 261.9 L 349.45 251.5 Z M 349.45 167 C 349.45 159.7 343.35 153.5 335.35 153.5 L 184.25 153.5 L 184.25 190.8 L 335.45 190.8 C 342.751 190.8 349.55 184.7 349.55 177.3 L 349.55 167 L 349.45 167 Z"
                        fill="#000000"
                        stroke-width="1"/>
                </svg>
                    
                    
                    
                    {/* <svg viewBox="0 0 24 24" width="20" height="18"><path fill={ router.pathname == "/donation-reports"   ? "#ffffff"   : "#000000"} d="M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z"></path></svg> */}
                    <ToogleSubMenu>
                      <span className="font-size-0-80rem">Donation Reports</span>
                      {toogleNav.donation ? (
                        <svg viewBox="0 0 24 24" width="20" height="18"><path d="m7 10 5 5 5-5z"></path></svg>
                      ) : (
                        <svg viewBox="0 0 24 24" width="20" height="18"><path d="m10 17 5-5-5-5v10z"></path></svg>
                      )}
                    </ToogleSubMenu>
                  </NavLinkActive>
                </NavLi>
                {toogleNav.donation && (
                  <SubNav>
                    <NavLinkActive onClick={() => router.push("/donations/donation-received")} className="cursor-pointer"  active={router.pathname == "/donations/donation-received" ? true : false}>
                      <span className="font-size-0-80rem">Donation Received</span>
                    </NavLinkActive>

                    <NavLinkActive  onClick={() => router.push("/donations/admin-commission")}
                      className="cursor-pointer"  active={router.pathname == "/donations/admin-commission" ? true : false }>
                      <span className="font-size-0-80rem">Admin Commission</span>
                    </NavLinkActive>

                    <NavLinkActive className="cursor-pointer" active={ router.pathname == "/donations/pending-transactions" ? true : false} onClick={()=>router.push("/donations/pending-transactions")}>
                      <span className="font-size-0-80rem">Pending Transactions</span>
                    </NavLinkActive>

                    <NavLinkActive className="cursor-pointer" active={ router.pathname == "/donations/transfer-transactions" ? true : false} onClick={()=>router.push("/donations/transfer-transactions")}>
                      <span className="font-size-0-80rem" >Transferred Transactions</span>
                    </NavLinkActive>

                    <NavLinkActive  className="cursor-pointer" active={ router.pathname == "/donations/withdraw-requests" ? true : false} onClick={()=>router.push("/donations/withdraw-requests")}>
                      <span className="font-size-0-80rem" >Withdrawal Requests</span>
                    </NavLinkActive>
                  </SubNav>
                )}

                <NavLi>
                  <NavLinkActive onClick={() => {this.toogleBar("audio")}} className="cursor-pointer">
                    <svg viewBox="0 0 24 24" width="20" height="18"><path fill={"#000000"} d="M12 3v9.28c-.47-.17-.97-.28-1.5-.28C8.01 12 6 14.01 6 16.5S8.01 21 10.5 21c2.31 0 4.2-1.75 4.45-4H15V6h4V3h-7z"></path></svg>
                    <ToogleSubMenu>
                      <span className="font-size-0-80rem">Audio</span>
                      {toogleNav.audio ? (
                        <svg viewBox="0 0 24 24" width="20" height="18"><path d="m7 10 5 5 5-5z"></path></svg>
                      ) : (
                        <svg viewBox="0 0 24 24" width="20" height="18"><path d="m10 17 5-5-5-5v10z"></path></svg>
                      )}
                    </ToogleSubMenu>
                  </NavLinkActive>
                </NavLi>
                {toogleNav.audio && (
                  <SubNav>
                    <NavLinkActive onClick={() => router.push("/audio/upload-audio")} className="cursor-pointer" active={router.pathname == "/audio/upload-audio" ? true : false}>
                      <span className="font-size-0-80rem">Upload Audio</span>
                    </NavLinkActive>
                    <NavLinkActive onClick={() => router.push("/audio/audio-list")} className="cursor-pointer" active={router.pathname == "/audio/audio-list" ? true : false}>
                      <span className="font-size-0-80rem">Audio List</span>
                    </NavLinkActive>
                  </SubNav>
                )}

                <NavLi>
                  <NavLinkActive onClick={() => {this.toogleBar("video")}} className="cursor-pointer">
                   <svg width="20" height="18" viewBox="0 0 24 24"><path  fill={"#000000"} d="M21 3H3c-1.11 0-2 .89-2 2v12c0 1.1.89 2 2 2h5v2h8v-2h5c1.1 0 1.99-.9 1.99-2L23 5c0-1.11-.9-2-2-2zm0 14H3V5h18v12zm-5-6-7 4V7z"></path></svg>
                    <ToogleSubMenu>
                      <span className="font-size-0-80rem">Reported Video</span>
                      {toogleNav.video ? (
                        <svg viewBox="0 0 24 24" width="20" height="18"><path d="m7 10 5 5 5-5z"></path></svg>
                      ) : (
                        <svg viewBox="0 0 24 24" width="20" height="18"><path d="m10 17 5-5-5-5v10z"></path></svg>
                      )}
                    </ToogleSubMenu>
                  </NavLinkActive>
                </NavLi>
                {toogleNav.video && (
                  <SubNav>
                    <NavLinkActive onClick={() => router.push("/reported-video")} className="cursor-pointer" active={router.pathname == "/reported-video" ? true : false}>
                      <span className="font-size-0-80rem"> Get All Reported Video </span>
                    </NavLinkActive>
                  </SubNav>
                )}

                <NavLi>
                  <NavLinkActive onClick={() => {this.toogleBar("notification")}} className="cursor-pointer">
                  <svg width="20" height="18" class="MuiSvgIcon-root MuiSvgIcon-fontSizeMedium MuiBox-root css-1om0hkc" focusable="false" aria-hidden="true" viewBox="0 0 24 24" data-testid="NotificationsIcon"><path fill="#000000"  d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"></path></svg>
                    <ToogleSubMenu>
                      <span className="font-size-0-80rem">Notification</span>
                      {toogleNav.notification ? (
                        <svg viewBox="0 0 24 24" width="20" height="18"><path d="m7 10 5 5 5-5z"></path></svg>
                      ) : (
                        <svg viewBox="0 0 24 24" width="20" height="18"><path d="m10 17 5-5-5-5v10z"></path></svg>
                      )}
                    </ToogleSubMenu>
                  </NavLinkActive>
                </NavLi>
                {toogleNav.notification && (
                  <SubNav>
                    <NavLinkActive onClick={() => router.push("/notification/view-notification")} className="cursor-pointer" active={router.pathname == "/notification/view-notification" ? true : false}>
                      <span className="font-size-0-80rem">Push Notification</span>
                    </NavLinkActive>
                    <NavLinkActive onClick={() => router.push("/notification/notification-log")} className="cursor-pointer" active={router.pathname == "/notification/notification-log" ? true : false}>
                      <span className="font-size-0-80rem">Notification Logs</span>
                    </NavLinkActive>
                  </SubNav>
                )}

                <NavLi>
                  <NavLinkActive onClick={() => router.push("/verification/verified-users")} className="cursor-pointer" active={router.pathname == "/verification/verified-users"? true: false}>
                    <svg width="20" height="18" viewBox="0 0 24 24"><path fill={ router.pathname == "/verification/verified-users" ? "#ffffff" : "#000000"} d="M12 1 3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm-2 16-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z" ></path></svg>
                    <span className="font-size-0-80rem">Verified User</span>
                  </NavLinkActive>
                </NavLi>

                <NavLi>
                  <NavLinkActive onClick={() => router.push("/change-password")} className="cursor-pointer" active={router.pathname == "/change-password" ? true : false}>
                    <svg  viewBox="0 0 24 24"  width="20"  height="18" fill={  router.pathname == "/change-password"  ? "#ffffff"  : "#000000"}><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"></path></svg>
                    <span className="font-size-0-80rem">Change Password</span>
                  </NavLinkActive>
                </NavLi>

                <NavLi>
                  <NavLinkActive onClick={() => router.push("/general-setting")} className="cursor-pointer" active={router.pathname == "/general-setting" ? true : false}>
                    <svg viewBox="0 0 24 24" width="20" height="18" fill={router.pathname == "/general-setting"? "#ffffff": "#000000"}><path d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"></path></svg>
                    <span className="font-size-0-80rem">General Settings</span>
                  </NavLinkActive>
                </NavLi>

                <NavLi onClick={this.handleLogout} className="cursor-pointer">
                  <NavLinkActive>
                    <svg viewBox="0 0 24 24" width="20" height="18"><path d="m17 7-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z"></path></svg>
                    <span className="font-size-0-80rem  ">Logout</span>
                  </NavLinkActive>
                </NavLi>
              </>
            )}
          </UserSideBar>
          <SideBarOpacity open={isOpen} className="hide" />
          <Content className="sidebar-content">{this.props.children}</Content>
        </Container>
      </>
    );
  }
}

export default withRouter(SideBar);
