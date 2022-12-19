import React from 'react'
import { Input, Wrapper, Error } from '../../styles/InputField.styled'

const InputField = (props) => {
    const { id, errmsg,type, onChange, ...inputData } = props
    let inputClass = errmsg ? " invalid focus" : "valid"
    return (
        <Wrapper>
            <Input {...inputData} onChange={onChange} className={inputClass} type={type} />
            {props.endAdornment || ""}
           { errmsg && <Error>{errmsg || ""}</Error>}
        </Wrapper>
    );
};

export default InputField;