<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Read Excel Row by Row</title>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="js/xlsx.full.min.js"></script>

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

<h2>Select Excel File</h2>
<input type="file" id="excelFile" accept=".xlsx,.xls">
<button id="readExcel">Read Excel</button>

<div id="output"></div>

<div id="output1"></div>

<script>
$(document).ready(function () {

    $('#readExcel').click(function () {

        var fileInput = $('#excelFile')[0];
        if (fileInput.files.length === 0) {
            alert("Please select an Excel file");
            return;
        }

        var reader = new FileReader();

        reader.onload = function (e) {
            var data = new Uint8Array(e.target.result);
            var workbook = XLSX.read(data, { type: 'array' });

            var sheetName = workbook.SheetNames[0];
            var sheet = workbook.Sheets[sheetName];

            // Convert sheet to 2D array (rows & columns)
            var excelArray = XLSX.utils.sheet_to_json(sheet, {
                header: 1,   // IMPORTANT → returns array of arrays
                defval: ""
            });
			
            alert(excelArray[0][3]);
            
            readRowColumn(excelArray);
        };

        reader.readAsArrayBuffer(fileInput.files[0]);
    });

    function readRowColumn(data) {

        var table = '<table>';

        for (var i = 0; i < data.length; i++) {   // ROW LOOP
            table += '<tr>';

            for (var j = 0; j < data[i].length; j++) {  // COLUMN LOOP
                console.log("Row " + i + ", Col " + j + " = " + data[i][j]);
                table += '<td>' + data[i][j] + '</td>';
            }

            table += '</tr>';
        }

        table += '</table>';
        $('#output').html(table);
        
        var table = '<table>';
        
        $('#output1').html(data[0][3]);
    }

});
</script>

</body>
</html>
