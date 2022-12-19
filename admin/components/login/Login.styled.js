import styled from "styled-components";
import colors from "../../colors";

export const Container = styled.div`
    width: 100vw;
    height: 100vh;
    background-color: #f1f2f5;
    display: flex;
    padding: 20px;

    /* Tablet */
    @media only screen and (max-width: 768px) {
        flex-direction: column;
        overflow-y: scroll;
    }
`;
export const Wrapper = styled.div`
    width: 50%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;

    /* TABLET */
    @media only screen and (max-width: 768px) {
        width: 100%;
        justify-content: center;
        align-items: center;
        padding: 30px;
    }

    /* MOBILE */
    @media only screen and (max-width: 460px) {
        padding: 10px;
    }
`;

export const FormWrapper = styled.div`
    width: 100%;
`;

export const Form = styled.form`
    width: 540px;
    background-color: #fff;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 25px;
    padding: 40px 30px;

    /* LAPTOP */
    @media only screen and (max-width: 1115px) {
        width: 100%;
    }
`;
export const LoginBtn = styled.button`
    width: 100%;
    height: 56px;
    background-color: ${colors.blueColor};
    color: #fff;
    font-size: 15px;
    font-weight: 600;
    letter-spacing: 0.27px;
    line-height: 26px;
    border-radius: 5px;
    border: 1px ${colors.blueColor} solid;
    box-shadow: 0px 2px 0px ${colors.blueColor};
    cursor: pointer;
`;

export const ContentWrapper = styled.div`
  display:flex;flex:1;justify-content:center;align-items:center;
  & img{max-width:450px;width:100%;}
`;

