<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Read Excel in JSP using JavaScript</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- SheetJS -->
<script src="https://cdn.jsdelivr.net/npm/xlsx/dist/xlsx.full.min.js"></script>

<style>
table {
    border-collapse: collapse;
    margin-top: 20px;
}
table, th, td {
    border: 1px solid #000;
}
th, td {
    padding: 8px;
}
</style>
</head>

<body>

<h2>Browse Excel File</h2>

<input type="file" id="excelFile" accept=".xlsx,.xls">
<button id="readExcel">Read Excel</button>

<div id="excelData"></div>

<script>
$(document).ready(function () {

    $('#readExcel').click(function () {

        var fileInput = $('#excelFile')[0];

        if (fileInput.files.length === 0) {
            alert("Please select an Excel file");
            return;
        }

        var file = fileInput.files[0];
        var reader = new FileReader();

        reader.onload = function (e) {
            var data = new Uint8Array(e.target.result);
            var workbook = XLSX.read(data, { type: 'array' });

            var sheetName = workbook.SheetNames[0];
            var sheet = workbook.Sheets[sheetName];

            var jsonData = XLSX.utils.sheet_to_json(sheet, { defval: "" });

            displayExcelData(jsonData);
        };

        reader.readAsArrayBuffer(file);
    });

    function displayExcelData(data) {
        if (data.length === 0) {
            $('#excelData').html("No data found");
            return;
        }

        var table = '<table><tr>';

        // Header
        $.each(Object.keys(data[0]), function (i, key) {
            table += '<th>' + key + '</th>';
        });
        table += '</tr>';

        // Rows
        $.each(data, function (i, row) {
            table += '<tr>';
            $.each(row, function (key, value) {
                table += '<td>' + value + '</td>';
            });
            table += '</tr>';
        });

        table += '</table>';

        $('#excelData').html(table);
    }

});
</script>

</body>
</html>
