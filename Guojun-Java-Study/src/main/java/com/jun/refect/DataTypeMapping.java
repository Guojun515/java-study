package com.jun.refect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @Target的意思是该注解作用于属性还是方法上面还是包上面, 自己点源码进去看, 所有支持的类型有哪些
 */
@Target(ElementType.FIELD)

/*
 * @Documented 生成api文档的时候是否也一并生成
 */
@Documented

/*
 * @Retention 该注解的生命周期
 * SOURCE 只在源文件中保留, 生成class的时候忽略
 * CLASS 生成class的时候也一并生成, 但是类被加载的时候不会加载进来
 * RUNTIME 加载类的时候也一并加载, 只有RUNTIME才能反射时被获取到
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTypeMapping {

	DBTypeEnum datatype() default DBTypeEnum.VARCHAR;

	String format() default "";

}