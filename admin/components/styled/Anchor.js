import styled from 'styled-components';
import colors from './../../colors';
const Anchor = styled.a`
    margin: ${props => props.margin || '0'};
    padding: ${props => props.padding || '0'};
    color: ${props => props.color || colors.text};
    font-size: ${props => props.size || '14px'};
    font-weight: ${props => props.weight || '400'};
    line-height: ${props => props.height || '1.5em'};
    text-align: ${props => props.textAlign || 'inherit'};
    transition: background-color 0.2s;
    :hover {
        color: ${props => props.colorHover || colors.primary};
    }
`;
export default Anchor;