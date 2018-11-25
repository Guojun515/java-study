package com.jun.excel;

import java.util.List;

import com.jun.excel.model.CheckResult;

public abstract class ExcelTableDefaultParser<T> extends ExcelTableParser<T> {

	@Override
	public CheckResult rowDataCheck(List<T> rowData) {
		return new CheckResult();
	}

}
