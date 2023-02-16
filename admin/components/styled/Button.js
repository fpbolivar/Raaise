import styled from 'styled-components';
import colors from './../../colors';

const Button = styled.button`
    padding: ${props => props.padding || '10px 10px'};
    margin: ${props => props.margin || '0'};
    color: ${props => props.color || 'white'};
    font-size: ${props => props.size || '14px'};
    font-weight: ${props => props.weight || '400'};
    font-family: inherit;
    outline: none;
    border: 0;
    border-radius: ${props => props.radius || '5px'};
    background: ${props => props.background || colors.primary};
    transition: background-color 0.2s;
    :hover {
        color: ${props => props.colorHover || 'white'};
        background: ${props => props.backgroundHover || colors.secondary};
        cursor: pointer;
    }
`;
export default Button;