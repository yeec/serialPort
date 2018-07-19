package bros.manage.business.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.LogSysStatemMapper;
import bros.manage.business.service.ILogSysStatemService;
import bros.manage.constants.ServiceErrorCodeContants;
import bros.manage.exception.ServiceException;

/**
 * 
 * @author liwei
 *
 */
@Service("logSysStatemService")
public class LogSysStatemServiceImpl implements ILogSysStatemService{
	
	/**
	 * 操作日志Log
	 */
	private static final  Logger logger = LoggerFactory.getLogger(LogSysStatemServiceImpl.class);
	// 记录操作日志实现类
	
	@Autowired
	private LogSysStatemMapper logSysStatemMapper;

	@Override
	public void addLogSysStatemInfo(Map<String, Object> contextMap) throws ServiceException {
		
		try{
			logSysStatemMapper.insertLogSysStatemInfo(contextMap);
		}catch (Exception e) {
			logger.error("Exception from " + this.getClass().getName() + "'s addLogSysStatemInfo method.", e);
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001, "记录操作日志失败", e);
		}
	}

}
