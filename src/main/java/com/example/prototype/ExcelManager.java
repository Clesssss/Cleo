package com.example.prototype;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelManager {
    private static Workbook workbook;
        private static String filePath = "C:/Users/Richard Kamitono/PRX/Cleo/General Journal.xlsx";

    public static void initializeWorkbook() throws IOException {
        createBackup();
        File file = new File(filePath);

        if (file.exists() && file.length() > 0) {
            try (FileInputStream fis = new FileInputStream(file)) {
                try {
                    // Try to load the workbook
                    workbook = WorkbookFactory.create(fis);
                } catch (IOException e) {
                    // File is corrupted, handle by creating a new workbook
                    file.delete();
                    workbook = new XSSFWorkbook();
                    saveWorkbook();
                }
            } catch (IOException e) {
                // If file reading fails for any reason, handle it
                throw new IOException("Failed to open the Excel file.", e);
            }
        } else {
            // File does not exist or is empty, create a new one
            workbook = new XSSFWorkbook();
            saveWorkbook();
        }
    }
    public static void createBackup() throws IOException {
        File originalFile = new File(filePath);
        if (originalFile.exists() && originalFile.length() > 0) {
            String backupFilePath = filePath + "_backup_" + System.currentTimeMillis() + ".xlsx";
            try (FileInputStream fis = new FileInputStream(originalFile);
                 FileOutputStream fos = new FileOutputStream(backupFilePath)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }
        } else {
            throw new IOException("The original file is empty or does not exist.");
        }
    }
    public static Workbook getWorkbook() {
        return workbook;
    }
    public static boolean isFileOpen(File file) {
        try (FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
             FileLock lock = channel.tryLock()) {
            // If the file is locked, the lock will be null or an exception will be thrown
            return lock == null;
        } catch (IOException e) {
            // If an IOException is thrown, the file is likely locked by another process
            return true;
        }
    }
    public static void initializeSheet(String month, int year) {
        try {
            // Get or create the sheet
            String sheetName = month + " " + year;
            Sheet sheet = getWorkbook().getSheet(sheetName);
            if (sheet == null) {
                sheet = getWorkbook().createSheet(sheetName);

                // Create a CellStyle for bold headers
                CellStyle headerStyle = getWorkbook().createCellStyle();
                Font headerFont = getWorkbook().createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                // Create a CellStyle for formatting numbers with thousand separators
                CellStyle numberStyle = getWorkbook().createCellStyle();
                DataFormat dataFormat = getWorkbook().createDataFormat();
                numberStyle.setDataFormat(dataFormat.getFormat("#,##0"));  // For comma separator

                // Create header row
                Row headerRow = sheet.createRow(0);  // Row index should be 0 for header

                Cell cell = headerRow.createCell(0);
                cell.setCellValue("Date");
                cell.setCellStyle(headerStyle);

                cell = headerRow.createCell(1);
                cell.setCellValue("Account Name");
                cell.setCellStyle(headerStyle);

                cell = headerRow.createCell(2);
                cell.setCellValue("Debit");
                cell.setCellStyle(headerStyle);

                cell = headerRow.createCell(3);
                cell.setCellValue("Credit");
                cell.setCellStyle(headerStyle);

                cell = headerRow.createCell(4);
                cell.setCellValue("Description");
                cell.setCellStyle(headerStyle);

                // Set column widths (convert pixel to character width approximation)
                sheet.setColumnWidth(0, (int)(12.58 * 256)); // Column A: Date
                sheet.setColumnWidth(1, (int)(20.47 * 256)); // Column B: Account Name
                sheet.setColumnWidth(2, (int)(14.16 * 256)); // Column C: Debit
                sheet.setColumnWidth(3, (int)(14.16 * 256)); // Column D: Credit
                sheet.setColumnWidth(4, (int)(41.53 * 256)); // Column E: Description

                // Save workbook
                saveWorkbook();
            }
        } catch (IOException ex) {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void saveWorkbook() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            getWorkbook().write(fos);
        }
    }
}
