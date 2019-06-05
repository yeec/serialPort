package bros.manage.business.service;

import java.util.Map;

/**
 * 
 * @author liwei
 * 电报发送列表接口
 */
public interface ITelSendQueueBakService {
	
	// 新增发送电报备份
	public int addTelSendInfoBak(Map<String, Object> contextMap);
	// 新增发送电报备份
	public int addTelTAllSend(Map<String, Object> contextMap);
}
