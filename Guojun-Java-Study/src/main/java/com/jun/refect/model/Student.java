package com.jun.refect.model;

import java.util.Date;

import com.jun.refect.annontation.DataTypeMapping;
import com.jun.refect.enums.DatabaseTypeEnum;

/**
 * 
 * @Description 学生model
 * @author Guojun
 * @Date 2018年5月26日 下午3:28:30
 *
 */
public class Student {
	public Student(int uid, String uname, String udateStr ) {
		this.uid = uid;
		this.uname = uname;
		this.udateStr = udateStr;
	}

	public Student(int uid, String uname, Date udate) {
		this.uid = uid;
		this.uname = uname;
		this.udate = udate;
	}

	@DataTypeMapping(datatype=DatabaseTypeEnum.NUMBER)
	private int uid;

	@DataTypeMapping(datatype=DatabaseTypeEnum.VARCHAR)
	private String uname;

	@DataTypeMapping(datatype=DatabaseTypeEnum.DATE, format="yyy-MM-dd hh24:mi:ss")
	private String udateStr;

	@DataTypeMapping(datatype=DatabaseTypeEnum.DATE, format="yyy-MM-dd hh24:mi:ss")
	private Date udate;

	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
}
