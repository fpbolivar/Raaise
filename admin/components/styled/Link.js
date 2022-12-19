import styled from 'styled-components';
import colors from './../../colors';
const Anchor = styled.a`
    font-size: ${props => props.size || '14px'};
    font-weight: ${props => props.weight || '400'};
    color: ${props => props.color || colors.text};
    padding: ${props => props.padding || '0px'};
    margin: ${props => props.margin || '0px'};
    text-align: ${props => props.align || 'unset'};
    transition: background-color 0.2s;
    background:${props => props.background || ''};
    border-radius:${props => props.bradius || ''};
    &:hover {
        color: ${props => props.hoverColor || colors.primary};
        cursor: pointer;
        background:${props => props.hoverbackground || ''};
    }
`;

const LinkT = (props) => {
    return (
        <Anchor onClick={props.onClick} href={props.href} background={props.background} size={props.size} color ={props.color} padding = {props.padding} align ={props.align} hoverColor={props.hoverColor} hoverbackground={props.hoverbackground} bradius={props.bradius} margin={props.margin} title={props.title} weight={props.weight}>{props.children}</Anchor>
    )
}


export default LinkT;