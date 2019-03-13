package bros.manage.business.service;

import java.util.Map;

import bros.manage.exception.ServiceException;
/**
 * 
 * @author wyc
 * @记录收发电报失败日志接口
 */
public interface ILogTAllExceptionService {
	// 记录收发电报失败日志表
	public void addLogTAllException(Map<String,Object> contextMap) throws ServiceException;;
}
