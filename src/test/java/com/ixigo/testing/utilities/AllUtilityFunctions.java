
package com.ixigo.testing.utilities;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class AllUtilityFunctions {

    public String getExcelData(
            String filePath,
            String sheetName,
            int rowNumber,
            int columnNumber) {

        try (FileInputStream input = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(input)) {

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new IllegalArgumentException(
                        "Sheet not found: " + sheetName);
            }

            Row row = sheet.getRow(rowNumber);

            if (row == null
                    || row.getCell(columnNumber) == null) {
                return "";
            }

            DataFormatter formatter = new DataFormatter();

            return formatter.formatCellValue(
                    row.getCell(columnNumber)).trim();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read Excel file: " + filePath, e);
        }
    }
}
