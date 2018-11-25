package com.jun.excel;

import com.jun.excel.model.CheckResult;

public abstract class ExcelRowDefaultParser<T> extends ExcelRowParser<T> {

	@Override
	public CheckResult rowDataCheck(T rowData) {
		return new CheckResult();
	}
}
