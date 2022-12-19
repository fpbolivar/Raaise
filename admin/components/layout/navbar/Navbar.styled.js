import Anchor from "./../../styled/Anchor";
import FlexContainer from "./../../styled/FlexContainer";
import styled from "styled-components";
import colors from "../../../colors";

export const Wrapper=styled.div`
background-color:black;
`;
export const Container = styled.div`
    max-width: 1440px;
    height: 100%;
    padding: 10px 0;
    margin: 0 auto;
    display: flex;
    align-items: center;
    box-sizing: border-box;
    justify-content: space-between;
    @media (max-width: 1499px) {
        padding: 10px 30px;
    }
`;
export const Logo = styled.a`
    width: 200px;
    @media (max-width: 767px) {
        width: auto;
    }
`;
export const LogoImage = styled.img`
    width: 130px;
`;
export const Navigation = styled.div`
    padding: 10px;
    box-sizing: border-box;
    @media (max-width: 767px) {
        position: absolute;
        display: none;
        left: 0;
        right: 0;
        top: calc(100% - 10px);
        background: ${colors.whiteF6};
        text-align: left;
        padding: 20px;
        box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
        border-radius: 0;
        z-index: 1;
        &.show {display: block;}
    }
`;
export const FlexNavigation = styled(FlexContainer)`
    column-gap: 30px;
    align-items: center;
    @media (max-width: 889px) {
        column-gap: 15px;
    }
`;
export const NavLink = styled(Anchor)`
    cursor: pointer;
    line-height: 40px;
    font-size: 17px;
    font-weight: 500;
    color: ${colors.white};
    :hover {color: ${colors.blueColor};text-decoration: none;}
    :active {color: ${colors.blueColor};text-decoration: none;}
    :focus {color: ${colors.blueColor};text-decoration: none;}
    & .admin-section{display:flex;column-gap: 10px;}
    & .admin-img{width: 40px;height: 40px; border-radius: 40px;}
`;
