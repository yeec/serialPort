package bros.manage.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import bros.manage.business.mapper.LogSysStatemMapper;
import bros.manage.business.service.LogSysStatemService;
import bros.manage.constants.ServiceErrorCodeContants;
import bros.manage.entity.LogSysStatemBen;
import bros.manage.exception.ServiceException;

/**
 * 
 * @author liwei
 *
 */
public class LogSysStatemServiceImpl implements LogSysStatemService{
	// 记录操作日志实现类
	
	@Autowired
	private LogSysStatemMapper logSysStatemMapper;

	@Override
	public void insertLogSysStatemInfo(LogSysStatemBen logSysStatemBen) throws ServiceException {
		
		try{
			logSysStatemMapper.insertLogSysStatemInfo(logSysStatemBen);
		}catch(Exception e){
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001,"记录操作日志失败");
		}
	}

}
