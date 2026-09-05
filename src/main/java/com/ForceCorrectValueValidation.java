package com;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.FileOutputStream;

public class ForceCorrectValueValidation {

    public static void main(String[] args) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        DataValidationHelper helper = sheet.getDataValidationHelper();

        // --------------------------------------------------
        // 1. Dropdown in column A (A2:A100)
        // --------------------------------------------------
        String[] options = {"A", "B"};

        CellRangeAddressList dropdownRange =
                new CellRangeAddressList(1, 99, 0, 0);  // A2:A100

        DataValidationConstraint dropdownConstraint =
                helper.createExplicitListConstraint(options);

        DataValidation dropdownValidation =
                helper.createValidation(dropdownConstraint, dropdownRange);

        sheet.addValidationData(dropdownValidation);

        // --------------------------------------------------
        // 2. Force user to re-enter value in B when A changes
        //
        // Validation formula:
        // =IF($A2="A", AND(B2>0, B2<10), AND(B2>0, B2<20))
        //
        // - Old B becomes invalid when A changes
        // - User must enter a new correct value
        // --------------------------------------------------
        CellRangeAddressList numberRange =
                new CellRangeAddressList(1, 99, 1, 1);  // B2:B100

        DataValidationConstraint formulaConstraint =
                helper.createCustomConstraint("IF($A2=\"A\",AND(B2>0,B2<10),AND(B2>0,B2<20))");

        DataValidation numberValidation =
                helper.createValidation(formulaConstraint, numberRange);

        numberValidation.setShowErrorBox(true);
        numberValidation.createErrorBox(
                "Invalid Entry",
                "If A → enter a positive number < 10\nIf B → enter a positive number < 20"
        );

        sheet.addValidationData(numberValidation);

        // Save
        FileOutputStream out = new FileOutputStream("d://force_new_value_validation.xlsx");
        workbook.write(out);
        out.close();
        //workbook.close();
    }
}
