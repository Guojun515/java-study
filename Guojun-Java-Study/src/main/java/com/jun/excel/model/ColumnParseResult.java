package com.jun.excel.model;

import java.text.MessageFormat;

/**
 * 列解析的模型 
 * @author Guojun
 * @Date 2018年11月24日 下午5:41:16
 *
 */
public class ColumnParseResult<T> {

	/**
	 * 数据结果
	 */
	private T columnData ;
	
	/**
	 * 检验结果
	 */
	private CheckResult checkResult;
	
	/**
	 * 结果成功
	 * @param columnData
	 */
	public void success(T columnData) {
		this.columnData = columnData;
		this.checkResult = new CheckResult();
	}
	
	/**
	 * 是否成功
	 * @return
	 */
	public boolean isCheckSuccess() {
		return this.checkResult.isCheckSuccess();
	}
	
	/**
	 * 失败
	 * @param columnIndex
	 * @param columnName
	 * @param message
	 */
	public void error(int columnIndex, String columnName, String message) {
		this.checkResult = new CheckResult(MessageFormat.format("第{0}列【{1}】数据错误：{2}", 
				columnIndex, columnName, message));
	}
	
	/**
	 * 错误消息
	 * @return
	 */
	public String getErrorMessage() {
		return this.checkResult.getErrorMessage();
	}
	
	public T getColumnData() {
		return columnData;
	}

	public CheckResult getCheckResult() {
		return checkResult;
	}
}
