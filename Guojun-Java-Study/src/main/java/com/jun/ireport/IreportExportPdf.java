package com.jun.ireport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

/**
 * 中文不显示解决方案：https://blog.csdn.net/biedazhangshu/article/details/51564755
 * @author v-yuguojun
 *
 */
public class IreportExportPdf {
	public static void main(String[] args) throws Exception {
        FileOutputStream outputStream=null;
        ByteArrayOutputStream outPut=new ByteArrayOutputStream();
        
        try {
        	//STEP 1 : 指定参数
        	Map<String, Object> parameters=new HashMap<>();
        	parameters.put("parameter1", "赵俊成");
        	parameters.put("parameter2", "万昭君");
        	parameters.put("parameter3", "张小凡");
        	parameters.put("parameter4", "陆雪琪");
        	//STEP 2 : 指定数据源
        	List<Map<String, String>> datas = new ArrayList<>();
        	for (int i = 0; i < 10; i++) {
        		Map<String, String> data = new HashMap<>();
        		data.put("field1", "" + (i + 1));
        		data.put("field2", "" + (i + 1));
        		data.put("field3", "" + (i + 1));
        		data.put("field4", "" + (i + 1));
        		data.put("field5", "" + (i + 1));
        		data.put("field6", "" + (i + 1));
        		data.put("field7", "" + (i + 1));
        		data.put("field8", "" + (i + 1));
        		
        		datas.add(data);
        	}
        	
        	// 空数据源
        	//JRDataSource datasource = new JREmptyDataSource();
        	
        	// 数据源
        	JRDataSource datasource = new JRBeanCollectionDataSource(datas);
        	
            JasperPrint jasperPrint=JasperFillManager.fillReport(IreportExportPdf.class.getClassLoader().getResourceAsStream("com/jun/ireport/report2.jasper"), parameters, datasource);
            
            // 导出PDF
            JRPdfExporter exporter=new JRPdfExporter();
            
            // 创建jasperPrint
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            //生成输出流
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outPut));
            
            // 属性设置
            PdfReportConfiguration pdfExporterConfiguration = new SimplePdfReportConfiguration();
            exporter.setConfiguration(pdfExporterConfiguration);
            exporter.exportReport();
            
            File file=new File("C:/Users/v-yuguojun/Desktop/temp/report.pdf");
            outputStream=new FileOutputStream(file);
            outputStream.write(outPut.toByteArray());
        } catch (JRException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                outPut.flush();
                outPut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
}
