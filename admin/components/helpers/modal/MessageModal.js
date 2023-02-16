import React from "react";
import { Modal, ModalContent, ModalFooter } from "./CustomModal.styled";

class MessageModal extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        const {msgtitle,message,modalAction,closeButton,buttontitle} = this.props;
        return (
            <>
                <Modal>
                    <ModalContent left="44px" top="129px">
                        {msgtitle && (
                            <>
                                <span className="title" >{msgtitle}</span>
                                <hr style={{ color: "rgb(234 221 221)" }} />
                            </>
                        )}
                        {message && (
                            <>
                                <p className="description">{message}</p>
                                <hr style={{ color: "rgb(234 221 221)" }} />
                            </>
                        )}
                        <ModalFooter>
                            {!closeButton && (
                                <button className="btn-blue" onClick={() => modalAction(false)}> CLOSE </button>
                            )}

                            {buttontitle  ? 
                                <button className="btn-blue" onClick={() => modalAction(true)}> {buttontitle} </button>
                            
                            : (
                                <button className="btn-blue" onClick={() => modalAction(true)}> OK </button>
                            )}
                        </ModalFooter>
                    </ModalContent>
                </Modal>
            </>
        );
    }
}

export default MessageModal;
