package com.jun.excel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import com.jun.excel.exception.ExcelParseException;
import com.jun.excel.model.CheckResult;
import com.jun.excel.model.ColumnParseResult;
import com.jun.excel.model.RowParseResult;

/**
 *  Excel 表格行信息
 * @Auther: guojun.yu
 * @Date: 2018/11/23 17:03
 * @Description:
 */
public abstract class ExcelRowParser<T> {
	/**
	 * 数据类型
	 */
	private Class<?> classType;

	public ExcelRowParser() {
		// 获取带有泛型的父类
		ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
		// 获取泛型的类型
		Type typeClazz = type.getActualTypeArguments()[0];
		this.classType =  (Class<?>) typeClazz;
	}

	/**
	 * 数据解析
	 * @param row
	 */
	protected RowParseResult<T> readRowData(Row row, CellStyle errorInfoCellStyle) {
		return this.readRowData(row, errorInfoCellStyle, null);
	};

	/**
	 * 数据解析
	 * @param row
	 */
	@SuppressWarnings("unchecked")
	protected RowParseResult<T> readRowData(Row row, CellStyle errorInfoCellStyle, FormulaEvaluator evaluator) {
		RowParseResult<T> result = new RowParseResult<>();

		List<ExcelColumnParser<?>> excelColumns = this.excelColumns();
		if (CollectionUtils.isEmpty(excelColumns)) {
			throw new ExcelParseException("系统解析Excel信息配置异常！");
		}

		StringBuilder errorMsg = new StringBuilder();
		Map<String, Object> rowDataInfo = new HashMap<>();
		excelColumns.stream().forEach(excelColumnParse -> {
			Cell cell = row.getCell(excelColumnParse.getColumnIndex());

			// 解析数据
			ColumnParseResult<?> columnParseResult = null;
			if (evaluator == null) {
				columnParseResult = excelColumnParse.readColumnData(cell);
			} else {
				columnParseResult = excelColumnParse.readColumnData(cell, evaluator);
			}

			// 检查是否有错误
			if (!columnParseResult.isCheckSuccess()) {
				errorMsg.append(columnParseResult.getErrorMessage()).append(";");
			} 

			rowDataInfo.put(excelColumnParse.getColumnField(), columnParseResult.getColumnData());
		});

		// 数据转换
		T rowData = null;

		if (errorMsg.length() == 0) {
			try {
				rowData = (T) classType.newInstance(); 
				BeanUtils.populate(rowData, rowDataInfo);
			} catch (Exception e) {
				throw new ExcelParseException("Excel行数据转换异常：", e);
			}

			// 行一级的数据教育
			CheckResult rowCheckResult = this.rowDataCheck(rowData);
			if (!rowCheckResult.isCheckSuccess()) {
				errorMsg.append(rowCheckResult.getErrorMessage()).append(";");
			}
		}

		if (errorMsg.length() > 0) {
			result.error();

			// 错误信息重新回填表
			Cell errorInfoCell = row.createCell(excelColumns.size(), CellType.STRING);
			errorInfoCell.setCellValue(errorMsg.toString());
			errorInfoCell.setCellStyle(errorInfoCellStyle);
		} else {
			result.success(rowData);
		}

		return result;
	};


	/**
	 * 列的数据校验
	 * @return 错误消息
	 */
	public abstract CheckResult rowDataCheck(T rowData);

	/**
	 * 列解析器
	 * @param excelColumns
	 */
	protected abstract List<ExcelColumnParser<?>> excelColumns();
}