package com.jun.refect;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jun.refect.annontation.DataTypeMapping;
import com.jun.refect.enums.DatabaseTypeEnum;
import com.jun.refect.model.Student;

/**
 * 
 * @Description 反射测试
 * @author Guojun
 * @Date 2018年5月26日 下午3:32:41
 *
 */
public class RefectDemo {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		Student stu = new Student(123456, "zhangsan", new Date());

		/*
		 * getFields 与 getDeclaredFields区别
		 * 1、getFields(): 返回public修饰的字段
		 * 2、getDeclaredFields(): 返回class所有的字段，包括所有的
		 */
		Field[] fields = stu.getClass().getDeclaredFields();

		//设置访问仅限, 主要针对private修饰的属性
		Field.setAccessible(fields, true);


		/*
		 * 下面是针对反射做一个某场景下的小应用
		 * 根据类自动生成sql语句
		 */
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO " + stu.getClass().getSimpleName().toUpperCase() + " ({col}) values({val}) ");
		StringBuilder col = new StringBuilder();
		StringBuilder val = new StringBuilder();
		for ( Field attr : fields ) {
			if ( col.length() > 0 ) {
				col.append(",");
			}
			
			if ( val.length() > 0 ) {
				val.append(",");
			}
			
			col.append(attr.getName());
			val.append(convert(attr, stu));
		}
		
		String insSql = sqlBuilder.toString().replace("{col}", col.toString()).replace("{val}", val.toString());
		System.out.println(insSql);
	}

	/**
	 * 获取值，并更具注解进行转换
	 * @param attr
	 * @param target
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String convert(Field attr, Object target) throws IllegalArgumentException, IllegalAccessException{
		Object value = attr.get(target);
		if( value == null ){
			value = "";
		}

		if (attr.isAnnotationPresent(DataTypeMapping.class)) {
			DataTypeMapping mappingAno = attr.getAnnotation(DataTypeMapping.class);
			DatabaseTypeEnum type = mappingAno.datatype();
			if ( type == DatabaseTypeEnum.NUMBER ) {
				return value + "";
			} else if( type == DatabaseTypeEnum.DATE ) {
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
}


