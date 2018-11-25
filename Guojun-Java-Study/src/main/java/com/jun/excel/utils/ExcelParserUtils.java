package com.jun.excel.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.mysql.cj.core.exceptions.PasswordExpiredException;

/**
 * Excel 解析工具类
 * @author Guojun
 * @Date 2018年11月24日 下午1:31:57
 *
 */
public class ExcelParserUtils {
	private ExcelParserUtils() {}
	
	/**
	 * 获取数值类型的整数部分
	 */
	private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#");
	
	/**
	 * 转换为实际的数据类型(从excel取到的值只有String，double, boolean类型)
	 * @param clazz 需要转换的类型
	 * @param data 原始数据
	 * @return 转换后的数值
	 * @throws PasswordExpiredException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseCellValue(Class<?> clazz, Object data) throws Exception {
		if (data == null) {
			return null;
		}
		
		Object result = null;
		try {
			// 转换为String类型
			if (clazz.isAssignableFrom(String.class)) {
				if (data instanceof String) {
					result = data;
				} else if(data instanceof Double) {
					//取所有整数部分  
					result = INTEGER_FORMAT.format(data);
				} else {
					result = String.valueOf(data);
				}
			}
			// 转换为int类型
			else if (clazz == int.class || clazz.isAssignableFrom(Integer.class)) {
				if (data instanceof Integer) {
					result = data;
				} else if (data instanceof String) {
					result = Integer.parseInt(String.valueOf(data));
				} else {
					result = ((Double)data).intValue();
				}
			}
			// 转换为long类型
			else if (clazz == long.class || clazz.isAssignableFrom(Long.class)) {
				if (data instanceof Long) {
					result = data;
				} else if (data instanceof String) {
					result = Long.parseLong(String.valueOf(data));
				} else {
					result = ((Double)data).longValue();
				}
			}
			// 转换为double类型
			else if (clazz == double.class || clazz.isAssignableFrom(Double.class)) {
				if (data instanceof Double) {
					result = data;
				} else {
					result = Double.parseDouble(String.valueOf(data));
				}
			}
			// 转换为float类型
			else if (clazz == float.class || clazz.isAssignableFrom(Float.class)) {
				if (data instanceof Float) {
					result = data;
				} else if (data instanceof Double) {
					result = ((Double)data).longValue();
				} else {
					result = Float.parseFloat(String.valueOf(data));
				}
			}
			// 转换为Boolean类型
			else if (clazz == boolean.class || clazz.isAssignableFrom(Boolean.class)) {
				if (data instanceof Boolean) {
					result = data;
				} else {
					result = Boolean.parseBoolean(String.valueOf(data));
				}
			}
			// 转换为Date类型
			else if (clazz.isAssignableFrom(Date.class)) {
				if (data instanceof Date) {
					result = data;
				} else {
					result = DateUtils.parseDate(String.valueOf(data), 
							"yyyy-MM-dd HH:mm:ss",
							"yyyy/MM/dd HH:mm:ss",
							"yyyy-MM-dd",
							"yyyy/MM/dd");
				}
			}
			// 转换为BigDecimal类型
			else if (clazz.isAssignableFrom(BigDecimal.class)) {
				if (data instanceof BigDecimal) {
					result = data;
				} else {
					data = new BigDecimal(String.valueOf(data));
				}
			} else {
				throw new Exception("不支持的数据类型");
			}
		} catch (Exception e) {
			throw new Exception("不支持的数据类型", e);
		}
		
		return (T) result;
	}
	
	/**
	 * 获取泛型的类型
	 * @return
	 */
	public static Class<?> getTClass(Class<?> clazz) {
		// 获取带有泛型的父类
		ParameterizedType type = (ParameterizedType)clazz.getGenericSuperclass();
		// 获取泛型的类型
		Type typeClazz = type.getActualTypeArguments()[0];
		return (Class<?>) typeClazz;
	}
}
