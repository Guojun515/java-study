package com.jun.excel.model;

/**
 * 解析结果
 * @author Guojun
 * @Date 2018年11月24日 下午1:15:43
 *
 */
public class CheckResult {
	
	private boolean checkSuccess = true;

	private String errorMessage ;

	/**
	 * 校验成调用此构造方法
	 */
	public CheckResult() {}

	/**
	 * 校验失败调用此构造方法
	 * @param errorMessage 错误信息
	 */
	public CheckResult(String errorMessage) {
		this.checkSuccess = false;
		this.errorMessage = errorMessage;
	}

	/**
	 * @return 校验结果
	 */
	public boolean isCheckSuccess() {
		return checkSuccess;
	}

	/**
	 * @return 校验异常信息
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}
