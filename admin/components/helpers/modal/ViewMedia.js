import React from "react";
import { Modal, ModalContent, CloseButton} from "./CustomModal.styled";
class ViewMedia extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const {modalAction, data, type,name } =this.props;
    return (
        <Modal>
          <ModalContent>
            <CloseButton>
              <span className="title">{name}</span>
              <svg className="btn" focusable="false" aria-hidden="true" viewBox="0 0 24 24" data-testid="ClearIcon" fill="red" onClick={() => modalAction(false)}> <path d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"></path></svg>
            </CloseButton>
            {type === "image" && (
              <img src={data} alt="" className="image"/>
            )}
            {type === 'audio' &&
              <iframe allow="autoplay" src={data} className="audio"></iframe>}
            {type === 'video' &&
              <iframe allow="autoplay" src={data} className="audio"></iframe>}
          </ModalContent>
        </Modal>
    );
  }
}

export default ViewMedia;
