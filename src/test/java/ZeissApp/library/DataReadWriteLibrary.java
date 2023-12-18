package ZeissApp.library;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import org.apache.poi.ss.usermodel.*;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataReadWriteLibrary {

    public static String getExcelData(String filePath, String sheetName, int rowNo, int cellNo) {
        try {
            File f = new File(filePath);
            FileInputStream fileInput = new FileInputStream(f);
            Workbook wb = WorkbookFactory.create(fileInput);

            Sheet st = wb.getSheet(sheetName);
            Row r = st.getRow(rowNo);
            if (r == null) {
                return " ";
            }
            Cell c = r.getCell(cellNo, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); // Prevent NullPointerException for
            if (c == null) {
                return " ";
            }

            String data = c.getStringCellValue();
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        }
    }


    public static void writeExcelData(String filePath, String sheetName, int rowNo, int cellNo, String data) {
        try {
            FileInputStream fileInput = new FileInputStream(filePath);
            Workbook wb = WorkbookFactory.create(fileInput);
            Sheet st = wb.getSheet(sheetName);
            Row r = st.getRow(rowNo);
            if (r == null)
                r = st.createRow(rowNo);
            Cell c = r.createCell(cellNo);
            if (c == null)
                c = r.createCell(cellNo);
            c.setCellValue(String.valueOf(CellType.STRING));
            c.setCellValue(data);
            FileOutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
        }

    }

    public static int getExcelRowCount(String filePath, String sheetName) {
        int rowNo = 0;
        try {
            FileInputStream fileInput = new FileInputStream(filePath);
            Workbook wb = WorkbookFactory.create(fileInput);
            Sheet st = wb.getSheet(sheetName);
            rowNo = st.getLastRowNum();

        } catch (Exception e) {

        }
        return rowNo;
    }

    public static int getExcelCellCount(String filePath, String sheetName, int rowNo) {
        try {
            FileInputStream fileInput = new FileInputStream(filePath);
            Workbook wb = WorkbookFactory.create(fileInput);
            Sheet st = wb.getSheet(sheetName);
            Row r = st.getRow(rowNo);
            return r.getLastCellNum();
        } catch (Exception e) {
            System.err.println("Error in getting cell count\n");
            return -1;
        }
    }

    public static Object getBooleanExcelData(String filePath, String sheetName, int rowNo, int cellNo) {
        try {
            File f = new File(filePath);
            FileInputStream fileInput = new FileInputStream(f);
            Workbook wb = WorkbookFactory.create(fileInput);
            Sheet st = wb.getSheet(sheetName);
            Row r = st.getRow(rowNo);

            if (r == null) {
                return " ";
            }
            Cell c = r.getCell(cellNo, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); // Prevent NullPointerException for
            if (c == null) {
                return " ";
            }

            Object data = c.getBooleanCellValue();
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        }
    }

    public static void writeResultToExcel(String path, String sheetName, int i, String result) {
        Workbook wb;
        try {
            FileInputStream file = new FileInputStream(path);
            wb = WorkbookFactory.create(file);
            writeExcelData(path, sheetName, i, 3, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Connection excelConnection;
    private synchronized Connection getConnection(String excelFilePath) {

        if (excelFilePath.isEmpty())
            return null;

        if (excelConnection != null)
            return excelConnection;
        try {
            Fillo fillo = new Fillo();
            excelConnection = fillo.getConnection(excelFilePath);
            return excelConnection;
        } catch (FilloException e) {
            System.err.println("Excel File [" + excelFilePath + "] not found.");
            return null;
        }
    }

    /**
     *
     *
     * @return
     * @throws FilloException
     */
    public LinkedHashMap<String,String> getMapByColumn(String filePath,String sheetName, String columnName, String columnValue) throws FilloException {

        Connection connection = getConnection(filePath);
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
        Recordset recordset = null;
        String strQuery = "";
        try {
            strQuery = String.format("Select * from %s where %s= '%s'", sheetName,columnName,columnValue);
            recordset = connection.executeQuery(strQuery);
        } catch (FilloException e) {
            System.err.println("Unable to execute "+strQuery);
        }
        while (recordset.next())
            for (String header : recordset.getFieldNames())
                dataMap.put(header, recordset.getField(header));

        if (dataMap.isEmpty())
            throw new RuntimeException("No Data found for : " + columnName);

        System.out.println(dataMap);
        return dataMap;
    }

    public String getJsonStringFromYamlFile(String fileName, String key) {
        Map<String, Object> jsonMap = null;
        try {
            String filePath = "./excel_lib/jsons/" + fileName + ".yaml";
            Yaml yaml = new Yaml();
            Reader fileReader = new FileReader(filePath);
            jsonMap = yaml.load(fileReader);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonMap.get(key).toString();
    }

}

