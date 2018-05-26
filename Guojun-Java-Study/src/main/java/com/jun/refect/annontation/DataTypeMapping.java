package com.jun.refect.annontation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jun.refect.enums.DatabaseTypeEnum;

/**
 * 
 * @Description 自定义注解
 * @author Guojun
 * @Date 2018年5月26日 下午3:22:10
 *
 */

/*
 * @Retention 该注解的生命周期
 * 1、SOURCE 只在源文件中保留, 生成class的时候忽略
 * 2、CLASS 生成class的时候也一并生成, 但是类被加载的时候不会加载进来
 * 3、RUNTIME 加载类的时候也一并加载, 只有RUNTIME才能反射时被获取到
 */
@Retention(RetentionPolicy.RUNTIME)

//@Target的意思是该注解作用于属性还是方法上面还是包上面, 自己点源码进去看, 所有支持的类型有哪些
@Target(ElementType.FIELD)

//@Documented 生成api文档的时候是否也一并生成
@Documented
public @interface DataTypeMapping {

	DatabaseTypeEnum datatype() default DatabaseTypeEnum.VARCHAR;

	String format() default "";

}