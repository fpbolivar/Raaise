import React, { useMemo, useState } from "react";
import { useTable, useGlobalFilter, useFilters, } from "react-table";
import ColumnFilter from "./ColumnFilter";
import GlobalFilter from "./GlobalFilter";
import Pagination from "./Pagination";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { TableContainer, Table, TableFilter, } from "./Table.styled";

const FilteringTable = (props) => {
  const [filterData, setFilterData] = useState(10);
  const [date,setDate] =useState({startDate:"",endDate:""}) 
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
    const { handleTable, pagination,setDateFilter } = props;
    handleTable({
      pageNo: pageNo || pagination.current,
      limit: limit || pagination.limit,
      column: column || pagination.column,
      value: value,
      sort_column: sort_column || pagination.sort_column,
      sort_by: sort_by || pagination.sort_by,
    });
  };

  const handleDate = (date) => {
    const myDate = new Date(date);
    console.log(myDate);
    this.setState((prevState) => ({
        startDate: moment(date).format('YYYY/MM/DD'),
    }))
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
      columns, data, defaultColumn,
      manualPagination: false,
      disableSortRemove: true,
    },
    useFilters,
    useGlobalFilter
  );
  sno = sno ? sno : 0
console.log("date",date);



  return (
    <TableContainer>
      {props.title && <h2>{props.title || ""}</h2>}
      {
        !filterStatus &&
        <TableFilter>
          <select className="show-page" value={pageSize}
            onChange={(e) => {
              setFilterData(e.target.value)
              handlePage({
                pageNo: 1,
                limit: e.target.value,
              })
            }}
          >
            {[10, 25, 50].map((pageSize) => (
              <option key={pageSize} value={pageSize}>
                {pageSize}
              </option>
            ))}
          </select>
          {props.isDateFilter&&
          <div className="date-div">
            <div className="select-date">
            <label for="start-date">From</label>
            <DatePicker  placeholderText="Start Date"
              selected={date.startDate}
              onChange={(e)=>setDate(prevSate=>{
                return {...prevSate,startDate:e}
              })} dateFormat="yyyy/MM/dd" />
            </div>
            
            <div className="select-date">
              <label for="start-date">To</label>
              <DatePicker  placeholderText="End Date"
                selected={date.endDate} minDate={date.startDate}
                onChange={(e)=>setDate(prevSate=>{return {...prevSate,endDate:e}})}
                dateFormat="yyyy/MM/dd" />
            </div>
            {date.startDate && date.endDate &&
            <div className="btn-div">
              <button className="btn" onClick={()=>props.setDateFilter(date)}>Go</button>
            </div>
            }
          </div>
}
          <span className="global-search">
            <GlobalFilter filter={globalFilter} setFilter={(value) => handlePage({ column: "query", value: value })} search={pagination.value || ""} />
          </span>
        </TableFilter>
      }
      <Table {...getTableProps()}>
        {headerGroups.map((headerGroup, i) => (
          <thead key={i}>
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map((column, index) => (
                <th {...column.getHeaderProps()} className="font-size-1rem">
                  {column.render("Header")}
                  <span style={{ display: "inline-grid" }}></span>
                  <div key={index}>
                    {column.canFilter ? (
                      <input className="coloum-search" type="text" id={column.id}
                        onChange={(e) => handlePage({ column: column.id, value: e.target.value, })}
                      />
                    ) : null}
                  </div>
                </th>
              ))}
            </tr>
          </thead>
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
