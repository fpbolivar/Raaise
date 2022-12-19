// import { format } from "date-fns";
// import ColumnFilter from "./ColumnFilter";

export const COLUMNS = [
    {
        Header: "Sno",
        accessor: "id",
        Footer: "Id",
        // Filter: ColumnFilter,
        disableFilters: true,
    },
    {
        Header: "Name",
        accessor: "name",
        Footer: "Name",
        // Filter: ColumnFilter
    },
    {
        Header: "Photo",
        accessor: "photo",
        Footer: "Photo",
        // Filter: ColumnFilter
    },
    {
        Header: "Username",
        accessor: "username",
        Footer: "Username",
        Cell: ({ value }) => {
            return value;
            // return format(new Date(value), 'dd/MM/yyyy');
        },
        // Filter: ColumnFilter
    },
    {
        Header: "Email",
        accessor: "email",
        Footer: "Email",
        // Filter: ColumnFilter
    },
    {
        Header: "Phone Number",
        accessor: "phone_number",
        Footer: "Phone Number",
        // Filter: ColumnFilter
    },
    {
        Header: "Login Via",
        accessor: "login_via",
        Footer: "Login Via",
        // Filter: ColumnFilter
    },
    {
        Header: "Android/IOS Users",
        accessor: "android_ios_users",
        Footer: "Android/IOS Users",
        // Filter: ColumnFilter
    },
    {
        Header: "Total Video",
        accessor: "total_video",
        Footer: "Total Video",
        // Filter: ColumnFilter
    },
    {
        Header: "Total Followers",
        accessor: "total_followers",
        Footer: "Total Followers",
        // Filter: ColumnFilter
    },
    {
        Header: "Total Donation Received",
        accessor: "total_donation_received",
        Footer: "Total Donation Received",
        // Filter: ColumnFilter
    },
    {
        Header: "Donation Given",
        accessor: "donation_given",
        Footer: "Donation Given",
        // Filter: ColumnFilter
    },
    {
        Header: "Actions",
        accessor: "actions",
        // Footer: "actions",
        // Filter: ColumnFilter
    },
];
