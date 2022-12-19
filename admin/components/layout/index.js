import React from "react";
import Navbar from "./navbar/Navbar";
import SideBar from "./sidebar";
const Layout = (props) => (
    <React.Fragment>
        <Navbar />
        <SideBar {...props} />
    </React.Fragment>
    
);
export default Layout;
