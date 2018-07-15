package bros.manage.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import bros.manage.constants.BaseErrorCodeConstants;

/**
 * 
 * @ClassName: ServiceException 
 * @Description: 服务层异常类，扩展自Exception，添加错误码，程序中抛出错误码，由Action捕获异常，
 * 错误处理错误信息，异常中已可以添加错误信息，可以输出到日志中或是默认情况使用)
 * @author 何鹏
 * @date 2016年5月14日 下午3:03:50 
 * @version 1.0
 */
public class ServiceException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 3682193913742844996L;
	private String errorCode;//错误码
	
	private  String errorMsg;//错误描述信息
	
	public String[] errorStations = null;  //错误信息中站位符对应的信息数组 如：XX{0}XX{1} 对应值
	/**
	 * 存储交易失败的时候返回数据信息
	 */
	private Map<String,Object> result = new HashMap<String, Object>();
	
	public ServiceException(){
		super();
	}
	
	public ServiceException(String errorCode){		
		super(errorCode);
		this.setErrorCode(errorCode);
	}
	
	/**
	 * 设置错误码、错误信息
	 * @param errorCode
	 * @param errorMsg
	 */
	public ServiceException(String errorCode,String errorMsg){
		super(errorCode);
		this.setErrorCode(errorCode);
		this.setErrorMsg(errorMsg);
	}
	
	public ServiceException(String s, Throwable throwable) {
		super(s, throwable);
		this.setErrorCode(BaseErrorCodeConstants.EBNT0001);
		this.setErrorMsg(s);
	}

	public ServiceException(String errorCode, String s, Throwable throwable) {
		super(s, throwable);
		this.setErrorCode(errorCode);
		this.setErrorMsg(s);
	}
	
	public ServiceException(String errorCode, String s, Throwable throwable,Map<String,Object> resultMap) {
		super(s, throwable);
		this.setErrorCode(errorCode);
		this.setErrorMsg(s);
		this.result = resultMap;
	}
	
	public ServiceException(Exception e){
		if(e.getCause() instanceof ServiceException){
			ServiceException b = (ServiceException)e.getCause();
			new ServiceException(b.getErrorCode(),b.getErrorMsg());
		}else{
			new ServiceException(BaseErrorCodeConstants.EBNT0001,"系统异常");
		}
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String[] getErrorStations() {
		return errorStations;
	}

	public void setErrorStations(String[] errorStations) {
		this.errorStations = errorStations;
	}
	
}
