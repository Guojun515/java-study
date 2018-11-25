package com.jun.excel.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jun.excel.ExcelColumnDefaultParser;
import com.jun.excel.ExcelColumnParser;
import com.jun.excel.ExcelRowDefaultParser;
import com.jun.excel.ExcelTableDefaultParser;
import com.jun.excel.ExcelTableParser;
import com.jun.excel.model.CheckResult;
import com.jun.excel.model.ExcelParseResult;

public class Main{
	public static void main(String[] args) throws Exception{
		InputStream fileInputStream = new FileInputStream("C:/Users/dell/Desktop/excel/aaa.xlsx");

		ExcelTableParser<User> excelParser = new UserExcelParser();
		ExcelParseResult<User> result = excelParser.readExcelData(fileInputStream, false);
		if (result.isCheckSuccess()) {
			result.getExcelData().stream().forEach(user -> System.out.println(user));
		} else  {
			System.out.println(result.getErrorFileDownloadPath());
		}

	}
}


/**
 * 在Spring体系中使用IntilizeBean初始化列信息
 * @author Guojun
 * @Date 2018年11月25日 上午12:10:16
 *
 */
class UserExcelRowParse extends ExcelRowDefaultParser<User> {

	private static final List<ExcelColumnParser<?>> excelColumns = new ArrayList<>(3);
	static {
		excelColumns.add(new ExcelColumnDefaultParser<String>("姓名", "userName", 0) {});
		excelColumns.add(new ExcelColumnParser<String>("电话", "telNum", 1) {
			@Override
			public CheckResult columnDataCheck(String columnData) {
				if (columnData.length() > 11) {
					return new CheckResult("电话号码异常");
				}
				return new CheckResult();
			}
		});
		excelColumns.add(new ExcelColumnDefaultParser<Integer>("年龄", "age", 2) {});
	}

	@Override
	protected List<ExcelColumnParser<?>> excelColumns() {
		return excelColumns;
	}
}

class UserExcelParser extends ExcelTableDefaultParser<User> {

	private static final UserExcelRowParse excelRowParse = new UserExcelRowParse();

	public UserExcelParser() {
		this.setExcelRowParse(excelRowParse);
	}

	@Override
	public String errorFileOutput(ByteArrayOutputStream outputStream) throws Exception {
		String fileName = System.currentTimeMillis() + "-error.xlsx";
		FileOutputStream fileOutputStream = new FileOutputStream("C:/Users/dell/Desktop/excel/" + fileName);
		outputStream.writeTo(fileOutputStream);

		return "C:/Users/dell/Desktop/excel/" + fileName;
	}

}

