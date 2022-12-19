import styled from "styled-components";

const FlexContainer = styled.div`
    width: ${(props) => props.width || "unset"};
    height: ${(props) => props.height || "unset"};

    display: flex;
    flex-direction: ${(props) => props.direction || "unset"};
    flex-wrap: ${(props) => props.wrap || "nowrap"};
    flex: ${(props) => props.flex || "unset"};

    justify-content: ${(props) => props.justify || "unset"};
    align-items: ${(props) => props.align || "unset"};

    padding: ${(props) => props.padding || "0"};
    margin: ${(props) => props.margin || "0"};

    background: ${(props) => props.background || "none"};

    word-break: ${(props) => props.wordBreak || "inherit"};

    column-gap: ${(props) => props.columnGap || "inherit"};
    row-gap: ${(props) => props.rowGap || "inherit"};
`;

export default FlexContainer;
