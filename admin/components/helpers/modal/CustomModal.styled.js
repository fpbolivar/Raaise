import styled from "styled-components";
import colors from "../../../colors"
export const Modal = styled.div`
  position: fixed;
  z-index: 1;
  padding:20px;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  background-color: rgb(0, 0, 0);
  background-color: rgba(0, 0, 0, 0.4);
`;

export const ModalContent = styled.div`
  background-color:${colors.white};
  margin: auto;
  padding: 25px;
  border: 1px solid #888;
  width: ${(props) => props.width || "40%"};
  height:${(props) => props.height };
  overflow-y:auto;
  position: absolute;
  top: ${(props) => props.top || "120px"};
  left: ${(props) => props.left || "0px"};
  right: 0;
  border-radius: 6px;
  .title {justify-content: center;display: flex;font-size: 24px;font-weight: 700;margin-bottom: 10px;}
  .description {color: #878787;padding: 15px;font-size: 16px;}
  .image {width: 100%;height: 300px;object-fit: contain;margin-top: 10px;}
  .audio {width: 100%;height: 250px;margin-top: 10px;}
  &.video{height: 200px;overflow-y: auto;}
  & .img-round {width: 120px;height: 120px;object-fit: cover;border-radius: 50%;}

`;
export const ModalFooter = styled.div`
  display: flex;
  justify-content: end;
  padding-top:10px ;
  column-gap:15px;
  .btn-blue { border: none;color: ${colors.white}; font-size: 16px; background-color: ${colors.blueColor}; padding: 10px 24px;border-radius: 8px;cursor: pointer; }
`;

export const CloseButton = styled.div`
  display: flex;
  justify-content: space-between;
  height: 40px;
  border-bottom: 1px solid ${colors.gray1};
  .btn {width: 30px;height: 30px;cursor: pointer;}
`;

