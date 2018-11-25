package com.jun.excel.model;

import java.util.List;

/**
 * 行解析的模型 
 * @author Guojun
 * @Date 2018年11月24日 下午5:41:16
 *
 */
public class ExcelParseResult<T> {

	/**
	 * 数据结果
	 */
	private List<T> excelData ;
	
	/**
	 * 检验结果
	 */
	private boolean checkSuccess;
	
	/**
	 * 错误信息的文件下载路径
	 */
	private String errorFileDownloadPath;
	
	/**
	 * 结果成功
	 * @param columnData
	 */
	public void success(List<T> excelData) {
		this.excelData = excelData;
		this.checkSuccess = true;
	}
	
	/**
	 * 失败
	 */
	public void error(String errorFileDownloadPath) {
		this.checkSuccess = false;
		this.errorFileDownloadPath = errorFileDownloadPath;
	}


	public List<T> getExcelData() {
		return excelData;
	}

	public boolean isCheckSuccess() {
		return checkSuccess;
	}

	public String getErrorFileDownloadPath() {
		return errorFileDownloadPath;
	}
}
