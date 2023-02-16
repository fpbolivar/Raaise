import React from "react";
import { Modal, ModalContent, ModalFooter } from "./CustomModal.styled";
import CustomModal from "./CustomModal";

class WithdrawModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            responseData: [],
            columnsFilters: {},
            pageNumber: 1,
            dataLimit: 10,
            // openModal: {
            //     open: false,
            //     title: "",
            //     description: "",
            //     id: "",
            //     action: "",
            // },
        };
    }

    // //to adminWithdrawRequest 
    // payWithdrawalAmount = async (id, status) => {
    //     // const { videoPageNo, videolimit } = this.state;
    //     try {
    //       const { data } = await axios.post( `${DONATION.payWithdrawalAmount}`, {
    //         requestId: id,
    //       });
    //       if (data.status == 200) {
    //         // this.getVideoCategories({
    //         //   pageNo: videoPageNo,
    //         //   limit: videolimit,
    //         // });
    //       }
    //     } catch (e) {
    //       console.log("error", e);
    //     }
    // };

    // // to open pay money modal
    // modalAction = (status) => {
    //     const { openModal } = this.state;
    //     if (status) {
    //       if (openModal.id && openModal.action === "pay") { //to pay the money
    //         // this.payWithdrawalAmount(openModal.id);
    //       }
    //       this.setState({
    //         openModal: {
    //         open: false,
    //         },
    //       });
    //     } else {
    //       this.setState({
    //       openModal: {
    //       open: status,
    //       },
    //       });
    //     }
    //   };


    render() {
    const {title,description,modalAction,closeButton,buttontitle} = this.props;
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

export default WithdrawModal;
