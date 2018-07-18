package bros.manage.business.service;

import bros.manage.entity.LogSysStatemBen;
import bros.manage.exception.ServiceException;

/**
 * 
 * @author liwei
 *
 */
public interface LogSysStatemService {
	// 记录操作日志接口
	public void insertLogSysStatemInfo(LogSysStatemBen logSysStatemBen) throws ServiceException;
}
