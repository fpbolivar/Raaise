import styled from "styled-components";
import colors from "../colors";

export const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    width: 100%;
    position: relative;
`;

export const Error = styled.span`
    text-align: right !important;
    color: ${colors.red};
    font-size: 13px;
    display: flex !important;
    margin-top: 5px;
`;

export const Input = styled.input`
    width: 100%;
    height: 56px;
    padding: 14px;
    font-size: 14px;
    font-weight: 600;
    color: #3d3b48;
    border-radius: 5px;
    border:${(props) => props.type == "file" ? `2px dotted ${colors.gray1}`  : `2px ${colors.blueColor} solid !important` }  ;
    &:focus{outline: ${colors.blueColor}  !important;}
    
    &.invalid ~ ${Error} {
        display: ${(props) => props.focused && "block"};
    }

    &.invalid {
        border: ${(props) => props.focused && `2px #4C75A3 solid`};
        background-position: bottom 15px right 20px;
    }
    &:password-hide {
        background: ${(props) =>   props.focused &&   `url(/assets/images/close-eye-icon.svg) no-repeat`};
        background-position: bottom 15px right 20px;
    }
    &.valid {
        border:${(props) => props.type == "file" ? `2px dotted ${colors.gray1}`  : `2px ${colors.blueColor} solid !important` };
        // margin-bottom:20px;
    }

    &.focus {
        outline: 2px solid transparent;
    }
`;

export const PasswordVisibleIcon = styled.div`
    background: url(/assets/icons/eye-icon.svg) no-repeat;
    position: absolute;
    right: 11px;
    top: 18px;
    height: 16px;
    width: 16px;
    cursor: pointer;
`;
export const PasswordInVisibleIcon = styled.div`
    background: url(/assets/icons/close-eye-icon.svg) no-repeat;
    position: absolute;
    right: 11px;
    top: 18px;
    height: 16px;
    width: 16px;
    cursor: pointer;
`;
