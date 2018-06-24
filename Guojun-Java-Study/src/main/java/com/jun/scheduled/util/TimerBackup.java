package com.jun.scheduled.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Description 定时备份数据文件
 * @author Guojun
 * @Date 2018年6月24日 下午6:00:33
 *
 */
public class TimerBackup {
	private static Logger log = LoggerFactory.getLogger(TimerBackup.class);

	private static String dbIp = null;
	private static String dbName = null;
	private static String dbUserName = null;
	private static String dbPassword = null;
	private static String backupUrl = null;
	private static String backupFile = null;
	private static Long timerDelay = null;

	static{
		try {
			Properties prop = new Properties();
			prop.load(Thread.currentThread().getClass().getResourceAsStream("/sysInfo.properties"));
			dbIp = prop.getProperty("db.ip");
			dbName = prop.getProperty("db.dbname");
			dbUserName = prop.getProperty("db.username");
			dbPassword = prop.getProperty("db.password");
			backupUrl = prop.getProperty("backup.url");
			backupFile = prop.getProperty("backup.file");
			timerDelay = doCalculation(prop.getProperty("timer.delay"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("系统初始化出错了！");
		}

	}

	public static void executeBackup(){
		ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable paramRunnable) {
				return new Thread(paramRunnable, "数据库备份定时任务");
			}
		});
		
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				TimerBackup.doBack();
			}
		}, 0, timerDelay, TimeUnit.MILLISECONDS);
	}

	/**
	 * 执行备份
	 */
	private static void doBack(){
		try {
			if(!checkParams()){
				return;
			}
			StringBuffer cmdStr = new StringBuffer();
			cmdStr.append("cmd /c ");
			cmdStr.append("mysqldump ");
			cmdStr.append("-h").append(dbIp).append(" ");
			cmdStr.append("-u").append(dbUserName).append(" ");
			cmdStr.append("-p").append(dbPassword).append(" ");
			cmdStr.append(dbName).append(" ");
			cmdStr.append("> ");
			cmdStr.append(backupUrl);
			
			createDir(backupUrl);

			String fileName = getFileName(backupFile);
			cmdStr.append(fileName);

			Runtime.getRuntime().exec(cmdStr.toString());
			log.info("执行备份成功！文件为：" + backupUrl+fileName);
		} catch (Exception e) {
			log.error("系统出错了！");
			e.printStackTrace();
		}
	}

	/**
	 * 检查参数
	 * @return
	 */
	private static boolean checkParams(){

		if(dbIp == null || dbIp.equals("")){
			dbIp = "localhost";
		}

		if(dbName == null || dbName.equals("")){
			log.error("数据库名称不能为空");
			return false;
		}

		if(dbUserName == null || dbUserName.equals("")){
			log.error("用户名不能为空");
			return false;
		}

		if(backupUrl == null || backupUrl.equals("")){
			log.error("备份文件的路径不能为空");
			return false;
		}

		if(backupFile == null || backupFile.equals("")){
			log.error("备份文件名径不能为空");
			return false;
		}

		return true;
	}

	/**
	 * 计算时间表达式
	 * @param str
	 * @return
	 */
	private static Long doCalculation(String str){
		if(str == null || str.equals("")){
			return 2L*60L*1000L;
		}else{
			String[] num = str.split("\\*");
			if(num != null && num.length > 1){
				Long result = Long.parseLong(num[0]);
				for(int i = 1; i<num.length; i++){
					result *= Long.parseLong(num[i]);
				}
				return result;
			}else if(num != null && num.length == 1){
				return Long.parseLong(num[0]);
			}else{
				return 0L;
			}
		}
	}

	/**
	 * 获取备份的文件名
	 * @param file
	 * @return
	 */
	private static String getFileName(String file){
 		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		String currentDate = format.format(new Date());
		int len = file.lastIndexOf(".");
		String prefix = file.substring(0, len);
		String suffix = file.substring(len);
		String result = null;
		if(backupUrl.endsWith("/")){
			result = prefix + "_" + currentDate +suffix;
		}else{
			result = "/" + prefix + "_" + currentDate +suffix;
		}
		return result;
	}
	
	/**
	 * 创建目录
	 * @param path
	 */
	private static void createDir(String path){
		File backFile = new File(path);
		if(!backFile.exists() && !backFile.isDirectory()){
			backFile.mkdir();
		}
	}
}
