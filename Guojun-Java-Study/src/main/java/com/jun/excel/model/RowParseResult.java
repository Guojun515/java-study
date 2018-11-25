package com.jun.excel.model;

/**
 * 行解析的模型 
 * @author Guojun
 * @Date 2018年11月24日 下午5:41:16
 *
 */
public class RowParseResult<T> {

	/**
	 * 数据结果
	 */
	private T rowData ;
	
	/**
	 * 检验结果
	 */
	private boolean checkSuccess;
	
	/**
	 * 结果成功
	 * @param columnData
	 */
	public void success(T rowData) {
		this.rowData = rowData;
		this.checkSuccess = true;
	}
	
	/**
	 * 失败
	 */
	public void error() {
		this.checkSuccess = false;
	}


	public T getRowData() {
		return rowData;
	}


	public boolean isCheckSuccess() {
		return checkSuccess;
	}
}
