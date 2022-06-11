package com.ensah.core.utils;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.ensah.core.web.controllers.ModuleController;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Data

public class ExcelExporter {
	private XSSFWorkbook workbook;
	public static XSSFSheet sheet;

	private String[] columnNames;
	private String[][] dataHeader;

	private String[] columnHeaderNames;
	private String[][] data;
	private String sheetName = "";

	public ExcelExporter(String[] columnNames, String[][] data, String sheetName) {
		this.columnNames = columnNames;

		this.data = data;
		this.sheetName = sheetName;
		workbook = new XSSFWorkbook();

	}

	public ExcelExporter(String[] columnHeaderNames, String[] columnNames, String[][] dataHeader, String[][] data, String sheetName) {
		this.columnHeaderNames = columnHeaderNames;
		this.columnNames = columnNames;
		this.dataHeader = dataHeader;
		this.data = data;
		this.sheetName = sheetName;
		workbook = new XSSFWorkbook();

	}

	private CellStyle styleMethode(Boolean bold, int font_height) {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(bold);
		font.setFontHeight(font_height);
		//style.setFillBackgroundColor(color);

		style.setFont(font);
		return style;
	}

	private void writeHeaderHeadLine() {
		sheet = workbook.createSheet(sheetName);

		Row row1 = sheet.createRow(0);

		CellStyle style = styleMethode(true, 16);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		int j = 0;
		for (String itt : columnHeaderNames) {
			createCell(row1, (j++), itt, style);
		}

	}

	private void writeHeaderLine() {
		Row row = sheet.createRow(4);

		CellStyle style = styleMethode(true, 16);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		int i = 0;
		for (String it : columnNames) {
			createCell(row, (i++), it, style);
		}

	}


	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataBodyLines() {
		int rowCount = 5;

		CellStyle style = styleMethode(false, 14);

		for (int i = 0; i < data.length; i++) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			for (int j = 0; j < data[i].length; j++) {
				createCell(row, columnCount++, data[i][j], style);
			}
		}

	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle style = styleMethode(false, 14);


		for (int i = 0; i < dataHeader.length; i++) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			for (int j = 0; j < dataHeader[i].length; j++) {
				createCell(row, columnCount++, dataHeader[i][j], style);
			}
		}

	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();


		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

	public void exportFile(HttpServletResponse response) throws IOException {
		writeHeaderHeadLine();
		writeDataLines();

		writeHeaderLine();
		writeDataBodyLines();


		// pour la moyenne et la validation
		String[] lettre = new String[]{"E", "F", "G", "H"};
		String formule = "";
		int i,j,n= 0;
		for (i = 0; i < ModuleController.nbrEtudiants; i++) {
			for ( j = 0; j < ModuleController.nbreElement; j++) {
				n = i + 6;
				formule += lettre[j] + n + "*" + ModuleController.coeffList.get(j) + "+";

			}
			// moyenne
			sheet.getRow(i + 5).getCell(3 + ModuleController.nbreElement + 1).setCellFormula(formule.substring(0, formule.length() - 1));
			formule = "";
			String fc="SI.CONDITIONS(" + lettre[j] + n + ">=12,\"V\"," + lettre[j] + n + "<12,\"R\")";
            // validation
			//sheet.getRow(i + 5).getCell(3 + ModuleController.nbreElement + 2).setCellFormula(fc);
			//sheet.getSheetConditionalFormatting();
			if(ModuleController.session.equals("Normale")) {

				sheet.getRow(i + 5).getCell(3 + ModuleController.nbreElement + 2).setCellFormula("SI.CONDITIONS(" + lettre[j] + n + ">=12,\"V\"," + lettre[j] + n + "<12,\"R\")");

			}else if(ModuleController.session.equals("Rattrapage")){

				sheet.getRow(i + 5).getCell(3 + ModuleController.nbreElement + 2).setCellFormula("SI.CONDITIONS(" + lettre[j] + n + "<8,\"NV\","+ lettre[j] + n + ">=8,\"V\")");

			}
		}

		//System.out.println(ModuleController.session);


		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}





}




