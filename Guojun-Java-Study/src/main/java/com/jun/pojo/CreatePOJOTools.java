package com.jun.pojo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 自动生成实例类
 * 
 */
class CreatePOJOTools {

	ArrayList<Object> attr = new ArrayList<Object>();
	String dbname;
	String username;
	String userpwd;
	String pack;
	String model_pathname;

	/**
	 * 创建方法
	 * @param tables
	 */
	public void create(String... tables) {
		java.sql.Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException ex) {
			Logger.getLogger(CreatePOJOTools.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("mysql 驱动程序不存在");
			System.exit(0);
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", 
					username, userpwd);
		}
		catch (SQLException ex) {
			System.out.println("连接数据库失败");
			Logger.getLogger(CreatePOJOTools.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(0);
		}

		for (int i = 0; i < tables.length; i++) {
			try {
				/* 实例文件名 */
				java.io.File file =
						new java.io.File(model_pathname + File.separator + pack + File.separator
								+ Character.toUpperCase(tables[i].charAt(0)) + this.toUpperCaseStyle(tables[i].substring(1)) + ".java"); // 文件名
				java.io.File dic = new File(model_pathname + File.separator + pack);
				if (!dic.exists()) {
					dic.mkdirs();
				}
				file.createNewFile();
				System.out.println(file.getAbsolutePath());

				/* 实例文件名 */
				java.io.BufferedWriter bw = new java.io.BufferedWriter(new FileWriter(file)); // 写文件对象
				this.attr.clear();
				String table = tables[i];
				String classname = Character.toUpperCase(table.charAt(0)) + this.toUpperCaseStyle(table.substring(1));
				this.createHead(bw, table, classname, pack); // 创建类头

				String sql =
						"select COLUMN_NAME,DATA_TYPE,COLUMN_TYPE,COLUMN_KEY,EXTRA,CHARACTER_MAXIMUM_LENGTH,IS_NULLABLE,COLUMN_COMMENT from information_schema.columns where table_name='"
								+ table + "'" + "order by ordinal_position; "; //获取表结构
				System.out.println(sql);
				java.sql.Statement sta = conn.createStatement();
				java.sql.ResultSet rs = sta.executeQuery(sql);
				Map<String,Object> cols = new HashMap<String,Object>();
				while (rs.next()) {
					String colname = rs.getString("COLUMN_NAME");
					String coltype = rs.getString("DATA_TYPE");
					String Key = rs.getString("COLUMN_KEY");
					String Extra = rs.getString("EXTRA");
					String Column_Type = rs.getString("COLUMN_TYPE");
					String isnull = rs.getString("IS_NULLABLE");
					String COLUMN_COMMENT = rs.getString("COLUMN_COMMENT");
					//long length = rs.getLong("CHARACTER_MAXIMUM_LENGTH");
					if(!cols.containsKey(colname)){
						cols.put(colname, colname);
						this.createAttribute(bw, colname, coltype, Key, Extra, Column_Type, isnull, COLUMN_COMMENT); // 创建类属性
					}
				}
				this.packageAttribute(bw, table);
				this.createTail(bw);
				bw.close();

			}
			catch (SQLException ex) {
				Logger.getLogger(CreatePOJOTools.class.getName()).log(Level.SEVERE, null, ex);
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 创建实例文件的头部
	 * @param bw
	 * @param table
	 * @param classname
	 * @param pack
	 * @throws IOException
	 */
	private void createHead(java.io.BufferedWriter bw, String table, String classname, String pack) throws IOException {
		bw.write("package cloudserver.domain." + pack + ";");
		bw.newLine();
		bw.newLine();
		bw.write("import cloudserver.domain.common.ModelBase;");
		bw.newLine();
		bw.write("import com.alibaba.fastjson.annotation.JSONField;");
		bw.newLine();
		bw.write("import java.util.Date;");
		bw.newLine();
		bw.write("import java.math.BigDecimal;");
		bw.newLine();
		bw.newLine();
		bw.newLine();
		bw.write("public class " + classname + " extends ModelBase {");
		bw.newLine();
	}

	/**
	 * 创建属性字段
	 * @param bw
	 * @param name
	 * @param type
	 * @param key
	 * @param extra
	 * @param column_type
	 * @param isnull
	 * @param column_comment
	 * @throws IOException
	 */
	private void createAttribute(java.io.BufferedWriter bw, String name, String type, String key, String extra, String column_type,
			String isnull, String column_comment) throws IOException {
		String typename = null;
		String typename_ref = null;
		if ("bit".equals(type)) {
			typename = "boolean";
			typename_ref = "Boolean";
		}
		else if ("char".equals(type)) {
			typename = "String";
			typename_ref = "String";
		}
		else if ("text".equals(type)) {
			typename = "String";
			typename_ref = "String";
		}
		else if ("longtext".equals(type)) {
			typename = "String";
			typename_ref = "String";
		}
		else if ("date".equals(type)) {
			typename = "Date";
			typename_ref = "Date";
		}
		else if ("datetime".equals(type)) {
			typename = "Date";
			typename_ref = "Date";
		}
		else if ("decimal".equals(type)) {
			typename = "BigDecimal";
			typename_ref = "BigDecimal";
		}
		else if ("double".equals(type)) {
			typename = "double";
			typename_ref = "Double";
		}
		else if ("float".equals(type)) {
			typename = "double";
			typename_ref = "Double";
		}
		else if ("integer".equals(type)) {
			typename = "int";
			typename_ref = "Integer";
		}
		else if ("int".equals(type)) {
			typename = "int";
			typename_ref = "Integer";
		}
		else if ("smallint".equals(type)) {
			typename = "int";
			typename_ref = "Integer";
		}
		else if ("time".equals(type)) {
			typename = "Date";
			typename_ref = "Date";
		}
		else if ("timestamp".equals(type)) {
			typename = "Timestamp";
			typename_ref = "Timestamp";
		}
		else if ("varchar".equals(type)) {
			typename = "String";
			typename_ref = "String";
		}
		else if ("longvarchar".equals(type)) {
			typename = "String";
			typename_ref = "String";
		}
		else if ("bigint".equals(type)) {
			typename = "long";
			typename_ref = "Long";
		}
		else if ("binary".equals(type)) {
			typename = "byte[]";
			typename_ref = "Byte[]";
		}
		else if ("blob".equals(type)) {
			typename = "byte[]";
			typename_ref = "Byte[]";
		}
		else if ("clob".equals(type)) {
			typename = "String";
			typename_ref = "String";
		}
		else if ("java_object".equals(type)) {
			typename = "Object";
		}
		else if ("longvarbinary".equals(type)) {
			typename = "byte[]";
			typename_ref = "Byte[]";
		}
		else if ("numeric".equals(type)) {
			typename = "BigDecimal";
			typename_ref = "BigDecimal";
		}
		else if ("real".equals(type)) {
			typename = "float";
			typename_ref = "Float";
		}
		else if ("tinyint".equals(type)) {
			typename = "bute";
			typename_ref = "Bute";
		}
		else if ("varbinary".equals(type)) {
			typename = "byte[]";
			typename_ref = "Byte[]";
		}
		else {
			typename = String.valueOf(type);
			typename_ref = String.valueOf(type);
		}

		if (column_comment != null && !column_comment.equals("")) {
			bw.newLine();
			bw.write("    /**");
			bw.newLine();
			bw.write("     * " + column_comment);
			bw.newLine();
			bw.write("     */");
		}

		bw.newLine();
		bw.write("    @JSONField(name = \"" + name + "\")");
		bw.newLine();
		bw.write("    private " + typename_ref);
		bw.write(" ");
		bw.write(this.toUpperCaseStyle(name));
		bw.write(";");
		bw.newLine();

		attr.add(new Object[] { name, typename, typename_ref, column_type, key, extra, isnull, column_comment });
	}

	/**
	 * 编写getter与setter方法
	 * @param bw
	 * @param table
	 * @throws IOException
	 */
	private void packageAttribute(java.io.BufferedWriter bw, String table) throws IOException {
		for (Object object : attr) {
			Object[] obj = (Object[]) object;
			String name = obj[0].toString();
			//String typename = obj[1].toString();
			String typename_ref = obj[2].toString();
			//String column_type = obj[3].toString();
			//String key = obj[4].toString();
			//String extra = obj[5].toString();
			//String isnull = obj[6].toString();
			String column_comment = obj[7].toString();

			if (column_comment != null && !column_comment.equals("")) {
				bw.newLine();
				bw.write("    /**");
				bw.newLine();
				bw.write("     * " + column_comment);
				bw.newLine();
				bw.write("     */");
			}
			bw.newLine();
			bw.write("    public " + typename_ref + " get" + Character.toUpperCase(name.charAt(0))
			+ this.toUpperCaseStyle(name.substring(1)) + "() {");
			bw.newLine();
			bw.write("        return this." + this.toUpperCaseStyle(name) + ";");
			bw.newLine();
			bw.write("    }");
			bw.newLine();

			bw.newLine();
			bw.write("    public void " + "set" + Character.toUpperCase(name.charAt(0)) + this.toUpperCaseStyle(name.substring(1)) + "("
					+ typename_ref + " " + this.toUpperCaseStyle(name) + ") {");
			bw.newLine();
			bw.write("        this." + this.toUpperCaseStyle(name) + " = " + this.toUpperCaseStyle(name) + ";");
			bw.newLine();
			bw.write("    }");
			bw.newLine();
		}
		bw.newLine();
		bw.newLine();
	}

	/**
	 * 结尾标记
	 * @param bw
	 * @throws IOException
	 */
	private void createTail(java.io.BufferedWriter bw) throws IOException {
		bw.write("}");

	}

	/**
	 * 下划线风格转驼峰风格
	 * @param name
	 * @return
	 */
	private String toUpperCaseStyle(String name) {
		StringBuilder newName = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (i > 0 && c == '_') {
				if (i < (name.length() - 1)) {
					newName.append(Character.toUpperCase(name.charAt(++i)));
				}
				else {
					newName.append(c);
				}
			}
			else {
				newName.append(c);
			}
		}
		return newName.toString();
	}

	/**
	 * 驼峰风格转下划线风格
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unused")
	private String toUnderlineStyle(String name) {
		StringBuilder newName = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i > 0) {
					newName.append("_");
				}
				newName.append(Character.toLowerCase(c));
			}
			else {
				newName.append(c);
			}
		}
		return newName.toString();
	}


	/**
	 * main方法
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws SQLException {
		CreatePOJOTools d = new CreatePOJOTools();
		d.dbname = "guo_jun";
		d.username = "root";
		d.userpwd = "root";
		d.model_pathname = "D:/aaa/";
		d.pack = "com.jun.entity";

		d.create("sys_user");
	}
}
