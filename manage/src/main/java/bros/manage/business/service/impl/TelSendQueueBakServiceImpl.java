package bros.manage.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.TelSendQueueBakMapper;
import bros.manage.business.service.ITelSendQueueBakService;

/**
 * 
 * @author liwei
 * 电报发送列表接口实现
 */
@Service("telSendQueueBakService")
public class TelSendQueueBakServiceImpl  implements ITelSendQueueBakService{
	
	@Autowired
	private TelSendQueueBakMapper telSendQueueBakMapper;
	
	// 新增发送电报备份接口实现
	@Override
	public int addTelSendInfoBak(Map<String, Object> contextMap) {
		return telSendQueueBakMapper.insertTelSendInfoBak(contextMap);
		
	}

	// 新增发送电报备份接口实现
	@Override
	public int addTelTAllSend(Map<String, Object> contextMap) {
		return telSendQueueBakMapper.insertTelTAllSend(contextMap);
	}
}
