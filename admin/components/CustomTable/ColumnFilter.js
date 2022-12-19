/**
 * column filter component
 */
const ColumnFilter = ({ column }) => {
    return (
        <span>
            {/* Search:{' '} */}
            <input coloum-search className="coloum-search" type="text" value={column.filterValue || ""}
             onChange={(e) => column.setFilter(e.target.value)}
            />
        </span>
    );
};
export default ColumnFilter;
