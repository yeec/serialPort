package bros.manage.business.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.TelSysStatusMapper;
import bros.manage.business.service.ITelSysStatusService;
import bros.manage.constants.ServiceErrorCodeContants;
import bros.manage.exception.ServiceException;

/**
 * 
 * @author wyc
 * @记录当前系统的状态表接口实现
 */
@Service("telSysStatusService")
public class TelSysStatusServiceImpl implements ITelSysStatusService{
	
	/**
	 * 操作日志Log
	 */
	private static final  Logger logger = LoggerFactory.getLogger(TelSysStatusServiceImpl.class);
	// 记录操作日志实现类
	
	@Autowired
	private TelSysStatusMapper TelSysStatusMapper;

	@Override
	// 记录当前系统的状态表
	public void addTelSysStatus(Map<String, Object> contextMap) throws ServiceException {
		try{
			TelSysStatusMapper.insertTelSysStatus(contextMap);
		}catch (Exception e) {
			logger.error("Exception from " + this.getClass().getName() + "'s addTelSysStatus method.", e);
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001, "记录当前系统的状态失败", e);
		}
	}

	@Override
	// 更新当前系统的状态表
	public int updateTelSysStatus(Map<String, Object> contextMap) throws ServiceException {
		try{
			return TelSysStatusMapper.updateTelSysStatus(contextMap);
		}catch (Exception e) {
			logger.error("Exception from " + this.getClass().getName() + "'s updateTelSysStatus method.", e);
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001, "更新当前系统的状态失败", e);
		}
	}

	@Override
	// 查询当前系统的状态表
	public int queryTelSysStatus() throws ServiceException {
		try{
			return TelSysStatusMapper.getTelSysStatus();
		}catch (Exception e) {
			logger.error("Exception from " + this.getClass().getName() + "'s queryTelSysStatus method.", e);
			throw new ServiceException(ServiceErrorCodeContants.EBMT0001, "查询当前系统的状态失败", e);
		}
	}

}
