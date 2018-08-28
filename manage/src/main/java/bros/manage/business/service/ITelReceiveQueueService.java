package bros.manage.business.service;

import java.util.Map;

import org.springframework.dao.DataAccessException;

import bros.manage.exception.ServiceException;

/**
 * 
 * @author wyc  记录电报接收队列表接口
 *
 */
public interface ITelReceiveQueueService {
	public void addTelReceiveQueueInfo(Map<String, Object> contextMap) throws DataAccessException;
}
