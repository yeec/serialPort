package bros.manage.business.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.TelReceiveQueueMapper;
import bros.manage.business.service.ITelReceiveQueueService;
import bros.manage.exception.ServiceException;

/**
 * 
 * @author wyc 记录电报接收队列表接口实现类
 *
 */

@Service("telReceiveQueueService")
public class TelReceiveQueueServiceImpl implements ITelReceiveQueueService{
	@Autowired
	private TelReceiveQueueMapper telReceiveQueueMapper;
	@Override
	public void addTelReceiveQueueInfo(Map<String, Object> contextMap) throws DataAccessException{
		try{
			telReceiveQueueMapper.insertTelReceiveInfo(contextMap);
		}catch(DataAccessException e){
			throw e;
		}
	}

}
