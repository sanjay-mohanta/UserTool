package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;

public class ExcelRead {
	public final static String cellToString(Cell cell) {
		int type;
		Object result;
		result = "";
		type = cell.getCellType();

		if (type == 2) {
			// System.out.println("Formula is " + cell.getCellFormula());
			switch (cell.getCachedFormulaResultType()) {
			case 0:
				result = cell.getNumericCellValue();
				break;
			case 1:
				result = cell.getRichStringCellValue();
				break;
			}
		} else {
			switch (type) {
			case 0: // numeric value in Excel
				// System.out.println("numeric");
				result = cell.getNumericCellValue();
				break;
			case 1: // String Value in Excel
				// System.out.println("String");
				result = cell.getStringCellValue();
				break;
			default:
				// System.out.println("other");
				result = "";
				break;
			// throw new RuntimeException("There is no support for this type of cell");
			}
		}
		return result.toString();
	}

	public static void main(String[] args) {
		try {
			Workbook workbook = null;
			String FILE_PATH = "D://172363-15670#25-26-ET-345-179173-638739-PriceSchedule.xls";
			FileInputStream excelFile = new FileInputStream(new File(FILE_PATH));
			workbook = new HSSFWorkbook(excelFile);
			int sheet_no = 9999;
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				String sheet_name = workbook.getSheetName(i);
				if ((sheet_name.toLowerCase()).contains("summary")) {
					sheet_no = i;
					break;
				}
			}
			String searchcols = "TotalAmountfor60monthsexclGST";

			int row_no = 0;
			int col_no = 0;

			Sheet sheet = workbook.getSheetAt(sheet_no);

			int num_of_rows = sheet.getLastRowNum();
			// System.out.println(" num_of_rows : "+num_of_rows+"<br>");
			for (int i = 0; i <= num_of_rows; i++) {
				// System.out.println(" sheet : "+sheet+"<br>");
				Row row = sheet.getRow(i);

				// System.out.println(" row : "+i+" "+row+"<br>");
				// Blank row in a sheet returns null value
				if (row != null) {
					int noOfColumns = row.getLastCellNum();
					for (int j = 0; j <= noOfColumns; j++) {
						try {
							if (row.getCell(j) != null) {
								if (!(cellToString(row.getCell(j)).trim().replaceAll("\\s+", "")).equals("")) {
									String cell_data = cellToString(row.getCell(j)).trim().replaceAll("\\s+", "");
									String searchcolstmp = "";
									try {
										searchcolstmp = searchcols;
										if (searchcolstmp.length() == 0)
											searchcolstmp = searchcols;
									} catch (Exception ss) {
										System.out.println("substring error " + ss + "  " + "cell_data " + cell_data);
										searchcolstmp = searchcols;
									}
									//System.out.println("cell_data : "+cell_data+" searchcolstmp "+searchcolstmp);
									//System.out.println("cell_data : "+cell_data);
									if (cell_data.contains(searchcolstmp)) {
										//System.out.println("inside");
										row_no = i;
										col_no = j;
										boolean check_data = false;
										int cover_cnt = 1;
										for (int yy = j + 1; yy <= noOfColumns; yy++) {
											//System.out.println("row.getCell(yy) : "+row.getCell(yy));
											if (cellToString(row.getCell(yy)).length() != 0) {
												System.out.println("cellToString(row.getCell(yy)) : "+cellToString(row.getCell(yy)));
											}

										}

									}
								}
							}
						} catch (Exception dd) {
							System.out.println("Exception : "+dd);
						}

					} // for end
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
