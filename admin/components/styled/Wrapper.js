import styled from "styled-components";
import colors from "./../../utils/Colors";

const Wrapper = styled.header`
    width: ${(props) => props.width || "100%"};
    position: ${(props) => props.position || "sticky"};
    top: ${(props) => props.top || "0"};
    left: ${(props) => props.left || "0"};
    background: ${(props) => props.background || colors.white};
    z-index: ${(props) => props.zIndex || "10"};
    @media (max-width: 767px) {
        position: sticky;
    }
    margin: ${(props) => props.margin || "0"};
    padding: ${(props) => props.padding || "0"};
`;

export default Wrapper;
