import styled from 'styled-components';
import colors from './../../colors';

const H2 = styled.h2`
    padding: ${props => props.padding || '0'};
    margin: ${props => props.margin || '0'};
    font-size: ${props => props.size || '14px'};
    font-weight: ${props => props.weight || '400'};
    color: ${props => props.color || colors.text};
    line-height: ${props => props.height || '1.3'};
    text-align: ${props => props.align || 'unset'};
`;

export default H2;