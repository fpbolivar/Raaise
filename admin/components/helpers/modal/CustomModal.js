import React from "react";
import { Modal, ModalContent, ModalFooter } from "./CustomModal.styled";

class CustomModal extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        const {title,description,modalAction, closeButton} = this.props;
        return (
            <>
                <Modal>
                    <ModalContent UserModal="UserModal">
                        {title && (
                            <>
                                <span className="title">{title}</span>
                                <hr style={{ color: "rgb(234 221 221)" }} />
                            </>
                        )}
                        {description && (
                            <>
                                <p className="description">{description}</p>
                                <hr style={{ color: "rgb(234 221 221)" }} />
                            </>
                        )}
                        <ModalFooter>
                            {!closeButton && (
                                <button className="btn-blue" onClick={() => modalAction(false)}> CLOSE </button>
                            )}
                            <button className="btn-blue" onClick={() => modalAction(true)}> OK </button>
                        </ModalFooter>
                    </ModalContent>
                </Modal>
            </>
        );
    }
}

export default CustomModal;
