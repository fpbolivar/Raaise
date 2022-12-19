import styled from 'styled-components';
import colors from './../../colors';
const Input = styled.input`
    width: ${props => props.width || 'auto'};
    height: ${props => props.height || '40px' };
    padding: ${props => props.padding || '0 16px' };
    margin: ${props => props.margin || '0' };
    border: 1px solid ${colors.lightGray};
    border-radius: 5px;
    outline: none;
    transition: all 0.33s ease;
    font-family: inherit;
    :focus {
        border: 1px solid ${colors.focusHover};
    }
`;
export default Input;