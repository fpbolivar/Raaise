import React, { useMemo } from "react";
import { useTable, useSortBy } from "react-table";
import { COLUMNS } from "./columns";
import MOCK_DATA from "./MOCK_DATA.json";

const SortingTable = (props) => {
    const columns = useMemo(() => COLUMNS, []);
    const data = useMemo(() => MOCK_DATA, []);

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        footerGroups,
        rows,
        prepareRow,
    } = useTable(
        {
            columns,
            data,
            disableSortRemove: true,
        },
        useSortBy
    );

    return (
        <div className="table-container">
            <h2>Sorting Table and format date of birth column</h2>
            <table {...getTableProps()}>
                <thead>
                    {headerGroups.map((headerGroup, i) => (
                        <tr {...headerGroup.getHeaderGroupProps()} key={i}>
                            {headerGroup.headers.map((column, i) => (
                                <th
                                    {...column.getHeaderProps(
                                        column.getSortByToggleProps()
                                    )}
                                    key={i}
                                >
                                    {column.render("Header")}
                                    <span>
                                        {column.isSorted
                                            ? column.isSortedDesc
                                                ? "🔽"
                                                : "🔼"
                                            : ""}
                                    </span>
                                </th>
                            ))}
                        </tr>
                    ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                    {rows.map((row, i) => {
                        prepareRow(row);

                        return (
                            <tr {...row.getRowProps()} key={i}>
                                {row.cells.map((cell, i) => {
                                    return (
                                        <td {...cell.getCellProps()} key={i}>
                                            {cell.render("Cell")}
                                        </td>
                                    );
                                })}
                            </tr>
                        );
                    })}
                </tbody>
                <tfoot>
                    {footerGroups.map((footerGroup, i) => (
                        <tr {...footerGroup.getFooterGroupProps()} key={i}>
                            {footerGroup.headers.map((column, i) => (
                                <td {...column.getFooterProps()} key={i}>
                                    {column.render("Footer")}{" "}
                                </td>
                            ))}
                        </tr>
                    ))}
                </tfoot>
            </table>
        </div>
    );
};

export default SortingTable;
