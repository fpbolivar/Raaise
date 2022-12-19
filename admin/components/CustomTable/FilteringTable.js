import React, { useMemo, useState} from "react";
import { useTable, useGlobalFilter, useFilters } from "react-table";
import ColumnFilter from "./ColumnFilter";
import GlobalFilter from "./GlobalFilter";
import Pagination from "./Pagination";
import {TableContainer,Table,TableFilter,} from "./Table.styled";

const FilteringTable = (props) => {
  const [filterData, setFilterData] = useState(10);
  const columns = useMemo(() => props.columns, [props.columns]);
  const pagination = props.pagination;
  const filterStatus = props.filterStatus
  const data = useMemo(() => props.data, [props.data]);
  const sno = (pagination.current - 1) * filterData
  const defaultColumn = useMemo(() => {
    return {
      Filter: ColumnFilter,
    };
  }, []);
  const handlePage = ({
    pageNo,
    limit,
    column,
    value,
    sort_column,
    sort_by,
  }) => {
    const { handleTable } = props;
    handleTable({
      pageNo: pageNo || pagination.current,
      limit: limit || pagination.limit,
      column: column || pagination.column,
      value: value || pagination.value,
      sort_column: sort_column || pagination.sort_column,
      sort_by: sort_by || pagination.sort_by,
    });
  };

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
    state: { globalFilter },
    state: { pageIndex, pageSize },
  } = useTable(
    {
      columns,data,defaultColumn,
      manualPagination: false,
      disableSortRemove: true,
    },
    useFilters,
    useGlobalFilter
  );
  sno = sno ? sno : 0

  return (
    <TableContainer>
      {props.title && <h2>{props.title || ""}</h2>}
      {
        !filterStatus &&
        <TableFilter>
         
          <select className="show-page"  value={pageSize}
            onChange={(e) =>{ setFilterData(e.target.value)
              handlePage({
                pageNo: 1,
                limit: e.target.value,
              })}
            }
          >
            {[10, 25, 50].map((pageSize) => (
              <option key={pageSize} value={pageSize}>
                  {pageSize}
              </option>
            ))}
          </select>
         <span className="global-search">
            <GlobalFilter filter={globalFilter}  setFilter={(value) => handlePage({column: "query",value: value})}/>
          </span>
        </TableFilter>
      }
     
      <Table {...getTableProps()}>
          {headerGroups.map((headerGroup, i) => (
            <tr {...headerGroup.getHeaderGroupProps()} key={i}>
              {headerGroup.headers.map((column, index) => (
                <th
                  {...column.getHeaderProps()}
                  key={index} className="font-size-1rem">
                  {column.render("Header")}
                  <span style={{display: "inline-grid" }}></span>
                  <div>
                    {column.canFilter ? (
                      <input className="coloum-search" type="text" id={column.id}
                        onChange={(e) =>handlePage({column: column.id,value: e.target.value,})}
                      />
                    ) : null}
                  </div>
                </th>
              ))}
            </tr>
          ))}
        <tbody {...getTableBodyProps()}>
          {pagination.isLoading ? (
            <tr className="odd">
              <td valign="top" colSpan={headerGroups[0].headers.length || 0} className="dataTables_empty">
                Loading...
              </td>
            </tr>
          ) : rows.length == 0 ? (
            <tr className="odd">
              <td valign="top" colSpan={headerGroups[0].headers.length || 0} className="dataTables_empty">
                No {props.name ? props.name : "Records"} found
              </td>
            </tr>
          ) : (
            rows.map((row, i) => {
              prepareRow(row);
              return (
                <tr {...row.getRowProps()} key={i}>
                  {row.cells.map((cell, index) => {
                    return (
                        cell.column.id == "serial" ? 
                          <td {...cell.getCellProps()} key={index} className="font-size-0-90rem" >
                            {(i + 1) + sno}
                          </td>
                        :
                          <td {...cell.getCellProps()} key={index} className="font-size-0-90rem" >
                            {cell.render("Cell")}
                          </td>
                    );
                  })}
                </tr>
              );
            })
          )}
        </tbody>
      </Table>
      {!filterStatus &&
        <Pagination
          pagination={pagination}
          pageChangeHandler={(page) => handlePage({ pageNo: page })}
        />
       }
    </TableContainer>
  );
};

export default FilteringTable;
