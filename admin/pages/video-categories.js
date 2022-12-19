import React from "react";
import withAuth from "../components/Auth";
import Layout from "../components/layout";
import Main from "../components/Videocategories";

class VideoCategories extends React.Component{
    render() {
        return(
            <Layout>
               <Main/>
            </Layout>
        )
    }
}
export default VideoCategories;