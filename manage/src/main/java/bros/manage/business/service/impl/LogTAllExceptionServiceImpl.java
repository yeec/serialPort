package bros.manage.business.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.LogTAllExceptionMapper;
import bros.manage.business.service.ILogTAllExceptionService;
import bros.manage.constants.ServiceErrorCodeContants;
import bros.manage.exception.ServiceException;

/**
 * 
 * @author wyc
 * @记录收发电报失败日志接口实现
 */
@Service("logTAllExceptionService")
public class LogTAllExceptionServiceImpl implements ILogTAllExceptionService{
	
	/**
	 * 操作日志Log
	 */
	private static final  Logger logger = LoggerFactory.getLogger(LogTAllExceptionServiceImpl.class);
	// 记录操作日志实现类
	
	@Autowired
	private LogTAllExceptionMapper logTAllExceptionMapper;

	@Override
	public void addLogTAllException(Map<String, Object> contextMap) throws ServiceException {
		try{
			logTAllExceptionMapper.insertLogTAllException(contextMap);
		}catch (Exception e) {
			logger.error("Exception from " + this.getClass().getName() + "'s addLogTAllException method.", e);
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001, "记录收发电报失败日志失败", e);
		}
	}
}
