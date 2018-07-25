package bros.manage.business.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.JaiJAiltimeMapper;
import bros.manage.business.service.IJaiJAiltimeService;
import bros.manage.constants.ServiceErrorCodeContants;
import bros.manage.exception.ServiceException;

/**
 * 
 * @author wyc
 * 监控时间表实现类
 */
@Service("jaiJAiltimeService")
public class JaiJAiltimeServiceImpl implements IJaiJAiltimeService{
	
	/**
	 * 操作日志Log
	 */
	private static final  Logger logger = LoggerFactory.getLogger(JaiJAiltimeServiceImpl.class);
	// 记录操作日志实现类
	
	@Autowired
	private JaiJAiltimeMapper jaiJAiltimeMapper;
	
	
	// 添加监控时间表
	@Override
	public void addJaiJailtimeinfo() throws ServiceException {
		
		try{
			jaiJAiltimeMapper.insertJaiJailtimeinfo();
		}catch (Exception e) {
			logger.error("Exception from " + this.getClass().getName() + "'s addJaiJailtimeinfo method.", e);
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001, "记录操作日志失败", e);
		}
	}
	
	// 更新监控时间表
	@Override
	public int updateJaiJailtimeinfo(Map<String, Object> contextMap) throws ServiceException{
		try{
			return jaiJAiltimeMapper.updateJaiJailtimeinfo(contextMap);
		}catch (Exception e) {
			logger.error("Exception from " + this.getClass().getName() + "'s updateJaiJailtimeinfo method.", e);
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001, "记录操作日志失败", e);
		}
		
	}

}
