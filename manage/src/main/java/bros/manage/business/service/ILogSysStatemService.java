package bros.manage.business.service;

import java.util.Map;

import bros.manage.exception.ServiceException;

/**
 * 
 * @author liwei
 *
 */
public interface ILogSysStatemService {
	// 记录操作日志接口
	public void addLogSysStatemInfo(Map<String, Object> contextMap) throws ServiceException;
}
