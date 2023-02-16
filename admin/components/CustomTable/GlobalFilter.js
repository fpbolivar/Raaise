/**
 * global filter component with useAsyncDebounce
 */

import React, { useEffect, useState, } from "react";
import "regenerator-runtime/runtime";
import { useAsyncDebounce } from "react-table";

const GlobalFilter = ({ filter, setFilter, search }) => {
    const [value, setValue] = useState(filter);
    const onChange = useAsyncDebounce((value1) => {
        setFilter(value1 || "");
    }, 1000);
    useEffect(() => {
        if (!search) setValue('')
    }, [search])

    return (
        <div>
            <input className="search-input" value={value || ""} type="text" placeholder="Search"
                onChange={(e) => {
                    setValue(e.target.value);
                    onChange(e.target.value);
                }}
            />
        </div>
    );
};

export default GlobalFilter;

/**
 * global filter component
 */
// const GlobalFilter = ({ filter, setFilter }) => {
//   return (
//     <span>
//       Search:{' '}
//       <input
//         type="text"
//         value={filter || ''}
//         onChange={e => {
//           setFilter(e.target.value);
//         }}
//       />
//     </span>
//   );
// };
