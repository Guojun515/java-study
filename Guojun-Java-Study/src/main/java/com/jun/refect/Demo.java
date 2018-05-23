package com.jun.refect;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Demo {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		Student stu = new Student(123456, "zhangsan", new Date());

		/*
		 * getFields 与 getDeclaredFields区别
		 */
		//Field[] fields = stu.getClass().getFields(); //返回public修饰的字段
		Field[] fields = stu.getClass().getDeclaredFields(); //返回class所有的字段，包括所有的

		//设置访问仅限, 主要针对private修饰的属性
		Field.setAccessible(fields, true);


		/*
		 * 下面是针对反射做一个某场景下的小应用
		 * 根据类自动生成sql语句
		 */
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO " + stu.getClass().getSimpleName().toUpperCase() + " ({col}) values({val}) ");
		StringBuilder col = new StringBuilder();
		StringBuilder val = new StringBuilder();
		for( Field attr : fields ){
			if( col.length() > 0 ){
				col.append(",");
			}
			if( val.length() > 0 ){
				val.append(",");
			}
			col.append(attr.getName());
			val.append(convert(attr, stu));
		}
		String insSql = sqlBuilder.toString().replace("{col}", col.toString()).replace("{val}", val.toString());
		System.out.println(insSql);
	}

	public static String convert(Field attr, Object target) throws IllegalArgumentException, IllegalAccessException{
		Object value = attr.get(target);
		if( value == null ){
			value = "";
		}

		if (attr.isAnnotationPresent(DataTypeMapping.class)) {
			DataTypeMapping mappingAno = attr.getAnnotation(DataTypeMapping.class);
			DBTypeEnum type = mappingAno.datatype();
			if ( type == DBTypeEnum.NUMBER ) {
				return value + "";
			} else if( type == DBTypeEnum.DATE ) {
				String tmpValue = ""; 
				if( value instanceof Date ){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					tmpValue = format.format((Date)value);
				}else{
					tmpValue = value.toString();
				}
				return "to_date('" + tmpValue + "'" + ",'" + mappingAno.format() + "')";
			}
		}

		return "'" + value + "'";
	}

	public static class Student{

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

		@DataTypeMapping(datatype=DBTypeEnum.NUMBER)
		private int uid;

		@DataTypeMapping(datatype=DBTypeEnum.VARCHAR)
		private String uname;

		@DataTypeMapping(datatype=DBTypeEnum.DATE, format="yyy-MM-dd hh24:mi:ss")
		private String udateStr;

		@DataTypeMapping(datatype=DBTypeEnum.DATE, format="yyy-MM-dd hh24:mi:ss")
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

}


