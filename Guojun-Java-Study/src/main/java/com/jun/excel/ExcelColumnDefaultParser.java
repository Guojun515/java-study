package com.jun.excel;

import com.jun.excel.model.CheckResult;

/**
 * Excel 表格列信息,默认不校验
 * @Auther: guojun.yu
 * @Date: 2018/11/23 16:31
 * @Description:
 */
public class ExcelColumnDefaultParser<T> extends ExcelColumnParser<T> {
	public ExcelColumnDefaultParser(String columnName, String columnField, int columnIndex) {
		super(columnName, columnField, columnIndex);
	}

	@Override
	public CheckResult columnDataCheck(T columnData) {
		return new CheckResult();
	}
}
