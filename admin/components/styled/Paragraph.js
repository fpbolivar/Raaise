import styled from 'styled-components';

const Paragraph = styled.p`

    font-size: ${props => props.size || '12px' };
    color: ${props => props.color || 'black' };
    margin: ${props => props.margin || '0' };
`;

export default Paragraph;