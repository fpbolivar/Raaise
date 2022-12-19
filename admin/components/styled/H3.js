import styled from 'styled-components';
import colors from './../../colors';

const H3 = styled.h3`
    padding: ${props => props.padding || '0'};
    margin: ${props => props.margin || '0'};

    font-size: ${props => props.size || '14px'};
    font-weight: ${props => props.weight || '400'};
    color: ${props => props.color || colors.text};
`;

export default H3;