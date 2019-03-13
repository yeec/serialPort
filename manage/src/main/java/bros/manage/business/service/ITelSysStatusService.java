package bros.manage.business.service;

import java.util.Map;

import bros.manage.exception.ServiceException;
/**
 * 
 * @author wyc
 * @记录当前系统的状态表接口
 */
public interface ITelSysStatusService {
	// 记录当前系统的状态表
	public void addTelSysStatus(Map<String,Object> contextMap) throws ServiceException;;
	// 更新当前系统的状态表
	public int updateTelSysStatus(Map<String, Object> contextMap) throws ServiceException;;
	// 查询当前系统的状态表
    public int queryTelSysStatus() throws ServiceException;;
}
