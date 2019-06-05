package bros.manage.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.TelSendQueueMapper;
import bros.manage.business.service.ITelSendQueueService;

/**
 * 
 * @author liwei
 * 电报发送列表接口实现
 */
@Service("telSendQueueService")
public class TelSendQueueServiceImpl  implements ITelSendQueueService{
	
	@Autowired
	private TelSendQueueMapper telSendQueueMapper;
	
    // 查询发送电报列表中发送标志为0的数据
	@Override
	public List<Map<String, Object>> queryTelSendInfo() {
		return telSendQueueMapper.getTelSendInfo();
		
	}
	
	
	// 更新发送电报列表中发送标志为0，更新为1
	@Override
	public void updateTelSendInfo(Map<String, Object> contextMap) {
		telSendQueueMapper.updateTelSendInfo(contextMap);
	}

	// 新增发送电报
	@Override
	public void addTelSendInfo(Map<String, Object> contextMap) {
		telSendQueueMapper.insertTelSendInfo(contextMap);
		
	}

	// 删除发送电报
	@Override
	public int deleteTelSendInfo(Map<String, Object> contextMap) {
		return telSendQueueMapper.deleteTelSendInfo(contextMap);
	}
	
	
}
