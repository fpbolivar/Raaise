import React from "react";
import { TablePagination } from "./Table.styled";

const Pagination = ({ pageChangeHandler, pagination }) => {
    return (
        <>
            <TablePagination>
                <span className="page-no">
                    Page{" "}
                    <strong> {pagination.current || 1} of{" "} {pagination.totalPages || 1}</strong>
                </span>
                <div className="buttons">
                    <button className="back-button"  disabled={!pagination.previous} onClick={() => pageChangeHandler(1)} >
                        {"<<"}
                    </button>
                    <button className="prev-next-button" disabled={!pagination.previous} onClick={() => pageChangeHandler(pagination.previous)} >
                        Previous
                    </button>
                    <button className="prev-next-button" disabled={!pagination.next} onClick={() => pageChangeHandler(pagination.next)}>
                        Next
                    </button>
                    <button className="next-button" disabled={!pagination.next} onClick={() => pageChangeHandler(pagination.totalPages)}>
                        {">>"}
                    </button>
                </div>
            </TablePagination>
        </>
    );
};

export default Pagination;
