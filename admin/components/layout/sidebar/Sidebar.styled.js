import styled from "styled-components";
import colors from "../../../colors";

export const Container = styled.div`
  display: flex;
  background-color: ${colors.gray1};
  min-width: 0;
  min-height: calc(100vh - 90px);
`;

export const SideNav = styled.div`
  display: flex;
  min-width: 0;
  flex-direction: column;
  background-color: white;
  padding: 1.1rem;
  opacity: 1;
  x: -100%;
  width: 31px;
`;
export const UserSideBar = styled(SideNav)`
  padding-top: 30px;
  x: ${(props) => (props.open ? 0 : "-100%")};
  width: ${(props) => (props.open ? "20%" : "74px")};
  @media only screen and (max-width: 1000px) {
    flex-basis: ${(props) => (props.open ? "30%" : "74px")};
    position: ${(props) => (props.open ? "absolute" : "relative")};
    width: ${(props) => (props.open ? "30%" : "74px")};
    height: 100vh;
    z-index: 999;
  }
  /* TABLET */
  @media only screen and (max-width: 768px) {
    flex-basis: ${(props) => (props.open ? "40%" : "74px")};
    position: ${(props) => (props.open ? "absolute" : "relative")};
    width: ${(props) => (props.open ? "40%" : "74px")};
    height: 100vh;
    z-index: 999;
  }

  /* MOBILE */
  @media only screen and (max-width: 460px) {
    flex-basis: ${(props) => (props.open ? "50%" : "74px")}; width: ${(props) => (props.open ? "50%" : "74px")};
    height: 100vh; z-index: 999;
  }
  .mobile-list {
    // position: absolute;
  }
`;
export const SideBarOpacity = styled.div`
  @media only screen and (max-width: 768px) {
    flex-basis: ${(props) => (props.open ? "100%" : "0px")};
    position: ${(props) => (props.open ? "absolute" : "relative")};
    width: ${(props) => (props.open ? "100%" : "0px")};
    height: 100vh;
    display: ${(props) => (props.open ? "block" : "none")};
    opacity: 0.2;
    background: rgb(0, 0, 0);
    z-index: 40;
    box-sizing: border-box;
  }

  /* MOBILE */
  @media only screen and (max-width: 460px) {
    flex-basis: ${(props) => (props.open ? "100%" : "0px")};
    position: ${(props) => (props.open ? "absolute" : "relative")};
    width: ${(props) => (props.open ? "100%" : "0px")};
    display: ${(props) => (props.open ? "block" : "none")};
    height: 100vh;
    opacity: 0.2;
    background: rgb(0, 0, 0);
    z-index: 40;
    box-sizing: border-box;
  }
`;

export const Content = styled.div`
  color: #000;
  min-width: 0;
  background-color:${colors.gray1};
  width:100%;
  box-sizing:border-box;
`;

export const CloseButton = styled.div`
  background: url(/assets/icons/back-arrow-icon.svg) no-repeat;
  background-size: 25px;
  width: 25px;
  height: 25px;
  color: #000;
  font-size: 0.7rem;
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
`;

export const Links = styled.ul`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 0;
  padding: 0;
  li {list-style-type: none;}
  a {color: #fff;text-decoration: none;}
`;

export const Hamburger = styled.div`
  background: url(/assets/icons/hamburger.svg) no-repeat;
  background-size: 25px;
  width: 25px;
  height: 25px;
  color: #000;
  font-size: 0.7rem;
  position: relative;
  top: -7px;
  left: 6px;
  cursor: pointer;
`;
export const NavLi = styled.li`
    text-decoration:none
    text-align: center;
    list-style-type: none;
`;

export const SubNav = styled.li`
    text-decoration:none
    text-align: center;
    list-style-type: none;
    padding-left:10px;
    
`;
export const NavLink = styled.a`
  list-style-type: none;
  display: flex;
  padding: 10px;
  text-decoration: none;
  flex-direction: row;
  gap: 10px;
  color: black;
  align-items: center;
  svg {margin-top: 4px;}
`;

export const Select = styled.select`
  list-style-type: none;
  display: flex;
  padding: 10px;
  text-decoration: none;
  flex-direction: row;
  gap: 10px;
  color: black;
  align-items: center;
  svg {margin-top: 4px;}
`;

export const NavLinkActive = styled(NavLink)`
  background: ${(props) => (props.active ? "#4C75A3" : "white")};
  color: ${(props) => (props.active ? "white" : "black")};
  border-radius: ${(props) => (props.active ? "10px" : "10px")};
`;

export const ToogleSubMenu = styled.div`
  display: flex;
  flex-grow: 1;
  justify-content: space-between;
  align-items: center;
`;
