import styled from "styled-components";
import colors from "../../colors";
export const Table = styled.table`
    { border-collapse: collapse; width: 100%; }
    th,td 
    { border: 1px solid #ddd; padding: 8px; vertical-align: top; word-break: break-all; }
    tr:nth-child(even) { background-color: #e9ebec; }
    th { padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #e9ebec; color: black; white-space: nowrap !important; }
    th:first-child, td:first-child, td:last-child {
        white-space: nowrap !important;
    }
    .coloum-search { padding: 6px 10px; outline: none; border-radius: 3px; border: 1px solid #b5bdc2;}
`;

export const TableFilter = styled.thead`
    width: 100%;
    .show-page { text-align: left; }
    .global-search { text-align: right; float: right; }
`;

export const TableContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
    width: fit-content;
    min-width: 100% !important;
    .show-page { outline: none; border-radius: 3px; border: 1px solid #b5bdc2;padding: 8px 12px;}
    .search-input { padding: 9px 12px; outline: none; border-radius: 3px; border: 1px solid #b5bdc2; width: 215px; }
`;

export const TablePagination = styled.div`
    width: 100%;
    .page-no { text-align: left;}
    .buttons { text-align: right; float: right;}
    .back-button { background-color: #dddddd; border: none; padding: 6px 10px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px;
        border-top-left-radius: 5px; border-bottom-left-radius: 5px; cursor: pointer; margin-right: 1px;
    }
    .prev-next-button {background-color: #dddddd; border: none; padding: 6px 10px; text-align: center; 
        text-decoration: none; display: inline-block; font-size: 16px; cursor: pointer;margin-right: 1px;
    }
    .next-button { background-color: #dddddd; border: none; padding: 6px 10px; text-align: center;text-decoration: none;
        display: inline-block; font-size: 16px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; cursor: pointer; margin-right: 1px;
    }
`;
export const TableAscIcon = styled.span`
    width: 0px;
    height: 0px;
    margin-top: 1px;
    margin-bottom: 2px;
    border-bottom: ${(props) =>
        props.active
            ? "5px solid rgb(163, 163, 163)"
            : "5px solid rgb(204, 204, 204)"};
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
`;
export const TableDescIcon = styled.span`
    width: 0px;
    height: 0px;
    margin-bottom: 1px;
    border-top: ${(props) =>
        props.active
            ? "5px solid rgb(163, 163, 163)"
            : "5px solid rgb(204, 204, 204)"};
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
`;

export const TableSortIcon = styled.span`
    display: inline-grid;
    vertical-align: top;
    margin-right: 10px;
`;

export const TableHeader = styled.div`
    border: 1px solid #d3d9e0;
    display: flex;
    justify-content: space-between;
    padding: 10px;
    background-color: ${colors.white};
    margin: 24px 0px;
    align-items: center;
    & .title{font-size: 24px; font-weight: 700; }
    & .push-notif-div{ background-color: ${colors.blueColor}; border-radius: 4px; padding: 10px;cursor:pointer;
        & a{color: white; text-decoration: none;cursor:pointer;}
    }
`;

export const TableBody = styled.div`
& .search-div{ display: flex; justify-content: end; margin-top:32px; margin-right: 16px;
    & .search-input{ padding: 10px 10px; outline: none; border-radius: 3px; border: 1px solid #b5bdc2; width: 215px;
    }
}`;

export const Container =  styled.div`
background-color: lightgrey;
`;

export const Section = styled.div`
margin: 20px;
padding: 20px;
border: 1px solid #d3d9e0;
background: 0 0;
background-color: #fff;
border-radius: 3px;
`;

