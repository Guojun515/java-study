package com.jun.excel.exception;

/**
 * excel解析异常
 * @author Guojun
 * @Date 2018年11月24日 下午6:29:51
 *
 */
public class ExcelParseException extends RuntimeException {
	private static final long serialVersionUID = -4023123688780895995L;

    public ExcelParseException(Throwable cause) {
        super(cause);
    }
    
    public ExcelParseException(String message) {
        super(message);
    }
    
    public ExcelParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
