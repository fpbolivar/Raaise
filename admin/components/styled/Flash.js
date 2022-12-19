import Text from './Text';

const Flash = Text.extend`
    display: ${props => props.show ? 'block' : 'none'};
    font-size: 12px;
`;

export default Flash;