import styled from 'styled-components';
import colors from './../../colors';
const Text = styled.p`
    width: ${props => props.width || 'auto'};
    padding: ${props => props.padding || '0'};
    margin: ${props => props.margin || '0'};
    font-size: ${props => props.size || '16px'};
    font-weight: ${props => props.weight || '400'};
    color: ${props => props.color || colors.text};
    line-height: ${props => props.height || '1.6'};
    text-align: ${props => props.align || 'unset'};
    :hover {
        color: ${props => props.colorHover || colors.text};
        text-decoration: ${props => props.decorationHover || 'inherit'};
    }
`;

export default Text;