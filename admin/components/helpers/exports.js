import * as FileSaver from "file-saver";
import * as XLSX from "xlsx";
import { CsvBuilder } from "filefy";
export const exportToCSV = (users, fileName) => {
  const fileType =
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
  const fileExtension = ".xlsx";
  const ws = XLSX.utils.json_to_sheet(users);
  const wb = { Sheets: { data: ws }, SheetNames: ["data"] };
  const excelBuffer = XLSX.write(wb, { bookType: "xlsx", type: "array" });
  const data = new Blob([excelBuffer], { type: fileType });
  FileSaver.saveAs(data, fileName + fileExtension);
};

export const exportData = (arr, col, file) => {
  const csvBuilder = new CsvBuilder(file)
    .setColumns(col)
    .addRows(arr)
    .exportFile();
};
