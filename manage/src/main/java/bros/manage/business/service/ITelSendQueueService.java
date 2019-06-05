package bros.manage.business.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author liwei
 * 电报发送列表接口
 */
public interface ITelSendQueueService {
	
	// 查询发送电报列表中发送标志为0的数据
	public List<Map<String, Object>> queryTelSendInfo();
	// 更新发送电报列表中发送标志为0，更新为1
	public void updateTelSendInfo(Map <String,Object> contextMap);
	// 新增发送电报
	public void addTelSendInfo(Map<String, Object> contextMap);
	// 删除发送电报
	public int deleteTelSendInfo(Map<String, Object> contextMap);
}
