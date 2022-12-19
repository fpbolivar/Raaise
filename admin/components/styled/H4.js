import styled from 'styled-components';
import colors from './../../colors';

const H4 = styled.h4`
    padding: ${props => props.padding || '0'};
    margin: ${props => props.margin || '0'};

    font-size: ${props => props.size || '14px'};
    font-weight: ${props => props.weight || '400'};
    color: ${props => props.color || colors.text};
`;

export default H4;