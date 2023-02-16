import React from "react";
import PropTypes from "prop-types";
import "react-quill/dist/quill.snow.css";
import dynamic from "next/dynamic";
const ReactQuill = dynamic(() => import("react-quill"), { ssr: false });
class TextEditor extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            editorHtml: props.value || "",
            theme: "snow",
        };
        this.handleChange = this.handleChange.bind(this);
    }
    handleChange(html) {
        let { handleChange } = this.props;
        if (html.replace(/<[^>]*>/g, "").length == 0) {
            this.setState({ editorHtml: "" });
            handleChange("");
            return;
        }else {
            this.setState({ editorHtml: html });
            handleChange(html);
        }
    }
    render() {
        const { editorHtml, theme } = this.state;
        const { placeholder, value } = this.props;
        return (
            <ReactQuill theme={theme} onChange={this.handleChange} value={value} modules={TextEditor.modules} bounds={".app"} placeholder={placeholder} />
        );
    }
}
TextEditor.modules = {
    toolbar: [
        [{ header: '1' }, { header: '2' }],
        [{ size: [] }],
        ["bold", "italic", "underline", "strike"],
        [{ list: "ordered" }, { list: "bullet" }],
    ],
    clipboard: { matchVisual: false },
};
TextEditor.formats = ["header", "font","size", "bold","italic","underline","strike", "blockquote","list","bullet","indent", "link", "image", "video",];
TextEditor.propTypes = {
    placeholder: PropTypes.string,
    handleChange: PropTypes.func,
    maxLength: PropTypes.string,
    value: PropTypes.string,
};
export default TextEditor;
