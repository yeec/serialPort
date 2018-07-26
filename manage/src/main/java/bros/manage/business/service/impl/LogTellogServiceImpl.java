package bros.manage.business.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.LogTellogMapper;
import bros.manage.business.service.ILogTellogService;
import bros.manage.constants.ServiceErrorCodeContants;
import bros.manage.exception.ServiceException;

/**
 * 
 * @author liwei
 * 电报日志表接口接口实现
 */
@Service("logTellogService")
public class LogTellogServiceImpl implements ILogTellogService{
	
	/**
	 * 操作日志Log
	 */
	private static final  Logger logger = LoggerFactory.getLogger(LogTellogServiceImpl.class);
	// 记录操作日志实现类
	
	@Autowired
	private LogTellogMapper logTellogMapper;
	// 添加电报日志表
	@Override
	public void addLogTellogInfo(Map<String,Object> contextMap) throws ServiceException {
		try{
			logTellogMapper.insertLogTellogInfo(contextMap);
		}catch (Exception e) {
			logger.error("Exception from " + this.getClass().getName() + "'s addLogTellogInfo method.", e);
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001, "记录操作日志失败", e);
		}
	}

}
