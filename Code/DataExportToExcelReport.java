/****************************************************************
@copyright Progneur Technology
File Name : DataExportToExcelReport.java
Functionality : Excel report generation functionality
Author : Bhavana Patil and Priyanka Tikhe
******************************************************************/
package com.teamcenter.tdps.view;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFHeader;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class DataExportToExcelReport {
	
	Table report_table;

 public DataExportToExcelReport(Table report_table) {
	 this.report_table=report_table;
	 
	}
	
	public void generateReport() throws IOException {
		try  {			
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Export OA Ledger data Sheet");
			
			
			
			//Set Header Font
			XSSFFont headerFont = wb.createFont();
			//headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 25);
			
					
			//Set Header Style
			CellStyle headerStyle = wb.createCellStyle();
			headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			//headerStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
			
			// XSSFRow row = sheet.addMergedRegion();
			XSSFRow row = sheet.createRow(0);			
			XSSFCell cell_row1 = row.createCell(0);
			cell_row1.setCellValue("OA Ledger Report");
			cell_row1.setCellStyle(headerStyle);
		
			// Merge the selected cells.
			CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 13);
			sheet.addMergedRegion(cellRangeAddress);
			
			//Set Column Font
			XSSFFont columnFont = wb.createFont();
			columnFont.setBold(true);
			columnFont.setFontHeightInPoints((short) 15);
			columnFont.setColor(XSSFFont.COLOR_RED);
	        //Column header style
            CellStyle headerColumnStyle = wb.createCellStyle();
           // headerColumnStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
		    //headerColumnStyle.setFillPattern(CellStyle.NO_FILL);
            headerColumnStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headerColumnStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		    headerColumnStyle.setFont(columnFont);
		    headerColumnStyle.setBorderBottom(CellStyle.BORDER_THIN);
		    headerColumnStyle.setBorderTop(CellStyle.BORDER_THIN);
		    headerColumnStyle.setBorderRight(CellStyle.BORDER_THIN);
		    headerColumnStyle.setBorderLeft(CellStyle.BORDER_THIN);
									
			XSSFRow columnRow = sheet.createRow(1);
			
			TableColumn[] columns = report_table.getColumns();
			for (int i = 0; i < columns.length; i++) {
				XSSFCell R2_C1 = columnRow.createCell(i);
				R2_C1.setCellStyle(headerColumnStyle);
				R2_C1.setCellValue(columns[i].getText());
				
			}	
			
			//Cell Style
			  CellStyle cellColumnStyle = wb.createCellStyle();
			    cellColumnStyle.setBorderBottom(CellStyle.BORDER_THIN);
			    cellColumnStyle.setBorderTop(CellStyle.BORDER_THIN);
			    cellColumnStyle.setBorderRight(CellStyle.BORDER_THIN);
			    cellColumnStyle.setBorderLeft(CellStyle.BORDER_THIN);
			
			
			int rowCount = 2;
			TableItem[] items = report_table.getItems();
			for (int r = 0; r < items.length; r++) 
			{
				XSSFRow rowC = sheet.createRow(rowCount);
				for (int c = 0; c < columns.length; c++) {
					
					XSSFCell colCell = rowC.createCell(c);
					colCell.setCellValue(items[r].getText(c));
					colCell.setCellStyle(cellColumnStyle);
				}
				rowCount++;
			}	
			
			
		/*	Set<String> keylist = maindatalist.keySet();
			for(String key: keylist)
			{
				LinkedHashMap<String, String> list = maindatalist.get(key);
				XSSFRow rowC = sheet.createRow(rowCount);
				int cnt = 0;
				
				for(String key_1: list.keySet())
				{
					String tabITemStr = list.get(key_1);
					XSSFCell colCell = rowC.createCell(cnt);
					colCell.setCellValue(tabITemStr);
					cnt++;
				}
				rowCount++;
			}*/

			DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss");
			Date date = new Date();
			String dateStr = dateFormat.format(date);
			
			
			   File file = new File("C:\\temp\\TDPS_EXCELREPORT");
		        if (!file.exists()) {
		            if (file.mkdir()) {
		              //  System.out.println("Directory is created!");
		            } else {
		               // System.out.println("Failed to create directory!");
		            }
		        }
			
			// Write the output to a file
			String filepath=file.getAbsolutePath()+"\\OA_ledger_"+dateStr+".xlsx";
			try(FileOutputStream fileOut = new FileOutputStream(filepath))
			{
				
				wb.write(fileOut);
				// JFrame parent = new JFrame();
	            //    JOptionPane.showMessageDialog(parent, "Report is generated at "+filepath);
				
				org.eclipse.swt.widgets.MessageBox messageBox;
	       		messageBox = new org.eclipse.swt.widgets.MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION| SWT.OK);
	            messageBox.setMessage(" Report is generated successfully.");
	            messageBox.setText("Excel Report");
	            int rc = messageBox.open();
	            if(rc == SWT.OK)
	            {
	            	///open on create
	            	Desktop.getDesktop().open(new File(filepath));
	            }			
			}			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

}


   
	
}