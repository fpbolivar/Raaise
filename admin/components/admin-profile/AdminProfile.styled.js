import styled from "styled-components";
import colors from "../../colors";

export const Wrapper = styled.div`
  width: 100%;
  padding: 20px;
  display: flex;
  justify-content: center;`;
export const Form = styled.form`
  width: 70%;
  background-color: ${colors.white};
  border-radius: 10px;
  align-items: left;
  padding: 30px;
  & .btn-section {width: 100%; text-align: center; margin-top: 25px;}
`;
export const Picture = styled.div`
width: 100%;
display: flex;
justify-content: center;
& img {width: 120px;height: 120px;object-fit: cover;border-radius: 50%;}
`;
export const FileDiv = styled.div`
  display: flex;
  justify-content: center !important;
  width: 100%;
  & label {margin-top: 16px; padding: 9px 20px; background-color:${colors.blueColor};  color:${colors.white}; font-size: 10px; border-radius: 5px;cursor: pointer;}
`;

