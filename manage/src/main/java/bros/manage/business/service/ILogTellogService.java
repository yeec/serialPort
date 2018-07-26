package bros.manage.business.service;

import java.util.Map;

import bros.manage.exception.ServiceException;

/**
 * 
 * @author liwei
 * 电报日志表接口
 */
public interface ILogTellogService {
	// 添加电报日志
	public void addLogTellogInfo(Map<String,Object> contextMap)throws ServiceException;
}
