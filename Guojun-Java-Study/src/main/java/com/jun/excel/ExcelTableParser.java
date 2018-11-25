package com.jun.excel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.jun.excel.exception.ExcelParseException;
import com.jun.excel.model.CheckResult;
import com.jun.excel.model.ExcelParseResult;
import com.jun.excel.model.RowParseResult;

/**
 * excel解析
 * @author Guojun
 * @Date 2018年11月24日 下午6:52:38
 *
 */
public abstract class ExcelTableParser<T> {
	/**
	 * 行解析器
	 */
	// @Autowired(泛型注入)
	private ExcelRowParser<T> excelRowParse;
	
	/**
	 * 行解析器
	 * @param excelRowParse
	 */
	public void setExcelRowParse(ExcelRowParser<T> excelRowParse) {
		this.excelRowParse = excelRowParse;
	};
	
	/**
	 * 读取Excel的方法
	 * @param excelInStream 输入流
	 * @param hasFormula 是否有公式
	 * @return
	 */
	public ExcelParseResult<T> readExcelData(InputStream excelInStream, boolean hasFormula) {
		ExcelParseResult<T> result = new ExcelParseResult<>();
		try (Workbook workBook = WorkbookFactory.create(excelInStream)) {
			// 输入流使用后，及时关闭！这是文件流操作中极好的一个习惯！
			//excelInStream.close();
			
			// 错误信息单元格的样式
    		Font errorInfoCellFont = workBook.createFont();
    		errorInfoCellFont.setColor(Font.COLOR_RED);
    		CellStyle errorInfoCellStyle = workBook.createCellStyle();
    		errorInfoCellStyle.setFont(errorInfoCellFont);
			
			boolean hasError = false;
			Sheet sheet = workBook.getSheetAt(0);
			List<T> excelData = new ArrayList<T>();
			//从第二行开始读
			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				
				RowParseResult<T> rowResult = null;
				if (hasFormula) {
					FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
					rowResult = excelRowParse.readRowData(row, errorInfoCellStyle, evaluator);
				} else {
					rowResult = excelRowParse.readRowData(row, errorInfoCellStyle);
				}
				
				if (!rowResult.isCheckSuccess()) {
					hasError = true;
				}
				
				// 获取Excel解析的数据
				if (!hasError) {
					excelData.add(rowResult.getRowData());
				}
			}
			
			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				if (hasError) {
					workBook.write(outputStream);
					String errorFileDownloadPath = this.errorFileOutput(outputStream);
					
					result.error(errorFileDownloadPath);
				} else {
					result.success(excelData);
				}
			} catch (Exception e) {
				throw new ExcelParseException("Excel解析数据异常：", e);
			} 
		} catch (Exception e) {
			throw new ExcelParseException("Excel解析异常：", e);
		}
		
		return result;
	}
	
	/**
	 * 错误文件输出
	 * @return 返回文件路径
	 */
	public abstract String errorFileOutput(ByteArrayOutputStream outputStream) throws Exception; 
	
	/**
	 * Excel数据校验
	 * @return 错误消息
	 */
	public abstract CheckResult rowDataCheck(List<T> rowData);
}
