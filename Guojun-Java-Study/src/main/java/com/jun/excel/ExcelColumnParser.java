package com.jun.excel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.jun.excel.model.CheckResult;
import com.jun.excel.model.ColumnParseResult;
import com.jun.excel.utils.ExcelParserUtils;

/**
 * Excel 表格列信息,包含列的校验
 * @Auther: guojun.yu
 * @Date: 2018/11/23 16:31
 * @Description:
 */
public abstract class ExcelColumnParser<T> {

	/**
	 * 列的标题
	 */
	private String columnName;

	/**
	 * 列对应的字段
	 */
	private String columnField;

	/**
	 * 列的索引位置，从0开始
	 */
	private int columnIndex;

	/**
	 * 数据类型
	 */
	private Class<?> classType;

	public ExcelColumnParser(String columnName, String columnField, int columnIndex) {
		this.columnName = columnName;
		this.columnField = columnField;
		this.columnIndex = columnIndex;

		// 获取带有泛型的父类
		ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
		// 获取泛型的类型
		Type typeClazz = type.getActualTypeArguments()[0];
		classType =  (Class<?>) typeClazz;
	}

	/**
	 * 数据读取
	 * @param cell
	 */
	protected ColumnParseResult<T> readColumnData(Cell cell) {
		ColumnParseResult<T> result = new ColumnParseResult<>();
		try {
			Object cellValue = null;
			if (cell != null)  {
				switch (cell.getCellTypeEnum()) {
				case NUMERIC: 
					cellValue = cell.getNumericCellValue();
					break;
				case STRING: 
					cellValue = cell.getStringCellValue();
					break;
				case BOOLEAN: 
					cellValue = cell.getBooleanCellValue();
					break;
				case ERROR: 
					result.error(this.columnIndex + 1, this.columnName,cell.getStringCellValue());
					return result;
				default: 
					cellValue = cell.getStringCellValue();
					break;
				}
			}
			this.parseCellValue(cellValue, result);
		} catch (Exception e) {
			result.error(this.columnIndex + 1, this.columnName, e.getMessage());
		}

		return result;
	}

	/**
	 * 数据读取, 可以读取计算公式
	 * @param cell
	 */
	protected ColumnParseResult<T> readColumnData(Cell cell, FormulaEvaluator evaluator) {
		ColumnParseResult<T> result = new ColumnParseResult<>();
		try {
			if (cell == null) {
				this.parseCellValue(null, result);
			} else if (cell.getCellTypeEnum().equals(CellType.FORMULA)) { 
				Object cellValue = null;

				CellValue cellValueObj = evaluator.evaluate(cell);
				switch (cellValueObj.getCellTypeEnum()) {
				case NUMERIC: 
					cellValue = cellValueObj.getNumberValue();
					break;
				case STRING: 
					cellValue = cellValueObj.getStringValue();
					break;
				case BOOLEAN:
					cellValue = cellValueObj.getBooleanValue();
					break;
				case ERROR: 
					result.error(this.columnIndex + 1, this.columnName, cellValueObj.getStringValue());
					return result;
				default: 
					cellValue = cellValueObj.getStringValue();
					break;
				} 

				this.parseCellValue(cellValue, result);
			} else {
				result = this.readColumnData(cell);
			}
		} catch (Exception e) {
			result.error(this.columnIndex + 1, this.columnName, e.getMessage());
			return result;
		}

		return result;
	}

	/**
	 * 转换为实际的数据类型
	 * @param cellValue
	 * @param result
	 * @throws Exception
	 */
	private void parseCellValue(Object cellValue, ColumnParseResult<T> result) throws Exception {
		// 转换为实际的数据类型
		T columnData = ExcelParserUtils.parseCellValue(classType, cellValue);
		CheckResult columnCheckResult = this.columnDataCheck(columnData);

		if (columnCheckResult.isCheckSuccess()) {
			result.success(columnData);
		} else {
			result.error(this.columnIndex + 1, this.columnName, columnCheckResult.getErrorMessage());
		}
	}

	/**
	 * 列的数据校验
	 * @return 错误消息
	 */
	public abstract CheckResult columnDataCheck(T columnData);

	/**
	 * @return 列的标题
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @return 列对应的字段
	 */
	public String getColumnField() {
		return columnField;
	}

	/**
	 * @return 列的索引位置，从0开始
	 */
	public int getColumnIndex() {
		return columnIndex;
	}
}
