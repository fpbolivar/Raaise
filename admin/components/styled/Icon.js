import styled from 'styled-components';
import colors from './../../colors';

const Icon = styled.i`
    margin: ${props => props.margin || '0'};
    padding: ${props => props.padding || '0'};
    color: ${props => props.color || colors.text};
    display: ${props => props.display || ''};
    align-self: ${props => props.alignSelf || ''};
`;

export default Icon;