package bros.manage.business.service;

import java.util.Map;

import bros.manage.exception.ServiceException;

public interface IJaiJAiltimeService {
	// 添加监控时间表
	public void addJaiJailtimeinfo() throws ServiceException;
	// 更新监控时间表
	public int updateJaiJailtimeinfo(Map<String, Object> contextMap) throws ServiceException;
}
