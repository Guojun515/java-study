package com.jun.regex.main;

import java.util.regex.Matcher;

import com.jun.regex.common.PatternManager;

/**
 * 
 * 
 * @Description 正则测试
 * @author Guojun
 * @Date 2017年11月18日 上午1:30:53
 *
 */
public class RegularTest {
	/**
	 * 匹配正常能见到的字符，包括键盘上的所有字符，空格符，汉字
	 */
	private final static String NORMAL_CHARACTER_PATTERN = "[\\w\\s\u4e00-\u9fa5`~!@#\\$%\\^&\\*\\(\\)\\-\\+=\\{\\}\\[\\]\\|\\\\:;\'\"<>,\\.\\?/]{1}";

	/**
	 * 获取正常的字符串
	 * @param str
	 * @param patternStr
	 * @return
	 */
	public static String getNormalStr(String str) {
		StringBuilder sb = new StringBuilder();
		PatternManager patternManager = PatternManager.getInstance();
		Matcher matcher = patternManager.buildPattern(NORMAL_CHARACTER_PATTERN).matcher(str);
		while (matcher.find()) {
			sb.append(matcher.group());
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String testStr = "~`@#$%^&*()_-+={}[]|\\:;\"\'<>,.?/中  华‌人民‌	共和国";
		System.out.println(testStr);
		System.out.println("不正常字符串的长度：" + testStr.length());
		testStr = getNormalStr(testStr);
		System.out.println(testStr);
		System.out.println("正常字符串的长度：" + testStr.length());
	}
}
