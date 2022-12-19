import styled from 'styled-components'

const Image = styled.img`
    width: ${props => props.width || 'unset'};
    height: ${props => props.height || 'unset'};
    object-fit: ${props => props.fit || 'unset'};
    object-position: ${props => props.position || 'unset'};
`;

export default Image