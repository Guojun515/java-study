package com.jun.excel.test;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = -1124685661728170093L;

	private String userName;

	private String telNum;

	private Integer age;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTelNum() {
		return telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", telNum=" + telNum + ", age=" + age + "]";
	}
}