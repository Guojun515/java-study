package com.jun.excel;

import org.apache.commons.lang3.StringUtils;

import com.jun.excel.model.CheckResult;

/**
 * Excel 表格列信息,默认不校验
 * @Auther: guojun.yu
 * @Date: 2018/11/23 16:31
 * @Description:
 */
public class ExcelColumnEmptyCheckParser<T> extends ExcelColumnParser<T> {
	public ExcelColumnEmptyCheckParser(String columnName, String columnField, int columnIndex) {
		super(columnName, columnField, columnIndex);
	}

	@Override
	public CheckResult columnDataCheck(T columnData) {
		if (columnData == null) {
			return new CheckResult("空白");
		}
		
		if (columnData instanceof String && StringUtils.isBlank((String) columnData)) {
			return new CheckResult("空白");
		} 
		return new CheckResult();
	}
}
